package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.util.DetonateGroundAttributes;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

public interface IDetonateGround {

    default void detonateGroundEffect(LivingEntity user, int amplifier, float fallDistance, World world, ItemStack stack) {
        float expansion = this.getDetonationAttributes().baseExpansion() + this.getDetonationAttributes().expansionMod() * Math.min(this.getDetonationAttributes().maxExpansion(), fallDistance / 10);
        float power = Math.min(this.getDetonationAttributes().maxDamage(), amplifier + fallDistance * this.getDetonationAttributes().fallDistanceDamageMod()); // fallDistance was prev. divided by 5
        Box box = user.getBoundingBox().expand(expansion);
        List<Entity> entities = world.getOtherEntities(user, box);
        for (Entity targets : entities) {
            if (targets instanceof LivingEntity livingEntity) {
                if (!livingEntity.isAlive() || livingEntity.isTeammate(user)) {
                    continue;
                }
                float bonus = world instanceof ServerWorld serverWorld ? EnchantmentHelper.getDamage(serverWorld, stack, livingEntity, user.getDamageSources().mobAttack(user), 0) : 0;
                boolean canDamageTarget = livingEntity.damage(DamageSourceRegistry.create(world, DamageSourceRegistry.OBLITERATED, user), power + bonus);
                if (canDamageTarget || ConfigConstructor.calculated_fall_hits_immune_entities) {
                    livingEntity.addVelocity(0, Math.min(fallDistance * this.getDetonationAttributes().launchMod(), this.getDetonationAttributes().maxLaunchPower()), 0);
                    float healMod = this.getDetonationAttributes().healMod();
                    if (healMod > 0) user.heal(ConfigConstructor.lifesteal_item_base_healing - 1 + (ConfigConstructor.lifesteal_item_heal_scales ? power * healMod : 0)); // power was prev. divided by 10
                    this.getDetonationAttributes().onEntityDamage().accept(livingEntity, user, fallDistance);
                }
            }
        }
        this.getDetonationAttributes().onTrigger().accept(user, fallDistance, stack);
        world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1f, 1f);
        float pDistance = fallDistance >= 25 ? fallDistance/25 : 1;
        if (!world.isClient) {
            ParticleHandler.particleOutburstMap(world, MathHelper.floor(200 * pDistance), user.getX(), user.getY(), user.getZ(), ParticleEvents.BASE_GRAND_SKYFALL_MAP, pDistance);
            for (ParticleEffect particle : this.getDetonationAttributes().particles().keySet()) {
                ParticleHandler.particleOutburst(world, MathHelper.floor(200 * pDistance), user.getX(), user.getY(), user.getZ(), particle, this.getDetonationAttributes().particles().get(particle), pDistance);
            }
        }
    }

    /**
     * Called in the fall damage mixin methods. {@link net.soulsweaponry.mixin.LivingEntityMixin#interceptFallDamage(float, float, DamageSource, CallbackInfoReturnable)}
     * and {@link net.soulsweaponry.mixin.PlayerEntityMixin#interceptFallDamage(float, float, DamageSource, CallbackInfoReturnable)}.
     * <p>Callback info/cancellation of the fall damage is handled in respective methods in the mixins.</p>
     * @param entity wielder of the weapon
     * @param fallDistance distance the user fell
     * @param source damage source (most likely fall damage)
     * @return whether the event was successful, i.e. had the effect, damage was from falling and the ground was detonated
     */
    static boolean triggerCalculateFall(LivingEntity entity, float fallDistance, DamageSource source) {
        if (source.isOf(DamageTypes.FALL) && entity.hasStatusEffect(EffectRegistry.CALCULATED_FALL)) {
            StatusEffectInstance effect = entity.getStatusEffect(EffectRegistry.CALCULATED_FALL);
            Item item = WeaponRegistry.COMET_SPEAR;
            ItemStack stack = new ItemStack(item);
            for (Hand hand : Hand.values()) {
                ItemStack itemStack = entity.getStackInHand(hand);
                if (stack.getItem() instanceof IDetonateGround) {
                    item = stack.getItem();
                    stack = itemStack;
                }
            }
            ((IDetonateGround)item).detonateGroundEffect(entity, effect.getAmplifier(), fallDistance, entity.getWorld(), stack);
            entity.removeStatusEffect(EffectRegistry.CALCULATED_FALL);
            //Removes, then re-adds for half a second so that "dream_on" advancement may trigger
            entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL, 10, 0));
            return true;
        }
        return false;
    }

    DetonateGroundAttributes getDetonationAttributes();
}
