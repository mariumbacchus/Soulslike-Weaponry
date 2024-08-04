package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.PostureData;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class SilverBulletEntity extends NonArrowProjectile implements IAnimatable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private int postureLoss;

    public SilverBulletEntity(EntityType<? extends SilverBulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public SilverBulletEntity(World world, LivingEntity owner) {
        super(EntityRegistry.SILVER_BULLET_ENTITY_TYPE, owner, world);
    }

    public SilverBulletEntity(EntityType<? extends SilverBulletEntity> entityType, World world, LivingEntity owner) {
        super(entityType, owner, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.inGround) {
            Vec3d vec3d = this.getVelocity();
            double e = vec3d.x;
            double f = vec3d.y;
            double g = vec3d.z;
            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.SMOKE, this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D, this.getZ() + g * (double)i / 4.0D, -e*0.2, (-f + 0.2D)*0.2, -g*0.2);
            }
        }
        Vec3d vec3d = this.getVelocity();
        this.setVelocity(vec3d.x, vec3d.y + (double)0.045f, vec3d.z);
        if (this.age > this.getMaxAge()) {
            this.discard();
        }
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_STONE_BREAK;
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.discard(); 
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (ConfigConstructor.can_projectiles_apply_posture_loss && entityHitResult.getEntity() instanceof LivingEntity target) {
            int posture = this.getPostureLoss();
            if (target instanceof PlayerEntity) {
                posture = MathHelper.floor((float) posture * ConfigConstructor.silver_bullet_posture_loss_on_player_modifier);
            }
            PostureData.addPosture((IEntityDataSaver) target, posture);
            if (target.isUndead()) {
                this.setDamage(this.getDamage() + (ConfigConstructor.silver_bullet_undead_bonus_damage / this.getVelocity().length()));
            }
        }
        super.onEntityHit(entityHitResult);
        this.discard();
    }

    public int getMaxAge() {
        return 200;
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemRegistry.SILVER_BULLET.getDefaultStack();
    }

    public void setPostureLoss(int postureLoss) {
        this.postureLoss = postureLoss;
    }

    public int getPostureLoss() {
        return postureLoss;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("postureLoss")) {
            this.setPostureLoss(nbt.getInt("postureLoss"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("postureLoss", this.getPostureLoss());
    }
}
