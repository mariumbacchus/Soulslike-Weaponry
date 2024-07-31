package net.soulsweaponry.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.loading.FMLLoader;
import net.soulsweaponry.client.entitydata.ClientParryData;
import net.soulsweaponry.client.model.entity.mobs.ScythePosing;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.entitydata.ParryData;
import net.soulsweaponry.items.SoulHarvestingItem;
import net.soulsweaponry.registry.WeaponRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends LivingEntity> {
    @Unique
    BipedEntityModel<?> model = ((BipedEntityModel<?>)(Object)this);
    @Unique
    private float parryProgress;
    @Unique
    private final Set<Item> customHoldItems = Set.of(
            WeaponRegistry.GUTS_SWORD.get(),
            WeaponRegistry.KRAKEN_SLAYER_CROSSBOW.get(),
            WeaponRegistry.DARKIN_SCYTHE_PRE.get(),
            WeaponRegistry.DARKIN_SCYTHE_PRIME.get(),
            WeaponRegistry.SHADOW_ASSASSIN_SCYTHE.get()
    );
    
    @Inject(at = @At("TAIL"), method = "positionRightArm")
    private void positionRightArm(T entity, CallbackInfo info) {//TODO: make scythes not use this posing, only guts' sword, also change the models to look better
        /*for (ItemStack stack : entity.getItemsHand()) {
            if (!stack.isOf(WeaponRegistry.FROSTMOURNE.get()) && (stack.getItem() instanceof SoulHarvestingItem || customHoldItems.contains(stack.getItem()))) {
                if (FMLLoader.getLoadingModList().getModFileById("bettercombat") == null) {
                    if (stack.isOf(WeaponRegistry.GUTS_SWORD.get())) {
                        CrossbowPosing.hold(model.rightArm, model.leftArm, model.head, true);
                    } else if (!stack.isOf(WeaponRegistry.KRAKEN_SLAYER_CROSSBOW.get())) {
                        ScythePosing.hold(model.rightArm, model.leftArm, model.head, true);
                    }
                } else if (entity instanceof Remnant) {
                    CrossbowPosing.hold(model.rightArm, model.leftArm, model.head, true);
                }
                if (stack.isOf(WeaponRegistry.KRAKEN_SLAYER_CROSSBOW.get())) {
                    CrossbowPosing.hold(model.rightArm, model.leftArm, model.head, true);
                }
            }
        }*/
    }

    @Inject(at = @At("HEAD"), method = "animateArms")
    protected void animateArms(T entity, float animationProgress, CallbackInfo info) {
        /*if (FMLLoader.getLoadingModList().getModFileById("bettercombat") == null) {
            ItemStack stack = entity.getMainHandStack();
            if (model.handSwingProgress > 0.0f && !stack.isOf(WeaponRegistry.FROSTMOURNE.get()) && (stack.getItem() instanceof SoulHarvestingItem || customHoldItems.contains(stack.getItem()))) {
                ScythePosing.meleeAttack(model.leftArm, model.rightArm, entity, entity.handSwingProgress, animationProgress);
            }
        }*/
        if (entity instanceof AbstractClientPlayerEntity) {
            int frames = ClientParryData.getParryFrames();
            if (frames >= 1) {
                this.parryProgress = frames == 1 ? 0.1f : parryProgress;
                float added = (1f / (float) ParryData.MAX_PARRY_FRAMES) / 3f;//6
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
                modelPart.roll += MathHelper.sin(parryProgress * (float)Math.PI) * -0.4f; //0.4, 0.8
            }
        }
    }
}
