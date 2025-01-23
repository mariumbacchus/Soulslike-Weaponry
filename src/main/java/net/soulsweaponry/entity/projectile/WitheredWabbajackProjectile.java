package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.*;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.util.LuckChosenObject;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WitheredWabbajackProjectile extends WitherSkullEntity {

    private static final List<LuckChosenObject<EntityType<?>>> ENTITIES = new ArrayList<>();
    private static final List<LuckChosenObject<CollisionEffect>> COLLISIONS = new ArrayList<>();

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
        if (!this.getWorld().isClient) {
            Entity entity = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            if (entity instanceof LivingEntity target && owner instanceof LivingEntity user) {
                Random random = new Random();
                int rng = random.nextInt(10) + 1;
                int power = this.getBound(75 , 5, user) + WeaponUtil.getLuckFactor(user) * 5;
                int amplifier = this.getBound(3 , 1, user) + WeaponUtil.getLuckFactor(user)/2;
                int duration = this.getBound(300 , 50, user) + WeaponUtil.getLuckFactor(user) * 50;
                switch (rng) {
                    case 1, 2, 3 ->
                            target.addStatusEffect(new StatusEffectInstance(this.getRandomEffect(true), duration, amplifier));
                    case 4, 5 ->
                            user.addStatusEffect(new StatusEffectInstance(this.getRandomEffect(false), duration, amplifier));
                    case 6 -> {
                        boolean luck = this.getBound(20, 1, user) + WeaponUtil.getLuckFactor(user) > 10;
                        LivingEntity living = luck ? target : user;
                        living.getArmorItems().iterator().forEachRemaining(itemStack -> {
                            boolean bl = this.random.nextBoolean();
                            if (bl) {
                                ItemStack separate = itemStack.copy();
                                living.dropStack(separate);
                                itemStack.decrement(1);
                                getWorld().playSound(null, living.getBlockPos(), SoundEvents.ENTITY_ARMOR_STAND_BREAK, SoundCategory.PLAYERS, 1f, 1f);
                            }
                        });
                    }
                    default -> {
                        if (power > 50) {
                            getWorld().playSound(null, this.getBlockPos(), SoundRegistry.CRIT_HIT_EVENT, SoundCategory.PLAYERS, .5f, 1f);
                        }
                        target.damage(this.getWorld().getDamageSources().magic(), power);
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > 100) {
            this.resetListLuckFactors();
            this.discard();
        }
    }

    private void resetListLuckFactors() {
        for (LuckChosenObject<?> luckChosenEntity : ENTITIES) {
            luckChosenEntity.setLuckFactor(10);
        }
        for (LuckChosenObject<?> luckChosenEntity : COLLISIONS) {
            luckChosenEntity.setLuckFactor(10);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onEntityHit((EntityHitResult)hitResult);
            this.getWorld().emitGameEvent(GameEvent.PROJECTILE_LAND, hitResult.getPos(), GameEvent.Emitter.of(this, null));
        } else if (type == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult)hitResult;
            this.onBlockHit(blockHitResult);
            BlockPos blockPos = blockHitResult.getBlockPos();
            this.getWorld().emitGameEvent(GameEvent.PROJECTILE_LAND, blockPos, GameEvent.Emitter.of(this, this.getWorld().getBlockState(blockPos)));
        }
        if (this.getOwner() != null && this.getOwner() instanceof LivingEntity) {
            this.randomCollisionEffect((LivingEntity)this.getOwner());
        }
        this.resetListLuckFactors();
        this.discard();
    }

    private void randomCollisionEffect(LivingEntity user) {
        int power = this.getBound(10 , 1, user) + WeaponUtil.getLuckFactor(user);
        boolean unluckyAf = this.getBound(100 , 10, user) == 1;
        if (unluckyAf) {
            boolean isWarden = this.random.nextBoolean();
            for (int i = 0; i < 3; i++) {
                if (isWarden && !this.getWorld().isClient) {
                    WardenEntity warden = new WardenEntity(EntityType.WARDEN, getWorld());
                    warden.initialize((ServerWorldAccess) getWorld(), getWorld().getLocalDifficulty(user.getBlockPos()), SpawnReason.MOB_SUMMONED, null, null);
                    warden.setPos(this.getX(), this.getY(), this.getZ());
                    warden.increaseAngerAt(user, 80, true);
                    getWorld().spawnEntity(warden);
                } else {
                    WitherEntity boss = new WitherEntity(EntityType.WITHER, getWorld());
                    boss.setPos(this.getX(), this.getY(), this.getZ());
                    getWorld().spawnEntity(boss);
                }
            }
            return;
        }
        switch (this.getCollisionEffectType(user)) {
            case BATS -> {
                for (int i = 0; i < power * 2; i++) {
                    BatEntity bat = new BatEntity(EntityType.BAT, getWorld());
                    bat.setPos(this.getX(), this.getY(), this.getZ());
                    this.getWorld().spawnEntity(bat);
                }
            }
            case DARKNESS -> {
                getWorld().playSound(null, this.getBlockPos(), SoundEvents.AMBIENT_CAVE.value(), SoundCategory.AMBIENT, 1f, 1f);
                getWorld().playSound(null, this.getBlockPos(), SoundEvents.ENTITY_WARDEN_EMERGE, SoundCategory.HOSTILE, 1f, 1f);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.DARKNESS, 60, 0));
            }
            case LIGHTNING -> {
                for (int i = 0; i < power; i++) {
                    LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, getWorld());
                    entity.setPos(this.getX(), this.getY(), this.getZ());
                    this.getWorld().spawnEntity(entity);
                }
            }
            case PARTICLES -> {
                if (!this.getWorld().isClient) {
                    DefaultParticleType particle = this.getRandomParticle();
                    int amount = 1000;
                    if (particle == ParticleTypes.ELDER_GUARDIAN) {
                        amount = 1;
                    }
                    ParticleHandler.particleSphereList(this.getWorld(), amount, this.getX(), this.getY(), this.getZ(), 1f, particle);
                }
            }
            case RANDOM_ENTITY -> this.summonRandomEntity(user, power);
            case SPECIFIC_ENTITY -> this.summonSpecificEntity(user, power);
            case CURSE -> {
                this.getWorld().addParticle(ParticleTypes.ELDER_GUARDIAN, this.getX(), this.getY(), this.getZ(), 0f, 0f, 0f);
                this.getWorld().playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 1f, 1f);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 200, 5));
            }
            default -> {
                boolean bl = getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
                this.getWorld().createExplosion(this.getOwner(), this.getX(), this.getY(), this.getZ(), power, bl, bl ? World.ExplosionSourceType.TNT : World.ExplosionSourceType.NONE);
            }
        }
    }

    private DefaultParticleType getRandomParticle() {
        Random number = new Random();
        ArrayList<DefaultParticleType> arr = new ArrayList<>();
        Registries.PARTICLE_TYPE.stream().forEach(p -> {
            if (p instanceof DefaultParticleType d) {
                arr.add(d);
            }
        });
        int rng = number.nextInt(arr.size());
        return arr.get(rng);
    }

    private void summonSpecificEntity(LivingEntity user, int power) {
        if (!this.getWorld().isClient) {
            EntityType<?> type = this.getEntityType(user);
            for (int i = 0; i < power; i++) {
                type.spawn((ServerWorld) this.getWorld(), this.getBlockPos(), SpawnReason.EVENT);
            }
        }
    }

    private void summonRandomEntity(LivingEntity user, int power) {
        if (!this.getWorld().isClient) {
            for (int i = 0; i < power; i++) {
                EntityType<?> type = this.getEntityType(user);
                type.spawn((ServerWorld) this.getWorld(), this.getBlockPos(), SpawnReason.EVENT);
            }
        }
    }

    private int getBound(int bound, int luckModifier, LivingEntity user) {
        int b = bound + WeaponUtil.getLuckFactor(user) * luckModifier;
        return b > 0 ? this.random.nextInt(b) : 1;
    }

    private CollisionEffect getCollisionEffectType(LivingEntity user) {
        return WeaponUtil.getRandomlyChosenObject(user, COLLISIONS, false);
    }

    private EntityType<?> getEntityType(LivingEntity user) {
        return WeaponUtil.getRandomlyChosenObject(user, ENTITIES, false);
    }

    private StatusEffect getRandomEffect(boolean flipLuckTypes) {
        if (this.getOwner() instanceof LivingEntity) {
            return WeaponUtil.getRandomlyChosenObject((LivingEntity)this.getOwner(), this.getEffectList(), flipLuckTypes);
        } else {
            //Glowing as default incase nothing works
            return StatusEffects.GLOWING;
        }
    }

    private List<LuckChosenObject<StatusEffect>> getEffectList() {
        List<LuckChosenObject<StatusEffect>> list = new ArrayList<>();
        for (StatusEffect effect : Registries.STATUS_EFFECT) {
            if (effect.getCategory().equals(StatusEffectCategory.HARMFUL)) {
                list.add(new LuckChosenObject<>(effect, WeaponUtil.LuckType.BAD));
            } else if (effect.getCategory().equals(StatusEffectCategory.BENEFICIAL)) {
                list.add(new LuckChosenObject<>(effect, WeaponUtil.LuckType.GOOD));
            } else {
                list.add(new LuckChosenObject<>(effect, WeaponUtil.LuckType.NEUTRAL));
            }
        }
        return list;
    }

    static {
        ENTITIES.add(new LuckChosenObject<>(EntityType.CREEPER, WeaponUtil.LuckType.BAD));
        ENTITIES.add(new LuckChosenObject<>(EntityType.ZOMBIE, WeaponUtil.LuckType.BAD));
        ENTITIES.add(new LuckChosenObject<>(EntityType.ENDERMITE, WeaponUtil.LuckType.BAD));
        ENTITIES.add(new LuckChosenObject<>(EntityType.BEE, WeaponUtil.LuckType.BAD));
        ENTITIES.add(new LuckChosenObject<>(EntityType.PUFFERFISH, WeaponUtil.LuckType.BAD));
        ENTITIES.add(new LuckChosenObject<>(EntityType.VEX, WeaponUtil.LuckType.BAD));
        ENTITIES.add(new LuckChosenObject<>(EntityType.SKELETON, WeaponUtil.LuckType.BAD));
        ENTITIES.add(new LuckChosenObject<>(EntityType.DROWNED, WeaponUtil.LuckType.BAD));
        ENTITIES.add(new LuckChosenObject<>(EntityType.GUARDIAN, WeaponUtil.LuckType.BAD));
        ENTITIES.add(new LuckChosenObject<>(EntityType.SALMON, WeaponUtil.LuckType.NEUTRAL));
        ENTITIES.add(new LuckChosenObject<>(EntityType.COW, WeaponUtil.LuckType.NEUTRAL));
        ENTITIES.add(new LuckChosenObject<>(EntityType.COD, WeaponUtil.LuckType.NEUTRAL));
        ENTITIES.add(new LuckChosenObject<>(EntityType.TROPICAL_FISH, WeaponUtil.LuckType.NEUTRAL));
        ENTITIES.add(new LuckChosenObject<>(EntityType.WANDERING_TRADER, WeaponUtil.LuckType.NEUTRAL));
        ENTITIES.add(new LuckChosenObject<>(EntityType.CHICKEN, WeaponUtil.LuckType.NEUTRAL));
        ENTITIES.add(new LuckChosenObject<>(EntityType.SHEEP, WeaponUtil.LuckType.NEUTRAL));
        ENTITIES.add(new LuckChosenObject<>(EntityType.EXPERIENCE_ORB, WeaponUtil.LuckType.GOOD));
    }

    static {
        COLLISIONS.add(new LuckChosenObject<>(CollisionEffect.RANDOM_ENTITY, WeaponUtil.LuckType.BAD));
        COLLISIONS.add(new LuckChosenObject<>(CollisionEffect.SPECIFIC_ENTITY, WeaponUtil.LuckType.BAD));
        COLLISIONS.add(new LuckChosenObject<>(CollisionEffect.BATS, WeaponUtil.LuckType.BAD));
        COLLISIONS.add(new LuckChosenObject<>(CollisionEffect.PARTICLES, WeaponUtil.LuckType.BAD));
        COLLISIONS.add(new LuckChosenObject<>(CollisionEffect.DARKNESS, WeaponUtil.LuckType.BAD));
        COLLISIONS.add(new LuckChosenObject<>(CollisionEffect.CURSE, WeaponUtil.LuckType.BAD));
        COLLISIONS.add(new LuckChosenObject<>(CollisionEffect.LIGHTNING, WeaponUtil.LuckType.GOOD));
        COLLISIONS.add(new LuckChosenObject<>(CollisionEffect.EXPLOSION, WeaponUtil.LuckType.GOOD));
    }

    enum CollisionEffect {
        LIGHTNING, RANDOM_ENTITY, SPECIFIC_ENTITY, BATS, PARTICLES, DARKNESS, EXPLOSION, CURSE
    }
}
