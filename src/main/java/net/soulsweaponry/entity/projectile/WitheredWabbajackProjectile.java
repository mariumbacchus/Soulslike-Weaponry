package net.soulsweaponry.entity.projectile;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion.DestructionType;
import net.soulsweaponry.items.WitheredWabbajack.LuckType;
import net.soulsweaponry.registry.PacketRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleNetworking;

public class WitheredWabbajackProjectile extends ExplosiveProjectileEntity {

    public WitheredWabbajackProjectile(EntityType<? extends WitheredWabbajackProjectile> entityType, World world) {
        super(entityType, world);
    }
    
    public WitheredWabbajackProjectile(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        super(EntityType.WITHER_SKULL, owner, directionX, directionY, directionZ, world);
    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (!this.world.isClient) {
            Entity entity = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            if (owner != null && entity != null && entity instanceof LivingEntity && owner instanceof LivingEntity) {
                LivingEntity target = (LivingEntity)entity;
                LivingEntity user = (LivingEntity)owner;
                Random random = new Random();
                int rng = random.nextInt(5) + 1;
                int power = random.nextInt((75 + 5*this.getLuckFactor(user))) + this.getLuckFactor(user)*5;
                int amplifier = random.nextInt(3 + this.getLuckFactor(user)) + this.getLuckFactor(user)/2;
                int duration = random.nextInt(300 + this.getLuckFactor(user)*50) + this.getLuckFactor(user)*50;
                switch (rng) {
                    case 1: case 3:
                        target.addStatusEffect(new StatusEffectInstance(this.getRandomEffect(true), duration, amplifier));
                    break;
                    case 2:
                        user.addStatusEffect(new StatusEffectInstance(this.getRandomEffect(false), duration, amplifier));
                    break;
                    default:
                        if (power > 50) {
                            world.playSound(null, this.getBlockPos(), SoundRegistry.CRIT_HIT_EVENT, SoundCategory.PLAYERS, .5f, 1f);
                        }
                        target.damage(DamageSource.MAGIC, power);
                    break;
                }
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.getOwner() != null && this.getOwner() instanceof LivingEntity) {
            this.randomCollisionEffect((LivingEntity)this.getOwner());
        }
        this.discard();
    }

    private void randomCollisionEffect(LivingEntity user) {
        int power = this.random.nextInt(10 + this.getLuckFactor(user)) + this.getLuckFactor(user);
        boolean unluckyAf = this.random.nextInt(100 + this.getLuckFactor(user)*10) == 1;
        if (unluckyAf) {
            for (int i = 0; i < 3; i++) {
                WitherEntity boss = new WitherEntity(EntityType.WITHER, world);
                boss.setPos(this.getX(), this.getY(), this.getZ());
                world.spawnEntity(boss);
            }
            return;
        }
        switch (this.getCollisionEffectType(user)) {
            case BATS:
                for (int i = 0; i < power*2; i++) {
                    BatEntity bat = new BatEntity(EntityType.BAT, world);
                    bat.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(bat);
                }
                break;
            case DARKNESS:
                world.playSound(null, this.getBlockPos(), SoundEvents.AMBIENT_CAVE.value(), SoundCategory.AMBIENT, 1f, 1f);
                ((ServerWorld) world).spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 5, 1.2D, 1.2D, 1.2D, 0.0D);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 60, 0));
                break;
            case LIGHTNING:
                for (int i = 0; i < power; i++) {
                    LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
                break;
            case PARTICLES:
                if (!this.world.isClient) {
                    ParticleNetworking.sendServerParticlePacket((ServerWorld) this.world, PacketRegistry.RANDOM_EXPLOSION_PACKET_ID, this.getBlockPos(), 1000);
                }
                break;
            case RANDOM_ENTITY:
                for (int i = 0; i < power; i++) {
                    Entity entity = this.getRandomEntity();
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
                break;
            case SPECIFIC_ENTITY:
                this.summonSpecificEntity(user, power);
                break;
            default:
                boolean bl = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
                this.world.createExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), power, bl, bl ? World.ExplosionSourceType.TNT : World.ExplosionSourceType.NONE);
                break;
        }
    }

    private void summonSpecificEntity(LivingEntity user, int power) {
        switch (this.getSpecificEntityType(user)) {
            case CATS:
                for (int i = 0; i < power; i++) {
                    CatEntity entity = new CatEntity(EntityType.CAT, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
                break;
            case CREEPER:
                for (int i = 0; i < power; i++) {
                    CreeperEntity entity = new CreeperEntity(EntityType.CREEPER, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
                break;
            case IRON_GOLEM:
                for (int i = 0; i < power; i++) {
                    IronGolemEntity entity = new IronGolemEntity(EntityType.IRON_GOLEM, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
                break;
            case STRIDER:
                for (int i = 0; i < power; i++) {
                    StriderEntity entity = new StriderEntity(EntityType.STRIDER, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
                break;
            case TRADER:
                for (int i = 0; i < power; i++) {
                    WanderingTraderEntity entity = new WanderingTraderEntity(EntityType.WANDERING_TRADER, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
                break;
            case WITHER_SKELETON:
                for (int i = 0; i < power; i++) {
                    WitherSkeletonEntity entity = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, world);
                    entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_AXE));
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
                break;
            default:
                for (int i = 0; i < power; i++) {
                    SkeletonEntity entity = new SkeletonEntity(EntityType.SKELETON, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
                break;
        }
    }

    private CollisionEffect getCollisionEffectType(LivingEntity user) {
        return (CollisionEffect) this.randomFromList(user, this.getTriggerList(), false);
    }

    private SpecificEntities getSpecificEntityType(LivingEntity user) {
        return (SpecificEntities) this.randomFromList(user, this.getSpecificEntityList(), false);
    }

    private Object[][] getTriggerList() {
        Object[][] triggers = {
            {CollisionEffect.LIGHTNING, LuckType.GOOD},
            {CollisionEffect.RANDOM_ENTITY, LuckType.BAD},
            {CollisionEffect.SPECIFIC_ENTITY, LuckType.BAD},
            {CollisionEffect.BATS, LuckType.BAD},
            {CollisionEffect.PARTICLES, LuckType.BAD},
            {CollisionEffect.DARKNESS, LuckType.BAD},
            {CollisionEffect.EXPLOSION, LuckType.GOOD},
        };
        return triggers;
    }

    private Object[][] getSpecificEntityList() {
        Object[][] entities = {
            {SpecificEntities.CATS, LuckType.NEUTRAL},
            {SpecificEntities.CREEPER, LuckType.BAD},
            {SpecificEntities.STRIDER, LuckType.NEUTRAL},
            {SpecificEntities.SKELETON, LuckType.BAD},
            {SpecificEntities.IRON_GOLEM, LuckType.GOOD},
            {SpecificEntities.WITHER_SKELETON, LuckType.BAD},
            {SpecificEntities.TRADER, LuckType.NEUTRAL},
        };
        return entities;
    }

    static enum CollisionEffect {
        LIGHTNING,
        RANDOM_ENTITY,
        SPECIFIC_ENTITY,
        BATS,
        PARTICLES,
        DARKNESS,
        EXPLOSION
    }

    static enum SpecificEntities {
        CATS,
        CREEPER,
        STRIDER,
        SKELETON,
        IRON_GOLEM,
        WITHER_SKELETON,
        TRADER
    }

    private Entity getRandomEntity() {
        if (this.getOwner() instanceof LivingEntity) {
            return (Entity) this.randomFromList((LivingEntity)this.getOwner(), this.getEntityList(), false);
        } else {
            //Chicken as default incase nothing works
            return new ChickenEntity(EntityType.CHICKEN, world);
        }
    }

    private StatusEffect getRandomEffect(boolean flipLuckTypes) {
        if (this.getOwner() instanceof LivingEntity) {
            return (StatusEffect) this.randomFromList((LivingEntity)this.getOwner(), this.getEffectList(), flipLuckTypes);
        } else {
            //Glowing as default incase nothing works
            return StatusEffects.GLOWING;
        }
    }

    /**
     * The new way to get a random object while including the status effect Luck found through
     * {@link #getLuckFactor(LivingEntity)} as a factor. As long as the given array contains exactly an
     * element with a given {@link LuckType}, an exception won't be thrown and the game won't crash :)
     */
    private Object randomFromList(LivingEntity user, Object[][] arr, boolean flipLuckTypes) {
        ArrayList<ArrayList<Object>> objectList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            ArrayList<Object> list = new ArrayList<>();
            list.add(arr[i][0]);
            list.add(arr[i][1]);

            int luckFactor;
            if (!flipLuckTypes) {
                switch ((LuckType)list.get(1)) {
                    case BAD:
                        luckFactor = (10 - this.getLuckFactor(user));
                        list.add(luckFactor);
                        break;
                    case GOOD:
                        luckFactor = (10 + this.getLuckFactor(user));
                        list.add(luckFactor);
                        break;
                    default:
                        luckFactor = 10;
                        list.add(luckFactor);
                        break;
                }
            } else {
                switch ((LuckType)list.get(1)) {
                    case GOOD:
                        luckFactor = (10 - this.getLuckFactor(user));
                        list.add(luckFactor);
                        break;
                    case BAD:
                        luckFactor = (10 + this.getLuckFactor(user));
                        list.add(luckFactor);
                        break;
                    default:
                        luckFactor = 10;
                        list.add(luckFactor);
                        break;
                }
            }
            
            if (!(luckFactor <= 0)) {
                objectList.add(list);
            }
        }

        int totalChance = 0;
        for (int i = 0; i < objectList.size(); i++) {
            totalChance += (int)objectList.get(i).get(2);
        }

        int random = new Random().nextInt(totalChance);
        int counter = 0;
        int under = 0;
        Object chosenObject = null;
        for (int i = 0; i < objectList.size(); i++) {
            counter += (int) objectList.get(i).get(2);
            if (i > 0) {
                under += (int) objectList.get(i - 1).get(2);
            } 
            if (random < counter && random >= under) {
                chosenObject = objectList.get(i).get(0);
            }
        }
        return chosenObject;
    }

    private Object[][] getEffectList() {
        Object[][] effects = {
            {StatusEffects.BLINDNESS, LuckType.BAD},
            {StatusEffects.WITHER, LuckType.BAD},
            {StatusEffects.GLOWING, LuckType.BAD},
            {StatusEffects.HASTE, LuckType.GOOD},
            {StatusEffects.STRENGTH, LuckType.GOOD},
            {StatusEffects.WEAKNESS, LuckType.GOOD},
            {StatusEffects.SLOWNESS, LuckType.BAD},
            {StatusEffects.INVISIBILITY, LuckType.NEUTRAL},
            {StatusEffects.SPEED, LuckType.GOOD},
            {StatusEffects.SLOW_FALLING, LuckType.BAD},
            {StatusEffects.REGENERATION, LuckType.GOOD},
            {StatusEffects.RESISTANCE, LuckType.GOOD},
            {StatusEffects.MINING_FATIGUE, LuckType.BAD},
            {StatusEffects.INSTANT_DAMAGE, LuckType.BAD},
            {StatusEffects.INSTANT_HEALTH, LuckType.GOOD},
            {StatusEffects.ABSORPTION, LuckType.GOOD},
            {StatusEffects.HUNGER, LuckType.BAD},
            {StatusEffects.POISON, LuckType.BAD},
            {StatusEffects.NAUSEA, LuckType.BAD}
        };
        return effects;
    }

    private Object[][] getEntityList() {
        Object[][] entities = {
            {new CreeperEntity(EntityType.CREEPER, world), LuckType.BAD},
            {new ZombieEntity(EntityType.ZOMBIE, world), LuckType.BAD},
            {new CowEntity(EntityType.COW, world), LuckType.NEUTRAL},
            {new CodEntity(EntityType.COD, world), LuckType.NEUTRAL},
            {new EndermiteEntity(EntityType.ENDERMITE, world), LuckType.BAD},
            {new BeeEntity(EntityType.BEE, world), LuckType.BAD},
            {new ExperienceOrbEntity(EntityType.EXPERIENCE_ORB, world), LuckType.GOOD},
            {new SalmonEntity(EntityType.SALMON, world), LuckType.NEUTRAL},
            {new PufferfishEntity(EntityType.PUFFERFISH, world), LuckType.BAD},
            {new VexEntity(EntityType.VEX, world), LuckType.BAD},
            {new SkeletonEntity(EntityType.SKELETON, world), LuckType.BAD},
            {new TropicalFishEntity(EntityType.TROPICAL_FISH, world), LuckType.NEUTRAL},
            {new WanderingTraderEntity(EntityType.WANDERING_TRADER, world), LuckType.NEUTRAL},
            {new ChickenEntity(EntityType.CHICKEN, world), LuckType.NEUTRAL}
        };
        return entities;
    }

    private int getLuckFactor(LivingEntity entity) {
        if (entity.hasStatusEffect(StatusEffects.LUCK)) {
            return entity.getStatusEffect(StatusEffects.LUCK).getAmplifier()*2 + 2;
        } else if (entity.hasStatusEffect(StatusEffects.UNLUCK)) {
            return -(entity.getStatusEffect(StatusEffects.UNLUCK).getAmplifier()*2 + 2);
        } else {
            return 0;
        }
    }
}
