package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.api.trickweapon.TrickWeaponUtil;
import net.soulsweaponry.items.abilities.IConfigDisable;
import net.soulsweaponry.networking.C2S.packets.SwitchTrickWeaponC2S;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;

public class SwitchTrickWeaponC2SReceiver {

    public static void receive(SwitchTrickWeaponC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        server.execute(() -> {
            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
            ItemStack newWeapon = TrickWeaponUtil.getMappedStack(serverWorld, stack);
            if (newWeapon != null && !player.getItemCooldownManager().isCoolingDown(stack)) {
                if (newWeapon.getItem() instanceof IConfigDisable disable && disable.isDisabled(stack)) {
                    disable.notifyDisabled(player);
                    return;
                }
                player.getItemCooldownManager().set(newWeapon, 20);
                serverWorld.playSound(null, player.getBlockPos(), SoundRegistry.TRICK_WEAPON_EVENT, SoundCategory.PLAYERS, 0.8f, MathHelper.nextFloat(player.getRandom(), 0.75f, 1.5f));
                ParticleHandler.particleSphereList(serverWorld, 20, player.getX(), player.getY(), player.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
                int slot = player.getInventory().selectedSlot;
                player.getInventory().removeStack(slot);
                player.getInventory().insertStack(slot, newWeapon);
            }
        });
    }
}
