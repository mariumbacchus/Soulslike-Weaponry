package net.soulsweaponry.items;

import net.minecraft.item.BowItem;

public abstract class ModdedBow extends BowItem {

    public ModdedBow(Settings settings) {
        super(settings);
    }

    public abstract float getReducedPullTime();

    public float getModdedPullProgress(int useTicks) {
        float f = (float)useTicks / (20.0f - this.getReducedPullTime());
        if ((f = (f * f + f * 2.0f) / 3.0f) > 1.0f) {
            f = 1.0f;
        }
        return f;
    }
}
