package net.soulsweaponry.networking.C2S;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.api.trickweapon.TrickWeaponUtil;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;

public record SwitchTrickWeaponC2S() implements CustomPayload {

    public static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "switch_trick_weapon");
    public static final CustomPayload.Id<SwitchTrickWeaponC2S> TYPE = new CustomPayload.Id<>(ID);
    public static final PacketCodec<RegistryByteBuf, SwitchTrickWeaponC2S> CODEC =
            PacketCodec.of(
                    (pkt, buf) -> { },
                    buf -> new SwitchTrickWeaponC2S()
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return TYPE;
    }

    public static void receive(SwitchTrickWeaponC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> {
            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
            Item handItem = stack.getItem();
            ItemStack newWeapon = TrickWeaponUtil.getMappedStack(stack);
            if (newWeapon != null && !player.getItemCooldownManager().isCoolingDown(handItem)) {
                if (newWeapon.getItem() instanceof IConfigDisable disable && disable.isDisabled(stack)) {
                    disable.notifyDisabled(player);
                    return;
                }
                player.getItemCooldownManager().set(newWeapon.getItem(), 20);
                serverWorld.playSound(null, player.getBlockPos(), SoundRegistry.TRICK_WEAPON_EVENT, SoundCategory.PLAYERS, 0.8f, MathHelper.nextFloat(player.getRandom(), 0.75f, 1.5f));
                ParticleHandler.particleSphereList(serverWorld, 20, player.getX(), player.getY(), player.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
                int slot = player.getInventory().selectedSlot;
                player.getInventory().removeStack(slot);
                player.getInventory().insertStack(slot, newWeapon);
            }
        });
    }
}