package net.soulsweaponry.mixin;

import net.soulsweaponry.items.SoulHarvestingItem;
import org.spongepowered.asm.mixin.Mixin;
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
    BipedEntityModel<?> model = ((BipedEntityModel<?>)(Object)this);
    
    @Inject(at = @At("TAIL"), method = "positionRightArm")
    private void positionRightArm(T entity, CallbackInfo info) {
        if ((entity.getItemsHand().iterator().next().getItem() instanceof SoulHarvestingItem || entity.getItemsHand().iterator().next().isOf(WeaponRegistry.GUTS_SWORD))
                && !entity.getItemsHand().iterator().next().isOf(WeaponRegistry.FROSTMOURNE)) {
            if (!FabricLoader.getInstance().isModLoaded("bettercombat")) {
                if (entity.getItemsHand().iterator().next().isOf(WeaponRegistry.GUTS_SWORD)) {
                    CrossbowPosing.hold(model.rightArm, model.leftArm, model.head, true);
                } else {
                    ScythePosing.hold(model.rightArm, model.leftArm, model.head, true);
                }
            } else if (entity instanceof Remnant) {
                CrossbowPosing.hold(model.rightArm, model.leftArm, model.head, true);
            }
        }
    }

    @Inject(at = @At("TAIL"), method = "animateArms")
    protected void animateArms(T entity, float animationProgress, CallbackInfo info) {
        if (!FabricLoader.getInstance().isModLoaded("bettercombat")) {
            if (entity.getItemsHand().iterator().next().getItem() instanceof SoulHarvestingItem) {
                ScythePosing.meleeAttack(model.leftArm, model.rightArm, entity, entity.handSwingProgress, animationProgress);
            }
        }
    }
}
