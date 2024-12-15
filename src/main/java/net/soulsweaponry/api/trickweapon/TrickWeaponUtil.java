package net.soulsweaponry.api.trickweapon;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import org.jetbrains.annotations.Nullable;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrickWeaponUtil {

    public static Map<Identifier, Identifier> itemMappings = new HashMap<>();
    public static final String MAPPED_TRICK_WEAPON = "mapped_trick_weapon";

    //TODO håndterer overriding som hvordan tags har replace = false by default
   /* public static void loadMappings(ResourceManager manager) {
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

    //TODO test if the value = false or true overwrites or doesnt overwrite the main json file by modifying the testpack file (datapack)
    public static void loadMappings(ResourceManager manager) {
        try {
            Gson gson = new Gson();
            Identifier resourceIdentifier = new Identifier(SoulsWeaponry.ModId, "trickweapons/item_mappings.json");
            var resources = manager.getAllResources(resourceIdentifier);

            // Temporary storage for new mappings
            Map<Identifier, Identifier> newMappings = new HashMap<>();
            boolean replaceFlagEncountered = false;

            // Process resources in reverse order (higher-priority first)
            List<? extends Resource> resourceList = resources.stream().toList();
            for (int i = resourceList.size() - 1; i >= 0; i--) {
                var resource = resourceList.get(i);

                SoulsWeaponry.LOGGER.info("Processing resource: {}", resource.getPack().getName()); // Debug: Track resource source

                try (var stream = resource.getInputStream();
                     var reader = new InputStreamReader(stream)) {

                    Type topLevelType = new TypeToken<Map<String, Object>>() {}.getType();
                    Map<String, Object> rawJson = gson.fromJson(reader, topLevelType);

                    // Check the "replace" flag (default is false)
                    boolean replace = rawJson.getOrDefault("replace", false) instanceof Boolean && (boolean) rawJson.get("replace");
                    SoulsWeaponry.LOGGER.info("Resource replace flag: {} from {}", replace, resource.getPack().getName()); // Debug: Track replace flag

                    // If replace is true and we haven't encountered a replace flag yet, clear existing mappings
                    if (replace && !replaceFlagEncountered) {
                        SoulsWeaponry.LOGGER.info("Clearing existing mappings due to replace flag from {}", resource.getPack().getName());
                        itemMappings.clear();
                        replaceFlagEncountered = true;
                    }

                    // Get the "values" object, which contains the item mappings
                    Object valuesObject = rawJson.get("values");
                    if (valuesObject instanceof Map<?, ?> rawValuesMap) {
                        for (Map.Entry<?, ?> entry : rawValuesMap.entrySet()) {
                            if (entry.getKey() instanceof String && entry.getValue() instanceof String) {
                                Identifier key = Identifier.tryParse((String) entry.getKey());
                                Identifier value = Identifier.tryParse((String) entry.getValue());
                                if (key != null && value != null) {
                                    newMappings.put(key, value);
                                } else {
                                    SoulsWeaponry.LOGGER.warn("Invalid identifier in trick weapon item_mappings: {}", entry);
                                }
                            } else {
                                SoulsWeaponry.LOGGER.warn("Invalid entry in 'values' inside trick weapon item_mappings: {}", entry);
                            }
                        }
                    }
                } catch (Exception e) {
                    SoulsWeaponry.LOGGER.error("Failed to load trick weapon item mappings from resource.", e);
                }
            }

            // Merge the new mappings into the global mappings
            SoulsWeaponry.LOGGER.info("Merging {} new mappings into global mappings.", newMappings.size());
            itemMappings.putAll(newMappings);

        } catch (Exception e) {
            SoulsWeaponry.LOGGER.error("Failed to load trick weapon item_mappings.", e);
        }
    }

    public static void loadMappings(MinecraftServer server) {
        loadMappings(server.getResourceManager());
    }

    @Nullable
    public static Item getMappedItem(Item heldItem) {
        Identifier heldItemId = Registries.ITEM.getId(heldItem);
        Identifier itemId = itemMappings.get(heldItemId);
        if (itemId != null) {
            return Registries.ITEM.get(itemId);
        }
        return null;
    }

    @Nullable
    public static Text getMappedItemName(Item heldItem) {
        Item mappedItem = getMappedItem(heldItem);
        if (mappedItem != null) {
            return mappedItem.getName();
        }
        return null;
    }

    @Nullable
    public static ItemStack getMappedStack(ItemStack heldStack) {
        ItemStack stack = null;
        if (heldStack.hasNbt() && heldStack.getNbt().contains(MAPPED_TRICK_WEAPON)) {
            stack = Registries.ITEM.get(Identifier.tryParse(heldStack.getNbt().getString(MAPPED_TRICK_WEAPON))).getDefaultStack();
        } else {
            Item item = getMappedItem(heldStack.getItem());
            if (item != null) {
                stack = item.getDefaultStack();
            }
        }
        if (stack == null) {
            return null;
        }
        stack.setCount(heldStack.getCount());
        if (heldStack.hasNbt()) {
            stack.setNbt(heldStack.getNbt().copy());
        }
        stack.getOrCreateNbt().putString(TrickWeaponUtil.MAPPED_TRICK_WEAPON, Registries.ITEM.getId(heldStack.getItem()).toString());
        return stack;
    }
}
