package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.ModTags;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Optional;

public abstract class SoulHarvestingItem extends ModdedSword {

    public SoulHarvestingItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.addTooltipAbility(TooltipAbilities.SOUL_TRAP, TooltipAbilities.COLLECT);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (target.isDead()) {
            this.handleKill(target, stack);
        }
        // Include dead entities hit by sweeping, Better Combat already does this so ignore if loaded
        if (attacker instanceof PlayerEntity player && !WeaponUtil.isFightModLoaded()) {
            for (LivingEntity livingEntity : player.getWorld().getNonSpectatingEntities(LivingEntity.class, target.getBoundingBox().expand(1.0, 0.25, 1.0))) {
                if (livingEntity != player
                        && livingEntity != target
                        && !player.isTeammate(livingEntity)
                        && (!(livingEntity instanceof ArmorStandEntity) || !((ArmorStandEntity)livingEntity).isMarker())
                        && player.squaredDistanceTo(livingEntity) < 9.0
                        && livingEntity.isDead()) {
                    this.handleKill(livingEntity, stack);
                }
            }
        }
        return super.postHit(stack, target, attacker);
    }

    public void handleKill(LivingEntity target, ItemStack stack) {
        if (target.getType().isIn(ModTags.Entities.BOSSES)) {
            this.addAmount(stack, 50);
        } else {
            this.addKillCounter(stack);
        }
    }

    public void addKillCounter(ItemStack stack) {
        this.addAmount(stack, 1);
    }

    public void addAmount(ItemStack stack, int amount) {
        amount += Optional.ofNullable(stack.get(ComponentRegistry.KILLS)).orElse(0);
        stack.set(ComponentRegistry.KILLS, amount);
    }

    public int getSouls(ItemStack stack) {
        return Optional.ofNullable(stack.get(ComponentRegistry.KILLS)).orElse(0);
    }
}
