package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.config.EnchantmentConfig;
import net.soulsweaponry.entitydata.PostureData;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;

public record UltraHeavy(int postureLossPostHit, int hasteDuration, int hasteAmp) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        PostureData.addPostureLoss(target, this.postureLossPostHit);
        if (WeaponConfig.ultra_heavy_haste_when_strength && attacker.hasStatusEffect(StatusEffects.STRENGTH)) {
            attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, this.hasteDuration, this.hasteAmp));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        int postureLoss = MathHelper.floor(EnchantmentConfig.stagger_enchant_posture_loss_on_player_modifier * EnchantmentConfig.stagger_enchant_posture_loss_applied_per_level);
        postureLoss = MathHelper.floor(postureLoss * WeaponConfig.ultra_heavy_posture_loss_modifier_when_stagger_enchant);
        postureLoss *= WeaponUtil.getLevel(stack, EnchantRegistry.STAGGER);
        postureLoss += this.postureLossPostHit;
        tooltip.add(Text.translatable("tooltip.soulsweapons.heavy_weapon").formatted(Formatting.RED));
        tooltip.add(Text.translatable("tooltip.soulsweapons.heavy_weapon.description.1").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.heavy_weapon.description.2", postureLoss).formatted(Formatting.GRAY));
        if (WeaponConfig.ultra_heavy_disables_shields) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.heavy_weapon.description.3").formatted(Formatting.GRAY));
        }
        return tooltip;
    }
}
