package net.soulsweaponry.entity.projectile;

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
import org.jetbrains.annotations.Nullable;

public class DragonslayerSwordspearEntity extends PersistentProjectileEntity {
    
    private static final TrackedData<Boolean> ENCHANTED;
    private boolean dealtDamage;

    public DragonslayerSwordspearEntity(EntityType<? extends DragonslayerSwordspearEntity> entityType, World world, ItemStack stack) {
        super(entityType, world, stack);
    }

    public DragonslayerSwordspearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.SWORDSPEAR_ENTITY_TYPE, owner, world, stack);
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
    }

    public boolean isEnchanted() {
        return this.dataTracker.get(ENCHANTED);
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
            f += EnchantmentHelper.getAttackDamage(this.asItemStack(), ((LivingEntity) entity).getGroup());
        }
        Entity entity2 = this.getOwner();
        DamageSource damageSource = this.getWorld().getDamageSources().lightningBolt();
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
        if (!getWorld().isClient) {
            BlockPos blockPos = entity.getBlockPos();
            if (this.getWorld().isSkyVisible(blockPos)) {
                for (int i = 0; i < ConfigConstructor.dragonslayer_swordspear_lightning_amount; i++) {
                    LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.getWorld());
                    lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                    lightningEntity.setChanneler(entity2 instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity2 : null);
                    this.getWorld().spawnEntity(lightningEntity);
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
