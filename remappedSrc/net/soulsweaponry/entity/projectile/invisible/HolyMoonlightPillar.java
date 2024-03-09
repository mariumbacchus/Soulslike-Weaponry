package net.soulsweaponry.entity.projectile.invisible;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.List;
import java.util.Random;

public class HolyMoonlightPillar extends InvisibleWarmupEntity {

    private float knockUp = ConfigConstructor.holy_moonlight_ability_knockup;

    public HolyMoonlightPillar(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.method_48926().isClient) {
            this.setWarmup(this.getWarmup() - 1);
            if (this.getWarmup() < 0) {
                if (this.getWarmup() == -7) {
                    List<LivingEntity> list = this.method_48926().getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(0.2D));
                    for (LivingEntity livingEntity : list) {
                        this.damage(livingEntity);
                    }
                    this.method_48926().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
                    this.method_48926().playSound(null, this.getBlockPos(), SoundRegistry.MOONLIGHT_SMALL_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                    this.discard();
                }
            }
        }
    }

    @Override
    public void onRemoved() {
        Random random = new Random();
        double d = random.nextGaussian() * 0.05D;
        double e = random.nextGaussian() * 0.05D;
        double f = random.nextGaussian() * 0.05D;
        for(int j = 0; j < 200; ++j) {
            double newX = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + d;
            double newZ = random.nextDouble() - 0.5D + random.nextGaussian() * 0.15D + e;
            double newY = random.nextDouble() - 0.5D + random.nextGaussian() * 0.5D + f;
            this.method_48926().addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY(), this.getZ(), newX / 2, newY / 0.5f, newZ / 2);
            this.method_48926().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY(), this.getZ(), newX / 2, newY / 0.5f, newZ / 2);
        }
        super.onRemoved();
    }

    private void damage(LivingEntity target) {
        Entity entity = this.getOwner();
        if (target.isAlive() && !target.isInvulnerable() && entity instanceof LivingEntity livingEntity && target != livingEntity) {
            if (!livingEntity.isTeammate(target)) {
                target.damage(this.getDamageSources().mobAttack((LivingEntity)entity), (float) this.getDamage() + 2 * EnchantmentHelper.getAttackDamage(this.getStack(), target.getGroup()));
                target.addVelocity(0, this.getKnockup(), 0);
            }
        }
    }

    private float getKnockup() {
        return this.knockUp;
    }

    public void setKnockUp(float knockUp) {
        this.knockUp = knockUp;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Knockup")) {
            this.knockUp = nbt.getFloat("Knockup");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("Knockup", this.knockUp);
    }
}
