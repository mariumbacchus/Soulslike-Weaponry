package net.soulsweaponry.entity.mobs;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.AttackWithOwnerGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TrackOwnerAttackerGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

public class Remnant extends TameableEntity {

    public Remnant(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.setTamed(true);
        this.initEquip();
    }
    
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1D, false));
        this.goalSelector.add(6, new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F, false));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<MobEntity>(this, MobEntity.class, 5, false, false, entity -> entity instanceof Monster 
            && !(entity instanceof CreeperEntity) && !this.isTeammate(entity)));
        this.targetSelector.add(4, new RevengeGoal(this, new Class[0]).setGroupRevenge());
    }

    public static DefaultAttributeContainer.Builder createRemnantAttributes() {
        return MobEntity.createMobAttributes()
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12D)
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 10D)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3000000003D)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
    }

    public void initEquip() {
        int[] chance = {6, 6, 2, 4, 1, 2, 2, 2};
        Item[] equipment = {
            ArmorRegistry.SOUL_INGOT_HELMET,
            ArmorRegistry.SOUL_INGOT_CHESTPLATE,
            ArmorRegistry.SOUL_INGOT_LEGGINGS,
            ArmorRegistry.SOUL_INGOT_BOOTS,
            Items.SHIELD,
            WeaponRegistry.TRANSLUCENT_SWORD,
            WeaponRegistry.TRANSLUCENT_GLAIVE,
            WeaponRegistry.TRANSLUCENT_DOUBLE_GREATSWORD,
        };
        EquipmentSlot[] spot = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET,
            EquipmentSlot.OFFHAND,
            EquipmentSlot.MAINHAND, //trenger en mainhand for hvert sverd
            EquipmentSlot.MAINHAND,
            EquipmentSlot.MAINHAND,
        };
        for (int i = 0; i < chance.length; i++) {
            int random = (new Random()).nextInt(10);
            if (random < chance[i]) {
                this.equipStack(spot[i], new ItemStack(equipment[i]));
            }
        }
    }

    @Override
    public boolean isTeammate(Entity other) {
        if (other instanceof Tameable) {
            if (((Tameable)other).getOwner() != null && this.getOwner() != null) {
                if (((Tameable)other).getOwner() == this.getOwner()) {
                    return true;
                }
            }
        }
        return super.isTeammate(other);
    }

    @Override
    public boolean isUndead() {
        return true;
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_HUSK_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_HUSK_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_HUSK_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_HUSK_STEP;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }
}
