package net.soulsweaponry.entity.mobs;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.soulsweaponry.registry.ArmorRegistry;

import java.util.EnumSet;

public class DarkSorcerer extends HostileEntity {

    public  DarkSorcerer(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.initEquip();
    }

    private static final TrackedData<Boolean> BEAMING = DataTracker.registerData(DarkSorcerer.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<BlockPos> BEAM_CORDS = DataTracker.registerData(DarkSorcerer.class, TrackedDataHandlerRegistry.BLOCK_POS);

    public static DefaultAttributeContainer.Builder createSorcererAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 25D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3000000003D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public boolean canSpawn(WorldView view) {
        BlockPos blockUnderEntity = new BlockPos(this.getBlockX(), this.getBlockY() - 1, this.getBlockZ());
        BlockState state = this.getWorld().getBlockState(this.getBlockPos());
        return view.doesNotIntersectEntities(this) && !getWorld().containsFluid(this.getBoundingBox())
                && state.getBlock().canMobSpawnInside(state)
                && this.getWorld().getBlockState(blockUnderEntity).isOf(Blocks.DEEPSLATE_TILES);
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(BEAMING, Boolean.FALSE);
        this.dataTracker.startTracking(BEAM_CORDS, new BlockPos(0, 0, 0));
    }

    public void setBeaming(boolean bl) {
        this.dataTracker.set(BEAMING, bl);
    }

    public boolean getBeaming() {
        return this.dataTracker.get(BEAMING);
    }

    public void setBeamCords(double x, double y, double z) {
        this.dataTracker.set(BEAM_CORDS, BlockPos.ofFloored(x, y, z));
    }

    public BlockPos getBeamCords() {
        return this.dataTracker.get(BEAM_CORDS);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new BeamTargetGoal(this));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, ReturningKnight.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    public static boolean canSpawn(EntityType<DarkSorcerer> darkSorcererEntityType, ServerWorldAccess serverWorldAccess, SpawnReason spawnReason, BlockPos blockPos, Random random) {
        return serverWorldAccess.getDifficulty() != Difficulty.PEACEFUL;
    }

    static class BeamTargetGoal extends Goal {
        private final DarkSorcerer user;
        private int attackTicks;
        private Path path;

        public BeamTargetGoal(DarkSorcerer user) {
            this.user = user;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.user.getTarget();
            if (target != null) {
                this.path = this.user.getNavigation().findPathTo(target, 0);
            }
            return target != null && target.isAlive() && this.user.canTarget(target) && this.path != null;
        }

        @Override
        public void stop() {
            this.user.setBeaming(false);
            super.stop();
        }

        public void tick() {
            attackTicks--;
            LivingEntity target = this.user.getTarget();
            double distanceToEntity = this.user.squaredDistanceTo(target);

            if (target != null) {
                this.user.getLookControl().lookAt(target.getX(), target.getEyeY(), target.getZ());

                //Heal boss when close
                if (target instanceof ReturningKnight) {
                    if (distanceToEntity < 150f) {
                        this.user.setBeaming(true);
                        this.user.setBeamCords(target.getBlockX(), target.getEyeY(), target.getBlockZ());
                        if (target.getHealth() < target.getMaxHealth()) {
                            target.setHealth(target.getHealth() + .5F + (float) ((ReturningKnight)target).getAttackingPlayers().size()/2f);
                        }
                    } else {
                        this.user.setBeaming(false);
                    }
                } else {
                    //Damage target each second
                    if (distanceToEntity < 150f) {
                        this.user.setBeaming(true);
                        this.user.setBeamCords(target.getBlockX(), target.getEyeY(), target.getBlockZ());
                        if (attackTicks < 0) {
                            target.damage(this.user.getWorld().getDamageSources().mobAttack(user), 2f);
                            attackTicks = 10;
                        }
                    } else {
                        this.user.setBeaming(false);
                        attackTicks = 10;
                    }
                }
                if (distanceToEntity < 100f) {
                    this.user.getNavigation().stop();
                }
                if (this.path != null && distanceToEntity > 100f) {
                    this.user.getNavigation().startMovingAlong(this.path, 1D);
                }
                super.tick();

            }
        }
    }

    public void tickMovement() {
        if (this.getBeaming()) {
            double e = this.getBeamCords().getX() - this.getX();
            double f = this.getBeamCords().getY() - this.getEyeY();
            double g = this.getBeamCords().getZ() - this.getZ();
            double h = Math.sqrt(e * e + f * f + g * g);
            e /= h;
            f /= h;
            g /= h;
            double length = this.random.nextDouble();
            for (int i = 0; i < 10; i++) {
                length += .5f + this.random.nextDouble();
                this.getWorld().addParticle(ParticleTypes.ELECTRIC_SPARK, this.getX() + e * length, this.getEyeY() + f * length, this.getZ() + g * length, 0.0D, 0.0D, 0.0D);
                this.getWorld().addParticle(ParticleTypes.GLOW, this.getX() + e * length, this.getEyeY() + f * length, this.getZ() + g * length, 0.0D, 0.0D, 0.0D);
            }

            double dd = this.random.nextGaussian() * 0.05D;
            double ee = this.random.nextGaussian() * 0.05D;
            double newX = this.random.nextDouble() - 0.5D + this.random.nextGaussian() * 0.15D + dd;
            double newZ = this.random.nextDouble() - 0.5D + this.random.nextGaussian() * 0.15D + ee;
            double newY = this.random.nextDouble() - 0.5D + this.random.nextDouble() * 0.5D;
            float body = this.bodyYaw * 0.017453292F + MathHelper.cos((float)this.age * 0.6662F) * 0.25F;
            float cosBody = MathHelper.cos(body);
            float sinBody = MathHelper.sin(body);
            this.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (double)cosBody * 0.5D, this.getY() + 1.8D, this.getZ() + (double)sinBody * 0.5D, newX, newY, newZ);
            this.getWorld().addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - (double)cosBody * 0.5D, this.getY() + 1.8D, this.getZ() - (double)sinBody * 0.5D, newX, newY, newZ);
        }
        super.tickMovement();
    }

    public void initEquip() {
        this.equipStack(EquipmentSlot.HEAD, new ItemStack(ArmorRegistry.SOUL_ROBES_HELMET.get()));
        this.equipStack(EquipmentSlot.CHEST, new ItemStack(ArmorRegistry.SOUL_ROBES_CHESTPLATE.get()));
        this.equipStack(EquipmentSlot.LEGS, new ItemStack(ArmorRegistry.SOUL_ROBES_LEGGINGS.get()));
        this.equipStack(EquipmentSlot.FEET, new ItemStack(ArmorRegistry.SOUL_ROBES_BOOTS.get()));
    }

    @Override
    public boolean isUndead() {
        return true;
    }
}