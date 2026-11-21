package net.soulsweaponry.entity.ai.goal;

import net.soulsweaponry.config.ReturningKnightConfig;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase1.*;
import net.soulsweaponry.entity.ai.goal.attacks.returningknight.phase2.*;
import net.soulsweaponry.entity.mobs.boss.ReturningKnight;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ReturningKnightGoal extends BossGoal<ReturningKnight.States, ReturningKnight, ReturningKnightGoal> {

    public ReturningKnightGoal(ReturningKnight boss, double speed, boolean pauseWhenMobIdle) {
        super(boss, speed, pauseWhenMobIdle);
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));

        ChildrenOfTheGrave childrenOfTheGrave = new ChildrenOfTheGrave(this, this.boss, 96,
                (int) ReturningKnightConfig.children_of_the_grave_weight,
                (int) ReturningKnightConfig.children_of_the_grave_cooldown,
                (int) ReturningKnightConfig.children_of_the_grave_special_cooldown
        );
        Obliterate obliterate = new Obliterate(this, this.boss, 64,
                (int) ReturningKnightConfig.obliterate_weight,
                (int) ReturningKnightConfig.obliterate_cooldown,
                (int) ReturningKnightConfig.obliterate_special_cooldown
        );
        BlindingLight blindingLight = new BlindingLight(this, this.boss, 38,
                (int) ReturningKnightConfig.blinding_light_weight,
                (int) ReturningKnightConfig.blinding_light_cooldown,
                (int) ReturningKnightConfig.blinding_light_special_cooldown
        );
        Eruption eruption = new Eruption(this, this.boss, 140,
                (int) ReturningKnightConfig.eruption_weight,
                (int) ReturningKnightConfig.eruption_cooldown,
                (int) ReturningKnightConfig.eruption_special_cooldown
        );
        MaceOfSpades1 maceOfSpades1 = new MaceOfSpades1(this, this.boss, 72,
                (int) ReturningKnightConfig.mace_of_spades_1_weight,
                (int) ReturningKnightConfig.mace_of_spades_1_cooldown,
                (int) ReturningKnightConfig.mace_of_spades_1_special_cooldown
        );
        MaceOfSpades2 maceOfSpades2 = new MaceOfSpades2(this, this.boss, 95,
                (int) ReturningKnightConfig.mace_of_spades_2_weight,
                (int) ReturningKnightConfig.mace_of_spades_2_cooldown,
                (int) ReturningKnightConfig.mace_of_spades_2_special_cooldown
        );
        MaceOfSpades3 maceOfSpades3 = new MaceOfSpades3(this, this.boss, 100,
                (int) ReturningKnightConfig.mace_of_spades_3_weight,
                (int) ReturningKnightConfig.mace_of_spades_3_cooldown,
                (int) ReturningKnightConfig.mace_of_spades_3_special_cooldown
        );
        MaceOfSpades4Spin maceOfSpades4 = new MaceOfSpades4Spin(this, this.boss, 103,
                (int) ReturningKnightConfig.mace_of_spades_4_weight,
                (int) ReturningKnightConfig.mace_of_spades_4_cooldown,
                (int) ReturningKnightConfig.mace_of_spades_4_special_cooldown
        );
        SeismicWave seismicWave = new SeismicWave(this, this.boss, 112,
                (int) ReturningKnightConfig.seismic_wave_weight,
                (int) ReturningKnightConfig.seismic_wave_cooldown,
                (int) ReturningKnightConfig.seismic_wave_special_cooldown
        );

        ObliterateShadow obliterateShadow = new ObliterateShadow(this ,this.boss, 72,
                (int) ReturningKnightConfig.obliterate_phase_2_weight,
                (int) ReturningKnightConfig.obliterate_phase_2_cooldown,
                (int) ReturningKnightConfig.obliterate_phase_2_special_cooldown
        );
        ObliterateFakeout obliterateFakeout = new ObliterateFakeout(this ,this.boss, 125,
                (int) ReturningKnightConfig.obliterate_fakeout_phase_2_weight,
                (int) ReturningKnightConfig.obliterate_fakeout_phase_2_cooldown,
                (int) ReturningKnightConfig.obliterate_fakeout_phase_2_special_cooldown
        );
        MaceOfSpades1Shadow maceOfSpades1Shadow = new MaceOfSpades1Shadow(this ,this.boss, 81,
                (int) ReturningKnightConfig.mace_of_spades_1_phase_2_weight,
                (int) ReturningKnightConfig.mace_of_spades_1_phase_2_cooldown,
                (int) ReturningKnightConfig.mace_of_spades_1_phase_2_special_cooldown
        );
        MaceOfSpades2Shadow maceOfSpades2Shadow = new MaceOfSpades2Shadow(this ,this.boss, 105,
                (int) ReturningKnightConfig.mace_of_spades_2_phase_2_weight,
                (int) ReturningKnightConfig.mace_of_spades_2_phase_2_cooldown,
                (int) ReturningKnightConfig.mace_of_spades_2_phase_2_special_cooldown
        );
        MaceOfSpades3Shadow maceOfSpades3Shadow = new MaceOfSpades3Shadow(this ,this.boss, 116,
                (int) ReturningKnightConfig.mace_of_spades_3_phase_2_weight,
                (int) ReturningKnightConfig.mace_of_spades_3_phase_2_cooldown,
                (int) ReturningKnightConfig.mace_of_spades_3_phase_2_special_cooldown
        );
        EruptionSummon eruptionSummon = new EruptionSummon(this ,this.boss, 140,
                (int) ReturningKnightConfig.children_of_the_grave_eruption_phase_2_weight,
                (int) ReturningKnightConfig.children_of_the_grave_eruption_phase_2_cooldown,
                (int) ReturningKnightConfig.children_of_the_grave_eruption_phase_2_special_cooldown
        );
        DarkCircle darkCircle = new DarkCircle(this ,this.boss, 120,
                (int) ReturningKnightConfig.dark_circle_phase_2_weight,
                (int) ReturningKnightConfig.dark_circle_phase_2_cooldown,
                (int) ReturningKnightConfig.dark_circle_phase_2_special_cooldown
        );

        // Phase 1
        this.addAttack(ReturningKnight.States.SUMMON, childrenOfTheGrave);
        this.addAttack(ReturningKnight.States.OBLITERATE, obliterate);
        this.addAttack(ReturningKnight.States.BLIND, blindingLight);
        this.addAttack(ReturningKnight.States.RUPTURE, eruption);
        this.addAttack(ReturningKnight.States.MACE_OF_SPADES_1, maceOfSpades1);
        this.addAttack(ReturningKnight.States.MACE_OF_SPADES_2, maceOfSpades2);
        this.addAttack(ReturningKnight.States.MACE_OF_SPADES_3, maceOfSpades3);
        this.addAttack(ReturningKnight.States.MACE_OF_SPADES_4_SPIN, maceOfSpades4);
        this.addAttack(ReturningKnight.States.SEISMIC_WAVE, seismicWave);

        // Phase 2
        this.addAttack(ReturningKnight.States.OBLITERATE_PHASE_2, obliterateShadow);
        this.addAttack(ReturningKnight.States.OBLITERATE_FAKEOUT_PHASE_2, obliterateFakeout);
        this.addAttack(ReturningKnight.States.MACE_OF_SPADES_1_PHASE_2, maceOfSpades1Shadow);
        this.addAttack(ReturningKnight.States.MACE_OF_SPADES_2_PHASE_2, maceOfSpades2Shadow);
        this.addAttack(ReturningKnight.States.MACE_OF_SPADES_3_PHASE_2, maceOfSpades3Shadow);
        this.addAttack(ReturningKnight.States.SUMMON_RUPTURE_PHASE_2, eruptionSummon);
        this.addAttack(ReturningKnight.States.DARK_CIRCLE, darkCircle);
    }

    @Override
    public int getModifiedCooldown(int cooldown) {
        return (int) (cooldown * ReturningKnightConfig.attack_cooldown_modifier
                - this.boss.getAttackingPlayersAmount() * ReturningKnightConfig.attack_cooldown_reduction_per_attacking_player);
    }

    @Override
    public int getModifiedSpecialCooldown(int specialCooldown) {
        return (int) (specialCooldown * ReturningKnightConfig.special_cooldown_modifier
                - this.boss.getAttackingPlayersAmount() * ReturningKnightConfig.special_cooldown_reduction_per_attacking_player);
    }

    @Override
    public @Nullable ReturningKnight.States getFirstAggroAttack() {
        return ReturningKnight.States.SUMMON;
    }
}
