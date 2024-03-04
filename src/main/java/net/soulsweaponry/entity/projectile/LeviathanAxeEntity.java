package net.soulsweaponry.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.LeviathanAxe;
import net.soulsweaponry.items.Mjolnir;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.ParticleEvents;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class LeviathanAxeEntity extends PersistentProjectileEntity implements GeoEntity {

    private final AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private ItemStack stack;
    private boolean dealtDamage;
    private static final String DEALT_DAMAGE = "DealtDamage";
    public int returnTimer;

    public LeviathanAxeEntity(EntityType<? extends LeviathanAxeEntity> entityType, World world) {
        super(entityType, world);
        this.stack = new ItemStack(WeaponRegistry.LEVIATHAN_AXE);
    }

    public LeviathanAxeEntity(EntityType<? extends LeviathanAxeEntity> type, double x, double y, double z,
            World world) {
        super(type, x, y, z, world);
        this.stack = new ItemStack(WeaponRegistry.LEVIATHAN_AXE);
    }

    public LeviathanAxeEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.LEVIATHAN_AXE_ENTITY_TYPE, owner, world);
        this.stack = stack.copy();
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity owner = this.getOwner() == null ? this : this.getOwner();
        Entity entity = entityHitResult.getEntity();
        float f = ConfigConstructor.leviathan_axe_projectile_damage + WeaponUtil.getEnchantDamageBonus(this.asItemStack());
        this.dealtDamage = true;
        if (this.collide(owner, entity, f)) {
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
        if (!getWorld().isClient && entity instanceof MjolnirProjectile) {
            ParticleEvents.mjolnirLeviathanAxeCollision(this.getWorld(), this.getX(), this.getY(), this.getZ());
            this.getWorld().createExplosion(null, this.getX(), this.getY(), this.getZ(), 6.0F, true, World.ExplosionSourceType.BLOCK);
        }
        this.setVelocity(this.getVelocity().multiply(-0.01, -0.1, -0.01));
    }

    public void tick() {
        if (this.inGroundTime > 4 || age > 60) {
            this.dealtDamage = true;
        }
        Entity entity = this.getOwner();
        double returnSpeed = ConfigConstructor.leviathan_axe_return_speed + (double) EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack)/2;
        if ((this.dealtDamage || this.isNoClip()) && entity != null) {
            this.setNoClip(true);
            Vec3d vec3d = entity.getEyePos().subtract(this.getPos());
            if (!this.isOwnerAlive()) {
                if (this.stack.hasNbt() && this.stack.getNbt().contains(Mjolnir.OWNERS_LAST_POS)) {
                    int[] pos = this.stack.getNbt().getIntArray(Mjolnir.OWNERS_LAST_POS);
                    vec3d = new Vec3d(pos[0], pos[1], pos[2]).subtract(this.getPos());
                    if (vec3d.getX() == pos[0] && vec3d.getY() == pos[1] && vec3d.getZ() == pos[2]) {
                        if (!this.getWorld().isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                            this.dropStack(this.asItemStack(), 0.1f);
                        }
                        this.discard();
                    }
                } else {
                    if (!this.getWorld().isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                        this.dropStack(this.asItemStack(), 0.1f);
                    }
                    this.discard();
                }
            }
            this.setPos(this.getX(), this.getY() + vec3d.y * 0.015 * returnSpeed, this.getZ());
            if (this.getWorld().isClient) {
                this.lastRenderY = this.getY();
            }
            double d = 0.05 * returnSpeed;
            this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
            if (this.returnTimer == 0) {
                this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0f, 1.0f);
            }
            ++this.returnTimer;
            float f = ConfigConstructor.leviathan_axe_projectile_damage + WeaponUtil.getEnchantDamageBonus(this.asItemStack());
            for (Entity entity1 : this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.2D))) {
                if (entity1 instanceof LivingEntity target && !entity.isTeammate(target)) {
                    this.collide(entity, target, f / 2f);
                }
            }
        }
        super.tick();
    }

    private boolean collide(Entity owner, Entity target, float damage) {
        DamageSource damageSource = this.getWorld().getDamageSources().trident(this, owner);
        LeviathanAxe.iceExplosion(getWorld(), this.getBlockPos(), this.getOwner(), EnchantmentHelper.getLevel(Enchantments.SHARPNESS, this.stack));
        return target.damage(damageSource, damage);
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
    protected boolean canHit(Entity entity) {
        if (entity instanceof MjolnirProjectile) {
            return true;
        } else {
            return super.canHit(entity);
        }
    }

    private boolean isOwnerAlive() {
        Entity entity = this.getOwner();
        if (entity == null || !entity.isAlive()) {
            return false;
        }
        return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
    }

    protected boolean tryPickup(PlayerEntity player) {
        int slot = player.getInventory().selectedSlot;
        if (!player.getInventory().getMainHandStack().isEmpty()) {
            slot = -1;
        }
        return super.tryPickup(player) || (this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(slot, this.asItemStack()));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Stack", NbtElement.COMPOUND_TYPE)) {
            this.stack = ItemStack.fromNbt(nbt.getCompound("Stack"));
        }
        this.dealtDamage = nbt.getBoolean(DEALT_DAMAGE);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Stack", this.stack.writeNbt(new NbtCompound()));
        nbt.putBoolean(DEALT_DAMAGE, this.dealtDamage);
    }

    private PlayState predicate(AnimationState<?> state) {
        state.getController().setAnimation(RawAnimation.begin().then("spin", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public ItemStack asItemStack() {
        return this.stack;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
    
}
