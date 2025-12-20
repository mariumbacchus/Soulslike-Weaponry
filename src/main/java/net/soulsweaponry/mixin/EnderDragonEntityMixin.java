package net.soulsweaponry.mixin;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.soulsweaponry.registry.ItemRegistry;

@Mixin(EnderDragonEntity.class)
public class EnderDragonEntityMixin {

    @Unique
    boolean canDropSoul = true;
    
    @Inject(at = @At("TAIL"), method = "updatePostDeath()V")
    protected void updatePostDeath(CallbackInfo info) {
        EnderDragonEntity enderDragon = (EnderDragonEntity) (Object) this;
        if (this.canDropSoul && enderDragon.getWorld() instanceof ServerWorld serverWorld) {
            ItemEntity soul = enderDragon.dropItem(serverWorld, ItemRegistry.LORD_SOUL_PURPLE);
            if (soul != null) {
                soul.setCovetedItem();
                this.canDropSoul = false;
            }
        }
    }
}
