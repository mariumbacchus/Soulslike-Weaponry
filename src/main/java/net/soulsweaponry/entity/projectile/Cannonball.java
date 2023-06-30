package net.soulsweaponry.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class Cannonball extends NonArrowProjectile implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private int life;

    public Cannonball(EntityType<? extends Cannonball> entityType, World world) {
        super(entityType, world);
    }

    public Cannonball(World world, LivingEntity owner) {
        super(EntityRegistry.CANNONBALL, owner, world);
    }

    public Cannonball(EntityType<? extends Cannonball> type, double x, double y, double z,
            World world) {
        super(type, x, y, z, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.inGround) {
            Vec3d vec3d = this.getVelocity();
            double e = vec3d.x;
            double f = vec3d.y;
            double g = vec3d.z;
            for (int i = 0; i < 2; ++i) {
                this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D, this.getZ() + g * (double)i / 4.0D, -e*0.2, (-f + 0.2D)*0.2, -g*0.2);
            }
        }
        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.x, vec3d.y + (double)0.045f, vec3d.z);
    }

    @Override
    protected void age() {
        ++this.life;
        if (this.life >= 600) {
            this.discard();
        }
    }

    public boolean isFireImmune() {
        return true;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (entityHitResult.getEntity() instanceof LivingEntity target && this.getOwner() instanceof PlayerEntity) {
            int random = this.random.nextInt(10) + 1;
            int level = EnchantmentHelper.getLevel(EnchantRegistry.VISCERAL, ((PlayerEntity )this.getOwner()).getMainHandStack());

            if (random < 4 + level * 3) {
                if (!target.hasStatusEffect(EffectRegistry.POSTURE_BREAK)) {
                    target.getWorld().playSound(null, target.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT, SoundCategory.PLAYERS, .5f, 1f);
                }
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK, 60, level));
            }
        }
        this.discard();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }
    
}
