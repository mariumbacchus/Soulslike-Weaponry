package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.TooltipAbilities;

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
        if (!FabricLoader.getInstance().isModLoaded("bettercombat") && (amp + 1) == this.getMaxStacks() && stack.hasNbt()) {
            int counter = stack.getNbt().contains("AOECounter") ? stack.getNbt().getInt("AOECounter") : 0;
            stack.getNbt().putInt("AOECounter", counter + 1);
            if (stack.getNbt().getInt("AOECounter") >= 3) {
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
                stack.getNbt().putInt("AOECounter", 0);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.getTotalDamage(stack), EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", this.getTotalAttackSpeed(stack), EntityAttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot);
        }
    }

    public static void updateBladeDanceItem(ItemStack stack, int effectAmplifier) {
        if (stack.getItem() instanceof BladeDanceItem item && stack.hasNbt()) {
            stack.getNbt().putFloat("BladeDanceBonusDamage", item.getBonusDamagePerStack() * effectAmplifier);
            stack.getNbt().putFloat("BladeDanceBonusAttackSpeed", item.getBonusAttackSpeedPerStack() * effectAmplifier);
        }
    }

    public abstract float getBonusDamagePerStack();
    public abstract float getBonusAttackSpeedPerStack();
    public abstract int getMaxStacks();
    public abstract void applyMaxStacksEffects(LivingEntity entity, ItemStack stack);
    public abstract int getMaxStacksCooldown();

    public float getTotalDamage(ItemStack stack) {
        float damage = this.getAttackDamage();
        if (stack.hasNbt() && stack.getNbt().contains("BladeDanceBonusDamage")) {
            damage += stack.getNbt().getFloat("BladeDanceBonusDamage");
        }
        return damage;
    }

    public float getTotalAttackSpeed(ItemStack stack) {
        float attackSpeed = this.getAttackSpeed();
        if (stack.hasNbt() && stack.getNbt().contains("BladeDanceBonusAttackSpeed")) {
            attackSpeed += stack.getNbt().getFloat("BladeDanceBonusAttackSpeed");
        }
        return attackSpeed;
    }
}
