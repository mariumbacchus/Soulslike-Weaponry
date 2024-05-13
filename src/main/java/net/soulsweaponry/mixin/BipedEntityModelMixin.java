package net.soulsweaponry.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.items.SoulHarvestingItem;
import net.soulsweaponry.entitydata.ParryData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.entity.LivingEntity;
import net.soulsweaponry.client.model.entity.mobs.ScythePosing;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.registry.WeaponRegistry;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends LivingEntity> {
    @Unique
    BipedEntityModel<?> model = ((BipedEntityModel<?>)(Object)this);
    @Unique
    private float parryProgress;

    @Inject(at = @At("TAIL"), method = "positionRightArm")
    private void positionRightArm(T entity, CallbackInfo info) {
        for (ItemStack stack : entity.getHandItems()) {
            if (stack.getItem() instanceof SoulHarvestingItem || stack.isOf(WeaponRegistry.GUTS_SWORD) && !stack.isOf(WeaponRegistry.FROSTMOURNE) || stack.isOf(WeaponRegistry.KRAKEN_SLAYER_CROSSBOW)) {
                if (!FabricLoader.getInstance().isModLoaded("bettercombat")) {
                    if (stack.isOf(WeaponRegistry.GUTS_SWORD)) {
                        CrossbowPosing.hold(model.rightArm, model.leftArm, model.head, true);
                    } else if (!stack.isOf(WeaponRegistry.KRAKEN_SLAYER_CROSSBOW)) {
                        ScythePosing.hold(model.rightArm, model.leftArm, model.head, true);
                    }
                } else if (entity instanceof Remnant) {
                    CrossbowPosing.hold(model.rightArm, model.leftArm, model.head, true);
                }
                if (stack.isOf(WeaponRegistry.KRAKEN_SLAYER_CROSSBOW)) {
                    CrossbowPosing.hold(model.rightArm, model.leftArm, model.head, true);
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "animateArms")
    protected void animateArms(T entity, float animationProgress, CallbackInfo info) {
        if (!FabricLoader.getInstance().isModLoaded("bettercombat")) {
            if (model.handSwingProgress > 0.0f && entity.getHandItems().iterator().next().getItem() instanceof SoulHarvestingItem) {
                ScythePosing.meleeAttack(model.leftArm, model.rightArm, entity, entity.handSwingProgress, animationProgress);
            }
        }
        if (entity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity) {
            int frames = ParryData.getParryFrames(abstractClientPlayerEntity);
            if (frames >= 1) {
                this.parryProgress = frames == 1 ? 0.1f : parryProgress;
                float added = (1f / (float) ParryData.MAX_PARRY_FRAMES) / 6f;
                this.parryProgress = Math.min(this.parryProgress + added, 1f);
                ModelPart modelPart = model.leftArm;
                float f = parryProgress;
                model.body.yaw = MathHelper.sin(MathHelper.sqrt(f) * ((float)Math.PI * 2)) * 0.2f;
                model.leftArm.pivotZ = -MathHelper.sin(model.body.yaw) * 5.0f;
                model.leftArm.pivotX = MathHelper.cos(model.body.yaw) * 5.0f;
                model.leftArm.yaw += model.body.yaw;
                model.leftArm.pitch += model.body.yaw;
                f = 1.0f - parryProgress;
                f *= f;
                f *= f;
                f = 1.0f - f;
                float g = MathHelper.sin(f * (float)Math.PI);
                float h = MathHelper.sin(parryProgress * (float)Math.PI) * -(model.head.pitch - 0.7f) * 0.75f;
                modelPart.pitch -= g * 1.2f + h;
                modelPart.yaw += model.body.yaw * 2.0f;
                modelPart.roll += MathHelper.sin(parryProgress * (float)Math.PI) * -0.8f; //0.4
            }
        }
    }
}
