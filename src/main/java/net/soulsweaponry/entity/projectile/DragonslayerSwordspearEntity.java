package net.soulsweaponry.entity.projectile;

import net.minecraft.server.world.ServerWorld;
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
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

public class DragonslayerSwordspearEntity extends ModPersistentProjectile {
    
    private static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(DragonslayerSwordspearEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private boolean dealtDamage;

    public DragonslayerSwordspearEntity(EntityType<? extends DragonslayerSwordspearEntity> entityType, World world) {
        super(entityType, world);
    }

    public DragonslayerSwordspearEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.SWORDSPEAR_ENTITY_TYPE, owner, world, stack, stack);
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ENCHANTED, false);
    }

    @Override
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

    @Override
    protected ItemStack getDefaultItemStack() {
        return WeaponRegistry.DRAGONSLAYER_SWORDSPEAR.getDefaultStack();
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = WeaponConfig.dragonslayer_swordspear_projectile_damage;
        if (this.getOwner() == null || entity == null) {
            return;
        }
        DamageSource damageSource = this.getWorld().getDamageSources().lightningBolt();
        if (entity instanceof LivingEntity && this.getWorld() instanceof ServerWorld serverWorld) {
            f = EnchantmentHelper.getDamage(serverWorld, this.getItemStack(), entity, damageSource, f);
        }
        Entity entity2 = this.getOwner();
        this.dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_HIT;
        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, this.getWeaponStack());
            }
            if (entity instanceof LivingEntity livingEntity) {
                this.knockback(livingEntity, damageSource);
                this.onHit(livingEntity);
            }
        }
        this.setVelocity(this.getVelocity().multiply(-0.01D, -0.1D, -0.01D));
        float g = 1.0F;
        if (!getWorld().isClient) {
            BlockPos blockPos = entity.getBlockPos();
            if (this.getWorld().isSkyVisible(blockPos)) {
                for (int i = 0; i < WeaponConfig.dragonslayer_swordspear_lightning_call_lightning_amount; i++) {
                    LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.getWorld());
                    lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                    lightningEntity.setChanneler(entity2 instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity2 : null);
                    this.getWorld().spawnEntity(lightningEntity);
                    soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER.value();
                    g = 5.0F;
                }
            }
        }
        this.playSound(soundEvent, g, 1.0F);
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    @Override
    protected float getDragInWater() {
        return 0.99F;
    }
    
    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean shouldAllowArrowSticking() {
        return false;
    }
}
