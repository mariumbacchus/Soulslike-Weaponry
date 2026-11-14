package net.soulsweaponry.entity.ai.goal.events;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.soulsweaponry.entity.ai.goal.hitboxes.BossHitboxHelper;
import net.soulsweaponry.entity.mobs.boss.BossEntity;
import org.jetbrains.annotations.Nullable;

public abstract class BossEvent<S extends Enum<S>, B extends BossEntity<S>> {

    private final B boss;
    private final int eventLength;
    private boolean completed;

    public BossEvent(B boss, int eventLength) {
        this.boss = boss;
        this.eventLength = eventLength;
    }

    /**
     * Determines whether this event should start.
     * Use getBoss().getTarget() if this particular event needs a target.
     */
    public abstract boolean canTrigger();

    /**
     * Called once when the event starts, after its animation state is set.
     */
    public void onStart() {}

    /**
     * Called every server tick while active, independently of combat goals.
     * eventTick starts at 1 and is measured in actual server ticks.
     */
    public abstract void tickEvent(int eventTick);

    /**
     * Called after the event finishes normally.
     */
    public void onFinish() {}

    /**
     * Called when interrupted by death, a phase transition, or another state.
     * Cancellation does not mark the event completed.
     */
    public void onCancel() {}

    public boolean isTick(int eventTick, int animationTick) {
        return BossHitboxHelper.didScaledTickPass(eventTick, this.boss.getAnimationSpeed(), animationTick);
    }

    public boolean isAnyTick(int eventTick, int... animationTicks) {
        return BossHitboxHelper.didAnyScaledTickPass(eventTick, this.boss.getAnimationSpeed(), animationTicks);
    }

    public void playSound(@Nullable BlockPos pos, SoundEvent sound, float volume, float pitch) {
        this.getWorld().playSound(null, pos == null ? this.boss.getBlockPos() : pos, sound, SoundCategory.HOSTILE, volume, pitch);
    }

    public void playSound(BlockPos pos, SoundEvent sound, float volume) {
        this.playSound(pos, sound, volume, 1F);
    }

    public void playSound(SoundEvent sound, float volume, float pitch) {
        this.playSound(null, sound, volume, pitch);
    }

    public void playSound(SoundEvent sound, float volume) {
        this.playSound(null, sound, volume, 1F);
    }

    public void playSound(SoundEvent sound, float volume, float minPitch, float maxPitch) {
        float pitch = this.boss.getRandom().nextBetween((int) (minPitch * 100), (int) (maxPitch * 100)) * 0.01F;
        this.playSound(null, sound, volume, pitch);
    }

    public boolean hasCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getEventLength() {
        return this.eventLength;
    }

    public B getBoss() {
        return this.boss;
    }

    public World getWorld() {
        return this.boss.getWorld();
    }
}