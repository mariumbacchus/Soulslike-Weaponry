package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleNetworking;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;

import java.util.List;

public abstract class AbstractDawnbreaker extends SwordItem implements IAnimatable {

    public AbstractDawnbreaker(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(4 + 3 * EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack));
        if (target.isUndead() || ConfigConstructor.dawnbreaker_affect_all_entities) {
            if (target.isDead()) {
                if (target.hasStatusEffect(EffectRegistry.RETRIBUTION)) {
                    double chance = ConfigConstructor.dawnbreaker_ability_chance_modifier + 1 - (Math.pow(.75, target.getStatusEffect(EffectRegistry.RETRIBUTION).getAmplifier()));
                    double random = target.getRandom().nextDouble();
                    if (random < chance) {
                        if (!attacker.world.isClient && attacker instanceof ServerPlayerEntity) {
                            ParticleNetworking.sendServerParticlePacket((ServerWorld) attacker.world, PacketRegistry.DAWNBREAKER_PACKET_ID, target.getBlockPos());
                        }
                        target.world.playSound(null, target.getBlockPos(), SoundRegistry.DAWNBREAKER_EVENT, SoundCategory.HOSTILE, 2f, 1f);
                        Box aoe = target.getBoundingBox().expand(10);
                        List<Entity> entities = attacker.getWorld().getOtherEntities(target, aoe);
                        boolean bl = ConfigConstructor.dawnbreaker_affect_all_entities;
                        for (Entity entity : entities) {
                            if (entity instanceof LivingEntity targetHit) {
                                if (targetHit.isUndead() || bl) {
                                    if (!targetHit.equals(attacker)) {
                                        targetHit.setOnFireFor(4 + EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack));
                                        targetHit.damage(DamageSource.explosion(attacker), ConfigConstructor.dawnbreaker_ability_damage + 5 * EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack));
                                        targetHit.addStatusEffect(new StatusEffectInstance(EffectRegistry.FEAR, 80, 0));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (target.hasStatusEffect(EffectRegistry.RETRIBUTION)) {
                int amplifier = target.getStatusEffect(EffectRegistry.RETRIBUTION).getAmplifier();
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.RETRIBUTION, 80, amplifier + 1));
            } else {
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.RETRIBUTION, 80, EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack)));
            }
        }
        return super.postHit(stack, target, attacker);
    }
}