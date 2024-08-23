package net.soulsweaponry.networking;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.networking.packets.C2S.*;
import net.soulsweaponry.networking.packets.S2C.*;

public class ModMessages {

    private static SimpleChannel INSTANCE;

    //Makes all messages have different id's
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new Identifier(SoulsWeaponry.ModId, "messages")).networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true).simpleChannel();
        INSTANCE = net;

        net.messageBuilder(CollectSummonsC2S.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(CollectSummonsC2S::new).encoder(CollectSummonsC2S::toBytes).consumerMainThread(CollectSummonsC2S::handle).add();
        net.messageBuilder(DamagingBoxC2S.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(DamagingBoxC2S::new).encoder(DamagingBoxC2S::toBytes).consumerMainThread(DamagingBoxC2S::handle).add();
        net.messageBuilder(KeybindAbilityC2S.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(KeybindAbilityC2S::new).encoder(KeybindAbilityC2S::toBytes).consumerMainThread(KeybindAbilityC2S::handle).add();
        net.messageBuilder(MoonlightC2S.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(MoonlightC2S::new).encoder(MoonlightC2S::toBytes).consumerMainThread(MoonlightC2S::handle).add();
        net.messageBuilder(ParryC2S.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ParryC2S::new).encoder(ParryC2S::toBytes).consumerMainThread(ParryC2S::handle).add();
        net.messageBuilder(ReturnFreyrSwordC2S.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ReturnFreyrSwordC2S::new).encoder(ReturnFreyrSwordC2S::toBytes).consumerMainThread(ReturnFreyrSwordC2S::handle).add();
        net.messageBuilder(ReturnThrownWeaponC2S.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(ReturnThrownWeaponC2S::new).encoder(ReturnThrownWeaponC2S::toBytes).consumerMainThread(ReturnThrownWeaponC2S::handle).add();
        net.messageBuilder(StationaryFreyrSwordC2S.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(StationaryFreyrSwordC2S::new).encoder(StationaryFreyrSwordC2S::toBytes).consumerMainThread(StationaryFreyrSwordC2S::handle).add();
        net.messageBuilder(SwitchTrickWeaponC2S.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(SwitchTrickWeaponC2S::new).encoder(SwitchTrickWeaponC2S::toBytes).consumerMainThread(SwitchTrickWeaponC2S::handle).add();

        net.messageBuilder(ParticleOutburstS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ParticleOutburstS2C::new).encoder(ParticleOutburstS2C::toBytes).consumerMainThread(ParticleOutburstS2C::handle).add();
        net.messageBuilder(ParticleSphereS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ParticleSphereS2C::new).encoder(ParticleSphereS2C::toBytes).consumerMainThread(ParticleSphereS2C::handle).add();
        net.messageBuilder(FlashParticleS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(FlashParticleS2C::new).encoder(FlashParticleS2C::toBytes).consumerMainThread(FlashParticleS2C::handle).add();
        net.messageBuilder(ParrySyncS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ParrySyncS2C::new).encoder(ParrySyncS2C::toBytes).consumerMainThread(ParrySyncS2C::handle).add();
        net.messageBuilder(PostureSyncS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(PostureSyncS2C::new).encoder(PostureSyncS2C::toBytes).consumerMainThread(PostureSyncS2C::handle).add();
        net.messageBuilder(SummonUUIDsSyncS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(SummonUUIDsSyncS2C::new).encoder(SummonUUIDsSyncS2C::toBytes).consumerMainThread(SummonUUIDsSyncS2C::handle).add();
        net.messageBuilder(FreyrSwordSummonDataSyncS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(FreyrSwordSummonDataSyncS2C::new).encoder(FreyrSwordSummonDataSyncS2C::toBytes).consumerMainThread(FreyrSwordSummonDataSyncS2C::handle).add();
        net.messageBuilder(UTDamageCooldownSyncS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(UTDamageCooldownSyncS2C::new).encoder(UTDamageCooldownSyncS2C::toBytes).consumerMainThread(UTDamageCooldownSyncS2C::handle).add();
        net.messageBuilder(ShouldDamageRidingSyncS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ShouldDamageRidingSyncS2C::new).encoder(ShouldDamageRidingSyncS2C::toBytes).consumerMainThread(ShouldDamageRidingSyncS2C::handle).add();
        net.messageBuilder(ReturningProjectileDataSyncS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ReturningProjectileDataSyncS2C::new).encoder(ReturningProjectileDataSyncS2C::toBytes).consumerMainThread(ReturningProjectileDataSyncS2C::handle).add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
