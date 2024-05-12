package net.soulsweaponry.entitydata;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {

    NbtCompound getPersistentData();
}