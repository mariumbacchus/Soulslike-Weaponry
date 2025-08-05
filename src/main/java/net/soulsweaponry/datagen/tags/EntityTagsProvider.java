package net.soulsweaponry.datagen.tags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.Identifier;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.ModTags;

import java.util.concurrent.CompletableFuture;

public class EntityTagsProvider extends FabricTagProvider.EntityTypeTagProvider {

    public EntityTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
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
                .add(EntityRegistry.ACCURSED_LORD_BOSS)
                .add(EntityRegistry.DRAUGR_BOSS)
                .add(EntityRegistry.NIGHT_SHADE)
                .add(EntityRegistry.RETURNING_KNIGHT)
                .add(EntityRegistry.CHAOS_MONARCH)
                .add(EntityRegistry.MOONKNIGHT)
                .add(EntityRegistry.DAY_STALKER)
                .add(EntityRegistry.NIGHT_PROWLER);
        this.getOrCreateTagBuilder(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                .add(EntityRegistry.FROST_GIANT)
                .add(EntityRegistry.RIME_SPECTRE);
        this.getOrCreateTagBuilder(ModTags.Entities.DRAGONS)
                .add(EntityType.ENDER_DRAGON)
                .addOptional(Identifier.of("iceandfire", "ice_dragon"))
                .addOptional(Identifier.of("iceandfire", "fire_dragon"))
                .addOptional(Identifier.of("iceandfire", "lightning_dragon"));
        this.getOrCreateTagBuilder(EntityTypeTags.ARROWS)
                .add(EntityRegistry.MOONLIGHT_ARROW)
                .add(EntityRegistry.SILVER_ARROW)
                .add(EntityRegistry.KRAKEN_SLAYER_PROJECTILE)
                .add(EntityRegistry.CHARGED_ARROW_ENTITY_TYPE);
        this.getOrCreateTagBuilder(EntityTypeTags.IMPACT_PROJECTILES) //TODO see if custom arrows automatically are added or not (should be due to arrows tag being added in vanilla)
                .add(EntityRegistry.MOONLIGHT_ENTITY_TYPE)
                .add(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE)
                .add(EntityRegistry.DARK_MOON_PROJECTILE)
                .add(EntityRegistry.VERTICAL_MOONLIGHT_ENTITY_TYPE)
                .add(EntityRegistry.HORIZONTAL_MOONLIGHT_ENTITY_TYPE)
                .add(EntityRegistry.SUNLIGHT_PROJECTILE_SMALL)
                .add(EntityRegistry.SUNLIGHT_PROJECTILE_BIG)
                .add(EntityRegistry.VERTICAL_SUNLIGHT_PROJECTILE)
                .add(EntityRegistry.SWORDSPEAR_ENTITY_TYPE)
                .add(EntityRegistry.COMET_SPEAR_ENTITY_TYPE)
                .add(EntityRegistry.SILVER_BULLET_ENTITY_TYPE)
                .add(EntityRegistry.CANNONBALL)
                .add(EntityRegistry.LEVIATHAN_AXE_ENTITY_TYPE)
                .add(EntityRegistry.MJOLNIR_ENTITY_TYPE)
                .add(EntityRegistry.SHADOW_ORB)
                .add(EntityRegistry.DRAUPNIR_SPEAR_TYPE)
                .add(EntityRegistry.DRAGON_STAFF_PROJECTILE)
                .add(EntityRegistry.WITHERED_WABBAJACK_PROJECTILE)
                .add(EntityRegistry.CHAOS_SKULL)
                .add(EntityRegistry.GROWING_FIREBALL_ENTITY)
                .add(EntityRegistry.NIGHT_SKULL)
                .add(EntityRegistry.BLACKFLAME_SNAKE_ENTITY)
                .add(EntityRegistry.NO_DRAG_WITHER_SKULL)
                .add(EntityRegistry.NIGHTS_EDGE)
                .add(EntityRegistry.NIGHT_WAVE)
                .add(EntityRegistry.FLAME_PILLAR)
                .add(EntityRegistry.HOLY_MOONLIGHT_PILLAR)
                .add(EntityRegistry.GHOST_GLAIVE_TYPE)
                .add(EntityRegistry.MOONVEIL_HORIZONTAL)
                .add(EntityRegistry.MOONVEIL_VERTICAL)
                .add(EntityRegistry.BLACKFLAME_EXPLOSION_ENTITY)
                .add(EntityRegistry.MOLTEN_METAL)
                .add(EntityRegistry.CHUNGUS_HEAD);
        this.getOrCreateTagBuilder(EntityTypeTags.UNDEAD)
                .add(EntityRegistry.CHAOS_MONARCH)
                .add(EntityRegistry.DRAUGR_BOSS)
                .add(EntityRegistry.FROST_GIANT)
                .add(EntityRegistry.NIGHT_SHADE)
                .add(EntityRegistry.RETURNING_KNIGHT)
                .add(EntityRegistry.WITHERED_DEMON)
                .add(EntityRegistry.ACCURSED_LORD_BOSS);
    }
}