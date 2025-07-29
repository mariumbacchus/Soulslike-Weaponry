package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ItemTagsProvider extends FabricTagProvider.ItemTagProvider {

    /**
     * Items in this list will be added to tags so that they are enchanted properly due to new enchanting system
     */
    public static final List<Item> SWORDS = new ArrayList<>();
    public static final List<Item> BOWS = new ArrayList<>();
    public static final List<Item> CROSSBOWS = new ArrayList<>();
    public static final List<Item> AXES = new ArrayList<>();

    public ItemTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(ModTags.Items.GUN_ENCHANTABLE)
                .add(GunRegistry.BLUNDERBUSS)
                .add(GunRegistry.GATLING_GUN)
                .add(GunRegistry.HUNTER_CANNON)
                .add(GunRegistry.HUNTER_PISTOL);

        this.getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(ItemRegistry.MOONSTONE_PICKAXE);

        this.getOrCreateTagBuilder(ItemTags.HOES)
                .add(ItemRegistry.MOONSTONE_HOE);

        this.getOrCreateTagBuilder(ItemTags.SHOVELS)
                .add(ItemRegistry.MOONSTONE_SHOVEL);

        this.getOrCreateTagBuilder(ItemTags.AXES)
                .add(AXES.toArray(Item[]::new))
                .add(ItemRegistry.MOONSTONE_AXE);

        this.getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(SWORDS.toArray(Item[]::new));

        this.getOrCreateTagBuilder(ItemTags.BOW_ENCHANTABLE)
                .add(BOWS.toArray(Item[]::new));

        this.getOrCreateTagBuilder(ItemTags.CROSSBOW_ENCHANTABLE)
                .add(CROSSBOWS.toArray(Item[]::new));
    }
}