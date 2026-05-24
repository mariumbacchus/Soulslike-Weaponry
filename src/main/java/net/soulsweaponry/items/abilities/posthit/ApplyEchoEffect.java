package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.entitydata.EchoDamageData;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record ApplyEchoEffect(
        int duration, float durationPerLvl, int amp, float ampPerLvl,
        float savedDamageMod, float savedDamageAddedModPerLvl,
        int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof PlayerEntity player && !this.isCoolingDown(player, stack) && !target.hasStatusEffect(EffectRegistry.ECHO.get())) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            int duration = (int) (this.duration + this.durationPerLvl * lvl);
            int amp = (int) (this.amp + this.ampPerLvl * lvl);
            float savedDamageMod = this.savedDamageMod + this.savedDamageAddedModPerLvl * lvl;
            EchoDamageData.setEchoDamageSavedMod(target, savedDamageMod);
            target.addStatusEffect(new StatusEffectInstance(EffectRegistry.ECHO.get(), duration, amp));
            this.applyItemCooldown(stack, player, Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * lvl));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.echo").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD),
                Text.translatable("tooltip.soulsweapons.echo.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.echo.2").formatted(Formatting.GRAY)
        );
    }
}
