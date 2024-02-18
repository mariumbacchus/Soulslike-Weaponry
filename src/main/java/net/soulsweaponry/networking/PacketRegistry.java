package net.soulsweaponry.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.soulsweaponry.networking.C2S.*;
import net.soulsweaponry.networking.S2C.*;

public class PacketRegistry {

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(PacketIds.MOONLIGHT, MoonlightC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(PacketIds.RETURN_FREYR_SWORD, ReturnFreyrSwordC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(PacketIds.STATIONARY_FREYR_SWORD, StationaryFreyrSwordC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(PacketIds.COLLECT_SUMMONS, CollectSummonsC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(PacketIds.SWITCH_TRICK_WEAPON, SwitchTrickWeaponC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(PacketIds.KEYBIND_ABILITY, KeybindAbilityC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(PacketIds.PARRY, ParryC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(PacketIds.POSTURE, PostureC2S::receive);
        ServerPlayNetworking.registerGlobalReceiver(PacketIds.DAMAGING_BOX, DamagingBox::receive);
    }

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(PacketIds.SPHERE_PARTICLES, ParticleSphereS2C::receive);
        ClientPlayNetworking.registerGlobalReceiver(PacketIds.OUTBURST_PARTICLES, ParticleOutburstS2C::receive);
        ClientPlayNetworking.registerGlobalReceiver(PacketIds.FLASH_PARTICLE, FlashParticleS2C::receive);
        ClientPlayNetworking.registerGlobalReceiver(PacketIds.SINGLE_PARTICLE, SingleParticleS2C::receive);
        ClientPlayNetworking.registerGlobalReceiver(PacketIds.PARRY, ParrySyncS2C::receive);
        ClientPlayNetworking.registerGlobalReceiver(PacketIds.POSTURE, PostureSyncS2C::receive);
    }

    public static void sendToAllPlayersS2C(ServerWorld world, BlockPos pos, Identifier packetId, PacketByteBuf buf) {
        for (ServerPlayerEntity player : PlayerLookup.tracking(world, pos)) {
            ServerPlayNetworking.send(player, packetId, buf);
        }
    }
}
