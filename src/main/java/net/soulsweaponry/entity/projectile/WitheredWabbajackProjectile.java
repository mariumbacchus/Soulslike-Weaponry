package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.*;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import net.soulsweaponry.items.WitheredWabbajack.LuckType;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class WitheredWabbajackProjectile extends WitherSkullEntity {

    public WitheredWabbajackProjectile(EntityType<? extends WitheredWabbajackProjectile> entityType, World world) {
        super(entityType, world);
    }

    public WitheredWabbajackProjectile(World world, LivingEntity owner, double directionX, double directionY, double directionZ) {
        this(owner.getX(), owner.getY(), owner.getZ(), directionX, directionY, directionZ, world);
        this.setOwner(owner);
        this.setRotation(owner.getYaw(), owner.getPitch());
    }

    public WitheredWabbajackProjectile(double x, double y, double z, double directionX, double directionY, double directionZ, World world) {
        super(EntityRegistry.WITHERED_WABBAJACK_PROJECTILE, world);
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
    public void tick() {
        super.tick();
        if (this.age > 100) {
            this.discard();
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
                world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_WITHER_SHOOT, SoundCategory.HOSTILE, 1f, 1f);
                world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_CREEPER_PRIMED, SoundCategory.HOSTILE, 1f, 1f);
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
                    DefaultParticleType particle = this.getRandomParticle();
                    int amount = 1000;
                    if (particle == ParticleTypes.ELDER_GUARDIAN) {
                        amount = 1;
                    }
                    ParticleHandler.particleSphereList(world, amount, this.getX(), this.getY(), this.getZ(), 1f, particle);
                }
            }
            case RANDOM_ENTITY -> this.summonRandomEntity(user, power);
            case SPECIFIC_ENTITY -> this.summonSpecificEntity(user, power);
            case CURSE -> {
                this.world.addParticle(ParticleTypes.ELDER_GUARDIAN, this.getX(), this.getY(), this.getZ(), 0f, 0f, 0f);
                this.world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 1f, 1f);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 5));
            }
            default -> {
                boolean bl = world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
                this.world.createExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), power, bl, bl ? Explosion.DestructionType.BREAK : Explosion.DestructionType.NONE);
            }
        }
    }

    private DefaultParticleType getRandomParticle() {
        Random number = new Random();
        ArrayList<DefaultParticleType> arr = new ArrayList<>();
        Registry.PARTICLE_TYPE.stream().forEach(p -> {
            if (p instanceof DefaultParticleType d) {
                arr.add(d);
            }
        });
        int rng = number.nextInt(arr.size());
        return arr.get(rng);
    }

    private void summonSpecificEntity(LivingEntity user, int power) {
        if (!this.world.isClient) {
            EntityType<?> type = this.getEntityType(user);
            for (int i = 0; i < power; i++) {
                type.spawn((ServerWorld) this.world, null, null, null, this.getBlockPos(), SpawnReason.EVENT, false, false);
            }
        }
    }

    private void summonRandomEntity(LivingEntity user, int power) {
        if (!this.world.isClient) {
            for (int i = 0; i < power; i++) {
                EntityType<?> type = this.getEntityType(user);
                type.spawn((ServerWorld) this.world, null, null, null, this.getBlockPos(), SpawnReason.EVENT, false, false);
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

    private EntityType<?> getEntityType(LivingEntity user) {
        return (EntityType<?>) this.randomFromList(user, this.getEntityList(), false);
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
                {CollisionEffect.CURSE, LuckType.BAD},
        };
    }

    enum CollisionEffect {
        LIGHTNING, RANDOM_ENTITY, SPECIFIC_ENTITY, BATS, PARTICLES, DARKNESS, EXPLOSION, CURSE
    }

    private StatusEffect getRandomEffect(boolean flipLuckTypes) {
        if (this.getOwner() instanceof LivingEntity) {
            return (StatusEffect) this.randomFromList((LivingEntity)this.getOwner(), this.getEffectObjectList(), flipLuckTypes);
        } else {
            //Glowing as default incase nothing works
            return StatusEffects.GLOWING;
        }
    }

    /**
     * The new way to get a random object while including the status effect Luck found through
     * {@link #getLuckFactor(LivingEntity)} as a factor. As long as the given array contains exactly an
     * element with a given {@link LuckType}, an exception won't be thrown and the game won't crash :)
     * <p></p>
     * EDIT: Cool message, past me! But this sucks! Message to future me: rework it one day when you feel like it :)
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

    private HashMap<StatusEffect, LuckType> getEffectMap() {
        HashMap<StatusEffect, LuckType> map = new HashMap<>();
        for (StatusEffect effect : Registry.STATUS_EFFECT) {
            if (effect.getCategory().equals(StatusEffectCategory.HARMFUL)) {
                map.put(effect, LuckType.BAD);
            } else if (effect.getCategory().equals(StatusEffectCategory.BENEFICIAL)) {
                map.put(effect, LuckType.GOOD);
            } else {
                map.put(effect, LuckType.NEUTRAL);
            }
        }
        return map;
    }

    /**
     * I'm not going to bother reworking this class today so this will do :)
     */
    private Object[][] getEffectObjectList() {
        HashMap<StatusEffect, LuckType> map = this.getEffectMap();
        int len = map.size();
        Object[][] arr = new Object[len][2];
        int i = 0;
        for (StatusEffect effect : map.keySet()) {
            arr[i][0] = effect;
            arr[i][1] = map.get(effect);
            i++;
        }
        return arr;
    }

    private Object[][] getEntityList() {
        return new Object[][]{
                {EntityType.CREEPER, LuckType.BAD},
                {EntityType.ZOMBIE, LuckType.BAD},
                {EntityType.COW, LuckType.NEUTRAL},
                {EntityType.COD, LuckType.NEUTRAL},
                {EntityType.ENDERMITE, LuckType.BAD},
                {EntityType.BEE, LuckType.BAD},
                {EntityType.EXPERIENCE_ORB, LuckType.GOOD},
                {EntityType.SALMON, LuckType.NEUTRAL},
                {EntityType.PUFFERFISH, LuckType.BAD},
                {EntityType.VEX, LuckType.BAD},
                {EntityType.SKELETON, LuckType.BAD},
                {EntityType.TROPICAL_FISH, LuckType.NEUTRAL},
                {EntityType.WANDERING_TRADER, LuckType.NEUTRAL},
                {EntityType.CHICKEN, LuckType.NEUTRAL},
                {EntityType.SHEEP, LuckType.NEUTRAL},
                {EntityType.DROWNED, LuckType.BAD},
                {EntityType.GUARDIAN, LuckType.BAD},
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