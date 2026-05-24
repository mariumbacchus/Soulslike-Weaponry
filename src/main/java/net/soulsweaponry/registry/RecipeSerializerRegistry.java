package net.soulsweaponry.registry;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;

public class RecipeSerializerRegistry {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SoulsWeaponry.ModId);

    public static final RegistryObject<RecipeSerializer<ItemUpgradeRecipe>> SMITHING_ITEM_UPGRADE = RECIPE_SERIALIZERS.register("smithing_item_upgrade", ItemUpgradeRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}