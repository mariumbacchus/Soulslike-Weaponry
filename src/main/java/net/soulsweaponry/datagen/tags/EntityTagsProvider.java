package net.soulsweaponry.datagen.tags;

import net.minecraft.data.DataOutput;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeEntityTypeTagsProvider;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class EntityTagsProvider extends ForgeEntityTypeTagsProvider {

    public EntityTagsProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper);
    }

    @Override
    public void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(ModTags.Entities.RANGED_MOBS)
                .add(EntityType.SKELETON)
                .add(EntityType.BLAZE)
                .add(EntityType.GUARDIAN)
                .add(EntityType.EVOKER)
                .add(EntityType.ELDER_GUARDIAN)
                .add(EntityType.ILLUSIONER)
                .add(EntityType.SHULKER)
                .add(EntityType.SNOW_GOLEM)
                .add(EntityType.WITCH)
                .add(EntityType.WITHER);
        this.getOrCreateTagBuilder(ModTags.Entities.SKELETONS)
                .add(EntityType.SKELETON)
                .add(EntityType.WITHER_SKELETON)
                .add(EntityType.SKELETON_HORSE);
        this.getOrCreateTagBuilder(ModTags.Entities.BOSSES)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.WITHER)
                .add(EntityRegistry.ACCURSED_LORD_BOSS.get())
                .add(EntityRegistry.DRAUGR_BOSS.get())
                .add(EntityRegistry.NIGHT_SHADE.get())
                .add(EntityRegistry.RETURNING_KNIGHT.get())
                .add(EntityRegistry.CHAOS_MONARCH.get())
                .add(EntityRegistry.MOONKNIGHT.get())
                .add(EntityRegistry.DAY_STALKER.get())
                .add(EntityRegistry.NIGHT_PROWLER.get());
    }
}
