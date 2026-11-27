package net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase2;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.soulsweaponry.config.ReturningKnightConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.ReturningKnightAttack;
import net.soulsweaponry.entity.mobs.EliteRemnant;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.entity.util.RandomSummonPos;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;

public class EruptionSummon extends ReturningKnightAttack {

    private final float summonRadius = ReturningKnightConfig.children_of_the_grave_eruption_phase_2_summon_radius;
    private final float summonAmount = ReturningKnightConfig.children_of_the_grave_eruption_phase_2_summon_amount;
    private final int eruptionSpawnRadius = (int) ReturningKnightConfig.children_of_the_grave_eruption_phase_2_eruption_spawn_radius;
    private final float eruptionDamage = ReturningKnightConfig.children_of_the_grave_eruption_phase_2_eruption_damage;
    private final int eruptionAmount = (int) ReturningKnightConfig.children_of_the_grave_eruption_phase_2_eruption_amount;

    public EruptionSummon(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus) {
        this.getBoss().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (this.isTick(attackStatus, 42) || this.isTick(attackStatus, 66)) {
            for (Entity entity : this.getWorld().getEntitiesByClass(PlayerEntity.class, this.getBoss().getBoundingBox().expand(20), p -> true)) {
                this.playSound(entity.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, 1f);
            }
        }
        if (this.isTick(attackStatus, 102)) {
            RandomSummonPos remnants = new RandomSummonPos(this.getWorld(), this.getBoss().getRandom(), (int) this.summonAmount, 10,
                    this.getBoss().getBlockPos(), (int) this.summonRadius, 8, 5,
                    (pos) -> {
                        this.summonAllies(pos, new EliteRemnant(EntityRegistry.ELITE_REMNANT, this.getWorld()), true);
                        // This was a bit brutal so maybe just have random eruptions around instead
                        // this.summonDarkPillarsCircle(target, (int) this.pillarRippleAmount, this.pillarRadius, this.pillarDamage, 1, new Vec2f(1.5f, 1.75f), pos);
                    });
            RandomSummonPos eruptions = new RandomSummonPos(this.getWorld(), this.getBoss().getRandom(), this.eruptionAmount, 5,
                    this.getBoss().getBlockPos(), this.eruptionSpawnRadius, 8, 5,
                    (pos) -> {
                        this.spawnEruption(pos.add(0, -0.5f, 0), 0, this.eruptionDamage, 22);
                    });
            remnants.applySummonSpawns();
            eruptions.applySummonSpawns();
            this.playSound(target.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, 1f);
        }
    }

    @Override
    public boolean canTrigger(LivingEntity target) {
        return this.getBoss().canSummonAgain() && !this.getBoss().hasSpecialSummonsAlive() && this.isWithinDistance(target, 15);
    }

    @Override
    public boolean isAvailable() {
        return this.getBoss().isPhaseTwo();
    }
}
