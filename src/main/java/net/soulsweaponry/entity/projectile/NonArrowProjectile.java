package net.soulsweaponry.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public abstract class NonArrowProjectile extends ModPersistentProjectile {

    private DamageSource damageSource = null;

    public NonArrowProjectile(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public NonArrowProjectile(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world) {
        this(type, world);
        this.setPosition(x, y, z);
    }

    public NonArrowProjectile(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack) {
        this(type, owner.getX(), owner.getEyeY() - (double)0.1f, owner.getZ(), world);
        this.setOwner(owner);
        if (owner instanceof PlayerEntity) {
            this.pickupType = PickupPermission.ALLOWED;
        }
        this.setItemStack(stack);
    }

    public void setCustomDamageSource(DamageSource damageSource) {
        this.damageSource = damageSource;
    }

    @Nullable
    public DamageSource getCustomDamageSource() {
        return this.damageSource;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        DamageSource damageSource;
        Entity owner = this.getOwner();
        Entity entity = entityHitResult.getEntity();
        float f = (float)this.getVelocity().length();
        int i = MathHelper.ceil(MathHelper.clamp((double)f * this.getDamage(), 0.0, 2.147483647E9));
        if (this.isCritical()) {
            long l = this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(l + (long)i, Integer.MAX_VALUE);
        }
        if (this.damageSource != null) {
            damageSource = this.damageSource;
        } else {
            damageSource = this.getWorld().getDamageSources().arrow(this, Objects.requireNonNullElse(owner, this));
        }
        if (owner != null) {
            if (owner instanceof LivingEntity livingOwner) {
                livingOwner.onAttacking(entity);
            }
        }
        boolean bl = entity.getType() == EntityType.ENDERMAN;
        int j = entity.getFireTicks();
        if (this.isOnFire() && !bl) {
            entity.setOnFireFor(5);
        }
        if (entity.damage(damageSource, i)) {
            if (bl) {
                return;
            }
            if (entity instanceof LivingEntity livingEntity) {
                if (this.getPunch() > 0) {
                    double d = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                    Vec3d vec3d = this.getVelocity().multiply(1.0, 0.0, 1.0).normalize().multiply((double)this.getPunch() * 0.6 * d);
                    if (vec3d.lengthSquared() > 0.0) {
                        livingEntity.addVelocity(vec3d.x, 0.1, vec3d.z);
                    }
                }
                if (!this.getWorld().isClient && owner instanceof LivingEntity livingOwner) {
                    EnchantmentHelper.onUserDamaged(livingEntity, livingOwner);
                    EnchantmentHelper.onTargetDamaged(livingOwner, livingEntity);
                }
                this.onHit(livingEntity);
                if (livingEntity != owner && livingEntity instanceof PlayerEntity && owner instanceof ServerPlayerEntity serverPlayerOwner && !this.isSilent()) {
                    serverPlayerOwner.networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
                }
            }
            this.playSound(this.getSound(), 1.0f, 1.0f - (this.random.nextFloat() / 2f));
            if (this.getPierceLevel() <= 0) {
                this.discard();
            }
        } else {
            entity.setFireTicks(j);
            this.setVelocity(this.getVelocity().multiply(-0.1));
            this.setYaw(this.getYaw() + 180.0f);
            this.prevYaw += 180.0f;
            if (!this.getWorld().isClient && this.getVelocity().lengthSquared() < 1.0E-7) {
                if (this.pickupType == PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1f);
                }
                this.discard();
            }
        }
    }
}