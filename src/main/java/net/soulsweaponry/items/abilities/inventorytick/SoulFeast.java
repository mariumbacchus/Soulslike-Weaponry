package net.soulsweaponry.items.abilities.inventorytick;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.IAnimatedDeath;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record SoulFeast(float range, float bonusRangePerLvl, float heal, float healPerLvl) implements IAbility {

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof PlayerEntity player) {
            if (player.getWorld().isClient) {
                return;
            }
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            float range = this.range + this.bonusRangePerLvl * lvl;
            float heal = this.heal + this.healPerLvl * lvl;
            for (Entity target : player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(range))) { //TODO IAnimatedDeaths can be remade
                if (target instanceof LivingEntity living && living.isDead() && (living instanceof IAnimatedDeath animatedDeath ? animatedDeath.getDeathTicks() == 1 : living.deathTime == 1)) {
                    player.heal(heal);
                }
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.soul_feast").formatted(Formatting.RED),
                Text.translatable("tooltip.soulsweapons.soul_feast.1").formatted(Formatting.GRAY)
        );
    }
}
