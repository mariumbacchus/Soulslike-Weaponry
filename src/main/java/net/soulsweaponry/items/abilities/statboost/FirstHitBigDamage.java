package net.soulsweaponry.items.abilities.statboost;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.TriPredicate;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record FirstHitBigDamage(
        float bonusDamage, float bonusDamagePerLvl,
        int minCooldown, int cooldown, int reducedCooldownPerLvl,
        TriPredicate<World, PlayerEntity, ItemStack> reducedCooldownPredicate, float cooldownModWhenPredicate
) implements IAbility {

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player) {
            float damage = WeaponUtil.getBaseAttackDamage(stack);
            float attackSpeed = WeaponUtil.getBaseAttackSpeed(stack);
            if (!this.isCoolingDown(player, stack)) {
                damage += this.bonusDamage;
            }
            WeaponUtil.modifyStackAttributes(stack, damage, attackSpeed);
        }
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player) {
            if (!this.isCoolingDown(player, stack)) {
                this.applyItemCooldown(stack, player, this.cooldown(player.getWorld(), player, stack));
            }
        }
    }

    private int cooldown(World world, PlayerEntity player, ItemStack stack) {
        int cd = this.cooldown - this.reducedCooldownPerLvl * WeaponUtil.getUpgradeLevel(stack);
        if (this.reducedCooldownPredicate.test(world, player, stack)) {
            cd = (int) (cd * this.cooldownModWhenPredicate);
        }
        return Math.max(this.minCooldown, cd);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.doom").formatted(Formatting.RED),
                Text.translatable("tooltip.soulsweapons.doom.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.doom.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.doom.3").formatted(Formatting.GRAY)
        );
    }
}
