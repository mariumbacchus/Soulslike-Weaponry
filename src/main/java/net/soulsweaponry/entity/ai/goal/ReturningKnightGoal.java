package net.soulsweaponry.entity.ai.goal;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.ai.goal.hitboxes.ReturningKnightHitboxes;
import net.soulsweaponry.entity.mobs.DarkSorcerer;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.entity.mobs.ReturningKnight;
import net.soulsweaponry.entity.util.RandomSummonPos;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.*;

public class ReturningKnightGoal extends Goal {
    private final ReturningKnight boss;
    private int targetNotVisibleTicks;
    private boolean hasUsedUnbreakable;
    private int unbreakableTimer;
    private int attackCooldown;
    private BlockPos targetPos;
    private boolean cordsRegistered;
    private int attackStatus;
    private int specialCooldown;
    private int summonCooldown;
    int randomAttack = 3;
    private final int numberOfAttacks = 6; // 4 = 0 = obliterate, 5 = mace of spades
    private final RotatableHitbox obliterateMaceHitbox = ReturningKnightHitboxes.createObliterateMaceHitboxPlaceholder();
    private final Set<UUID> obliterateHitEntities = new HashSet<>();
    private boolean obliterateImpactDone;

    public ReturningKnightGoal(ReturningKnight boss) {
        this.boss = boss;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        LivingEntity target = this.boss.getTarget();
        return target != null && target.isAlive() && this.boss.canTarget(target);
    }

    public void resetAttackCooldown(float cooldownModifier) {
        this.attackCooldown = (int) Math.floor(EntityConfig.returning_knight_attack_cooldown_ticks * cooldownModifier) - this.boss.getReducedCooldownAttackers()*2;
    }

    public void resetSummonCooldown(float cooldownModifier) {
        this.summonCooldown = (int) Math.floor(EntityConfig.returning_knight_summon_cooldown_ticks * cooldownModifier) - this.boss.getReducedCooldownAttackers()*2;
    }

    public void resetSpecialCooldown(float cooldownModifier) {
        this.specialCooldown = (int) Math.floor(EntityConfig.returning_knight_special_cooldown_ticks * cooldownModifier) - this.boss.getReducedCooldownAttackers()*2;
    }

    public float getModifiedDamage(float damage) {
        return damage * EntityConfig.returning_knight_damage_modifier;
    }

    @Override
    public void stop() {
        super.stop();
        this.boss.setAttacking(false);
        this.boss.setObliterate(false);
        this.boss.setBlind(false);
        this.boss.setRupture(false);
        this.boss.setSummon(false);
        this.boss.setMaceOfSpades(false);
        this.attackCooldown = 10;
        this.attackStatus = 0;
        this.cordsRegistered = false;
        this.boss.setAttackStartWorldTime(-1L);
        this.obliterateHitEntities.clear();
        this.obliterateImpactDone = false;
    }

    public void tick() {
        attackCooldown--;
        unbreakableTimer--;
        specialCooldown--;
        summonCooldown--;
        LivingEntity target = this.boss.getTarget();

        if (target != null && !this.boss.isSpawning()) {
            this.boss.setAttacking(true);                
            this.boss.getLookControl().lookAt(target.getX(), target.getEyeY(), target.getZ());

            boolean entityInSight = this.boss.getVisibilityCache().canSee(target);
            double distanceToEntity = this.boss.squaredDistanceTo(target);

            if (entityInSight) {
                this.targetNotVisibleTicks = 0;
            } else {
                ++this.targetNotVisibleTicks;
            }

            //Unbreakable
            if (this.boss.getHealth() <= this.boss.getMaxHealth() / 2.0F && !this.hasUsedUnbreakable && this.attackCooldown > 20) {
                this.hasUsedUnbreakable = true;
                this.unbreakableTimer = 38;
                this.boss.setUnbreakable(true);
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 40));
            } else if (this.unbreakableTimer == 19) {
                this.boss.getWorld().playSound(null, this.boss.getBlockPos(), SoundRegistry.NIGHTFALL_SHIELD_EVENT, SoundCategory.HOSTILE, .75f, 1f);
            }
            if (this.unbreakableTimer < 0) {
                this.boss.setUnbreakable(false);
                this.unbreakableTimer = -5;
            }

