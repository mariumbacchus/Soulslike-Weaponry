package net.soulsweaponry.events;

import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.PostureData;
import net.soulsweaponry.util.CustomDamageSource;

public class LivingEntityTickHandler implements LivingEntityTickCallback {

    @Override
    public ActionResult tick(LivingEntity entity) {
        int posture = PostureData.getPosture(entity);
        int bleed = BleedData.getBleed(entity);
        if (posture >= ConfigConstructor.max_posture_loss) {
            if (!entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK)) {
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT, SoundCategory.PLAYERS, .5f, 1f);
            }
            entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK, 60, 1));
            PostureData.setPosture((IEntityDataSaver) entity, 0);
        }
        if (bleed >= ConfigConstructor.max_bleed) {
            entity.damage(CustomDamageSource.create(entity.getWorld(), CustomDamageSource.BLEED), ConfigConstructor.bleed_damage);
            if (entity.getWorld().isClient) {
                for (int i = 0; i < 40; i++) {
                    entity.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState()),
                            entity.getParticleX(1D), entity.getBodyY(0.5) + entity.getRandom().nextDouble() * 2 - 1D, entity.getParticleZ(1D), 0, 0, 0);
                }
            }
            BleedData.setBleed((IEntityDataSaver) entity, 0);
        }
        if (!entity.getWorld().isClient) {
            if (entity.age % ((int) ConfigConstructor.posture_loss_reduction_interval) == 0 && posture > 0) {
                PostureData.reducePosture((IEntityDataSaver) entity, (int) ConfigConstructor.posture_loss_reduction_amount);
            }
            if (entity.age % ((int) ConfigConstructor.bleed_reduction_interval) == 0 && bleed > 0) {
                BleedData.reduceBleed((IEntityDataSaver) entity, (int) ConfigConstructor.bleed_reduction_amount);
            }
        }
        return ActionResult.PASS;
    }
}
