package net.soulsweaponry.datagen.tags;

import net.minecraft.data.DataOutput;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.Tags;
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
        this.getOrCreateTagBuilder(Tags.EntityTypes.BOSSES)
                .add(EntityRegistry.ACCURSED_LORD_BOSS.get())
                .add(EntityRegistry.DRAUGR_BOSS.get())
                .add(EntityRegistry.NIGHT_SHADE.get())
                .add(EntityRegistry.RETURNING_KNIGHT.get())
                .add(EntityRegistry.CHAOS_MONARCH.get())
                .add(EntityRegistry.MOONKNIGHT.get())
                .add(EntityRegistry.DAY_STALKER.get())
                .add(EntityRegistry.NIGHT_PROWLER.get());
        this.getOrCreateTagBuilder(ModTags.Entities.ARTHROPOD)
                .add(EntityType.SPIDER)
                .add(EntityType.CAVE_SPIDER)
                .add(EntityType.ENDERMITE)
                .add(EntityType.SILVERFISH)
                .add(EntityType.BEE);
        this.getOrCreateTagBuilder(ModTags.Entities.UNDEAD)
                .add(EntityType.DROWNED)
                .add(EntityType.GIANT)
                .add(EntityType.HUSK)
                .add(EntityType.PHANTOM)
                .add(EntityType.SKELETON)
                .add(EntityType.SKELETON_HORSE)
                .add(EntityType.STRAY)
                .add(EntityType.WITHER)
                .add(EntityType.WITHER_SKELETON)
                .add(EntityType.ZOGLIN)
                .add(EntityType.ZOMBIE)
                .add(EntityType.ZOMBIE_HORSE)
                .add(EntityType.ZOMBIE_VILLAGER)
                .add(EntityType.ZOMBIFIED_PIGLIN)
                .add(EntityRegistry.WITHERED_DEMON.get())
                .add(EntityRegistry.ACCURSED_LORD_BOSS.get())
                .add(EntityRegistry.DRAUGR_BOSS.get())
                .add(EntityRegistry.NIGHT_SHADE.get())
                .add(EntityRegistry.RETURNING_KNIGHT.get())
                .add(EntityRegistry.REMNANT.get())
                .add(EntityRegistry.DARK_SORCERER.get())
                .add(EntityRegistry.SOUL_REAPER_GHOST.get())
                .add(EntityRegistry.FORLORN.get())
                .add(EntityRegistry.EVIL_FORLORN.get())
                .add(EntityRegistry.SOULMASS.get())
                .add(EntityRegistry.CHAOS_MONARCH.get())
                .add(EntityRegistry.FROST_GIANT.get());
        this.getOrCreateTagBuilder(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                .add(EntityRegistry.FROST_GIANT.get())
                .add(EntityRegistry.RIME_SPECTRE.get());
        this.getOrCreateTagBuilder(ModTags.Entities.DRAGONS)
                .add(EntityType.ENDER_DRAGON)
                .addOptional(Identifier.of("iceandfire", "ice_dragon"))
                .addOptional(Identifier.of("iceandfire", "fire_dragon"))
                .addOptional(Identifier.of("iceandfire", "lightning_dragon"));
        this.getOrCreateTagBuilder(EntityTypeTags.ARROWS)
                .add(EntityRegistry.MOONLIGHT_ARROW.get())
                .add(EntityRegistry.SILVER_ARROW.get())
                .add(EntityRegistry.KRAKEN_SLAYER_PROJECTILE.get())
                .add(EntityRegistry.CHARGED_ARROW_ENTITY_TYPE.get());
        this.getOrCreateTagBuilder(EntityTypeTags.IMPACT_PROJECTILES)
                .add(EntityRegistry.MOONLIGHT_ARROW.get())
                .add(EntityRegistry.SILVER_ARROW.get())
                .add(EntityRegistry.KRAKEN_SLAYER_PROJECTILE.get())
                .add(EntityRegistry.CHARGED_ARROW_ENTITY_TYPE.get())
                .add(EntityRegistry.MOONLIGHT_ENTITY_TYPE.get())
                .add(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE.get())
                .add(EntityRegistry.DARK_MOON_PROJECTILE.get())
                .add(EntityRegistry.VERTICAL_MOONLIGHT_ENTITY_TYPE.get())
                .add(EntityRegistry.HORIZONTAL_MOONLIGHT_ENTITY_TYPE.get())
                .add(EntityRegistry.SUNLIGHT_PROJECTILE_SMALL.get())
                .add(EntityRegistry.SUNLIGHT_PROJECTILE_BIG.get())
                .add(EntityRegistry.VERTICAL_SUNLIGHT_PROJECTILE.get())
                .add(EntityRegistry.SWORDSPEAR_ENTITY_TYPE.get())
                .add(EntityRegistry.COMET_SPEAR_ENTITY_TYPE.get())
                .add(EntityRegistry.SILVER_BULLET_ENTITY_TYPE.get())
                .add(EntityRegistry.CANNONBALL.get())
                .add(EntityRegistry.LEVIATHAN_AXE_ENTITY_TYPE.get())
                .add(EntityRegistry.MJOLNIR_ENTITY_TYPE.get())
                .add(EntityRegistry.SHADOW_ORB.get())
                .add(EntityRegistry.DRAUPNIR_SPEAR_TYPE.get())
                .add(EntityRegistry.DRAGON_STAFF_PROJECTILE.get())
                .add(EntityRegistry.WITHERED_WABBAJACK_PROJECTILE.get())
                .add(EntityRegistry.CHAOS_SKULL.get())
                .add(EntityRegistry.GROWING_FIREBALL_ENTITY.get())
                .add(EntityRegistry.NIGHT_SKULL.get())
                .add(EntityRegistry.BLACKFLAME_SNAKE_ENTITY.get())
                .add(EntityRegistry.NO_DRAG_WITHER_SKULL.get())
                .add(EntityRegistry.NIGHTS_EDGE.get())
                .add(EntityRegistry.NIGHT_WAVE.get())
                .add(EntityRegistry.FLAME_PILLAR.get())
                .add(EntityRegistry.HOLY_MOONLIGHT_PILLAR.get())
                .add(EntityRegistry.GHOST_GLAIVE_TYPE.get())
                .add(EntityRegistry.MOONVEIL_HORIZONTAL.get())
                .add(EntityRegistry.MOONVEIL_VERTICAL.get())
                .add(EntityRegistry.BLACKFLAME_EXPLOSION_ENTITY.get())
                .add(EntityRegistry.MOLTEN_METAL.get())
                .add(EntityRegistry.CHUNGUS_HEAD.get());
    }
}
