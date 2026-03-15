package net.soulsweaponry.util;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * Under is generic nbt helper methods to avoid the spam of .hasNbt() and .getNbt().contains(string)
     * and to look like the component system from 1.21.1 with fallbacks and stuff.
     */

    public static NbtCompound getTag(ItemStack stack) {
        return stack.getOrCreateNbt();
    }

    public static boolean has(ItemStack stack, String key) {
        return stack.hasNbt() && stack.getNbt().contains(key);
    }

    public static void remove(ItemStack stack, String key) {
        if (stack.hasNbt()) {
            stack.getNbt().remove(key);
        }
    }

    /** Ints */

    public static int getInt(ItemStack stack, String key, int fallback) {
        return has(stack, key) ? stack.getNbt().getInt(key) : fallback;
    }

    public static void putInt(ItemStack stack, String key, int value) {
        getTag(stack).putInt(key, value);
    }

    /** Floats */

    public static float getFloat(ItemStack stack, String key, float fallback) {
        return has(stack, key) ? stack.getNbt().getFloat(key) : fallback;
    }

    public static void putFloat(ItemStack stack, String key, float value) {
        getTag(stack).putFloat(key, value);
    }

    /** Doubles */

    public static double getDouble(ItemStack stack, String key, double fallback) {
        return has(stack, key) ? stack.getNbt().getDouble(key) : fallback;
    }

    public static void putDouble(ItemStack stack, String key, double value) {
        getTag(stack).putDouble(key, value);
    }

    /** Booleans */

    public static boolean getBoolean(ItemStack stack, String key, boolean fallback) {
        return has(stack, key) ? stack.getNbt().getBoolean(key) : fallback;
    }

    public static void putBoolean(ItemStack stack, String key, boolean value) {
        getTag(stack).putBoolean(key, value);
    }

    /** Strings */

    public static String getString(ItemStack stack, String key, String fallback) {
        return has(stack, key) ? stack.getNbt().getString(key) : fallback;
    }

    public static void putString(ItemStack stack, String key, String value) {
        getTag(stack).putString(key, value);
    }

    /** UUIDs */

    @Nullable
    public static UUID getUUID(ItemStack stack, String key) {
        return has(stack, key) ? stack.getNbt().getUuid(key) : null;
    }

    public static void putUUID(ItemStack stack, String key, UUID uuid) {
        getTag(stack).putUuid(key, uuid);
    }

    /** UUID Lists */

    public static List<UUID> getUUIDList(ItemStack stack, String key) {
        List<UUID> result = new ArrayList<>();
        if (!stack.hasNbt()) return result;

        NbtList list = stack.getNbt().getList(key, NbtElement.COMPOUND_TYPE);

        for (int i = 0; i < list.size(); i++) {
            result.add(list.getCompound(i).getUuid("UUID"));
        }

        return result;
    }

    public static void addUUID(ItemStack stack, String key, UUID uuid) {
        NbtCompound tag = getTag(stack);
        NbtList list = tag.getList(key, NbtElement.COMPOUND_TYPE);

        NbtCompound entry = new NbtCompound();
        entry.putUuid("UUID", uuid);

        list.add(entry);
        tag.put(key, list);
    }

    public static void removeUUID(ItemStack stack, String key, UUID uuid) {
        if (!stack.hasNbt()) return;

        NbtList list = stack.getNbt().getList(key, NbtElement.COMPOUND_TYPE);
        NbtList newList = new NbtList();

        for (int i = 0; i < list.size(); i++) {
            NbtCompound entry = list.getCompound(i);
            if (!entry.getUuid("UUID").equals(uuid)) {
                newList.add(entry);
            }
        }

        stack.getNbt().put(key, newList);
    }

    public static void clearList(ItemStack stack, String key) {
        remove(stack, key);
    }

    /** Entity storage */

    public static void saveEntityInListOnItem(ItemStack stack, Entity entity) {
        addUUID(stack, NbtIds.SAVED_ENTITY_UUID_LIST, entity.getUuid());
    }

    public static List<UUID> getSavedEntities(ItemStack stack) {
        return NbtHelper.getUUIDList(stack, NbtIds.SAVED_ENTITY_UUID_LIST);
    }

    public static void clearSavedEntities(ItemStack stack) {
        NbtHelper.clearList(stack, NbtIds.SAVED_ENTITY_UUID_LIST);
    }
}