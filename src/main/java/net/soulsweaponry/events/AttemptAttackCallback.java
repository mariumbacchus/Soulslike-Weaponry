package net.soulsweaponry.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public interface AttemptAttackCallback {
    
    Event<AttemptAttackCallback> EVENT = EventFactory.createArrayBacked(AttemptAttackCallback.class, 
        (listeners) -> (player, world) -> {
            for (AttemptAttackCallback listener : listeners) {
                ActionResult result = listener.useViaAttack(player, world);
                if(result != ActionResult.PASS) {
                    return result;
                }
            }
        return ActionResult.PASS;
    });

    ActionResult useViaAttack(PlayerEntity player, World world);
}
