package net.soulsweaponry.items.abilities.inventorytick;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record SoulOfCinder(int duration, int durationPerLvl, int amp, float ampPerLvl) implements IAbility {

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (selected && entity instanceof LivingEntity living && living.age % 20 == 0 && living.isOnFire()) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            living.addStatusEffect(new StatusEffectInstance(EffectRegistry.SOUL_OF_CINDER, this.duration + this.durationPerLvl * lvl, (int) (this.amp + this.ampPerLvl * lvl)));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.soul_of_cinder").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.soul_of_cinder.1").formatted(Formatting.GRAY)
        );
    }
}
