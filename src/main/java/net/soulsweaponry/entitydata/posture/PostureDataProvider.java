package net.soulsweaponry.entitydata.posture;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PostureDataProvider implements ICapabilityProvider, INBTSerializable<NbtCompound> {
    public static Capability<PostureData> POSTURE_DATA = CapabilityManager.get(new CapabilityToken<PostureData>() { });

    private PostureData postureData = null;
    private final LazyOptional<PostureData> optional = LazyOptional.of(this::createPostureData);

    private PostureData createPostureData() {
        if (this.postureData == null) {
            this.postureData = new PostureData();
        }
        return this.postureData;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        if (capability == POSTURE_DATA) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public NbtCompound serializeNBT() {
        NbtCompound nbt = new NbtCompound();
        createPostureData().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(NbtCompound nbt) {
        createPostureData().loadNBTData(nbt);
    }
}
