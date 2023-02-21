package net.soulsweaponry.entity.projectile;

import net.soulsweaponry.items.Mjolnir;
import org.jetbrains.annotations.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class MjolnirProjectile extends PersistentProjectileEntity implements GeoEntity {

    private ItemStack stack;
    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private boolean dealtDamage;
    private static final String DEALT_DAMAGE = "DealtDamage";
    public int returnTimer;

    public MjolnirProjectile(EntityType<? extends MjolnirProjectile> entityType, World world) {
        super(entityType, world);
        this.stack = new ItemStack(WeaponRegistry.MJOLNIR);
    }

    public MjolnirProjectile(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.MJOLNIR_ENTITY_TYPE, owner, world);
        this.stack = stack.copy();
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        Entity entity = this.getOwner();
        int returnSpeed = MathHelper.floor(5 + WeaponUtil.getEnchantDamageBonus(this.asItemStack()));
        if ((this.dealtDamage || this.isNoClip()) && entity != null) {
            this.setNoClip(true);
            Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
            if (!this.isOwnerAlive()) {
                if (this.stack.hasNbt() && this.stack.getNbt().contains(Mjolnir.OWNERS_LAST_POS)) {
                    int[] pos = this.stack.getNbt().getIntArray(Mjolnir.OWNERS_LAST_POS);
                    vec3d = new Vec3d(pos[0], pos[1], pos[2]).subtract(this.getPos());
                    if (vec3d.getX() == pos[0] && vec3d.getY() == pos[1] && vec3d.getZ() == pos[2]) {
                        if (!this.world.isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                            this.dropStack(this.asItemStack(), 0.1f);
                        }
                        this.discard();
                    }
                } else {
                    if (!this.world.isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                        this.dropStack(this.asItemStack(), 0.1f);
                    }
                    this.discard();
                }
            }
            this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * (double)returnSpeed, this.getZ());
            if (this.world.isClient) {
                this.lastRenderY = this.getY();
            }
            double d = 0.05 * (double)returnSpeed;
            this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
            if (this.returnTimer == 0) {
                this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0f, 1.0f);
            }
            ++this.returnTimer;
        }
        super.tick();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        BlockPos blockPos;
        Entity entity2;
        Entity entity = entityHitResult.getEntity();
        float f = ConfigConstructor.mjolnir_projectile_damage;
        if (entity instanceof LivingEntity) f += EnchantmentHelper.getAttackDamage(this.asItemStack(), ((LivingEntity) entity).getGroup());
        //Custom damage source maybe?
        DamageSource damageSource = DamageSource.trident(this, (entity2 = this.getOwner()) == null ? this : entity2);
        this.dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_HIT;
        if (entity.damage(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity2 = (LivingEntity)entity;
                if (entity2 instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity2, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity2);
                }
                this.onHit(livingEntity2);
            }
        }
        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
        float g = 1.0f;
        int strikes = 1;
        if (this.world instanceof ServerWorld && this.world.isSkyVisible(blockPos = entity.getBlockPos())) {
            if (this.world.isThundering()) strikes = 3;
            for (int i = 0; i < strikes; i++) {
                LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.world);
                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                lightningEntity.setChanneler(entity2 instanceof ServerPlayerEntity ? (ServerPlayerEntity)entity2 : null);
                this.world.spawnEntity(lightningEntity);
            }
            soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
            g = 5.0f;
        }
        this.playSound(soundEvent, g, 1.0f);
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity == null || !entity.isAlive()) {
            return false;
        }
        return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        int slot = player.getInventory().selectedSlot;
        if (!player.getInventory().getMainHandStack().isEmpty()) {
            slot = -1;
        }
        return super.tryPickup(player) || (this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(slot, this.asItemStack()));
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Mjolnir", NbtElement.COMPOUND_TYPE)) {
            this.stack = ItemStack.fromNbt(nbt.getCompound("Mjolnir"));
        }
        this.dealtDamage = nbt.getBoolean(DEALT_DAMAGE);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Mjolnir", this.stack.writeNbt(new NbtCompound()));
        nbt.putBoolean(DEALT_DAMAGE, this.dealtDamage);
    }

    @Override
    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        if (this.dealtDamage) {
            return null;
        }
        return super.getEntityCollision(currentPosition, nextPosition);
    }

    @Override
    protected float getDragInWater() {
        return 0.99f;
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return true;
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
        return this.stack;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }
}
