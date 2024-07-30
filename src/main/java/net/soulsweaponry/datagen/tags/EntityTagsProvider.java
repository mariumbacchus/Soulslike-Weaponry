package net.soulsweaponry.datagen.tags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.EntityTypeTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.ModTags;

public class EntityTagsProvider extends EntityTypeTagProvider {

    public EntityTagsProvider(DataGenerator root, ExistingFileHelper helper) {
        super(root, SoulsWeaponry.ModId, helper);
    }

    @Override
    protected void configure() {
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
