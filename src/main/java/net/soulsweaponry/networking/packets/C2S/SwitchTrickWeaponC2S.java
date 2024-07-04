package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import net.soulsweaponry.items.FreyrSword;
import net.soulsweaponry.items.IChargeNeeded;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static net.soulsweaponry.util.WeaponUtil.TRICK_WEAPONS;

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
        Item handItem = player.getStackInHand(Hand.MAIN_HAND).getItem();
        if (handItem instanceof TrickWeapon weapon && !player.getItemCooldownManager().isCoolingDown(handItem)) {
            if (weapon.isDisabled()) {
                weapon.notifyDisabled(player);
                return;
            }
            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
            TrickWeapon switchWeapon = TRICK_WEAPONS[((TrickWeapon) handItem).getSwitchWeaponIndex()];
            if (stack.hasNbt() && stack.getNbt().contains(WeaponUtil.PREV_TRICK_WEAPON)) {
                switchWeapon = TRICK_WEAPONS[stack.getNbt().getInt(WeaponUtil.PREV_TRICK_WEAPON)];
            }
            ItemStack newWeapon = new ItemStack(switchWeapon);
            Map<Enchantment, Integer> enchants = EnchantmentHelper.get(stack);
            for (Enchantment enchant : enchants.keySet()) {
                newWeapon.addEnchantment(enchant, enchants.get(enchant));
            }
            if (stack.hasCustomName()) {
                newWeapon.setCustomName(stack.getName());
            }
            if (newWeapon.hasNbt()) {
                newWeapon.getNbt().putInt(WeaponUtil.PREV_TRICK_WEAPON, ((TrickWeapon) handItem).getOwnWeaponIndex());
                if (stack.getNbt().contains(IChargeNeeded.CHARGE)) {
                    newWeapon.getNbt().putInt(IChargeNeeded.CHARGE, stack.getNbt().getInt(IChargeNeeded.CHARGE));
                }
            }
            player.getWorld().playSound(null, player.getBlockPos(), SoundRegistry.TRICK_WEAPON_EVENT.get(), SoundCategory.PLAYERS, 0.8f, MathHelper.nextFloat(player.getRandom(), 0.75f, 1.5f));
            ParticleHandler.particleSphereList(player.getWorld(), 20, player.getX(), player.getY(), player.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
            newWeapon.setDamage(stack.getDamage());
            int slot = player.getInventory().selectedSlot;
            player.getInventory().removeStack(slot);
            player.getInventory().insertStack(slot, newWeapon);
            player.getItemCooldownManager().set(switchWeapon, 20);
        }
    }
}
