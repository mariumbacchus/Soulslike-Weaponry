package net.soulsweaponry.entity.mobs;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.BackoffAndHealGoal;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

import java.util.Map;

public class EliteRemnant extends Remnant {

    public int goldenAppleCooldownTicks;
    private ItemStack savedOffhand;
    public static final int GOLDEN_APPLE_COOLDOWN = (int) EntityConfig.elite_remnant_eat_golden_apple_cooldown_ticks;

    private static final float SHIELD_FLASH_DURATION = 20F;
    private static final TrackedData<Long> SHIELD_FLASH_START = DataTracker.registerData(EliteRemnant.class, TrackedDataHandlerRegistry.LONG);

    public EliteRemnant(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SHIELD_FLASH_START, -1L);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(3, new BackoffAndHealGoal(this, 1.25));
    }

    @Override
    protected void mobTick() {
        super.mobTick();
        if (this.goldenAppleCooldownTicks > 0) {
            this.goldenAppleCooldownTicks--;
        }
    }

    public void flashShield() {
        if (!this.getWorld().isClient) {
            this.dataTracker.set(SHIELD_FLASH_START, this.getWorld().getTime());
        }
    }

    public float getShieldOpacity(float tickDelta) {
        long start = this.dataTracker.get(SHIELD_FLASH_START);
        if (start < 0) {
            return 0F;
        }
        float elapsed = (this.getWorld().getTime() - start) + tickDelta;
        return MathHelper.clamp(1F - elapsed / SHIELD_FLASH_DURATION, 0F, 1F);
    }

    public void beginEatingGoldenApple() {
        if (this.savedOffhand != null) {
            return;
        }
        this.stopUsingItem();
        this.savedOffhand = this.getOffHandStack();
        this.setStackInHand(Hand.OFF_HAND, Items.GOLDEN_APPLE.getDefaultStack());
        this.setCurrentHand(Hand.OFF_HAND);
    }

    public boolean isEatingGoldenApple() {
        return this.getOffHandStack().isOf(Items.GOLDEN_APPLE);
    }

    @Override
    protected void consumeItem() {
        boolean healingApple = this.savedOffhand != null && this.isEatingGoldenApple();
        // Vanilla consumes the apple and handles its normal effects
        super.consumeItem();
        if (healingApple && !this.getWorld().isClient) {
            // Remnants are undead so heal via method instead of effects
            this.heal(this.getMaxHealth() * 0.5F);
            this.endEatingGoldenApple();
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.getWorld().isClient || !this.isEatingGoldenApple() || this.age % 4 != 0) {
            return;
        }
        this.spawnItemParticles(Items.GOLDEN_APPLE.getDefaultStack(), 5);
    }

    // For some reason this method is private in LivingEntity so copy-paste it is
    private void spawnItemParticles(ItemStack stack, int count) {
        for (int i = 0; i < count; ++i) {
            Vec3d vec3d = new Vec3d(((double)this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            vec3d = vec3d.rotateX(-this.getPitch() * ((float)Math.PI / 180));
            vec3d = vec3d.rotateY(-this.getYaw() * ((float)Math.PI / 180));
            double d = (double)(-this.random.nextFloat()) * 0.6 - 0.3;
            Vec3d vec3d2 = new Vec3d(((double)this.random.nextFloat() - 0.5) * 0.3, d, 0.6);
            vec3d2 = vec3d2.rotateX(-this.getPitch() * ((float)Math.PI / 180));
            vec3d2 = vec3d2.rotateY(-this.getYaw() * ((float)Math.PI / 180));
            vec3d2 = vec3d2.add(this.getX(), this.getEyeY(), this.getZ());
            this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y + 0.05, vec3d.z);
        }
    }

    public void endEatingGoldenApple() {
        if (this.savedOffhand == null) {
            return;
        }
        this.stopUsingItem();
        this.setStackInHand(Hand.OFF_HAND, this.savedOffhand);
        this.savedOffhand = null;
    }

    @Override
    public void onDeath(DamageSource source) {
        // Restore the real equipment before death drops are calculated
        this.endEatingGoldenApple();
        super.onDeath(source);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("GoldenAppleCooldown", this.goldenAppleCooldownTicks);
        // Save the original offhand if unloaded while eating
        if (this.savedOffhand != null) {
            NbtList hands = nbt.getList("HandItems", NbtElement.COMPOUND_TYPE);
            if (hands.size() >= 2) {
                hands.set(1, this.savedOffhand.isEmpty() ? new NbtCompound() : this.savedOffhand.encode(this.getRegistryManager()));
            }
            nbt.putInt("GoldenAppleCooldown", Math.max(this.goldenAppleCooldownTicks, GOLDEN_APPLE_COOLDOWN));
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.goldenAppleCooldownTicks = Math.max(0, nbt.getInt("GoldenAppleCooldown"));
    }

    @Override
    public void initEquip() {
        Map<EquipmentSlot, ItemStack> armor = Map.of(
                EquipmentSlot.HEAD, ArmorRegistry.ELITE_SOUL_INGOT_HELMET.getDefaultStack(),
                EquipmentSlot.CHEST, ArmorRegistry.SOUL_INGOT_CHESTPLATE.getDefaultStack(),
                EquipmentSlot.LEGS, ArmorRegistry.SOUL_INGOT_LEGGINGS.getDefaultStack(),
                EquipmentSlot.FEET, ArmorRegistry.SOUL_INGOT_BOOTS.getDefaultStack()
        );
        for (EquipmentSlot slot : armor.keySet()) {
            this.equipStack(slot, armor.get(slot));
        }
        this.equipStack(EquipmentSlot.OFFHAND, Items.SHIELD.getDefaultStack());
        ItemStack weapon = this.random.nextBoolean() ? WeaponRegistry.TRANSLUCENT_GLAIVE.getDefaultStack()
                : this.random.nextBoolean() ? WeaponRegistry.TRANSLUCENT_SWORD.getDefaultStack()
                    : WeaponRegistry.TRANSLUCENT_DOUBLE_GREATSWORD.getDefaultStack();
        this.equipStack(EquipmentSlot.MAINHAND, weapon);
    }

    public static DefaultAttributeContainer.Builder createRemnantAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 30D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, EntityConfig.elite_remnant_health)
                .add(EntityAttributes.GENERIC_ARMOR, EntityConfig.elite_remnant_bonus_armor)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3000000003D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, EntityConfig.elite_remnant_base_attack_damage);
    }
}