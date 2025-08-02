package net.soulsweaponry.mixin;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.soulsweaponry.registry.ItemRegistry;

@Mixin(WitherEntity.class)
public class WitherEntityMixin {
    
    @Inject(at = @At("TAIL"), method = "dropEquipment")
    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        WitherEntity wither = ((WitherEntity)(Object)this);
        ItemEntity[] drops = {wither.dropItem(ItemRegistry.LORD_SOUL_VOID), wither.dropItem(ItemRegistry.SHARD_OF_UNCERTAINTY)};
        for (ItemEntity entity : drops) {
            if (entity != null) {
                entity.setCovetedItem();
            }
        }
    }
}
