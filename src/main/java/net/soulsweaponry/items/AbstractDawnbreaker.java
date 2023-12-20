package net.soulsweaponry.items;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.AABB;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.List;

public abstract class AbstractDawnbreaker extends SwordItem implements IAnimatable {

    public AbstractDawnbreaker(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setSecondsOnFire(4 + 3 * EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack));
        if (target.getMobType() == MobType.UNDEAD || CommonConfig.DAWNBREAKER_ABILITY_AFFECT_ALL.get()) {
            if (target.isDeadOrDying()) {
                if (target.hasEffect(EffectRegistry.RETRIBUTION.get())) {
                    double chance = CommonConfig.DAWNBREAKER_ABILITY_CHANCE_MOD.get() + 1 - (Math.pow(.75, target.getEffect(EffectRegistry.RETRIBUTION.get()).getAmplifier()));
                    double random = target.getRandom().nextDouble();
                    if (random < chance) {
                        AbstractDawnbreaker.dawnbreakerEvent(target, attacker, stack);
                    }
                }
            }
            if (target.hasEffect(EffectRegistry.RETRIBUTION.get())) {
                int amplifier = target.getEffect(EffectRegistry.RETRIBUTION.get()).getAmplifier();
                target.addEffect(new MobEffectInstance(EffectRegistry.RETRIBUTION.get(), 80, amplifier + 1));
            } else {
                target.addEffect(new MobEffectInstance(EffectRegistry.RETRIBUTION.get(), 80, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack)));
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    /**
     * Excecutes the dawnbreaker explosion.
     * @param target the position of where the explosion and sound will come from the target's position
     * @param attacker damage and effects will not apply to attacker
     * @param stack used to gather damage buffs
     */
    public static void dawnbreakerEvent(LivingEntity target, LivingEntity attacker, ItemStack stack) {
//        if (!attacker.world.isClient && attacker instanceof ServerPlayerEntity) { TODO implement particle handling
//            ParticleNetworking.sendServerParticlePacket((ServerWorld) attacker.world, PacketRegistry.DAWNBREAKER_PACKET_ID, target.getBlockPos());
//        }
        target.playSound(SoundRegistry.DAWNBREAKER_EVENT.get(), 2f, 1f);
        AABB aoe = target.getBoundingBox().inflate(10);
        List<Entity> entities = attacker.getLevel().getEntities(target, aoe);
        boolean bl = CommonConfig.DAWNBREAKER_ABILITY_AFFECT_ALL.get();
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity targetHit) {
                if (targetHit.getMobType() == MobType.UNDEAD || bl) {//TODO implement this in weapon util maybe?
                    if (!targetHit.equals(attacker)) {
                        targetHit.setSecondsOnFire(4 + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack));
                        targetHit.hurt(DamageSource.explosion(attacker), CommonConfig.DAWNBREAKER_ABILITY_DAMAGE.get() + 5 * EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack));
                        targetHit.addEffect(new MobEffectInstance(EffectRegistry.FEAR.get(), 80, 0));
                    }
                }
            }
        }
    }
}
