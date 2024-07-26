package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.items.DarkinScythePre;
import net.soulsweaponry.items.SoulHarvestingItem;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;

import java.util.function.Supplier;

public class CollectSummonsC2S {

    public CollectSummonsC2S() {

    }

    //Same as decode
    public CollectSummonsC2S(PacketByteBuf buf) {

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

    private void handlePacket(ServerPlayerEntity player, CollectSummonsC2S packet) {
        for (Hand hand : Hand.values()) {
            Item handItem = player.getStackInHand(hand).getItem();
            if (handItem instanceof SoulHarvestingItem && !(handItem instanceof DarkinScythePre)) {
                int collectedSouls = 0;
                for (Entity entity : player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(8))) {
                    if (entity instanceof Remnant remnant && ((Remnant)entity).getOwner() == player) {
                        collectedSouls += remnant.getSoulAmount();
                        ParticleHandler.particleSphereList(player.getWorld(), 10, entity.getX(), entity.getY(), entity.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
                        player.world.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.5f, 0.7f);
                        entity.discard();
                    }
                }
                SoulHarvestingItem item = (SoulHarvestingItem)player.getStackInHand(hand).getItem();
                Text msg = collectedSouls == 0 ? new TranslatableText("soulsweapons.weapon.no_collected_souls")
                        : new TranslatableText("soulsweapons.weapon.collected_souls", collectedSouls).append(item.getName());
                item.addAmount(player.getStackInHand(hand), collectedSouls);
                player.sendMessage(msg, true);
            }
        }
    }
}
