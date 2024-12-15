package net.soulsweaponry.networking.C2S;

import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.api.trickweapon.TrickWeaponUtil;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;

public class SwitchTrickWeaponC2S {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        server.execute(() -> {
            ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.getWorld()).orNull();
            if (serverWorld != null) {
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
            }
        });
    }
}