package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraftforge.network.NetworkEvent;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.targetdeath.SoulHarvest;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;

import java.util.Optional;
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
        ServerWorld serverWorld = player.getServerWorld();
        for (Hand hand : Hand.values()) {
            Item handItem = player.getStackInHand(hand).getItem();
            if (handItem instanceof IHasAbilities hasAbilities) {
                Optional<SoulHarvest> ability = hasAbilities.findAbility(SoulHarvest.class);
                if (ability.isPresent() && ability.get().canCollectSouls()) {
                    int collectedSouls = 0;
                    for (Entity entity : serverWorld.getOtherEntities(player, player.getBoundingBox().expand(8))) {
                        if (entity instanceof Remnant remnant && remnant.getOwner() != null && remnant.getOwner().equals(player)) {
                            collectedSouls += remnant.getSoulAmount();
                            ParticleHandler.particleSphereList(serverWorld, 10, entity.getX(), entity.getY(), entity.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
                            serverWorld.playSound(null, entity.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.5f, 0.7f);
                            entity.discard();
                        }
                    }
                    Text msg = null;
                    if (ConfigConstructor.inform_player_about_no_souls_to_collect && collectedSouls == 0) {
                        msg = Text.translatableWithFallback("soulsweapons.weapon.no_collected_souls", "There were no bound allies to collect!");
                    } else if (ConfigConstructor.inform_player_about_collected_souls && collectedSouls > 0) {
                        msg = Text.translatable("soulsweapons.weapon.collected_souls", collectedSouls).append(handItem.getName());
                    }
                    ability.get().addAmount(player.getStackInHand(hand), collectedSouls);
                    if (msg != null) {
                        player.sendMessage(msg, true);
                    }
                }
            }
        }
    }
}
