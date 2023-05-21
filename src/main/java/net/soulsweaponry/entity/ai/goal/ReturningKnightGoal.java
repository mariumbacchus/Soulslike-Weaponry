package net.soulsweaponry.entity.ai.goal;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.DarkSorcerer;
import net.soulsweaponry.entity.mobs.EvilRemnant;
import net.soulsweaponry.entity.mobs.ReturningKnight;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.ParticleNetworking;

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
    int randomAttack = 3;
    private BlockPos pos;
    private final int numberOfAttacks = 6; // 4 = 0 = obliterate, 5 = mace of spades

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
        this.attackCooldown = (int) Math.floor(ConfigConstructor.returning_knight_attack_cooldown_ticks * cooldownModifier) - this.boss.getReducedCooldownAttackers()*2;
    }

    public void resetSpecialCooldown(float cooldownModifier) {
        this.specialCooldown = (int) Math.floor(ConfigConstructor.returning_knight_special_cooldown_ticks * cooldownModifier) - this.boss.getReducedCooldownAttackers()*2;
    }

    public float getModifiedDamage(float damage) {
        return damage * ConfigConstructor.returning_knight_damage_modifier;
    }

    public boolean canSummon() {
        if (!this.boss.world.getBlockState(this.pos).isAir()) {
            this.pos = new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
            this.canSummon();
        }
        return true;
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
    }

    public void tick() {
        attackCooldown--;
        unbreakableTimer--;
        specialCooldown--;
        LivingEntity target = this.boss.getTarget();

        if (target != null && !this.boss.getSpawning()) {
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
                this.boss.world.playSound(null, this.boss.getBlockPos(), SoundRegistry.NIGHTFALL_SHIELD_EVENT, SoundCategory.HOSTILE, .75f, 1f);
            }
            if (this.unbreakableTimer < 0) {
                this.boss.setUnbreakable(false);
                this.unbreakableTimer = -5;
            }

            //Children of the grave (Summoning)
            if (this.attackCooldown < 0 && this.specialCooldown < 0 && this.randomAttack == 3) {
                this.boss.setSummon(true);
            }
            if (this.boss.getSummon()) {
                this.attackStatus++;
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));

                if (this.attackStatus == 30) { //58,4 ticks
                    int enemyNumber = this.boss.getRandom().nextInt(5 - 2) + 2;
                    int healerNumber = this.boss.getRandom().nextInt(3 - 1) + 1;
                    for (int j = 0; j < enemyNumber; j++) {
                        EvilRemnant entity = new EvilRemnant(EntityRegistry.REMNANT, this.boss.world);    
                        this.pos = new BlockPos(this.boss.getBlockX() + this.boss.getRandom().nextInt(32) - 16, this.boss.getBlockY() - 3, this.boss.getBlockZ() + this.boss.getRandom().nextInt(32) - 16);
                        if (this.canSummon()) entity.setPos(this.pos.getX(), this.pos.getY(), this.pos.getZ());
                        //entity.setOwner(null);
                        this.boss.world.playSound(null, entity.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                        this.boss.world.spawnEntity(entity);
                        if (!this.boss.world.isClient) {
                            ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.SOUL_RUPTURE_PACKET_ID, this.pos, 100);
                        }
                    }
                    for (int j = 0; j < healerNumber; j++) {
                        DarkSorcerer entity = new DarkSorcerer(EntityRegistry.DARK_SORCERER, this.boss.world);  
                        this.pos = new BlockPos(this.boss.getBlockX() + this.boss.getRandom().nextInt(32) - 16, this.boss.getBlockY() - 3, this.boss.getBlockZ() + this.boss.getRandom().nextInt(32) - 16);
                        if (this.canSummon()) entity.setPos(this.pos.getX(), this.pos.getY() + .2f, this.pos.getZ());    
                        //entity.setOwner(null);
                        this.boss.world.playSound(null, entity.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                        this.boss.world.spawnEntity(entity);
                        if (!this.boss.world.isClient) {
                            ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.SOUL_RUPTURE_PACKET_ID, this.pos, 100);
                        }
                    }
                    if (!this.boss.world.isClient) {
                        ParticleNetworking.specificServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.GROUND_RUPTURE_ID, target.getBlockPos(), target.getX(), (float) target.getZ());
                    }
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 1));
                    this.boss.world.playSound(null, target.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 0.7f, 1f);
                }
                if (this.attackStatus >= 48) { //96,6 ticks
                    this.boss.setSummon(false);
                    this.resetAttackCooldown(1);
                    this.resetSpecialCooldown(1.5f);
                    this.attackStatus = 0;
                    this.randomAttack = this.boss.getRandom().nextInt(this.numberOfAttacks);
                }
            }
            
            //Mace of Spades
            if (this.attackCooldown < 0 && !this.cordsRegistered && distanceToEntity < 50D && this.randomAttack == 5 && target.getBlockPos() != null) {
                this.targetPos = target.getBlockPos();
                this.boss.setMaceOfSpades(true);
                this.cordsRegistered = true;
            }
            if (this.boss.getMaceOfSpades() && this.targetPos != null) {
                this.attackStatus++;
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
                this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
                this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
                
                Box aoe = new Box(targetPos.getX() - 5, targetPos.getY() - 2, targetPos.getZ() - 5, targetPos.getX() + 5, targetPos.getY() + 2, targetPos.getZ() + 5);
                List<Entity> entities = this.boss.world.getOtherEntities(this.boss, aoe);
                if (this.attackStatus == 7) {
                    for (Entity entity : entities) {
                        if (entity instanceof LivingEntity livingEntity) {
                            livingEntity.takeKnockback(2f, -(livingEntity.getX() - this.boss.getX()), -(livingEntity.getZ() - this.boss.getZ()));
                            livingEntity.damage(this.boss.world.getDamageSources().mobAttack(this.boss), this.getModifiedDamage(20f));
                        }
                    }
                    this.boss.world.playSound(null, this.targetPos, SoundRegistry.KNIGHT_SWIPE_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                }
                if (this.attackStatus == 13 && target.getBlockPos() != null) {
                    this.targetPos = target.getBlockPos();
                }
                if (this.attackStatus == 21 && this.targetPos != null) {
                    entities = this.boss.world.getOtherEntities(this.boss, new Box(this.targetPos).expand(3D));
                    for (Entity entity : entities) {
                        if (entity instanceof LivingEntity livingEntity) {
                            livingEntity.addVelocity(0, 1, 0);
                            livingEntity.damage(this.boss.world.getDamageSources().mobAttack(this.boss), this.getModifiedDamage(25f));
                        }
                    }
                    this.boss.world.playSound(null, this.targetPos, SoundRegistry.NIGHTFALL_BONK_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                    if (!this.boss.world.isClient) {
                        ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.OBLITERATE_ID, this.targetPos, 200);
                    }
                }
                if (this.attackStatus >= 36) { //38
                    this.boss.setMaceOfSpades(false);
                    this.resetAttackCooldown(1);
                    this.cordsRegistered = false;
                    this.attackStatus = 0;
                    this.boss.getNavigation().stop();
                    this.randomAttack = this.boss.getRandom().nextInt(this.numberOfAttacks);
                }
            }

            //Obliterate
            if (this.attackCooldown < 0 && !this.cordsRegistered && distanceToEntity < 75D && this.randomAttack == 0 && target.getBlockPos() != null) {
                this.targetPos = target.getBlockPos();
                this.boss.setObliterate(true);
                this.cordsRegistered = true;
            }
            if (this.boss.getObliterate() && this.targetPos != null) {  //46,6 ticks
                this.attackStatus++;
                this.boss.getLookControl().lookAt(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ());
                this.boss.getNavigation().startMovingTo(this.targetPos.getX(), this.targetPos.getY(), this.targetPos.getZ(), 0.0D);
                this.boss.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
                Box aoe = new Box(targetPos).expand(3D);
                List<Entity> entities = this.boss.world.getOtherEntities(this.boss, aoe);
                
                if (this.attackStatus == 18) { //23
                    for (Entity entity : entities) {
                        if (entity instanceof LivingEntity) {
                            entity.damage(CustomDamageSource.create(this.boss.world, CustomDamageSource.OBLITERATED, this.boss), this.getModifiedDamage(60f));
                            entity.setVelocity(entity.getVelocity().x, 1, entity.getVelocity().z);
                        }
                    }
                    this.boss.world.playSound(null, this.targetPos, SoundRegistry.NIGHTFALL_BONK_EVENT, SoundCategory.HOSTILE, 1f, 1f);
                    if (!this.boss.world.isClient) {
                        ParticleNetworking.sendServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.OBLITERATE_ID, this.targetPos, 200);
                    }
                }
                if (this.attackStatus >= 32) { //38
                    this.boss.setObliterate(false);
                    this.resetAttackCooldown(1);
                    this.cordsRegistered = false;
                    this.attackStatus = 0;
                    this.boss.getNavigation().stop();
                    this.randomAttack = this.boss.getRandom().nextInt(this.numberOfAttacks);
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
                    target.damage(this.boss.world.getDamageSources().mobAttack(this.boss), this.getModifiedDamage(10f));
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 0));
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 0));
                    target.takeKnockback(2f, -x, -z);
                    this.boss.world.playSound(null, target.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_FALL, SoundCategory.HOSTILE, 1f, 1f);
                    if (!this.boss.world.isClient) {
                        ParticleNetworking.specificServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.BLINDING_LIGHT_ID, target.getBlockPos(), target.getEyeY());
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
                List<Entity> entities = this.boss.world.getOtherEntities(this.boss, aoe);
                if (attackStatus == 21 || attackStatus == 33) {
                    for (Entity entity : this.boss.world.getOtherEntities(this.boss, this.boss.getBoundingBox().expand(12))) {
                        this.boss.world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.HOSTILE, 1f, 1f);
                    }
                }
                if (this.attackStatus == 52) {
                    for (Entity entity : entities) {
                        if (entity instanceof LivingEntity) {
                            entity.damage(this.boss.world.getDamageSources().mobAttack(this.boss), this.getModifiedDamage(30f));
                            entity.setVelocity(entity.getVelocity().x, 1.5f, entity.getVelocity().z);
                            this.boss.world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1f, 1f);
                            if (!this.boss.world.isClient) {
                                ParticleNetworking.specificServerParticlePacket((ServerWorld) this.boss.world, PacketRegistry.GROUND_RUPTURE_ID, entity.getBlockPos(), entity.getX(), (float) entity.getZ());
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
}
