package net.soulsweaponry.entity.ai.goal.attacks;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.ReturningKnightGoal;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.entity.util.RandomSummonPos;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;

public class ChildrenOfTheGrave extends ReturningKnightAttack {

    private final int enemyBound = (int) EntityConfig.returning_knight_children_of_the_grave_extra_enemy_bound;
    private final int minEnemyCount = (int) EntityConfig.returning_knight_children_of_the_grave_min_enemy_count;
    private final int healerBound = (int) EntityConfig.returning_knight_children_of_the_grave_extra_healer_bound;
    private final int minHealerCount = (int) EntityConfig.returning_knight_children_of_the_grave_min_healer_count;

    public ChildrenOfTheGrave(ReturningKnightGoal goal, ReturningKnight boss, int attackLength, int weight, int cooldown, int specialCooldown) {
        super(goal, boss, attackLength, weight, cooldown, specialCooldown);
    }

    @Override
    public void tickAttack(LivingEntity target, int attackStatus, double distanceToTarget) {
        this.getBoss().addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 20));
        if (attackStatus == 59) {
            int enemyNumber = this.getBoss().getRandom().nextInt(this.enemyBound) + this.minEnemyCount;
            int healerNumber = this.getBoss().getRandom().nextInt(this.healerBound) + this.minHealerCount;
            if (this.getBoss().getHealth() <= this.getBoss().getMaxHealth() / 2.0F) {
                enemyNumber += healerNumber;
                healerNumber = 0;
            }
            RandomSummonPos remnants = new RandomSummonPos(this.getWorld(), this.getBoss().getRandom(), enemyNumber, 10, this.getBoss().getBlockPos(), 10, 8, 5, (pos) -> this.getGoal().summonAllies(pos, false));
            RandomSummonPos healers = new RandomSummonPos(this.getWorld(), this.getBoss().getRandom(), healerNumber, 10, this.getBoss().getBlockPos(), 10, 8, 5, (pos) -> this.getGoal().summonAllies(pos, true));
            remnants.applySummonSpawns();
            healers.applySummonSpawns();
            if (!this.getWorld().isClient) {
                ParticleHandler.particleOutburstMap(this.getWorld(), 300, target.getX(), target.getY(), target.getZ(), ParticleEvents.GROUND_RUPTURE_MAP, 1f);
            }
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 1));
            this.playSound(target.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, 0.7f);
        }
    }

    @Override
    public boolean canTrigger(LivingEntity target, double distanceToTarget) {
        return !this.getBoss().hasHealersAlive();
    }
}
