package net.soulsweaponry.items.abilities.inventorytick;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

/**
 * @param activeThreshold % the users health should be at to give effects
 * @param resistanceAmp
 * @param resistanceAmpPerLvl
 * @param magicResistAmp
 * @param magicResistAmpPerLvl
 */
public record HalfHealthResistances(
        float activeThreshold, float bonusActivateThresholdPerLvl,
        int resistanceAmp, float resistanceAmpPerLvl, int magicResistAmp, float magicResistAmpPerLvl
) implements IAbility {

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        if (entity instanceof PlayerEntity player && player.getHealth() <= player.getMaxHealth() * this.getThreshold(lvl)) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, (int) (this.resistanceAmp + this.resistanceAmpPerLvl * lvl), false, false));
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.MAGIC_RESISTANCE.get(), 40, (int) (this.magicResistAmp + this.magicResistAmpPerLvl * lvl), false, false));
        }
    }

    public float getThreshold(int lvl) {
        return this.activeThreshold + this.bonusActivateThresholdPerLvl * lvl;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        MutableText health = Text.of(String.format("%.0f", this.getThreshold(WeaponUtil.getUpgradeLevel(stack)) * 100) + "%").copy();
        return List.of(
                Text.translatable("tooltip.soulsweapons.unbreakable_effect").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.unbreakable_effect.1", health.formatted(Formatting.RED)).formatted(Formatting.GRAY)
        );
    }
}
