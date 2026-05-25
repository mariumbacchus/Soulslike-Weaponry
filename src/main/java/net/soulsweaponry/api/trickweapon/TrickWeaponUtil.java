package net.soulsweaponry.api.trickweapon;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrickWeaponUtil {

    public static Map<Identifier, Identifier> itemMappings = new HashMap<>();
    public static final String MAPPED_TRICK_WEAPON = "mapped_trick_weapon";

    // Default method without "replace" value handling
    /*public static void loadMappings(ResourceManager manager) {
        try {
            Gson gson = new Gson();
            var resource = manager.getResource(new Identifier(SoulsWeaponry.ModId, "trickweapons/item_mappings.json"));
            if (resource.isPresent()) {
                var stream = resource.get().getInputStream();
                Type type = new TypeToken<Map<String, String>>() {}.getType();
                Map<String, String> rawMappings = gson.fromJson(new InputStreamReader(stream), type);
                itemMappings.clear();
                for (Map.Entry<String, String> entry : rawMappings.entrySet()) {
                    Identifier key = Identifier.tryParse(entry.getKey());
                    Identifier value = Identifier.tryParse(entry.getValue());
                    if (key != null && value != null) {
                        itemMappings.put(key, value);
                    } else {
                        SoulsWeaponry.LOGGER.warn("Invalid identifier in item mappings: {}", entry);
                    }
                }
            }
        } catch (Exception e) {
            SoulsWeaponry.LOGGER.error("Failed to load trick weapon item mappings.", e);
        }
    }*/

    public static void loadMappings(ResourceManager manager) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<Identifier, Identifier> newMappings = new HashMap<>();
            // Get all resources with the same path
            List<Resource> resources = manager.getAllResources(new Identifier(SoulsWeaponry.ModId, "trickweapons/item_mappings.json"));
            boolean shouldReplace = false;
            for (Resource resource : resources) {
                try (var stream = resource.getInputStream(); InputStreamReader reader = new InputStreamReader(stream)) {
                    Map<String, Object> rawJson = gson.fromJson(reader, type);
                    // Check if "replace" is specified and clear mappings if replace is true
                    Object replaceValue = rawJson.get("replace");
                    if (replaceValue instanceof Boolean && (Boolean) replaceValue) {
                        shouldReplace = true;
                        newMappings.clear();
                    }
                    for (Map.Entry<String, Object> entry : rawJson.entrySet()) {
                        if ("replace".equals(entry.getKey())) continue;
                        String key = entry.getKey();
                        String value = entry.getValue().toString();
                        Identifier keyId = Identifier.tryParse(key);
                        Identifier valueId = Identifier.tryParse(value);
                        if (keyId != null && valueId != null) {
                            newMappings.put(keyId, valueId);
                        } else {
                            SoulsWeaponry.LOGGER.warn("Invalid identifier in item mappings: {}", entry);
                        }
                    }
                }
            }
            if (shouldReplace) {
                itemMappings.clear();
            }
            itemMappings.putAll(newMappings);
        } catch (Exception e) {
            SoulsWeaponry.LOGGER.error("Failed to load trick weapon item mappings.", e);
        }
    }

    public static void loadMappings(MinecraftServer server) {
        loadMappings(server.getResourceManager());
    }

    @Nullable
    public static Item getMappedItem(Item heldItem) {
        Identifier heldItemId = ForgeRegistries.ITEMS.getKey(heldItem);
        Identifier itemId = itemMappings.get(heldItemId);
        if (itemId != null) {
            return ForgeRegistries.ITEMS.getValue(itemId);
        }
        return null;
    }

    @Nullable
    public static Item getMappedItem(ItemStack heldStack) {
        if (heldStack.hasNbt() && heldStack.getNbt().contains(MAPPED_TRICK_WEAPON)) {
            return ForgeRegistries.ITEMS.getValue(Identifier.tryParse(heldStack.getNbt().getString(MAPPED_TRICK_WEAPON)));
        } else {
            return getMappedItem(heldStack.getItem());
        }
    }

    @Nullable
    public static Text getMappedItemName(ItemStack heldItem) {
        Item mappedItem = getMappedItem(heldItem);
        if (mappedItem != null) {
            return mappedItem.getName();
        }
        return null;
    }

    @Nullable
    public static ItemStack getMappedStack(World world, ItemStack heldStack) {
        Item item = getMappedItem(heldStack);
        if (item == null) {
            return null;
        }
        ItemStack outStack = item.getDefaultStack();
        WeaponUtil.copyOverItemComponents(world, heldStack, outStack);
        outStack.getOrCreateNbt().putString(TrickWeaponUtil.MAPPED_TRICK_WEAPON, ForgeRegistries.ITEMS.getKey(heldStack.getItem()).toString());
        return outStack;
    }
}