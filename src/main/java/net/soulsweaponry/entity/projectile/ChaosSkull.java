package net.soulsweaponry.entity.projectile;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.BigChungus;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleNetworking;

import java.util.HashMap;

public class ChaosSkull extends WitherSkullEntity {

    public ChaosSkull(EntityType<ChaosSkull> chaosSkullEntityType, World world) {
        super(chaosSkullEntityType, world);
    }

    public ChaosSkull(double x, double y, double z, double directionX, double directionY, double directionZ, World world) {
        super(EntityRegistry.CHAOS_SKULL, world);
        this.refreshPositionAndAngles(x, y, z, this.getYaw(), this.getPitch());
        this.refreshPosition();
        double d = Math.sqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);
        if (d != 0.0) {
            this.powerX = directionX / d * 0.1;
            this.powerY = directionY / d * 0.1;
            this.powerZ = directionZ / d * 0.1;
        }
    }

    @Override
    protected float getDrag() {
        return 1f;
    }

    private float getModifiedDamage(float damage) {
        return damage*ConfigConstructor.chaos_monarch_damage_modifier;
    }
    
    /**
     * On specifically entities hit, and will react only to instances of {@linkplain LivingEntity}.
     * Will do random effects that include random damage and random potion effects.
     * Via {@link #onCollision(HitResult)}, it will call {@link #finisher()} to spawn particles and {@link #discard()} the projectile.
     * <p> The Default in the switch statement will always be triggered and will include the damaging effects while having only a small chance
     * for applying potion effects.
     * Potion effects will not be applied to the caster, although it would be funny seeing the boss suddenly turn invisible or
     * even invincible.
     * <p> The duration and amplifier of the effect won't be large as a minute levitation effect or weakness 50 will
     * be just unplayable.
     */
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (entityHitResult.getEntity() != null && entityHitResult.getEntity() instanceof LivingEntity entity && this.getOwner() instanceof LivingEntity) {
            int rng = this.random.nextInt(3);
            if (rng == 0) {
                int duration = this.random.nextInt(160) + 40;
                int amplifier = this.random.nextInt(3);
                entity.addStatusEffect(new StatusEffectInstance(this.getPotionEffect(), duration, amplifier));
            }
            float damage = this.getModifiedDamage(this.random.nextInt(30) + 1);
            if (damage > this.getModifiedDamage(20)) {
                this.world.playSound(null, this.getBlockPos(), SoundRegistry.CRIT_HIT_EVENT, SoundCategory.HOSTILE, .5f, 1f);
            }
            entity.damage(this.world.getDamageSources().mobAttack((LivingEntity)this.getOwner()), damage);
        }
    }

    /**
     * On any collision, could be both entities or blocks. It will always trigger when hitting entites.
     * Will do random effects that include spawning random hostile entites (since the user is a boss),
     * random amounts of lightning, randomly powerfull explosions, and other times just harmless spawning of bats or something
     * similar. In the end it will call {@link #finisher()} to spawn particles and {@link #discard()} the projectile.
     * <p> It will trigger an event random amount of times based on the rng in a switch statement.
     * <p> 1 = Wither Skeleton ambush
     * <p> 2 = Lightning storm
     * <p> 3 = Random hostile mobs
     * <p> 4 = Random passive mobs
     * <p> 5 = Randomly large explosion
     * <p> > Over 5 and under 0 will simply break the function and call {@link #finisher()}.
     */
    @Override
    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult)hitResult);
            this.world.emitGameEvent(GameEvent.PROJECTILE_LAND, hitResult.getPos(), GameEvent.Emitter.of(this, null));
        } else if (type == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            this.onBlockHit(blockHitResult);
            BlockPos blockPos = blockHitResult.getBlockPos();
            this.world.emitGameEvent(GameEvent.PROJECTILE_LAND, blockPos, GameEvent.Emitter.of(this, this.world.getBlockState(blockPos)));
        }

        if (this.getOwner() instanceof LivingEntity && !this.world.isClient) {
            int amount = this.random.nextInt(5) + 1;
            int rng = this.random.nextInt(6);
            for (int i = 0; i < amount; i++) {
                switch (rng) {
                    case 1 -> {
                        WitherSkeletonEntity skeleton = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, this.world);
                        skeleton.setPos(this.getX(), this.getY(), this.getZ());
                        this.initEquip(skeleton);
                        this.world.spawnEntity(skeleton);
                    }
                    case 2 -> {
                        LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                        lightningEntity.setPos(this.getX(), this.getY(), this.getZ());
                        this.world.spawnEntity(lightningEntity);
                    }
                    case 3 -> this.randomHostile().spawn((ServerWorld) this.world, this.getBlockPos(), SpawnReason.EVENT);
                    case 4 -> this.randomPassive().spawn((ServerWorld) this.world, this.getBlockPos(), SpawnReason.EVENT);
                    case 5 -> {
                        float power = this.random.nextFloat() * 3;
                        this.world.createExplosion(this, this.world.getDamageSources().mobAttack((LivingEntity) this.getOwner()), new ExplosionBehavior(), this.getX(), this.getY(), this.getZ(), power, false, World.ExplosionSourceType.TNT);
                    }
                    default -> {
                        for (int j = 0; j < 2; j++) {
                            BigChungus chungus = new BigChungus(EntityRegistry.BIG_CHUNGUS, this.world);
                            chungus.setPos(this.getX(), this.getY(), this.getZ());
                            this.world.spawnEntity(chungus);
                        }
                    }
                }
            }
        }
        this.finisher();
    }

    private void initEquip(LivingEntity entity) {
        HashMap<ItemStack, EquipmentSlot> equip = new HashMap<>();
        equip.put(new ItemStack(Items.NETHERITE_HELMET), EquipmentSlot.HEAD);
        equip.put(new ItemStack(Items.NETHERITE_CHESTPLATE), EquipmentSlot.CHEST);
        equip.put(new ItemStack(Items.NETHERITE_LEGGINGS), EquipmentSlot.LEGS);
        equip.put(new ItemStack(Items.NETHERITE_BOOTS), EquipmentSlot.FEET);
        equip.put(new ItemStack(Items.STONE_SWORD), EquipmentSlot.MAINHAND);
        for (ItemStack stack : equip.keySet()) {
            if (entity.getRandom().nextDouble() < 0.5D) {
                if (!(stack.getItem() instanceof SwordItem)) {
                    stack.addEnchantment(Enchantments.PROTECTION, 2);
                    stack.addEnchantment(Enchantments.VANISHING_CURSE, 1);
                }
                entity.equipStack(equip.get(stack), stack);
            }
        }
    }

    private void finisher() {
        this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 1f, 1f);
        if (!this.world.isClient) ParticleNetworking.sendServerParticlePacket((ServerWorld) this.world, PacketRegistry.DARK_EXPLOSION_ID, this.getBlockPos(), 100);
        this.discard();
    }

    /**
     * There are mainly bad/irritating effects with a few good ones sprinkled in, this is for a boss fight after all.
     */
    private StatusEffect getPotionEffect() {
        StatusEffect[] effects = {
            StatusEffects.ABSORPTION,
            StatusEffects.BAD_OMEN,
            StatusEffects.BLINDNESS,
            StatusEffects.DARKNESS,
            StatusEffects.FIRE_RESISTANCE,
            StatusEffects.GLOWING,
            StatusEffects.HASTE,
            StatusEffects.HUNGER,
            StatusEffects.INVISIBILITY,
            StatusEffects.JUMP_BOOST,
            StatusEffects.LEVITATION,
            StatusEffects.MINING_FATIGUE,
            StatusEffects.NAUSEA,
            StatusEffects.NIGHT_VISION,
            StatusEffects.POISON,
            StatusEffects.REGENERATION,
            StatusEffects.SLOWNESS,
            StatusEffects.SLOW_FALLING,
            StatusEffects.SPEED,
            StatusEffects.STRENGTH,
            StatusEffects.WEAKNESS,
            StatusEffects.WITHER,
        };
        return effects[this.random.nextInt(effects.length)];
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > 100) {
            this.finisher();
        }
    }

    private EntityType<?> randomHostile() {
        EntityType<?>[] hostileEntities = {
                EntityType.BLAZE, EntityType.CREEPER, EntityType.DROWNED, EntityType.ENDERMAN, EntityType.SILVERFISH,
                EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.WITCH, EntityType.WITHER_SKELETON,
                EntityType.ZOMBIE
        };
        return hostileEntities[this.random.nextInt(hostileEntities.length)];
    }

    private EntityType<?> randomPassive() {
        EntityType<?>[] passiveEntities = {
            EntityType.BAT, EntityType.BEE, EntityType.CHICKEN, EntityType.COD, EntityType.COW, EntityType.GLOW_SQUID,
                EntityType.HORSE, EntityType.LLAMA, EntityType.WANDERING_TRADER, EntityType.MOOSHROOM, EntityType.PIG,
                EntityType.POLAR_BEAR, EntityType.PUFFERFISH, EntityType.RABBIT, EntityType.SALMON
        };
        return passiveEntities[this.random.nextInt(passiveEntities.length)];
    }
}
