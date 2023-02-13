package net.soulsweaponry.items;

import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;

public class ModMusicDiscs extends MusicDiscItem {

    public ModMusicDiscs(int comparatorOutput, SoundEvent sound, Settings settings, int lengthInSeconds) {
        super(comparatorOutput, sound, settings, lengthInSeconds);
    }
    
}
