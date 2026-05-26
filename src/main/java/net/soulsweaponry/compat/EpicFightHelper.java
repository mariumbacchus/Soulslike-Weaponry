package net.soulsweaponry.compat;

import net.minecraft.server.network.ServerPlayerEntity;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class EpicFightHelper {

    public static boolean isBusyWithEpicFight(ServerPlayerEntity player) {
        ServerPlayerPatch patch = EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
        if (patch == null) {
            return false;
        }
        return patch.isChargingSkill()
                || patch.getEntityState().inaction()
                || patch.getEntityState().attacking()
                || !patch.getEntityState().canUseSkill()
                || !patch.getEntityState().canBasicAttack();
    }
}
