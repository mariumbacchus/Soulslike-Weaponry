package net.soulsweaponry.blocks.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.soulsweaponry.registry.BlockRegistry;

public class CrimsonObsidianBlockEntity extends BlockEntity {

    private int bloodCount = 0;

    public CrimsonObsidianBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.CRIMSON_OBSIDIAN_BLOCK_ENTITY, pos, state);
    }

    public void increaseBloodCount() {
        this.bloodCount++;
        this.markDirty();
    }

    public int getBloodCount() {
        return this.bloodCount;
    }

    public boolean dripBlood() {
        if (this.bloodCount - 5 > 0) {
            this.bloodCount -= 5;
            return true;
        }
        return false;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putInt("BloodCount", this.bloodCount);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("BloodCount")) {
            this.bloodCount = nbt.getInt("BloodCount");
        }
    }
}
