package net.soulsweaponry.entity.projectile;

import org.jetbrains.annotations.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

public class DragonslayerSwordspearEntity extends PersistentProjectileEntity {
    
    private static final TrackedData<Boolean> ENCHANTED;
    private ItemStack spearStack;
    private boolean dealtDamage;
    public int returnTimer;

    public DragonslayerSwordspearEntity(EntityType<? extends DragonslayerSwordspearEntity> entityType, World world) {
        super(entityType, world);
        this.spearStack = new ItemStack(WeaponRegistry.DRAGONSLAYER_SWORDSPEAR);
    }

    public DragonslayerSwordspearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.SWORDSPEAR_ENTITY_TYPE, owner, world);
        this.spearStack = stack.copy();
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ENCHANTED, false);
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if (this.age > 60) {
            this.remove(RemovalReason.DISCARDED);
        }
        super.tick();

        //Returning funksjon was changed to Infinity to make Mjolnir more unique
        /* Entity entity = this.getOwner();
        if (this.dealtDamage || this.isNoClip() && entity != null) {
            if (!this.isOwnerAlive()) {
                if (!this.world.isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1F);
                }

                //this.discard();
            } else if (entity != null) {
                this.setNoClip(true);
                Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
                this.setPos(this.getX(), this.getY() + vec3d.y*0.015D*3D, this.getZ());
                if (this.world.isClient) {
                    this.lastRenderY = this.getY();
                }

                double d = 0.05D * 3D;
                this.setVelocity(this.getVelocity().multiply(0.95D).add(vec3d.normalize().multiply(d)));
                if (this.returnTimer == 0) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
                }
                ++this.returnTimer;
            }
        } */
    }

    /* private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
           return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
        } else {
           return false;
        }
    } */

    protected ItemStack asItemStack() {
        return this.spearStack.copy();
    }

    public boolean isEnchanted() {
        return (Boolean)this.dataTracker.get(ENCHANTED);
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = ConfigConstructor.dragonslayer_swordspear_projectile_damage;
        if (this.getOwner() == null || entity == null) return;
        if (entity instanceof LivingEntity) {
            f += EnchantmentHelper.getAttackDamage(this.spearStack, ((LivingEntity) entity).getGroup());
        }
        Entity entity2 = this.getOwner();
        DamageSource damageSource = this.world.getDamageSources().lightningBolt();
        this.dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_HIT;
        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity livingEntity2) {
                if (entity2 instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity2, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity2);
                }
                this.onHit(livingEntity2);
            }
        }

        this.setVelocity(this.getVelocity().multiply(-0.01D, -0.1D, -0.01D));
        float g = 1.0F;
        if (!world.isClient) {
            BlockPos blockPos = entity.getBlockPos();
            if (this.world.isSkyVisible(blockPos)) {
                for (int i = 0; i < ConfigConstructor.dragonslayer_swordspear_lightning_amount; i++) {
                    LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.world);
                    lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                    lightningEntity.setChanneler(entity2 instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity2 : null);
                    this.world.spawnEntity(lightningEntity);
                    soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
                    g = 5.0F;
                }
            }
        }
        this.playSound(soundEvent, g, 1.0F);
    }

    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    protected float getDragInWater() {
        return 0.99F;
    }

    static {
        ENCHANTED = DataTracker.registerData(DragonslayerSwordspearEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
    
    @Override
    public boolean isFireImmune() {
        return true;
    }
}
