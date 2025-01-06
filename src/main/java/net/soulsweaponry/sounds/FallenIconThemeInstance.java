package net.soulsweaponry.sounds;

import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.soulsweaponry.entity.mobs.Moonknight;
import net.soulsweaponry.registry.SoundRegistry;

public class FallenIconThemeInstance extends MovingSoundInstance {

    private final Moonknight entity;

    public FallenIconThemeInstance(Moonknight entity) {
        super(SoundRegistry.FALLEN_ICON_SONG, SoundCategory.MUSIC, SoundInstance.createRandom());
        this.entity = entity;
    }

    @Override
    public boolean canPlay() {
        return !this.entity.isPlayingMusic();
    }

    @Override
    public void tick() {
        if (this.entity.isRemoved()) {
            this.setDone();
        }
    }
}
