package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.api.trickweapon.TrickWeaponUtil;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.function.Supplier;

public class SwitchTrickWeaponC2S {

    public SwitchTrickWeaponC2S() {

    }

    //Same as decode
    public SwitchTrickWeaponC2S(PacketByteBuf buf) {

    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {

    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            this.handlePacket(player, this);
        });
        context.setPacketHandled(true);
    }

    private void handlePacket(ServerPlayerEntity player, SwitchTrickWeaponC2S packet) {
        ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
        Item handItem = stack.getItem();
        ItemStack newWeapon = TrickWeaponUtil.getMappedStack(stack);
        if (newWeapon != null && !player.getItemCooldownManager().isCoolingDown(handItem)) {
            if (newWeapon.getItem() instanceof IConfigDisable disable && disable.isDisabled(stack)) {
                disable.notifyDisabled(player);
                return;
            }
            player.getItemCooldownManager().set(newWeapon.getItem(), 20);
            player.getWorld().playSound(null, player.getBlockPos(), SoundRegistry.TRICK_WEAPON_EVENT.get(), SoundCategory.PLAYERS, 0.8f, MathHelper.nextFloat(player.getRandom(), 0.75f, 1.5f));
            ParticleHandler.particleSphereList(player.getWorld(), 20, player.getX(), player.getY(), player.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
            newWeapon.setDamage(stack.getDamage());
            int slot = player.getInventory().selectedSlot;
            player.getInventory().removeStack(slot);
            player.getInventory().insertStack(slot, newWeapon);
        }
    }
}
