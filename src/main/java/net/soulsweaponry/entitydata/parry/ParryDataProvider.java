package net.soulsweaponry.entitydata.parry;

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

public class ParryDataProvider implements ICapabilityProvider, INBTSerializable<NbtCompound> {
    public static Capability<ParryData> PARRY_DATA = CapabilityManager.get(new CapabilityToken<ParryData>() { });

    private ParryData parryData = null;
    private final LazyOptional<ParryData> optional = LazyOptional.of(this::createParryData);

    private ParryData createParryData() {
        if (this.parryData == null) {
            this.parryData = new ParryData();
        }
        return this.parryData;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        if (capability == PARRY_DATA) {
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public NbtCompound serializeNBT() {
        NbtCompound nbt = new NbtCompound();
        createParryData().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(NbtCompound nbt) {
        createParryData().loadNBTData(nbt);
    }
}
