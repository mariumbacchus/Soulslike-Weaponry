package net.soulsweaponry.networking.C2S;

import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.soulsweaponry.items.IChargeNeeded;
import net.soulsweaponry.items.TrickWeapon;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Map;

import static net.soulsweaponry.util.WeaponUtil.TRICK_WEAPONS;

public class SwitchTrickWeaponC2S {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        server.execute(() -> {
            ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
            if (serverWorld != null) {
                ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
                Item handItem = stack.getItem();
                if (handItem instanceof TrickWeapon weapon && !player.getItemCooldownManager().isCoolingDown(handItem)) {
                    if (weapon.isDisabled(stack)) {
                        weapon.notifyDisabled(player);
                        return;
                    }
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
                    serverWorld.playSound(null, player.getBlockPos(), SoundRegistry.TRICK_WEAPON_EVENT, SoundCategory.PLAYERS, 0.8f, MathHelper.nextFloat(player.getRandom(), 0.75f, 1.5f));
                    ParticleHandler.particleSphereList(serverWorld, 20, player.getX(), player.getY(), player.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
                    newWeapon.setDamage(stack.getDamage());
                    int slot = player.getInventory().selectedSlot;
                    player.getInventory().removeStack(slot);
                    player.getInventory().insertStack(slot, newWeapon);
                    player.getItemCooldownManager().set(switchWeapon, 20);
                }
            }
        });
    }
}
