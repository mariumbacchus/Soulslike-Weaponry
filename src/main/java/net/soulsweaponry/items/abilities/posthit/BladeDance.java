package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.NbtHelper;
import net.soulsweaponry.util.NbtIds;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public record BladeDance(float bonusDamagePerAmp, float bonusAttackSpeedPerAmp, int maxBladeDanceAmp,
                         BiConsumer<LivingEntity, ItemStack> onMaxStacksEffects,
                         int maxStacksMinCooldown, int maxStacksCooldown, int maxStacksReducedCooldownPerLvl
) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int amp = attacker.hasStatusEffect(EffectRegistry.BLADE_DANCE) ? attacker.getStatusEffect(EffectRegistry.BLADE_DANCE).getAmplifier() + 1 : 0;
        amp = Math.min(amp, this.maxBladeDanceAmp);
        attacker.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLADE_DANCE, 160, amp));
        if (!WeaponUtil.isFightModLoaded() && amp == this.maxBladeDanceAmp) {
            int counter = NbtHelper.getInt(stack, NbtIds.BLADE_DANCE_POST_HIT_COUNTER, 0);
            NbtHelper.putInt(stack, NbtIds.BLADE_DANCE_POST_HIT_COUNTER, counter + 1);
            if (counter + 1 >= 3) {
                double damage = attacker.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                for (Entity entity : attacker.getWorld().getOtherEntities(attacker, attacker.getBoundingBox().expand(2D, 1D, 2D))) {
                    if (entity instanceof LivingEntity living) {
                        living.damage(attacker.getDamageSources().mobAttack(attacker), (float) damage);
                    }
                }
                for (int i = 0; i < 360; i += 30) {
                    float r = 2f;
                    double x0 = attacker.getX();
                    double z0 = attacker.getZ();
                    double x = x0 + r * Math.cos(i * Math.PI / 180);
                    double z = z0 + r * Math.sin(i * Math.PI / 180);
                    ParticleHandler.singleParticle(attacker.getWorld(), ParticleTypes.SWEEP_ATTACK, x, attacker.getBodyY(0.5f), z, 0, 0, 0);
                }
                NbtHelper.putInt(stack, NbtIds.BLADE_DANCE_POST_HIT_COUNTER, 0);
            }
        }
    }

    public void updateBladeDanceItem(ItemStack stack, int effectAmplifier) {
        double damage = WeaponUtil.getBaseItemAttackDamage(stack);
        double attackSpeed = WeaponUtil.getBaseItemAttackSpeed(stack);
        effectAmplifier += 1;
        damage += this.bonusDamagePerAmp * effectAmplifier;
        attackSpeed += this.bonusAttackSpeedPerAmp * effectAmplifier;
        WeaponUtil.modifyStackAttributes(stack, damage, attackSpeed);
    }

    /**
     * Resets the item back to original stats.
     */
    public void resetBladeDanceItem(ItemStack stack) {
        this.updateBladeDanceItem(stack, -1);
    }

    public int totalMaxStacksEffectCooldown(ItemStack stack) {
        return Math.max(this.maxStacksMinCooldown(), this.maxStacksCooldown()
                - this.maxStacksReducedCooldownPerLvl() * WeaponUtil.getUpgradeLevel(stack));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (entity instanceof LivingEntity living) {
            int amp = -1;
            var inst = living.getStatusEffect(EffectRegistry.BLADE_DANCE);
            if (inst != null) {
                amp = inst.getAmplifier();
            }
            updateBladeDanceItem(stack, amp);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        int cooldown = this.totalMaxStacksEffectCooldown(stack);
        List<Text> tooltip = new ArrayList<>(List.of(
                Text.translatable("tooltip.soulsweapons.blade_dance").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.blade_dance.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.blade_dance.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.blade_dance.3").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.blade_dance.4").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.blade_dance.5", this.maxBladeDanceAmp).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.blade_dance.6", MathHelper.floor(cooldown / 20f)).formatted(Formatting.GRAY)
        ));
        if (!WeaponUtil.isFightModLoaded()) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.blade_dance.7").formatted(Formatting.GRAY));
        }
        return tooltip;
    }
}
