package net.soulsweaponry.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.soulsweaponry.util.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityDataSaverMixin implements IEntityDataSaver {

    private NbtCompound persistentData;

    @Override
    public NbtCompound getPersistentData() {
        if (this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void interceptWriteNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> info) {
        if (persistentData != null) {
            nbt.put("parry_data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void interceptReadNbt(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("parry_data")) {
            persistentData = nbt.getCompound("parry_data");
        }
    }
}
