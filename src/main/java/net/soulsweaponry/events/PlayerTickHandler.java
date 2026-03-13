package net.soulsweaponry.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.ParryData;
import net.soulsweaponry.entitydata.TargetPostureData;

public class PlayerTickHandler implements ServerTickEvents.StartTick {

    @Override
    public void onStartTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            int frames = ParryData.getParryTicks(player);
            if (frames >= 1) {
                ParryData.addParryTicks(player, 1);
                player.stopUsingItem();
            }
            if (player.getAttacking() != null) {
                TargetPostureData.updateTargetPosture((IEntityDataSaver) player, player.getAttacking());
            } else {
                TargetPostureData.resetValues((IEntityDataSaver) player);
            }
        }
    }
}