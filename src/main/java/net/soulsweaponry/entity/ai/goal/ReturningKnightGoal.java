package net.soulsweaponry.entity.ai.goal;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.config.EntityConfig;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.*;
import net.soulsweaponry.entity.ai.goal.events.Unbreakable;
import net.soulsweaponry.entity.mobs.DarkSorcerer;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.UUID;

public class ReturningKnightGoal extends BossGoal<ReturningKnight.States, ReturningKnight, ReturningKnightGoal> {

    public ReturningKnightGoal(ReturningKnight boss, double speed, boolean pauseWhenMobIdle) {
        super(boss, speed, pauseWhenMobIdle);
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));

        ChildrenOfTheGrave childrenOfTheGrave = new ChildrenOfTheGrave(this, this.boss, 96,
                (int) EntityConfig.returning_knight_children_of_the_grave_weight,
                (int) EntityConfig.returning_knight_children_of_the_grave_cooldown,
                (int) EntityConfig.returning_knight_children_of_the_grave_special_cooldown
        );
        Obliterate obliterate = new Obliterate(this, this.boss, 64,
                (int) EntityConfig.returning_knight_obliterate_weight,
                (int) EntityConfig.returning_knight_obliterate_cooldown,
                (int) EntityConfig.returning_knight_obliterate_special_cooldown
        );
        BlindingLight blindingLight = new BlindingLight(this, this.boss, 38,
                (int) EntityConfig.returning_knight_blinding_light_weight,
                (int) EntityConfig.returning_knight_blinding_light_cooldown,
                (int) EntityConfig.returning_knight_blinding_light_special_cooldown
        );
        Eruption eruption = new Eruption(this, this.boss, 140,
                (int) EntityConfig.returning_knight_eruption_weight,
                (int) EntityConfig.returning_knight_eruption_cooldown,
                (int) EntityConfig.returning_knight_eruption_special_cooldown
        );
        MaceOfSpades1 maceOfSpades1 = new MaceOfSpades1(this, this.boss, 72,
                (int) EntityConfig.returning_knight_mace_of_spades_1_weight,
                (int) EntityConfig.returning_knight_mace_of_spades_1_cooldown,
                (int) EntityConfig.returning_knight_mace_of_spades_1_special_cooldown
        );
        MaceOfSpades2 maceOfSpades2 = new MaceOfSpades2(this, this.boss, 95,
                (int) EntityConfig.returning_knight_mace_of_spades_2_weight,
                (int) EntityConfig.returning_knight_mace_of_spades_2_cooldown,
                (int) EntityConfig.returning_knight_mace_of_spades_2_special_cooldown
        );
        MaceOfSpades3 maceOfSpades3 = new MaceOfSpades3(this, this.boss, 100,
                (int) EntityConfig.returning_knight_mace_of_spades_3_weight,
                (int) EntityConfig.returning_knight_mace_of_spades_3_cooldown,
                (int) EntityConfig.returning_knight_mace_of_spades_3_special_cooldown
        );
        MaceOfSpades4Spin maceOfSpades4 = new MaceOfSpades4Spin(this, this.boss, 103,
                (int) EntityConfig.returning_knight_mace_of_spades_4_weight,
                (int) EntityConfig.returning_knight_mace_of_spades_4_cooldown,
                (int) EntityConfig.returning_knight_mace_of_spades_4_special_cooldown
        );
        SeismicWave seismicWave = new SeismicWave(this, this.boss, 112,
                (int) EntityConfig.returning_knight_seismic_wave_weight,
                (int) EntityConfig.returning_knight_seismic_wave_cooldown,
                (int) EntityConfig.returning_knight_seismic_wave_special_cooldown
        );

        this.addAttack(ReturningKnight.States.SUMMON, childrenOfTheGrave);
        this.addAttack(ReturningKnight.States.OBLITERATE, obliterate);
        this.addAttack(ReturningKnight.States.BLIND, blindingLight);
        this.addAttack(ReturningKnight.States.RUPTURE, eruption);
        this.addAttack(ReturningKnight.States.MACE_OF_SPADES_1, maceOfSpades1);
        this.addAttack(ReturningKnight.States.MACE_OF_SPADES_2, maceOfSpades2);
        this.addAttack(ReturningKnight.States.MACE_OF_SPADES_3, maceOfSpades3);
        this.addAttack(ReturningKnight.States.MACE_OF_SPADES_4_SPIN, maceOfSpades4);
        this.addAttack(ReturningKnight.States.SEISMIC_WAVE, seismicWave);

        Unbreakable unbreakable = new Unbreakable(this, this.boss, 76);
        this.addEvent(ReturningKnight.States.UNBREAKABLE, unbreakable);
    }

    @Override
    public int getModifiedCooldown(int cooldown) {
        return (int) (cooldown * EntityConfig.returning_knight_attack_cooldown_modifier
                - this.boss.getReducedCooldownAttackers() * EntityConfig.returning_knight_attack_cooldown_reduction_modifier_per_nearby_foe);
    }

    @Override
    public int getModifiedSpecialCooldown(int specialCooldown) {
        return (int) (specialCooldown * EntityConfig.returning_knight_special_cooldown_modifier
                - this.boss.getReducedCooldownAttackers() * EntityConfig.returning_knight_special_cooldown_reduction_modifier_per_nearby_foe);
    }

    public float getModifiedDamage(float damage) {
        return damage * EntityConfig.returning_knight_damage_modifier;
    }

    @Override
    public @Nullable ReturningKnight.States getDebugState() {
        return null;//ReturningKnight.States.SEISMIC_WAVE;
    }

    public UUID summonAllies(Vec3d pos, boolean healer) {
        MobEntity entity = healer ? new DarkSorcerer(EntityRegistry.DARK_SORCERER, this.getWorld()) : new Remnant(EntityRegistry.REMNANT, this.getWorld());
        entity.setPosition(pos);
        this.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.HOSTILE, 1f, 1f);
        this.getWorld().spawnEntity(entity);
        if (healer) {
            this.getBoss().addHealer(entity.getUuid());
        }
        if (!this.getWorld().isClient) {
            ParticleHandler.particleOutburstMap(this.getWorld(), 100, pos.getX(), pos.getY(), pos.getZ(), ParticleEvents.SOUL_RUPTURE_MAP, 1f);
        }
        return entity.getUuid();
    }

    public boolean isValidSpawn(BlockPos pos) {
        return this.boss.getWorld().getBlockState(pos).isAir() && !this.boss.getWorld().getBlockState(pos.down()).isAir();
    }
}
