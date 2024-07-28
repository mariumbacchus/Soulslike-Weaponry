package net.soulsweaponry.entity.projectile;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.mixin.PersistentProjectileMixin;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ParticleRegistry;

import java.util.List;

public class MoonlightArrow extends PersistentProjectileEntity {

    private int maxArrowAge = 1000;

    public MoonlightArrow(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public MoonlightArrow(World world, LivingEntity owner) {
        super(EntityRegistry.MOONLIGHT_ARROW, owner, world);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public void tick() {
        if (!this.inGround) {
            Vec3d vec3d = this.getVelocity();
            double e = vec3d.x;
            double f = vec3d.y + 0.2f;
            double g = vec3d.z;
            for (int i = 0; i < 4; ++i) {
                this.getWorld().addParticle(this.getParticleType(), this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D, this.getZ() + g * (double)i / 4.0D, 0, 0, 0); //-e, -f + 0.2D, -g
            }
        }
        super.tick();
        if (this.age > this.maxArrowAge) {
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        IntOpenHashSet piercedEntities = ((PersistentProjectileMixin) this).getPiercedEntities();
        List<Entity> piercingKilledEntities = ((PersistentProjectileMixin) this).getPiercingKilledEntities();
        DamageSource damageSource;
        Entity entity2;
        Entity entity = entityHitResult.getEntity();
        if (entity != null && this.isOwner(entity)) {
            this.discard();
            return;
        }
        float f = (float)this.getVelocity().length();
        int i = MathHelper.ceil(MathHelper.clamp((double)f * this.getDamage(), 0.0, 2.147483647E9));
        if (this.getPierceLevel() > 0) {
            if (piercedEntities == null) {
                piercedEntities = new IntOpenHashSet(5);
            }
            if (piercingKilledEntities == null) {
                ((PersistentProjectileMixin) this).setPiercingKilledEntities(Lists.newArrayListWithCapacity(5));
            }
            if (piercedEntities.size() < this.getPierceLevel() + 1) {
                piercedEntities.add(entity.getId());
                ((PersistentProjectileMixin) this).setPiercedEntities(piercedEntities);
            } else {
                this.discard();
                return;
            }
        }
        if (this.isCritical()) {
            long l = this.random.nextInt(i / 2 + 2);
            i = (int)Math.min(l + (long)i, Integer.MAX_VALUE);
        }
        if ((entity2 = this.getOwner()) == null) {
            damageSource = DamageSource.magic(this, this);
        } else {
            damageSource = DamageSource.magic(this, entity2);
            if (entity2 instanceof LivingEntity) {
                ((LivingEntity)entity2).onAttacking(entity);
            }
        }
        boolean enderman = entity.getType() == EntityType.ENDERMAN;
        int j = entity.getFireTicks();
        if (this.isOnFire() && !enderman) {
            entity.setOnFireFor(5);
        }
        if (enderman) {
            return;
        }
        if (entity.damage(damageSource, i)) {
            if (entity instanceof LivingEntity livingEntity) {
                Vec3d vec3d;
                if (!this.world.isClient && this.getPierceLevel() <= 0) {
                    livingEntity.setStuckArrowCount(livingEntity.getStuckArrowCount() + 1);
                }
                if (this.getPunch() > 0 && (vec3d = this.getVelocity().multiply(1.0, 0.0, 1.0).normalize().multiply((double)this.getPunch() * 0.6)).lengthSquared() > 0.0) {
                    livingEntity.addVelocity(vec3d.x, 0.1, vec3d.z);
                }
                if (!this.world.isClient && entity2 instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity);
                }
                this.onHit(livingEntity);
                if (livingEntity != entity2 && livingEntity instanceof PlayerEntity && entity2 instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity)entity2).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
                }
                if (!entity.isAlive() && piercingKilledEntities != null) {
                    piercingKilledEntities.add(livingEntity);
                    ((PersistentProjectileMixin) this).setPiercingKilledEntities(piercingKilledEntities);
                }
                if (!this.world.isClient && entity2 instanceof ServerPlayerEntity serverPlayerEntity) {
                    if (piercingKilledEntities != null && this.isShotFromCrossbow()) {
                        Criteria.KILLED_BY_CROSSBOW.trigger(serverPlayerEntity, piercingKilledEntities);
                    } else if (!entity.isAlive() && this.isShotFromCrossbow()) {
                        Criteria.KILLED_BY_CROSSBOW.trigger(serverPlayerEntity, List.of(entity));
                    }
                }
            }
            this.playSound(this.getSound(), 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
            if (this.getPierceLevel() <= 0) {
                this.discard();
            }
        } else {
            entity.setFireTicks(j);
            this.setVelocity(this.getVelocity().multiply(-0.1));
            this.setYaw(this.getYaw() + 180.0f);
            this.prevYaw += 180.0f;
            if (!this.world.isClient && this.getVelocity().lengthSquared() < 1.0E-7) {
                if (this.pickupType == PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1f);
                }
                this.discard();
            }
        }
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        if (this.maxArrowAge > 100) {
            return super.tryPickup(player);
        } else {
            return false;
        }
    }

    protected ParticleEffect getParticleType() {
        return ParticleRegistry.NIGHTFALL_PARTICLE;
    }

    @Override
    protected ItemStack asItemStack() {
        return Items.ARROW.getDefaultStack();
    }

    public void setMaxArrowAge(int maxArrowAge) {
        this.maxArrowAge = maxArrowAge;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("maxArrowAge")) {
            this.maxArrowAge = nbt.getInt("maxArrowAge");
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("maxArrowAge", this.maxArrowAge);
    }
}