            //Children of the grave (Summoning), only do if no healers are alive
            if (this.attackCooldown < 0 && this.specialCooldown < 0 && this.summonCooldown < 0 && this.randomAttack == 3 && !this.boss.hasHealersAlive()) {
                this.boss.setSummon(true);
            }
            if (this.boss.getSummon()) {
                this.attackStatus++;
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
                if (this.attackStatus == 30) { //58,4 ticks
                    int enemyNumber = this.boss.getRandom().nextInt(5 - 2) + 2;
                    int healerNumber = this.boss.getRandom().nextInt(3 - 1) + 1;
                    if (this.boss.getHealth() <= this.boss.getMaxHealth() / 2.0F) {
                        enemyNumber += healerNumber;
                        healerNumber = 0;
                    }
                    RandomSummonPos remnants = new RandomSummonPos(this.boss.getWorld(), this.boss.getRandom(), enemyNumber, 10, this.boss.getBlockPos(), 10, 8, 5, (pos) -> this.summonAllies(pos, false));
                    RandomSummonPos healers = new RandomSummonPos(this.boss.getWorld(), this.boss.getRandom(), healerNumber, 10, this.boss.getBlockPos(), 10, 8, 5, (pos) -> this.summonAllies(pos, true));
                    remnants.applySummonSpawns();
                    healers.applySummonSpawns();
                    if (!this.boss.getWorld().isClient) {
                        ParticleHandler.particleOutburstMap(this.boss.getWorld(), 300, target.getX(), target.getY(), target.getZ(), ParticleEvents.GROUND_RUPTURE_MAP, 1f);
                    }
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 1));
                    this.boss.getWorld().playSound(null, target.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 0.7f, 1f);
                }
                if (this.attackStatus >= 48) { //96,6 ticks
                    this.boss.setSummon(false);
                    this.resetAttackCooldown(1);
                    this.resetSummonCooldown(1);
                    this.resetSpecialCooldown(1.5f);
                    this.attackStatus = 0;
                    this.randomAttack = this.boss.getRandom().nextInt(this.numberOfAttacks);
                }
            }
            
            //Mace of Spades
            if (this.attackCooldown < 0 && !this.cordsRegistered && distanceToEntity < 50D && this.randomAttack == 5 && target.getBlockPos() != null) {
                this.targetPos = target.getBlockPos();
                this.boss.setObliterateTarget(this.targetPos);
                this.boss.setObliterate(true);

                this.obliterateHitEntities.clear();
                this.obliterateImpactDone = false;

                this.cordsRegistered = true;
            }
            if (this.boss.getMaceOfSpades() && this.targetPos != null) {
                this.attackStatus++;
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
                this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
                this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
                
                Box aoe = new Box(targetPos.getX() - 5, targetPos.getY() - 2, targetPos.getZ() - 5, targetPos.getX() + 5, targetPos.getY() + 2, targetPos.getZ() + 5);
                List<Entity> entities = this.boss.getWorld().getOtherEntities(this.boss, aoe);
                if (this.attackStatus == 7) {
                    for (Entity entity : entities) {
                        if (entity instanceof LivingEntity livingEntity) {
                            livingEntity.takeKnockback(2f, -(livingEntity.getX() - this.boss.getX()), -(livingEntity.getZ() - this.boss.getZ()));
                            livingEntity.damage(this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(20f));
                        }
                    }
                    this.boss.getWorld().playSound(null, this.targetPos, SoundRegistry.KNIGHT_SWIPE_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                }
                if (this.attackStatus == 13 && target.getBlockPos() != null) {
                    this.targetPos = target.getBlockPos();
                    this.boss.setObliterateTarget(this.targetPos);
                }
                if (this.attackStatus == 21 && this.targetPos != null) {
                    entities = this.boss.getWorld().getOtherEntities(this.boss, new Box(this.targetPos).expand(3D));
                    for (Entity entity : entities) {
                        if (entity instanceof LivingEntity livingEntity) {
                            livingEntity.addVelocity(0, 1, 0);
                            livingEntity.damage(this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(25f));
                        }
                    }
                    this.boss.getWorld().playSound(null, this.targetPos, SoundRegistry.NIGHTFALL_BONK_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                    if (!this.boss.getWorld().isClient) {
                        ParticleHandler.particleOutburstMap(this.boss.getWorld(), 300, this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), ParticleEvents.OBLITERATE_MAP, 1f);
                    }
                }
                if (this.attackStatus >= 36) { //38
                    this.boss.setMaceOfSpades(false);
                    this.resetAttackCooldown(1);
                    this.cordsRegistered = false;
                    this.attackStatus = 0;
                    this.boss.getNavigation().stop();
                    this.randomAttack = this.boss.getRandom().nextInt(this.numberOfAttacks);
                    this.obliterateHitEntities.clear();
                    this.obliterateImpactDone = false;
                }
            }

            //Obliterate
            if (this.attackCooldown < 0 && !this.cordsRegistered && distanceToEntity < 100D && this.randomAttack == 0 && target.getBlockPos() != null) { //75 range
                this.targetPos = target.getBlockPos();
                this.boss.setObliterateTarget(this.targetPos);
                this.boss.setAttackStartWorldTime(this.boss.getWorld().getTime());
                this.boss.setObliterate(true);

                this.obliterateHitEntities.clear();
                this.obliterateImpactDone = false;

                this.cordsRegistered = true;
            }
            if (this.boss.getObliterate() && this.targetPos != null) {  //46,6 ticks
                this.attackStatus++;
                ReturningKnightHitboxes.updateObliterateMaceHitbox(this.obliterateMaceHitbox, this.boss, this.targetPos, this.attackStatus);
                this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
                this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
                if (ReturningKnightHitboxes.isObliterateDamageTick(this.attackStatus)) {
                    List<LivingEntity> entities = this.obliterateMaceHitbox.getIntersectingTargets(this.boss.getWorld(), this.boss);
                    for (LivingEntity living : entities) {
                        if (!this.obliterateHitEntities.add(living.getUuid())) {
                            continue;
                        }
                        living.damage(DamageSourceRegistry.create(this.boss.getWorld(), DamageSourceRegistry.OBLITERATED, this.boss), this.getModifiedDamage(60f));
                        living.setVelocity(living.getVelocity().x, 1.0, living.getVelocity().z);
                        if (living.isDead() && this.isValidSpawn(living.getBlockPos())) {
                            this.summonAllies(living.getPos(), false);
                            if (this.boss.getHealth() <= this.boss.getMaxHealth() / 2.0F) {
                                this.summonAllies(living.getPos(), false);
                            }
                        }
                    }
                    if (!this.obliterateImpactDone && this.attackStatus >= 19) {
                        this.obliterateImpactDone = true;
                        Vec3d effectPos = BossHitboxHelper.findGroundImpactPos(this.boss.getWorld(), this.obliterateMaceHitbox.getCenter(), 2);
                        this.boss.getWorld().playSound(null, BlockPos.ofFloored(effectPos), SoundRegistry.NIGHTFALL_BONK_EVENT, SoundCategory.HOSTILE, 3f, 1f);
                        ParticleHandler.particleOutburstMap(this.boss.getWorld(), 300, effectPos.x, effectPos.y, effectPos.z, ParticleEvents.OBLITERATE_MAP, 1f);
                    }
                }
                if (ReturningKnightHitboxes.isObliterateDamageTick(this.attackStatus)) {
                    BossHitboxHelper.breakBlocksInsideHitbox(this.obliterateMaceHitbox.copy().offsetWorld(0, ReturningKnightHitboxes.OBLITERATE_MACE_SIZE.y / 2.0D, 0), this.boss.getWorld(), this.boss, this.boss::canDestroy, false);
                }
                if (this.attackStatus >= 32) {
                    this.boss.setObliterate(false);
                    this.resetAttackCooldown(1);
                    this.cordsRegistered = false;
                    this.attackStatus = 0;
                    this.boss.getNavigation().stop();
                    this.randomAttack = this.boss.getRandom().nextInt(this.numberOfAttacks);
                    this.boss.setAttackStartWorldTime(-1L);
                    this.obliterateHitEntities.clear();
                    this.obliterateImpactDone = false;
                }
            }

            //Blinding Light
            if (this.attackCooldown < 0 && distanceToEntity < 25D && this.randomAttack == 1) {
                this.boss.setBlind(true);
            }
            if (this.boss.getBlind()) { //22,6 ticks
                this.attackStatus++;
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
                double x = target.getX() - this.boss.getX();
                double z = target.getZ() - this.boss.getZ();
                if (attackStatus == 12 && distanceToEntity < 25f) {
                    target.damage(this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(10f));
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 0));
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0));
                    target.takeKnockback(2f, -x, -z);
                    this.boss.getWorld().playSound(null, target.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_FALL, SoundCategory.HOSTILE, 1f, 1f);
                    if (!this.boss.getWorld().isClient) {
                        ParticleHandler.particleOutburstMap(this.boss.getWorld(), 150, target.getX(), target.getEyeY(), target.getZ(), ParticleEvents.BLINDING_LIGHT_MAP, 1f);
                    }
                }
                if (this.attackStatus >= 19) {
                    this.boss.setBlind(false);
                    this.resetAttackCooldown(0);
                    this.attackStatus = 0;
                    this.randomAttack = this.boss.getRandom().nextInt(this.numberOfAttacks);
                }
            }

            //Eruption
            if (this.attackCooldown < 0 && this.specialCooldown < 0 && distanceToEntity < 300D && this.randomAttack == 2) {
                this.boss.setRupture(true);
            }
            if (this.boss.getRupture()) { //101,6 ticks
                this.attackStatus++;
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
                Box aoe = new Box(this.boss.getX() - 18, this.boss.getY() - 8, this.boss.getZ() - 18, this.boss.getX() + 18, this.boss.getY() + 8, this.boss.getZ() + 18);
                List<Entity> entities = this.boss.getWorld().getOtherEntities(this.boss, aoe);
                if (attackStatus == 21 || attackStatus == 33) {
                    for (Entity entity : this.boss.getWorld().getOtherEntities(this.boss, this.boss.getBoundingBox().expand(12))) {
                        this.boss.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.HOSTILE, 1f, 1f);
                    }
                }
                if (this.attackStatus == 52) {
                    for (Entity entity : entities) {
                        if (entity instanceof LivingEntity) {
                            entity.damage(this.boss.getWorld().getDamageSources().mobAttack(this.boss), this.getModifiedDamage(30f));
                            entity.setVelocity(entity.getVelocity().x, 1.5f, entity.getVelocity().z);
                            this.boss.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.HOSTILE, 1f, 1f);
                            if (!this.boss.getWorld().isClient) {
                                ParticleHandler.particleOutburstMap(this.boss.getWorld(), 300, entity.getX(), entity.getY(), entity.getZ(), ParticleEvents.GROUND_RUPTURE_MAP, 1f);
                            }
                        }
                    }
                }
                if (this.attackStatus >= 70) {
                    this.boss.setRupture(false);
                    this.resetAttackCooldown(0);
                    this.resetSpecialCooldown(1); //300
                    this.attackStatus = 0;
                    this.randomAttack = this.boss.getRandom().nextInt(this.numberOfAttacks);
                }
            }

            if (this.specialCooldown > 60 && this.randomAttack == 2) {
                this.randomAttack = this.boss.getRandom().nextInt(this.numberOfAttacks);
            }
            if (this.specialCooldown > 100 && this.randomAttack == 3) {
                this.randomAttack = this.boss.getRandom().nextInt(this.numberOfAttacks);
            }
            if (this.randomAttack == 4) {
                this.randomAttack = 0;
            }
            if (((this.randomAttack == 1 && !this.boss.getBlind()) || (this.randomAttack == 5 && !this.boss.getMaceOfSpades()) || (this.randomAttack == 0 && !this.boss.getObliterate())) && this.attackCooldown < -40) {
                this.randomAttack = this.boss.getRandom().nextInt(this.numberOfAttacks);
            }

            if (this.targetNotVisibleTicks < 5) {
                this.boss.getMoveControl().moveTo(target.getX(), target.getY(), target.getZ(), 1.0D);
            }
            super.tick();
        }
    }

    private void summonAllies(Vec3d pos, boolean healer) {
        MobEntity entity = healer ? new DarkSorcerer(EntityRegistry.DARK_SORCERER, this.boss.getWorld()) : new Remnant(EntityRegistry.REMNANT, this.boss.getWorld());
        entity.setPosition(pos);
        this.boss.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        this.boss.getWorld().spawnEntity(entity);
        if (healer) this.boss.addHealer(entity.getUuid());
        if (!this.boss.getWorld().isClient) {
            ParticleHandler.particleOutburstMap(this.boss.getWorld(), 100, pos.getX(), pos.getY(), pos.getZ(), ParticleEvents.SOUL_RUPTURE_MAP, 1f);
        }
    }

    private boolean isValidSpawn(BlockPos pos) {
        return this.boss.getWorld().getBlockState(pos).isAir() && !this.boss.getWorld().getBlockState(pos.down()).isAir();
    }
}
