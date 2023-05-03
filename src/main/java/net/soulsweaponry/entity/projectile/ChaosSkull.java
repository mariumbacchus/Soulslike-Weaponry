package net.soulsweaponry.entity.projectile;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.BigChungus;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleNetworking;

import java.util.HashMap;

public class ChaosSkull extends ExplosiveProjectileEntity {

    public ChaosSkull(double x, double y, double z, double directionX, double directionY, double directionZ, World world) {
        super(EntityType.WITHER_SKULL, x, y, z, directionX, directionY, directionZ, world);
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
        super.onEntityHit(entityHitResult);
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
        super.onCollision(hitResult);
        if (this.getOwner() instanceof LivingEntity) {
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
                    case 3 -> {
                        Entity hostile = randomHostile();
                        hostile.setPos(this.getX(), this.getY(), this.getZ());
                        this.world.spawnEntity(hostile);
                    }
                    case 4 -> {
                        Entity passive = randomPassive();
                        passive.setPos(this.getX(), this.getY(), this.getZ());
                        this.world.spawnEntity(passive);
                    }
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

    private Entity randomHostile() {
        Entity[] hostileEntities = {
            new BlazeEntity(EntityType.BLAZE, this.world),
            new CreeperEntity(EntityType.CREEPER, this.world),
            new DrownedEntity(EntityType.DROWNED, this.world),
            new EndermanEntity(EntityType.ENDERMAN, this.world),
            new SilverfishEntity(EntityType.SILVERFISH, this.world),
            new SkeletonEntity(EntityType.SKELETON, this.world),
            new SlimeEntity(EntityType.SLIME, this.world),
            new SpiderEntity(EntityType.SPIDER, this.world),
            new WitchEntity(EntityType.WITCH, this.world),
            new WitherSkeletonEntity(EntityType.WITHER_SKELETON, this.world),
            new ZombieEntity(EntityType.ZOMBIE, this.world),
        };
        return hostileEntities[this.random.nextInt(hostileEntities.length)];
    }

    private Entity randomPassive() {
        Entity[] passiveEntities = {
            new BatEntity(EntityType.BAT, this.world),
            new BeeEntity(EntityType.BEE, this.world),
            new ChickenEntity(EntityType.CHICKEN, this.world),
            new CodEntity(EntityType.COD, this.world),
            new CowEntity(EntityType.COW, this.world),
            new GlowSquidEntity(EntityType.GLOW_SQUID, this.world),
            new HorseEntity(EntityType.HORSE, this.world),
            new LlamaEntity(EntityType.LLAMA, this.world),
            new WanderingTraderEntity(EntityType.WANDERING_TRADER, this.world),
            new MooshroomEntity(EntityType.MOOSHROOM, this.world),
            new PigEntity(EntityType.PIG, this.world),
            new PolarBearEntity(EntityType.POLAR_BEAR, this.world),
            new PufferfishEntity(EntityType.PUFFERFISH, this.world),
            new RabbitEntity(EntityType.RABBIT, this.world),
            new SalmonEntity(EntityType.SALMON, this.world),
        };
        return passiveEntities[this.random.nextInt(passiveEntities.length)];
    }
}
