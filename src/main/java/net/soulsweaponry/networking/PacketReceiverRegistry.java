package net.soulsweaponry.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.soulsweaponry.networking.C2S.packets.*;
import net.soulsweaponry.networking.C2S.receivers.*;
import net.soulsweaponry.networking.S2C.packets.*;
import net.soulsweaponry.networking.S2C.receivers.*;

public class PacketReceiverRegistry {

    public static void registerC2SReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(MoonlightC2S.TYPE, MoonlightC2SReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(ReturnFreyrSwordC2S.TYPE, ReturnFreyrSwordC2SReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(StationaryFreyrSwordC2S.TYPE, StationaryFreyrSwordC2SReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(CollectSummonsC2S.TYPE, CollectSummonsC2SReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(SwitchTrickWeaponC2S.TYPE, SwitchTrickWeaponC2SReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(KeybindAbilityC2S.TYPE, KeybindAbilityC2SReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(ParryC2S.TYPE, ParryC2SReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(DamagingBoxC2S.TYPE, DamagingBoxC2SReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(ReturnThrownWeaponC2S.TYPE, ReturnThrownWeaponC2SReceiver::receive);
        ServerPlayNetworking.registerGlobalReceiver(AttackClickC2S.TYPE, AttackClickC2SReceiver::receive);

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ServerPlayNetworking.registerGlobalReceiver(KillNearbyEntitiesC2S.TYPE, KillNearbyEntitiesC2SReceiver::receive);
            ServerPlayNetworking.registerGlobalReceiver(GiveResistanceC2S.TYPE, GiveResistanceC2SReceiver::receive);
        }
    }

    public static void registerS2CReceivers() {
        ClientPlayNetworking.registerGlobalReceiver(ParticleSphereS2C.TYPE, ParticleSphereS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(ParticleOutburstS2C.TYPE, ParticleOutburstS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(FlashParticleS2C.TYPE, FlashParticleS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(SingleParticleS2C.TYPE, SingleParticleS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(ParrySyncS2C.TYPE, ParrySyncS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(PostureSyncS2C.TYPE, PostureSyncS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(SummonUUIDsSyncS2C.TYPE, SummonUUIDsSyncS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(FreyrSwordSummonDataSyncS2C.TYPE, FreyrSwordSummonDataSyncS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(ShouldDamageRidingSyncS2C.TYPE, ShouldDamageRidingSyncS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(ReturningProjectileDataSyncS2C.TYPE, ReturningProjectileDataSyncS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(UTDamageCooldownSyncS2C.TYPE, UTDamageCooldownSyncS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(StopBossMusicS2C.TYPE, StopBossMusicS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(ChainLightningS2C.TYPE, ChainLightningS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(BleedSyncS2C.TYPE, BleedSyncS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(TargetPostureSyncS2C.TYPE, TargetPostureSyncS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(MaxPostureSyncS2C.TYPE, MaxPostureSyncS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(FrostSyncS2C.TYPE, FrostSyncS2CReceiver::receive);
        ClientPlayNetworking.registerGlobalReceiver(EchoDamageSyncS2C.TYPE, EchoDamageSyncS2CReceiver::receive);
    }
}
