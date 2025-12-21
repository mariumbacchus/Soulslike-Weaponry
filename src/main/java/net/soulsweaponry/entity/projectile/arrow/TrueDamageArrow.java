package net.soulsweaponry.entity.projectile.arrow;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EntityRegistry;

/**
 * Used in {@link net.soulsweaponry.items.bow.KrakenSlayer} and {@link net.soulsweaponry.items.crossbow.KrakenSlayerCrossbow}
 */
public class TrueDamageArrow extends ModArrow {
    private float trueDamage;

    public TrueDamageArrow(EntityType<? extends ArrowEntity> entityType, World world) {
        super(entityType, world);
    }

    public TrueDamageArrow(World world, LivingEntity owner, ItemStack arrowStack, ItemStack weaponStack) {
        super(EntityRegistry.KRAKEN_SLAYER_PROJECTILE, owner, world, arrowStack, weaponStack);
    }

    public void setTrueDamage(float trueDamage) {
        this.trueDamage = trueDamage;
    }

    public float getTrueDamage() {
        return trueDamage;
    }

    public void tick() {
        if (!this.isInGround()) {
            Vec3d vec3d = this.getVelocity();
            double e = vec3d.x;
            double f = vec3d.y + 0.2f;
            double g = vec3d.z;
            for (int i = 0; i < 4; ++i) {
                this.getWorld().addParticle(this.getParticleType(), this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D, this.getZ() + g * (double)i / 4.0D, 0, 0, 0); //-e, -f + 0.2D, -g
            }
        }
        super.tick();
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.FIREWORK;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("trueDamage")) {
            this.trueDamage = nbt.getFloat("trueDamage");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putFloat("trueDamage", this.trueDamage);
    }

    @Override
    public boolean canHaveArrowEffects() {
        return ConfigConstructor.kraken_slayer_can_apply_arrow_effects;
    }
}
