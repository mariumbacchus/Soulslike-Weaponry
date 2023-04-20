package net.soulsweaponry.entity.projectile;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.*;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
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
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import net.soulsweaponry.items.WitheredWabbajack.LuckType;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleNetworking;

public class WitheredWabbajackProjectile extends WitherSkullEntity {

    public WitheredWabbajackProjectile(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        this(owner.getX(), owner.getY(), owner.getZ(), directionX, directionY, directionZ, world);
        this.setOwner(owner);
        this.setRotation(owner.getYaw(), owner.getPitch());
    }

    public WitheredWabbajackProjectile(double x, double y, double z, double directionX, double directionY, double directionZ, World world) {
        super(EntityType.WITHER_SKULL, world);
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

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        if (!this.world.isClient) {
            Entity entity = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            if (entity instanceof LivingEntity target && owner instanceof LivingEntity user) {
                Random random = new Random();
                int rng = random.nextInt(10) + 1;
                int power = this.getBound(75 , 5, user) + this.getLuckFactor(user) * 5;
                int amplifier = this.getBound(3 , 1, user) + this.getLuckFactor(user)/2;
                int duration = this.getBound(300 , 50, user) + this.getLuckFactor(user) * 50;
                switch (rng) {
                    case 1, 2, 3 ->
                            target.addStatusEffect(new StatusEffectInstance(this.getRandomEffect(true), duration, amplifier));
                    case 4, 5 ->
                            user.addStatusEffect(new StatusEffectInstance(this.getRandomEffect(false), duration, amplifier));
                    case 6 -> {
                        boolean luck = this.getBound(20, 1, user) + this.getLuckFactor(user) > 10;
                        LivingEntity living = luck ? target : user;
                        living.getArmorItems().iterator().forEachRemaining(itemStack -> {
                            boolean bl = this.random.nextBoolean();
                            if (bl) {
                                ItemStack separate = itemStack.copy();
                                living.dropStack(separate);
                                itemStack.decrement(1);
                                world.playSound(null, living.getBlockPos(), SoundEvents.ENTITY_ARMOR_STAND_BREAK, SoundCategory.PLAYERS, 1f, 1f);
                            }
                        });
                    }
                    default -> {
                        if (power > 50) {
                            world.playSound(null, this.getBlockPos(), SoundRegistry.CRIT_HIT_EVENT, SoundCategory.PLAYERS, .5f, 1f);
                        }
                        target.damage(DamageSource.MAGIC, power);
                    }
                }
            }
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult)hitResult);
        } else if (type == HitResult.Type.BLOCK) {
            this.onBlockHit((BlockHitResult)hitResult);
        }
        if (type != HitResult.Type.MISS) {
            this.emitGameEvent(GameEvent.PROJECTILE_LAND, this.getOwner());
        }
        if (this.getOwner() != null && this.getOwner() instanceof LivingEntity) {
            this.randomCollisionEffect((LivingEntity)this.getOwner());
        }
        this.discard();
    }

    private void randomCollisionEffect(LivingEntity user) {
        int power = this.getBound(10 , 1, user) + this.getLuckFactor(user);
        boolean unluckyAf = this.getBound(100 , 10, user) == 1;
        if (unluckyAf) {
            for (int i = 0; i < 3; i++) {
                WitherEntity boss = new WitherEntity(EntityType.WITHER, world);
                boss.setPos(this.getX(), this.getY(), this.getZ());
                world.spawnEntity(boss);
            }
            return;
        }
        switch (this.getCollisionEffectType(user)) {
            case BATS -> {
                for (int i = 0; i < power * 2; i++) {
                    BatEntity bat = new BatEntity(EntityType.BAT, world);
                    bat.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(bat);
                }
            }
            case DARKNESS -> {
                world.playSound(null, this.getBlockPos(), SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, 1f, 1f);
                ((ServerWorld) world).spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 5, 1.2D, 1.2D, 1.2D, 0.0D);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 30, 0));
            }
            case LIGHTNING -> {
                for (int i = 0; i < power; i++) {
                    LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
            }
            case PARTICLES -> {
                if (!this.world.isClient) {
                    ParticleNetworking.sendServerParticlePacket((ServerWorld) this.world, PacketRegistry.RANDOM_EXPLOSION_PACKET_ID, this.getBlockPos(), 1000);
                }
            }
            case RANDOM_ENTITY -> {
                for (int i = 0; i < power; i++) {
                    Entity entity = this.getRandomEntity();
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
            }
            case SPECIFIC_ENTITY -> this.summonSpecificEntity(user, power);
            default -> {
                boolean bl = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
                this.world.createExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), power, bl, bl ? Explosion.DestructionType.BREAK : Explosion.DestructionType.NONE);
            }
        }
    }

    private void summonSpecificEntity(LivingEntity user, int power) {
        switch (this.getSpecificEntityType(user)) {
            case CATS -> {
                for (int i = 0; i < power; i++) {
                    CatEntity entity = new CatEntity(EntityType.CAT, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
            }
            case CREEPER -> {
                for (int i = 0; i < power; i++) {
                    CreeperEntity entity = new CreeperEntity(EntityType.CREEPER, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
            }
            case IRON_GOLEM -> {
                for (int i = 0; i < power; i++) {
                    IronGolemEntity entity = new IronGolemEntity(EntityType.IRON_GOLEM, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
            }
            case STRIDER -> {
                for (int i = 0; i < power; i++) {
                    StriderEntity entity = new StriderEntity(EntityType.STRIDER, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
            }
            case TRADER -> {
                for (int i = 0; i < power; i++) {
                    WanderingTraderEntity entity = new WanderingTraderEntity(EntityType.WANDERING_TRADER, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
            }
            case WITHER_SKELETON -> {
                for (int i = 0; i < power; i++) {
                    WitherSkeletonEntity entity = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, world);
                    entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_AXE));
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
            }
            default -> {
                for (int i = 0; i < power; i++) {
                    SkeletonEntity entity = new SkeletonEntity(EntityType.SKELETON, world);
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.world.spawnEntity(entity);
                }
            }
        }
    }

    private int getBound(int bound, int luckModifier, LivingEntity user) {
        int b = bound + this.getLuckFactor(user) * luckModifier;
        return b > 0 ? this.random.nextInt(b) : 1;
    }

    private CollisionEffect getCollisionEffectType(LivingEntity user) {
        return (CollisionEffect) this.randomFromList(user, this.getTriggerList(), false);
    }

    private SpecificEntities getSpecificEntityType(LivingEntity user) {
        return (SpecificEntities) this.randomFromList(user, this.getSpecificEntityList(), false);
    }

    private Object[][] getTriggerList() {
        return new Object[][]{
                {CollisionEffect.LIGHTNING, LuckType.GOOD},
                {CollisionEffect.RANDOM_ENTITY, LuckType.BAD},
                {CollisionEffect.SPECIFIC_ENTITY, LuckType.BAD},
                {CollisionEffect.BATS, LuckType.BAD},
                {CollisionEffect.PARTICLES, LuckType.BAD},
                {CollisionEffect.DARKNESS, LuckType.BAD},
                {CollisionEffect.EXPLOSION, LuckType.GOOD},
        };
    }

    private Object[][] getSpecificEntityList() {
        return new Object[][]{
                {SpecificEntities.CATS, LuckType.NEUTRAL},
                {SpecificEntities.CREEPER, LuckType.BAD},
                {SpecificEntities.STRIDER, LuckType.NEUTRAL},
                {SpecificEntities.SKELETON, LuckType.BAD},
                {SpecificEntities.IRON_GOLEM, LuckType.GOOD},
                {SpecificEntities.WITHER_SKELETON, LuckType.BAD},
                {SpecificEntities.TRADER, LuckType.NEUTRAL},
        };
    }

    enum CollisionEffect {
        LIGHTNING, RANDOM_ENTITY, SPECIFIC_ENTITY, BATS, PARTICLES, DARKNESS, EXPLOSION
    }

    enum SpecificEntities {
        CATS, CREEPER, STRIDER, SKELETON, IRON_GOLEM, WITHER_SKELETON, TRADER
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
        for (Object[] objects : arr) {
            ArrayList<Object> list = new ArrayList<>();
            list.add(objects[0]);
            list.add(objects[1]);

            int luckFactor;
            if (!flipLuckTypes) {
                switch ((LuckType) list.get(1)) {
                    case BAD -> {
                        luckFactor = (10 - this.getLuckFactor(user));
                        list.add(luckFactor);
                    }
                    case GOOD -> {
                        luckFactor = (10 + this.getLuckFactor(user));
                        list.add(luckFactor);
                    }
                    default -> {
                        luckFactor = 10;
                        list.add(luckFactor);
                    }
                }
            } else {
                switch ((LuckType) list.get(1)) {
                    case GOOD -> {
                        luckFactor = (10 - this.getLuckFactor(user));
                        list.add(luckFactor);
                    }
                    case BAD -> {
                        luckFactor = (10 + this.getLuckFactor(user));
                        list.add(luckFactor);
                    }
                    default -> {
                        luckFactor = 10;
                        list.add(luckFactor);
                    }
                }
            }

            if (!(luckFactor <= 0)) {
                objectList.add(list);
            }
        }

        int totalChance = 0;
        for (ArrayList<Object> objects : objectList) {
            totalChance += (int) objects.get(2);
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
        return new Object[][]{
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
    }

    private Object[][] getEntityList() {
        return new Object[][]{
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
    }

    private int getLuckFactor(LivingEntity entity) {
        if (entity.hasStatusEffect(StatusEffects.LUCK)) {
            return entity.getStatusEffect(StatusEffects.LUCK).getAmplifier() * 2 + 2;
        } else if (entity.hasStatusEffect(StatusEffects.UNLUCK)) {
            return - entity.getStatusEffect(StatusEffects.UNLUCK).getAmplifier() * 2 + 2;
        } else {
            return 0;
        }
    }
}