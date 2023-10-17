package net.soulsweaponry.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.IEntityDataSaver;
import net.soulsweaponry.util.PostureData;

public class LivingEntityTickHandler implements LivingEntityTickCallback {
    @Override
    public ActionResult tick(LivingEntity entity) {
        int posture = PostureData.getPosture(entity);
        if (posture >= ConfigConstructor.max_posture_loss) {
            if (!entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK)) {
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT, SoundCategory.PLAYERS, .5f, 1f);
            }
            entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK, 60, 1));
            PostureData.setPosture((IEntityDataSaver) entity, 0);
        } else if (posture > 0 && entity.age % 4 == 0) {
            PostureData.reducePosture((IEntityDataSaver) entity, 1);
        }
        return ActionResult.PASS;
    }
}