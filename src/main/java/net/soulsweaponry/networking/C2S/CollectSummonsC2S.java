package net.soulsweaponry.networking.C2S;

import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.items.DarkinScythePre;
import net.soulsweaponry.items.SoulHarvestingItem;
import net.soulsweaponry.items.UmbralTrespassItem;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;

public class CollectSummonsC2S {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        server.execute(() -> {
            ServerWorld serverWorld = Iterables.tryFind(server.getWorlds(), (element) -> element == player.getWorld()).orNull();
            if (serverWorld != null) {
                for (Hand hand : Hand.values()) {
                    Item handItem = player.getStackInHand(hand).getItem();
                    if (handItem instanceof SoulHarvestingItem && !(handItem instanceof DarkinScythePre)) {
                        int collectedSouls = 0;
                        for (Entity entity : serverWorld.getOtherEntities(player, player.getBoundingBox().expand(8))) {
                            if (entity instanceof Remnant remnant && ((Remnant)entity).getOwner() == player) {
                                collectedSouls += remnant.getSoulAmount();
                                ParticleHandler.particleSphereList(serverWorld, 10, entity.getX(), entity.getY(), entity.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
                                serverWorld.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.5f, 0.7f);
                                entity.discard();
                            }
                        }
                        SoulHarvestingItem item = (SoulHarvestingItem)player.getStackInHand(hand).getItem();
                        Text msg = collectedSouls == 0 ? Text.translatableWithFallback("soulsweapons.weapon.no_collected_souls", "There were no bound allies to collect!")
                                : Text.translatable("soulsweapons.weapon.collected_souls", collectedSouls).append(item.getName());
                        item.addAmount(player.getStackInHand(hand), collectedSouls);
                        player.sendMessage(msg, true);
                    }
                }
            }
        });
    }
}