package net.soulsweaponry.entitydata.summons;

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

public class SummonsDataProvider implements ICapabilityProvider, INBTSerializable<NbtCompound> {
    public static Capability<SummonsData> SUMMONS_DATA = CapabilityManager.get(new CapabilityToken<SummonsData>() { });

    private SummonsData summonsData = null;
    private final LazyOptional<SummonsData> optional = LazyOptional.of(this::createSummonsData);

    private SummonsData createSummonsData() {
        if (this.summonsData == null) {
            this.summonsData = new SummonsData();
        }
        return this.summonsData;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        if (capability == SUMMONS_DATA) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public NbtCompound serializeNBT() {
        NbtCompound nbt = new NbtCompound();
        //createSummonsData().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(NbtCompound nbt) {
        //createSummonsData().loadNBTData(nbt);
    }
}
