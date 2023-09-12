package net.soulsweaponry.datagen.models;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.item.Item;
import net.soulsweaponry.mixin.ModelInvoker;

import java.util.HashMap;

// Due to bug regarding Valhelsia Core redirecting the mixin used in the model provider, the game crashes if both this
// mod and VC is installed, therefore, the generation of models here has been deprecated.
@Deprecated
public class ModelProvider extends FabricModelProvider {

    public static HashMap<Item, Model> ITEMS = new HashMap<>();
    public static Model SPAWN_EGG = ModelInvoker.invokeItemModelBuilder("template_spawn_egg");

    public ModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (Item item : ITEMS.keySet()) {
            itemModelGenerator.register(item, ITEMS.get(item));
        }
    }
}