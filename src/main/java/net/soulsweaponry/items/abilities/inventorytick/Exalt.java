package net.soulsweaponry.items.abilities.inventorytick;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record Exalt(float ampPerMissingHealthPercent, float bonusPerLvl, int maxAmp, float maxPerLvl, int duration) implements IAbility {

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof LivingEntity living) {
            if (living.isOnFire() && living.age % 20 == 0) {
                living.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0, false, false));
            }
            if (living.getHealth() < living.getMaxHealth()) {
                int amp = this.getExaltAmplifier(living, stack);
                boolean visible = amp > 2 + this.maxPerLvl * WeaponUtil.getUpgradeLevel(stack);
                living.addStatusEffect(new StatusEffectInstance(EffectRegistry.EXALTED, this.duration, amp, false, visible));
            }
        }
    }

    public int getExaltAmplifier(LivingEntity user, ItemStack stack) {
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        float health = user.getHealth();
        float maxHealth = user.getMaxHealth();
        if (maxHealth <= 0f) {
            return 0;
        }
        float healthPct = (health / maxHealth) * 100f;
        float missingPct = 100f - healthPct;
        float rawAmp = missingPct * (this.ampPerMissingHealthPercent + this.bonusPerLvl * lvl);

        int amp = (int) Math.floor(rawAmp);
        int maxAmp = (int) (this.maxAmp + this.maxPerLvl * lvl);
        if (amp < 0) amp = 0;
        if (amp > maxAmp) amp = maxAmp;

        return amp;
    }


    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.exalt").formatted(Formatting.RED),
                Text.translatable("tooltip.soulsweapons.exalt.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.exalt.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.exalt.3").formatted(Formatting.GRAY)
        );
    }
}
