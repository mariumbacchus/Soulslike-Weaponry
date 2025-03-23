package net.soulsweaponry.registry;

import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public class JukeboxSongRegistry {

	public static RegistryKey<JukeboxSong> BIG_CHUNGUS_SONG = registerJukeboxSong("big_chungus_song");

	public static Identifier registerId(String name) {
		return Identifier.of(SoulsWeaponry.ModId, name);
	}

	public static RegistryKey<JukeboxSong> registerJukeboxSong(String id) {
		Identifier identifier = registerId(id);
		return RegistryKey.of(RegistryKeys.JUKEBOX_SONG, identifier);
	}

	// TODO: datagen
}
