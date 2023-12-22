package net.soulsweaponry.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.networking.packets.C2S.Example;
import net.soulsweaponry.networking.packets.S2C.ParticleOutburstS2C;
import net.soulsweaponry.networking.packets.S2C.ParticleSphereS2C;

public class ModMessages {

    private static SimpleChannel INSTANCE;

    //Makes all messages have different id's
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(SoulsWeaponry.MOD_ID, "messages")).networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true).simpleChannel();
        INSTANCE = net;

        net.messageBuilder(Example.class, id(), NetworkDirection.PLAY_TO_SERVER).decoder(Example::new).encoder(Example::toBytes).consumer(Example::handle).add();
        net.messageBuilder(ParticleOutburstS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ParticleOutburstS2C::new).encoder(ParticleOutburstS2C::toBytes).consumer(ParticleOutburstS2C::handle).add();
        net.messageBuilder(ParticleSphereS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(ParticleSphereS2C::new).encoder(ParticleSphereS2C::toBytes).consumer(ParticleSphereS2C::handle).add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
