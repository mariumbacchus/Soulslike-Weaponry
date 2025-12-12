package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.soulsweaponry.entity.projectile.noclip.ArrowStormEntity;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Send out a cloud spawning tons of Moonlight Arrows. The damage is divided by the velocityFix to accurately
 * set the resulting total damage (since bonus damage is done based on arrow velocity).
 */
public record ArrowStorm(
        float projectileDamage, float bonusDamagePerLvl,
        int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IKeybindAbility {

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (!this.isCoolingDown(player, stack)) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            float damage = this.projectileDamage + this.bonusDamagePerLvl * lvl;
            ArrowStormEntity entity = new ArrowStormEntity(EntityRegistry.ARROW_STORM_ENTITY, world);
            entity.setPos(player.getX(), player.getY() + 4.5F, player.getZ());
            entity.setVelocity(player, 0, player.getYaw(), 0.0F, 1f, 1.0F);
            entity.setOwner(player);
            entity.setDamage(damage);
            entity.setMaxArrowAge(40);
            world.spawnEntity(entity);

            this.applyItemCooldown(stack, player, Math.max(this.minCooldown, this.cooldown - lvl * this.reducedCooldownPerLvl));
            stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
            world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, 1f, 1f);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.arrow_storm").formatted(Formatting.DARK_PURPLE),
                Text.translatable("tooltip.soulsweapons.arrow_storm.1").formatted(Formatting.GRAY)
        );
    }
}
