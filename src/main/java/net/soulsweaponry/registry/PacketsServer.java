package net.soulsweaponry.registry;

import com.google.common.collect.Iterables;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.entity.mobs.Forlorn;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.entity.mobs.Soulmass;
import net.soulsweaponry.items.*;
import net.soulsweaponry.util.ParticleNetworking;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static net.soulsweaponry.util.WeaponUtil.TRICK_WEAPONS;

public class PacketsServer {

    public static void initServer() {
        ServerPlayNetworking.registerGlobalReceiver(PacketRegistry.MOONLIGHT, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
                if (serverWorld != null) {
                    MoonlightShortsword.summonSmallProjectile(serverWorld, player);
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(PacketRegistry.RETURN_FREYR_SWORD, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
                if (serverWorld != null) {
                    //NOTE: reading bufs does not work for some reason
                    try {
                        Optional<UUID> op = player.getDataTracker().get(FreyrSword.SUMMON_UUID);
                        if (op.isPresent() && player.getBlockPos() != null) {
                            Entity sword = serverWorld.getEntity(op.get());
                            if (sword != null && sword instanceof FreyrSwordEntity) {
                                if (!((FreyrSwordEntity)sword).insertStack(player)) {
                                    sword.setPos(player.getX(), player.getEyeY(), player.getZ());
                                    ((FreyrSwordEntity)sword).dropStack();
                                }
                                sword.discard();
                            } else {
                                player.sendMessage(Text.literal("There is no Freyr Sword bound to you!"), true);
                            }
                        }
                    } catch (Exception e) {
                        player.sendMessage(Text.literal("There is no Freyr Sword bound to you!"), true);
                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(PacketRegistry.STATIONARY_FREYR_SWORD, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
                if (serverWorld != null) {
                    try {
                        Optional<UUID> op = player.getDataTracker().get(FreyrSword.SUMMON_UUID);
                        if (op.isPresent() && player.getBlockPos() != null) {
                            Entity entity = serverWorld.getEntity(player.getDataTracker().get(FreyrSword.SUMMON_UUID).get());
                            if (entity != null && entity instanceof FreyrSwordEntity) {
                                FreyrSwordEntity sword = ((FreyrSwordEntity)entity);
                                sword.setStationaryPos(player.getBlockPos());
                            } else {
                                player.sendMessage(Text.literal("There is no Freyr Sword bound to you!"), true);
                            }
                        }
                    } catch (Exception e) {
                        player.sendMessage(Text.literal("There is no Freyr Sword bound to you!"), true);
                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(PacketRegistry.COLLECT_SUMMONS, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
                if (serverWorld != null) {
                    for (Hand hand : Hand.values()) {
                        Item handItem = player.getStackInHand(hand).getItem();
                        if (handItem instanceof SoulHarvestingItem && !(handItem instanceof UmbralTrespassItem || handItem instanceof DarkinScythePre)) {
                            int collectedSouls = 0;
                            for (Entity entity : serverWorld.getOtherEntities(player, player.getBoundingBox().expand(8))) {
                                if (entity instanceof Remnant && ((Remnant)entity).getOwner() == player) {
                                    if (entity instanceof Soulmass) {
                                        collectedSouls += 30;
                                    } else if (entity instanceof Forlorn) {
                                        collectedSouls += 10;
                                    } else {
                                        collectedSouls += 3;
                                    }
                                    ParticleNetworking.sendServerParticlePacket(serverWorld, PacketRegistry.DARK_EXPLOSION_ID, entity.getBlockPos(), 10);
                                    serverWorld.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.5f, 0.7f);
                                    entity.discard();
                                }
                            }
                            SoulHarvestingItem item = (SoulHarvestingItem)player.getStackInHand(hand).getItem();
                            String msg = collectedSouls == 0 ? "There were no bound allies to collect!" : "Collected " + collectedSouls + " souls back to the " + item.getName().getString();
                            item.addAmount(player.getStackInHand(hand), collectedSouls);
                            player.sendMessage(Text.literal(msg), true);
                        }
                    }
                }
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(PacketRegistry.SWITCH_TRICK_WEAPON, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.world).orNull();
                if (serverWorld != null) {
                    Item handItem = player.getStackInHand(Hand.MAIN_HAND).getItem();
                    if (handItem instanceof TrickWeapon && !player.getItemCooldownManager().isCoolingDown(handItem)) {
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
                        }
                        serverWorld.playSound(null, player.getBlockPos(), SoundRegistry.TRICK_WEAPON_EVENT, SoundCategory.PLAYERS, 0.8f, MathHelper.nextFloat(player.getRandom(), 0.75f, 1.5f));
                        ParticleNetworking.sendServerParticlePacket(serverWorld, PacketRegistry.DARK_EXPLOSION_ID, player.getBlockPos(), 20);
                        newWeapon.setDamage(stack.getDamage());
                        int slot = player.getInventory().selectedSlot;
                        player.getInventory().removeStack(slot);
                        player.getInventory().insertStack(slot, newWeapon);
                        player.getItemCooldownManager().set(switchWeapon, 20);
                    }
                }
            });
        });
    }
}
