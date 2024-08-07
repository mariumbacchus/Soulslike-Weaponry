package net.soulsweaponry.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.entitydata.ReturningProjectileData;
import net.soulsweaponry.items.Mjolnir;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public abstract class ReturningProjectile extends PersistentProjectileEntity {

    public ItemStack stack;
    public boolean dealtDamage;
    private boolean shouldReturn;
    private int returnTimer;
    public static final String DEALT_DAMAGE = "DealtDamage";
    public static final String SHOULD_RETURN = "ShouldReturn";

    public ReturningProjectile(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public ReturningProjectile(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world) {
        super(type, x, y, z, world);
    }

    public ReturningProjectile(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world) {
        super(type, owner, world);
    }

    public ReturningProjectile(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack) {
        super(type, owner, world);
        this.stack = stack.copy();
    }

    public abstract float getDamage(Entity target);
    public abstract boolean collide(Entity owner, Entity target, float damage);
    public abstract double getReturnSpeed(ItemStack stack);

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity owner = this.getOwner() == null ? this : this.getOwner();
        Entity entity = entityHitResult.getEntity();
        this.dealtDamage = true;
        if (this.collide(owner, entity, this.getDamage(entity))) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity target) {
                if (owner instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(target, owner);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)owner, target);
                }
                this.onHit(target);
            }
        }
        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
    }

    public void saveOnPlayer(PlayerEntity player) {
        UUID uuid = this.getUuid();
        UUID prevUuid = ReturningProjectileData.getReturningProjectileUuid(player);
        if (player.getWorld() instanceof ServerWorld serverWorld) {
            if (prevUuid == null) {
                ReturningProjectileData.setReturningProjectileUuid(player, uuid);
                prevUuid = uuid;
            }
            Entity entity = serverWorld.getEntity(prevUuid);
            if (entity instanceof ReturningProjectile returning) {
                returning.setShouldReturn(true);
            }
            ReturningProjectileData.setReturningProjectileUuid(player, uuid);
        }
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4 || age > 60) {
            this.dealtDamage = true;
        }
        if (!this.inGround && age > 60) {
            this.setShouldReturn(true);
        }
        Entity owner = this.getOwner();
        double returnSpeed = this.getReturnSpeed(this.stack);
        if (this.shouldReturn() && (this.dealtDamage || this.isNoClip()) && owner != null) {
            this.setNoClip(true);
            Vec3d vec3d = owner.getEyePos().subtract(this.getPos());
            if (!this.isOwnerAlive()) {
                if (this.stack.hasNbt() && this.stack.getNbt().contains(Mjolnir.OWNERS_LAST_POS)) {
                    int[] pos = this.stack.getNbt().getIntArray(Mjolnir.OWNERS_LAST_POS);
                    vec3d = new Vec3d(pos[0], pos[1], pos[2]).subtract(this.getPos());
                    if (vec3d.getX() == pos[0] && vec3d.getY() == pos[1] && vec3d.getZ() == pos[2]) {
                        this.dropStack();
                    }
                } else {
                    this.dropStack();
                }
            }
            this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * returnSpeed, this.getZ());
            if (this.world.isClient) {
                this.lastRenderY = this.getY();
            }
            double d = 0.05 * returnSpeed;
            this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
            if (this.returnTimer == 0) {
                this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0f, 1.0f);
            }
            this.returnTimer++;
            for (Entity entity1 : this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.2D))) {
                if (entity1 instanceof LivingEntity target && !target.isTeammate(owner)) {
                    this.collide(owner, target, this.getDamage(target));
                }
            }
        }
        List<Entity> list = this.getWorld().getOtherEntities(this, this.getBoundingBox()).stream().filter(e -> e instanceof PlayerEntity).toList();
        if (this.dealtDamage && !list.isEmpty()) {
            PlayerEntity player = (PlayerEntity) list.get(0);
            if (owner != null) {
                for (Entity entity1 : list) {
                    if (entity1.equals(owner)) {
                        player = (PlayerEntity) entity1;
                    }
                }
            }
            if (this.getOwner() == null) {
                this.insertStack(player);
            } else {
                if (this.isOwner(player)) {
                    this.insertStack(player);
                }
            }
        }
        super.tick();
    }

    public void insertStack(PlayerEntity player) {
        int slot = player.getInventory().selectedSlot;
        if (!player.getInventory().getMainHandStack().isEmpty()) {
            slot = -1;
        }
        boolean inserted = player.getInventory().insertStack(slot, this.asItemStack());
        if (inserted) {
            player.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 1f, (float) this.random.nextGaussian() + .25f);
        }
        if (!inserted) {
            this.dropStack(this.asItemStack(), 0.1f);
        }
        this.discard();
    }

    private void dropStack() {
        if (!this.world.isClient && this.pickupType == PickupPermission.ALLOWED) {
            this.dropStack(this.asItemStack(), 0.1f);
        }
        this.discard();
    }

    public boolean shouldReturn() {
        return this.shouldReturn;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    public void setShouldReturn(boolean bl) {
        this.dealtDamage = bl;
        this.shouldReturn = bl;
        this.setNoClip(bl);
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity == null || !entity.isAlive()) {
            return false;
        }
        return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
    }

    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        if (this.dealtDamage) {
            return null;
        }
        return super.getEntityCollision(currentPosition, nextPosition);
    }

    public int getReturnTimer() {
        return this.returnTimer;
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
        if (nbt.contains("Stack", NbtElement.COMPOUND_TYPE)) {
            this.stack = ItemStack.fromNbt(nbt.getCompound("Stack"));
        }
        if (nbt.contains(SHOULD_RETURN)) {
            this.shouldReturn = nbt.getBoolean(SHOULD_RETURN);
        }
        if (nbt.contains("ReturnTimer")) {
            this.returnTimer = nbt.getInt("ReturnTimer");
        }
        this.dealtDamage = nbt.getBoolean(DEALT_DAMAGE);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Stack", this.stack.writeNbt(new NbtCompound()));
        nbt.putBoolean(DEALT_DAMAGE, this.dealtDamage);
        nbt.putBoolean(SHOULD_RETURN, this.shouldReturn);
        nbt.putInt("ReturnTimer", this.returnTimer);
    }

    @Override
    public ItemStack asItemStack() {
        return this.stack;
    }

    @Override
    protected float getDragInWater() {
        return 0.99f;
    }
}
