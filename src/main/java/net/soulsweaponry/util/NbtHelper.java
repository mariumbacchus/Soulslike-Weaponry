package net.soulsweaponry.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class NbtHelper {

    public static NbtCompound serializeUUIDArray(UUID[] uuids) {
        NbtCompound tag = new NbtCompound();
        for (int i = 0; i < uuids.length; i++) {
            tag.putUuid("UUID" + i, uuids[i]);
        }
        return tag;
    }

    public static UUID[] deserializeUUIDArray(NbtCompound tag) {
        UUID[] uuids = new UUID[tag.getSize()];
        for (int i = 0; i < tag.getSize(); i++) {
            uuids[i] = tag.getUuid("UUID" + i);
        }
        return uuids;
    }

    public static void saveUUIDArr(NbtCompound tag, UUID[] uuids, String arrayId) {
        tag.put(arrayId, serializeUUIDArray(uuids));
    }

    /**
     * Get the saved UUID array in the given compound tag.
     * @param tag compound
     * @param arrayId id of the UUID array in the compound
     * @return the UUID array
     */
    public static UUID[] getUUIDArr(NbtCompound tag, String arrayId) {
        return deserializeUUIDArray(tag.getCompound(arrayId));
    }

    public static void saveUUIDArrToStack(ItemStack stack, UUID[] uuids, String arrayId) {
        NbtCompound tag = stack.getOrCreateNbt();
        tag.put(arrayId, serializeUUIDArray(uuids));
        stack.setNbt(tag);
    }

    public static UUID[] getUUIDArrFromStack(ItemStack stack, String arrayId) {
        NbtCompound tag = stack.getOrCreateNbt();
        return deserializeUUIDArray(tag.getCompound(arrayId));
    }

    /**
     * Add a specified UUID from the uuid list in the nbt
     * @param tag Compound
     * @param uuid UUID to remove
     * @param arrayId id of the UUID array in the compound
     */
    public static void addUUIDToArr(NbtCompound tag, UUID uuid, String arrayId) {
        UUID[] existingUUIDs = getUUIDArr(tag, arrayId);
        UUID[] newUUIDs = new UUID[existingUUIDs.length + 1];
        System.arraycopy(existingUUIDs, 0, newUUIDs, 0, existingUUIDs.length);
        newUUIDs[existingUUIDs.length] = uuid;
        saveUUIDArr(tag, newUUIDs, arrayId);
    }

    /**
     * Remove a specified UUID from the uuid list in the nbt
     * @param tag Compound
     * @param uuidToRemove UUID to remove
     * @param arrayId id of the UUID array in the compound
     */
    public static void removeUUIDFromArr(NbtCompound tag, UUID uuidToRemove, String arrayId) {
        UUID[] existingUUIDs = getUUIDArr(tag, arrayId);
        int newSize = existingUUIDs.length - 1;
        UUID[] newUUIDs = new UUID[newSize];
        int newIndex = 0;
        for (UUID uuid : existingUUIDs) {
            if (!uuid.equals(uuidToRemove)) {
                newUUIDs[newIndex] = uuid;
                newIndex++;
            }
        }
        saveUUIDArr(tag, newUUIDs, arrayId);
    }
}