package net.soulsweaponry.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.soulsweaponry.api.entitystats.EntityBleed;
import net.soulsweaponry.api.entitystats.EntityPosture;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.PostureData;

public class LivingEntityTickHandler implements LivingEntityTickCallback {

    @Override
    public ActionResult tick(LivingEntity entity) {
        int posture = PostureData.getPosture(entity);
        int bleed = BleedData.getBleed(entity);
        if (!EntityPosture.isPostureDisabled(entity) && posture >= EntityPosture.getMaxPostureLoss(entity)) {
            if (!entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK)) {
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT, SoundCategory.PLAYERS, .5f, 1f);
            }
            entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK, 60, 1));
            PostureData.setPosture((IEntityDataSaver) entity, 0);
        }
        if (!EntityBleed.isBleedDisabled(entity) && bleed >= EntityBleed.getMaxBleed(entity)) {
            EntityBleed.triggerBloodLoss(entity);
        }
        if (!entity.getWorld().isClient) {
            if (entity.age % ((int) ConfigConstructor.posture_loss_reduction_interval) == 0 && posture > 0) {
                PostureData.reducePosture(entity, (int) ConfigConstructor.posture_loss_reduction_amount);
            }
            if (entity.age % ((int) ConfigConstructor.bleed_reduction_interval) == 0 && bleed > 0) {
                BleedData.reduceBleed((IEntityDataSaver) entity, (int) ConfigConstructor.bleed_reduction_amount);
            }
        }
        return ActionResult.PASS;
    }
}
