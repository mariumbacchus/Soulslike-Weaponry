package net.soulsweaponry.entity.projectile.noclip;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.ModTags;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Map;

public class MoltenMetal extends NoClipEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public MoltenMetal(EntityType<? extends MoltenMetal> entityType, World world) {
        super(entityType, world);
    }

    public MoltenMetal(World world, float width, float height) {
        super(EntityRegistry.MOLTEN_METAL, world);
        this.setBoundingBoxWidth(width);
        this.setBoundingBoxHeight(height);
        this.setDespawnParticle(ParticleTypes.LAVA);
        this.setDespawnParticleCount(50);
        this.setAreaParticle(ParticleTypes.LAVA);
    }

    public MoltenMetal(World world, float width) {
        super(EntityRegistry.MOLTEN_METAL, world);
        this.setBoundingBoxWidth(width);
        this.setBoundingBoxHeight(0.3f + world.random.nextBetween(-20, 20) * 0.01f);
        this.setDespawnParticle(ParticleTypes.LAVA);
        this.setDespawnParticleCount(50);
        this.setAreaParticle(ParticleTypes.LAVA);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age % this.random.nextBetween(20, 70) == 0) {
            this.playSound(SoundEvents.BLOCK_LAVA_POP, 1f, 1f);
            if (this.age < this.getMaxAge() - 80) {
                this.playSound(SoundEvents.BLOCK_LAVA_AMBIENT, 1f, 1f);
            }
        }
        if (this.getWorld().isClient) {
            if (this.age % 3 == 0) {
                double d = this.getParticleX(0.5f);
                double e = this.getY();
                double f = this.getParticleZ(0.5f);
                this.getWorld().addParticle(this.getAreaParticle(), d, e, f, 0.0, 0.0, 0.0);
            }
        }
        List<LivingEntity> list = this.getWorld().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(0.2D));
        for (LivingEntity livingEntity : list) {
            if (!livingEntity.isAlive() || livingEntity.isInvulnerable()) {
                continue;
            }
            if (this.getOwner() != null && (livingEntity.isTeammate(this.getOwner()) || this.isOwner(livingEntity))) {
                continue;
            }
            livingEntity.damage(CustomDamageSource.create(this.getWorld(), CustomDamageSource.PLAYER_FIRE, this, this.getOwner()), (float) this.getDamage());
            livingEntity.setOnFireFor((int) ConfigConstructor.supernova_molten_metal_fire_seconds);
            ItemStack stack = livingEntity.getOffHandStack();
            if (livingEntity instanceof PlayerEntity player && stack.isIn(ConventionalItemTags.SHIELD_TOOLS) && !player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
                player.disableShield();
                stack.damage((int) ConfigConstructor.supernova_molten_metal_shield_damage, player, LivingEntity.getSlotForHand(Hand.OFF_HAND));
            }
        }
        if (this.age > this.getMaxAge() || this.getWorld().getBlockState(this.getBlockPos().down()).isAir()) {
            this.discardParticles();
            this.discard();
        }
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, this.getSoundCategory(), 1f, 0.75f, false);
        ParticleHandler.particleOutburstMap(this.getWorld(), 40, this.getX(), this.getY(), this.getZ(), Map.of(ParticleTypes.LARGE_SMOKE, new Vec3d(1, 10, 1), ParticleTypes.FLAME, new Vec3d(1, 10, 1), ParticleTypes.LAVA, new Vec3d(1, 10, 1)), 1f);
    }

    private void discardParticles() {
        for (int i = 0; i < this.getDespawnParticleCount(); i++) {
            double d = this.getParticleX(0.5f);
            double e = this.getY();
            double f = this.getParticleZ(0.5f);
            this.getWorld().addParticle(this.getDespawnParticle(), d, e, f, 0.0, 0.0, 0.0);
        }
        this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_LAVA_EXTINGUISH, this.getSoundCategory(), 1f, 0.75f, false);
    }

    @Override
    public int getMaxAge() {
        return (int) ConfigConstructor.supernova_molten_metal_max_age_ticks;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}
