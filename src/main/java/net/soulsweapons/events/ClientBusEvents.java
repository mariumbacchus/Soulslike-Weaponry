package net.soulsweapons.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.soulsweapons.SoulsWeaponry;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientBusEvents {
}
