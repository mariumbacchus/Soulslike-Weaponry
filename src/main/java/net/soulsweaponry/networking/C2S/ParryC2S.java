package net.soulsweaponry.networking.C2S;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.ParryData;
import net.soulsweaponry.util.ModTags;

public record ParryC2S() implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "parry_keybind");
    public static final CustomPayload.Id<ParryC2S> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, ParryC2S> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> { },
                    buf -> new ParryC2S()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(ParryC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        server.execute(() -> {
            ItemStack stack = player.getStackInHand(Hand.OFF_HAND);
            if (ConfigConstructor.enable_shield_parry && stack.isIn(ConventionalItemTags.SHIELD_TOOLS) && !player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
                ParryData.setParryFrames((IEntityDataSaver) player, 1);
                player.getItemCooldownManager().set(stack.getItem(), player.isCreative() ? 10 : (int) ConfigConstructor.shield_parry_cooldown);
            }
        });
    }
}