package net.soulsweaponry.compat;

import net.minecraft.server.network.ServerPlayerEntity;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class EpicFightHelper {

    public static boolean isBusyWithEpicFight(ServerPlayerEntity player) {
        ServerPlayerPatch patch = EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
        if (patch == null || !patch.isEpicFightMode()) {
            return false;
        }

        return !patch.getEntityState().canSwitchHoldingItem();
    }
}
