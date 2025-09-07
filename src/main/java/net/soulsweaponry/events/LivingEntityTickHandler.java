package net.soulsweaponry.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.soulsweaponry.api.entitystats.EntityBleed;
import net.soulsweaponry.api.entitystats.EntityFrost;
import net.soulsweaponry.api.entitystats.EntityPosture;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.entitydata.FrostData;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.PostureData;

public class LivingEntityTickHandler implements LivingEntityTickCallback {

    @Override
    public ActionResult tick(LivingEntity entity) {
        int posture = PostureData.getPosture(entity);
        int bleed = BleedData.getBleed(entity);
        int frost = FrostData.getFrost(entity);
        boolean frostCoolingDown = FrostData.isFrostCoolingDown(entity);
        if (!EntityPosture.isPostureDisabled(entity) && posture >= EntityPosture.getMaxPostureLoss(entity) && EntityPosture.getMaxPostureLoss(entity) != 0) {
            if (!entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK)) {
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT, SoundCategory.PLAYERS, .5f, 1f);
            }
            entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK, 60, 1));
            PostureData.setPosture((IEntityDataSaver) entity, 0);
        }
        if (!EntityBleed.isBleedDisabled(entity) && bleed >= EntityBleed.getMaxBleed(entity)) {
            EntityBleed.triggerBloodLoss(entity);
        }
        if (!EntityFrost.isFrostBuildupDisabled(entity) && frost >= EntityFrost.getMaxFrostBuildup(entity)) {
            EntityFrost.triggerFrost(entity);
        }
        if (!entity.getWorld().isClient) {
            if (entity.age % ((int) ConfigConstructor.posture_loss_reduction_interval) == 0 && posture > 0) {
                PostureData.reducePosture(entity, (int) ConfigConstructor.posture_loss_reduction_amount);
            }
            if (entity.age % ((int) ConfigConstructor.bleed_reduction_interval) == 0 && bleed > 0) {
                BleedData.reduceBleed((IEntityDataSaver) entity, (int) ConfigConstructor.bleed_reduction_amount);
            }
            if (entity.age % ((int) ConfigConstructor.frost_reduction_interval) == 0 && frostCoolingDown) {
                int newFrost = FrostData.reduceFrost((IEntityDataSaver) entity, (int) ConfigConstructor.frost_reduction_amount);
                if (newFrost <= 0) {
                    FrostData.setFrostCoolingDown((IEntityDataSaver) entity, false);
                }
            }
        }
        return ActionResult.PASS;
    }
}
