package net.soulsweaponry.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ActionResult;

public interface LivingEntityTickCallback {

    Event<LivingEntityTickCallback> EVENT = EventFactory.createArrayBacked(LivingEntityTickCallback.class,
            (listeners) -> (entity) -> {
                for (LivingEntityTickCallback listener : listeners) {
                    ActionResult result = listener.tick(entity);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });

    ActionResult tick(LivingEntity entity);
}
