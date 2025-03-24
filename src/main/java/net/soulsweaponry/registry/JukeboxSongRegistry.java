package net.soulsweaponry.registry;

import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.soulsweaponry.SoulsWeaponry;

public class JukeboxSongRegistry {

	public static RegistryKey<JukeboxSong> BIG_CHUNGUS_SONG = registerJukeboxSong("big_chungus_song");

	public static RegistryKey<JukeboxSong> registerJukeboxSong(String path) {
		return RegistryKey.of(RegistryKeys.JUKEBOX_SONG, SoulsWeaponry.id(path));
	}

	// TODO: datagen
}
