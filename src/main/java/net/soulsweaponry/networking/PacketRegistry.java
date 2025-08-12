package net.soulsweaponry.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.soulsweaponry.networking.C2S.packets.*;
import net.soulsweaponry.networking.S2C.packets.*;

public class PacketRegistry {

    public static void registerPackets() {
        // Client to Server
        PayloadTypeRegistry.playC2S().register(CollectSummonsC2S.TYPE, CollectSummonsC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(DamagingBoxC2S.TYPE, DamagingBoxC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(KeybindAbilityC2S.TYPE, KeybindAbilityC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(MoonlightC2S.TYPE, MoonlightC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(ReturnFreyrSwordC2S.TYPE, ReturnFreyrSwordC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(StationaryFreyrSwordC2S.TYPE, StationaryFreyrSwordC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(SwitchTrickWeaponC2S.TYPE, SwitchTrickWeaponC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(ParryC2S.TYPE, ParryC2S.CODEC);
        PayloadTypeRegistry.playC2S().register(ReturnThrownWeaponC2S.TYPE, ReturnThrownWeaponC2S.CODEC);

        // Packet ids used for debugging in dev environment only
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            PayloadTypeRegistry.playC2S().register(GiveResistanceC2S.TYPE, GiveResistanceC2S.CODEC);
            PayloadTypeRegistry.playC2S().register(KillNearbyEntitiesC2S.TYPE, KillNearbyEntitiesC2S.CODEC);
        }

        // Server to Client
        PayloadTypeRegistry.playS2C().register(BleedSyncS2C.TYPE, BleedSyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(ParrySyncS2C.TYPE, ParrySyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(MaxPostureSyncS2C.TYPE, MaxPostureSyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(PostureSyncS2C.TYPE, PostureSyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(TargetPostureSyncS2C.TYPE, TargetPostureSyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(UTDamageCooldownSyncS2C.TYPE, UTDamageCooldownSyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(SummonUUIDsSyncS2C.TYPE, SummonUUIDsSyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(ShouldDamageRidingSyncS2C.TYPE, ShouldDamageRidingSyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(ReturningProjectileDataSyncS2C.TYPE, ReturningProjectileDataSyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(FreyrSwordSummonDataSyncS2C.TYPE, FreyrSwordSummonDataSyncS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(StopBossMusicS2C.TYPE, StopBossMusicS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(ChainLightningS2C.TYPE, ChainLightningS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(FlashParticleS2C.TYPE, FlashParticleS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(SingleParticleS2C.TYPE, SingleParticleS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(ParticleSphereS2C.TYPE, ParticleSphereS2C.CODEC);
        PayloadTypeRegistry.playS2C().register(ParticleOutburstS2C.TYPE, ParticleOutburstS2C.CODEC);
    }
}
