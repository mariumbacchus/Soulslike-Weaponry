package net.soulsweaponry.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Optional;

public abstract class BladeDanceItem extends ModdedSword {

    public BladeDanceItem(ToolMaterial toolMaterial, int attackDamage, float ingameAttackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, ingameAttackSpeed, settings);
        this.addTooltipAbility(TooltipAbilities.GLAIVE_DANCE);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        int amp = attacker.hasStatusEffect(EffectRegistry.BLADE_DANCE) ? attacker.getStatusEffect(EffectRegistry.BLADE_DANCE).getAmplifier() + 1 : 0;
        amp = Math.min(amp, this.getMaxStacks() - 1);
        attacker.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLADE_DANCE, 160, amp));
        if (!WeaponUtil.isFightModLoaded() && (amp + 1) == this.getMaxStacks()) {
            int counter = Optional.ofNullable(stack.get(ComponentRegistry.AMOUNT_USED)).orElse(0);
            stack.set(ComponentRegistry.AMOUNT_USED, counter + 1);
            if (Optional.ofNullable(stack.get(ComponentRegistry.AMOUNT_USED)).orElse(0) >= 3) {
                for (Entity entity : attacker.getWorld().getOtherEntities(attacker, attacker.getBoundingBox().expand(2D, 1D, 2D))) {
                    if (entity instanceof LivingEntity living) {
                        living.damage(attacker.getDamageSources().mobAttack(attacker), this.getTotalDamage(stack));
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
                stack.set(ComponentRegistry.AMOUNT_USED, 0);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (this.isDisabled(stack)) return;
        WeaponUtil.modifyStackAttributes(stack, this.getTotalDamage(stack), this.getTotalAttackSpeed(stack));
    }

    public static void updateBladeDanceItem(ItemStack stack, int effectAmplifier) {
        if (stack.getItem() instanceof BladeDanceItem item) {
            stack.set(ComponentRegistry.BLADE_DANCE_BONUS_DAMAGE, item.getBonusDamagePerStack() * effectAmplifier);
            stack.set(ComponentRegistry.BLADE_DANCE_BONUS_ATTACK_SPEED, item.getBonusAttackSpeedPerStack() * effectAmplifier);
        }
    }

    public abstract float getBonusDamagePerStack();
    public abstract float getBonusAttackSpeedPerStack();
    public abstract int getMaxStacks();
    public abstract void applyMaxStacksEffects(LivingEntity entity, ItemStack stack);
    public abstract int getMaxStacksCooldown();

    public float getTotalDamage(ItemStack stack) {
        float damage = this.getAttackDamage();
        damage += Optional.ofNullable(stack.get(ComponentRegistry.BLADE_DANCE_BONUS_DAMAGE)).orElse(0f);
        return damage;
    }

    public float getTotalAttackSpeed(ItemStack stack) {
        float attackSpeed = this.getAttackSpeed();
        attackSpeed += Optional.ofNullable(stack.get(ComponentRegistry.BLADE_DANCE_BONUS_ATTACK_SPEED)).orElse(0f);
        return attackSpeed;
    }
}
