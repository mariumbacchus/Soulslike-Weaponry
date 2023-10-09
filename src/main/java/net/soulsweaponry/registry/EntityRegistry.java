package net.soulsweaponry.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.AreaEffectSphere;
import net.soulsweaponry.entity.ai.goal.NightProwlerGoal;
import net.soulsweaponry.entity.mobs.*;
import net.soulsweaponry.entity.projectile.*;
import software.bernie.example.registry.EntityRegistryBuilder;

public class EntityRegistry {

    private static final String ModId = SoulsWeaponry.ModId;
    public static final ItemGroup MAIN_GROUP = SoulsWeaponry.MAIN_GROUP;
    
    public static final EntityType<WitheredDemon> WITHERED_DEMON = EntityRegistryBuilder.<WitheredDemon>createBuilder(new Identifier(ModId, "withered_demon")).category(SpawnGroup.MONSTER).entity(WitheredDemon::new).dimensions(EntityDimensions.changing(1F, 2F)).build();
    public static final EntityType<AccursedLordBoss> ACCURSED_LORD_BOSS = EntityRegistryBuilder.<AccursedLordBoss>createBuilder(new Identifier(ModId, "accursed_lord_boss")).category(SpawnGroup.MONSTER).entity(AccursedLordBoss::new).dimensions(EntityDimensions.changing(3F, 6F)).build();
    public static final EntityType<DraugrBoss> DRAUGR_BOSS = EntityRegistryBuilder.<DraugrBoss>createBuilder(new Identifier(ModId, "draugr_boss")).category(SpawnGroup.MONSTER).entity(DraugrBoss::new).dimensions(EntityDimensions.changing(1.5F, 3F)).build();
    public static final EntityType<NightShade> NIGHT_SHADE = EntityRegistryBuilder.<NightShade>createBuilder(new Identifier(ModId, "night_shade")).category(SpawnGroup.MONSTER).entity(NightShade::new).dimensions(EntityDimensions.changing(1.5F, 3.5F)).build();
    public static final EntityType<ReturningKnight> RETURNING_KNIGHT = EntityRegistryBuilder.<ReturningKnight>createBuilder(new Identifier(ModId, "returning_knight")).category(SpawnGroup.MONSTER).entity(ReturningKnight::new).dimensions(EntityDimensions.changing(3.0F, 8.0F)).build();
    public static final EntityType<Remnant> REMNANT = Registry.register(Registry.ENTITY_TYPE ,new Identifier(ModId, "remnant"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, Remnant::new).dimensions(EntityDimensions.fixed(1F, 1.75F)).build());
    public static final EntityType<DarkSorcerer> DARK_SORCERER = Registry.register(Registry.ENTITY_TYPE ,new Identifier(ModId, "dark_sorcerer"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, DarkSorcerer::new).dimensions(EntityDimensions.fixed(1F, 1.75F)).build());
    public static final EntityType<BigChungus> BIG_CHUNGUS = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "big_chungus"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BigChungus::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build());
    public static final EntityType<SoulReaperGhost> SOUL_REAPER_GHOST = Registry.register(Registry.ENTITY_TYPE ,new Identifier(ModId, "soul_reaper_ghost"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, SoulReaperGhost::new).dimensions(EntityDimensions.fixed(1F, 1.75F)).build());
    public static final EntityType<Forlorn> FORLORN = Registry.register(Registry.ENTITY_TYPE ,new Identifier(ModId, "forlorn"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, Forlorn::new).dimensions(EntityDimensions.fixed(1F, 1.75F)).build());
    public static final EntityType<EvilForlorn> EVIL_FORLORN = Registry.register(Registry.ENTITY_TYPE ,new Identifier(ModId, "evil_forlorn"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EvilForlorn::new).dimensions(EntityDimensions.fixed(1F, 1.75F)).build());
    public static final EntityType<Soulmass> SOULMASS = Registry.register(Registry.ENTITY_TYPE ,new Identifier(ModId, "soulmass"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, Soulmass::new).dimensions(EntityDimensions.fixed(2.7F, 3.5F)).build());
    public static final EntityType<ChaosMonarch> CHAOS_MONARCH = Registry.register(Registry.ENTITY_TYPE ,new Identifier(ModId, "chaos_monarch"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ChaosMonarch::new).dimensions(EntityDimensions.fixed(2.5F, 6F)).build());
    public static final EntityType<Moonknight> MOONKNIGHT = Registry.register(Registry.ENTITY_TYPE ,new Identifier(ModId, "moonknight"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, Moonknight::new).dimensions(EntityDimensions.fixed(3F, 8F)).build());
    public static final EntityType<FrostGiant> FROST_GIANT = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "frost_giant"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, FrostGiant::new).dimensions(EntityDimensions.fixed(1.25F, 2.6F)).build());
    public static final EntityType<RimeSpectre> RIME_SPECTRE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "rime_spectre"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RimeSpectre::new).dimensions(EntityDimensions.fixed(1F, 2F)).build());
    public static final EntityType<DayStalker> DAY_STALKER = registerWithSpawnEgg(FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DayStalker::new).dimensions(EntityDimensions.changing(3.5F, 5.5F)).build(), "day_stalker", 0x212121, 0xff8000);
    public static final EntityType<NightProwler> NIGHT_PROWLER = registerWithSpawnEgg(FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, NightProwler::new).dimensions(EntityDimensions.changing(3.5F, 5.5F)).build(), "night_prowler", 0x345385, 0xff177c);
    public static final EntityType<WarmthEntity> WARMTH_ENTITY = registerWithSpawnEgg(FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, WarmthEntity::new).dimensions(EntityDimensions.changing(1f, 1f)).build(), "warmth_entity", 0xdb8700, 0xfff7eb);

    public static final EntityType<MoonlightProjectile> MOONLIGHT_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "moonlight_projectile"), FabricEntityTypeBuilder.<MoonlightProjectile>create(SpawnGroup.MISC, MoonlightProjectile::new).dimensions(EntityDimensions.fixed(1F, 1F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<MoonlightProjectile> MOONLIGHT_BIG_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "big_moonlight_projectile"), FabricEntityTypeBuilder.<MoonlightProjectile>create(SpawnGroup.MISC, MoonlightProjectile::new).dimensions(EntityDimensions.fixed(2F, 1F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<MoonlightProjectile> VERTICAL_MOONLIGHT_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "vertical_moonlight_projectile"), FabricEntityTypeBuilder.<MoonlightProjectile>create(SpawnGroup.MISC, MoonlightProjectile::new).dimensions(EntityDimensions.fixed(1F, 3F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<MoonlightProjectile> SUNLIGHT_PROJECTILE_SMALL = registerEntity("sunlight_projectile_small", FabricEntityTypeBuilder.<MoonlightProjectile>create(SpawnGroup.MISC, MoonlightProjectile::new).dimensions(EntityDimensions.fixed(1F, 1F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<MoonlightProjectile> SUNLIGHT_PROJECTILE_BIG = registerEntity("sunlight_projectile_big", FabricEntityTypeBuilder.<MoonlightProjectile>create(SpawnGroup.MISC, MoonlightProjectile::new).dimensions(EntityDimensions.fixed(2F, 1F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<MoonlightProjectile> VERTICAL_SUNLIGHT_PROJECTILE = registerEntity("vertical_sunlight_projectile", FabricEntityTypeBuilder.<MoonlightProjectile>create(SpawnGroup.MISC, MoonlightProjectile::new).dimensions(EntityDimensions.fixed(1F, 3F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<DragonslayerSwordspearEntity> SWORDSPEAR_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "swordspear_entity"), FabricEntityTypeBuilder.<DragonslayerSwordspearEntity>create(SpawnGroup.MISC, DragonslayerSwordspearEntity::new).dimensions(EntityDimensions.fixed(1F, 1F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<CometSpearEntity> COMET_SPEAR_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "comet_spear_entity"), FabricEntityTypeBuilder.<CometSpearEntity>create(SpawnGroup.MISC, CometSpearEntity::new).dimensions(EntityDimensions.fixed(1F, 1F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<ChargedArrow> CHARGED_ARROW_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "charged_arrow_entity"), FabricEntityTypeBuilder.<ChargedArrow>create(SpawnGroup.MISC, ChargedArrow::new).dimensions(EntityDimensions.fixed(1F, 1F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<SilverBulletEntity> SILVER_BULLET_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "silver_bullet_entity"), FabricEntityTypeBuilder.<SilverBulletEntity>create(SpawnGroup.MISC, SilverBulletEntity::new).dimensions(EntityDimensions.fixed(0.5F, 0.5F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<Cannonball> CANNONBALL = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "cannonball_entity_type"), FabricEntityTypeBuilder.<Cannonball>create(SpawnGroup.MISC, Cannonball::new).dimensions(EntityDimensions.fixed(0.5F, 0.5F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<LeviathanAxeEntity> LEVIATHAN_AXE_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "leviathan_axe_entity"), FabricEntityTypeBuilder.<LeviathanAxeEntity>create(SpawnGroup.MISC, LeviathanAxeEntity::new).dimensions(EntityDimensions.fixed(.5F, 1F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<MjolnirProjectile> MJOLNIR_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "mjolnir_entity"), FabricEntityTypeBuilder.<MjolnirProjectile>create(SpawnGroup.MISC, MjolnirProjectile::new).dimensions(EntityDimensions.fixed(.75F, .75F)).trackRangeChunks(4).trackedUpdateRate(20).build());
    public static final EntityType<FreyrSwordEntity> FREYR_SWORD_ENTITY_TYPE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "freyr_sword_entity"), FabricEntityTypeBuilder.<FreyrSwordEntity>create(SpawnGroup.MISC, FreyrSwordEntity::new).dimensions(EntityDimensions.fixed(.3F, 2F)).build());
    public static final EntityType<ShadowOrb> SHADOW_ORB = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "shadow_orb_entity"), FabricEntityTypeBuilder.<ShadowOrb>create(SpawnGroup.MISC, ShadowOrb::new).dimensions(EntityDimensions.fixed(0.5F, 0.5F)).trackRangeChunks(4).trackedUpdateRate(30).build());
    public static final EntityType<DraupnirSpearEntity> DRAUPNIR_SPEAR_TYPE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "draupnir_spear_entity"), FabricEntityTypeBuilder.<DraupnirSpearEntity>create(SpawnGroup.MISC, DraupnirSpearEntity::new).dimensions(EntityDimensions.fixed(1F, 1F)).trackRangeChunks(4).trackedUpdateRate(30).build());
    public static final EntityType<AreaEffectSphere> AREA_EFFECT_SPHERE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "area_effect_sphere"), FabricEntityTypeBuilder.<AreaEffectSphere>create(SpawnGroup.MISC, AreaEffectSphere::new).dimensions(EntityDimensions.changing(3F, 3F)).fireImmune().trackRangeChunks(4).trackedUpdateRate(Integer.MAX_VALUE).build());
    public static final EntityType<DragonStaffProjectile> DRAGON_STAFF_PROJECTILE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "dragon_staff_projectile"), FabricEntityTypeBuilder.<DragonStaffProjectile>create(SpawnGroup.MISC, DragonStaffProjectile::new).dimensions(EntityDimensions.fixed(1F, 1F)).trackRangeChunks(4).trackedUpdateRate(10).build());
    public static final EntityType<WitheredWabbajackProjectile> WITHERED_WABBAJACK_PROJECTILE = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "withered_wabbajack_projectile"), FabricEntityTypeBuilder.<WitheredWabbajackProjectile>create(SpawnGroup.MISC, WitheredWabbajackProjectile::new).dimensions(EntityDimensions.fixed(0.3125f, 0.3125f)).trackRangeChunks(4).trackedUpdateRate(10).build());
    public static final EntityType<ChaosSkull> CHAOS_SKULL = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "chaos_skull"), FabricEntityTypeBuilder.<ChaosSkull>create(SpawnGroup.MISC, ChaosSkull::new).dimensions(EntityDimensions.fixed(0.4f, 0.4f)).trackRangeChunks(4).trackedUpdateRate(10).build());
    public static final EntityType<ChaosOrbEntity> CHAOS_ORB_ENTITY = Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, "chaos_orb_entity"), FabricEntityTypeBuilder.<ChaosOrbEntity>create(SpawnGroup.MISC, ChaosOrbEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).trackRangeChunks(4).trackedUpdateRate(10).build());
    public static final EntityType<GrowingFireball> GROWING_FIREBALL_ENTITY = registerEntity("growing_fireball", FabricEntityTypeBuilder.<GrowingFireball>create(SpawnGroup.MISC, GrowingFireball::new).dimensions(EntityDimensions.changing(0.5f, 0.5f)).trackRangeChunks(10).trackedUpdateRate(20).build());
    public static final EntityType<NightSkull> NIGHT_SKULL = registerEntity("night_skull", FabricEntityTypeBuilder.create(SpawnGroup.MISC, NightSkull::new).dimensions(EntityDimensions.changing(0.5f, 0.75f)).trackRangeChunks(15).trackedUpdateRate(20).build());
    public static final EntityType<FogEntity> FOG_ENTITY = registerEntity("fog_entity", FabricEntityTypeBuilder.create(SpawnGroup.MISC, FogEntity::new).dimensions(EntityDimensions.changing(8f, 3f)).fireImmune().trackRangeChunks(4).trackedUpdateRate(Integer.MAX_VALUE).build());
    public static final EntityType<BlackflameSnakeEntity> BLACKFLAME_SNAKE_ENTITY = registerEntity("blackflame_snake_entity", FabricEntityTypeBuilder.create(SpawnGroup.MISC, BlackflameSnakeEntity::new).dimensions(EntityDimensions.fixed(2f, 2f)).fireImmune().trackRangeChunks(4).trackedUpdateRate(Integer.MAX_VALUE).build());
    public static final EntityType<NoDragWitherSkull> NO_DRAG_WITHER_SKULL = registerEntity("no_drag_wither_skull", FabricEntityTypeBuilder.create(SpawnGroup.MISC, NoDragWitherSkull::new).dimensions(EntityDimensions.fixed(0.3125f, 0.3125f)).trackRangeChunks(15).trackedUpdateRate(20).build());
    public static final EntityType<NightProwlerGoal.DeathSpiralEntity> DEATH_SPIRAL_ENTITY = registerEntity("death_spiral", FabricEntityTypeBuilder.create(SpawnGroup.MISC, NightProwlerGoal.DeathSpiralEntity::new).dimensions(EntityDimensions.fixed(2f, 2f)).fireImmune().trackRangeChunks(4).trackedUpdateRate(Integer.MAX_VALUE).build());
    public static final EntityType<NightsEdge> NIGHTS_EDGE = registerEntity("nights_edge", FabricEntityTypeBuilder.create(SpawnGroup.MISC, NightsEdge::new).dimensions(EntityDimensions.fixed(0.75f, 2f)).trackRangeChunks(6).trackedUpdateRate(Integer.MAX_VALUE).build());
    public static final EntityType<NightWaveEntity> NIGHT_WAVE = registerEntity("night_wave", FabricEntityTypeBuilder.create(SpawnGroup.MISC, NightWaveEntity::new).dimensions(EntityDimensions.fixed(3.5f, 1f)).fireImmune().trackRangeChunks(4).trackedUpdateRate(Integer.MAX_VALUE).build());
    public static final EntityType<FlamePillar> FLAME_PILLAR = registerEntity("flame_pillar", FabricEntityTypeBuilder.create(SpawnGroup.MISC, FlamePillar::new).dimensions(EntityDimensions.fixed(1.5f, 1.5f)).fireImmune().trackRangeChunks(4).trackedUpdateRate(Integer.MAX_VALUE).build());

    public static final Item WITHERED_DEMON_SPAWN_EGG = new SpawnEggItem(WITHERED_DEMON, 10027008, 0, new Item.Settings().group(MAIN_GROUP));
    public static final Item ACCURSED_LORD_BOSS_SPAWN_EGG = new SpawnEggItem(ACCURSED_LORD_BOSS, 0, 10027008, new Item.Settings().group(MAIN_GROUP));
    public static final Item DRAUGR_BOSS_SPAWN_EGG = new SpawnEggItem(DRAUGR_BOSS, 10263708, 7694143, new Item.Settings().group(MAIN_GROUP));
    public static final Item NIGHT_SHADE_SPAWN_EGG = new SpawnEggItem(NIGHT_SHADE, 398638, 16576575, new Item.Settings().group(MAIN_GROUP));
    public static final Item RETURNING_KNIGHT_SPAWN_EGG = new SpawnEggItem(RETURNING_KNIGHT, 2251096, 6554982, new Item.Settings().group(MAIN_GROUP));
    public static final Item REMNANT_SPAWN_EGG = new SpawnEggItem(REMNANT, 6447971, 65514, new Item.Settings().group(MAIN_GROUP));
    public static final Item DARK_SORCERER_SPAWN_EGG = new SpawnEggItem(DARK_SORCERER, 0, 2572343, new Item.Settings().group(MAIN_GROUP));
    public static final Item BIG_CHUNGUS_SPAWN_EGG = new SpawnEggItem(BIG_CHUNGUS, 12636653, 0, new Item.Settings().group(MAIN_GROUP));
    public static final Item SOUL_REAPER_GHOST_SPAWN_EGG = new SpawnEggItem(SOUL_REAPER_GHOST, 13480150, 13200614, new Item.Settings().group(MAIN_GROUP));
    public static final Item FORLORN_SPAWN_EGG = new SpawnEggItem(FORLORN,4859716,5701896, new Item.Settings().group(MAIN_GROUP));
    public static final Item EVIL_FORLORN_SPAWN_EGG = new SpawnEggItem(EVIL_FORLORN, 5701896, 4859716, new Item.Settings().group(MAIN_GROUP));
    public static final Item SOULMASS_SPAWN_EGG = new SpawnEggItem(SOULMASS, 4494266, 9658504, new Item.Settings().group(MAIN_GROUP));
    public static final Item CHAOS_MONARCH_SPAWN_EGG = new SpawnEggItem(CHAOS_MONARCH, 4325468, 0, new Item.Settings().group(MAIN_GROUP));
    public static final Item MOONKNIGHT_SPAWN_EGG = new SpawnEggItem(MOONKNIGHT, 13357520, 390585, new Item.Settings().group(MAIN_GROUP));
    public static final Item FROST_GIANT_SPAWN_EGG = new SpawnEggItem(FROST_GIANT, 0x02523f, 0x46dffa, new Item.Settings().group(MAIN_GROUP));
    public static final Item RIME_SPECTRE_SPAWN_EGG = new SpawnEggItem(RIME_SPECTRE, 0x6ae6fc, 0x064854, new Item.Settings().group(MAIN_GROUP));

    public static void init() {
        FabricDefaultAttributeRegistry.register(WITHERED_DEMON, WitheredDemon.createDemonAttributes());
        FabricDefaultAttributeRegistry.register(ACCURSED_LORD_BOSS, AccursedLordBoss.createDemonAttributes());
        FabricDefaultAttributeRegistry.register(DRAUGR_BOSS, DraugrBoss.createBossAttributes());
        FabricDefaultAttributeRegistry.register(NIGHT_SHADE, NightShade.createBossAttributes());
        FabricDefaultAttributeRegistry.register(RETURNING_KNIGHT, ReturningKnight.createBossAttributes());
        FabricDefaultAttributeRegistry.register(BIG_CHUNGUS, BigChungus.createChungusAttributes());
        FabricDefaultAttributeRegistry.register(REMNANT, Remnant.createRemnantAttributes());
        FabricDefaultAttributeRegistry.register(DARK_SORCERER, DarkSorcerer.createSorcererAttributes());
        FabricDefaultAttributeRegistry.register(SOUL_REAPER_GHOST, SoulReaperGhost.createRemnantAttributes());
        FabricDefaultAttributeRegistry.register(FORLORN, Forlorn.createForlornAttributes());
        FabricDefaultAttributeRegistry.register(EVIL_FORLORN, EvilForlorn.createForlornAttributes());
        FabricDefaultAttributeRegistry.register(SOULMASS, Soulmass.createSoulmassAttributes());
        FabricDefaultAttributeRegistry.register(CHAOS_MONARCH, ChaosMonarch.createBossAttributes());
        FabricDefaultAttributeRegistry.register(FREYR_SWORD_ENTITY_TYPE, FreyrSwordEntity.createEntityAttributes());
        FabricDefaultAttributeRegistry.register(MOONKNIGHT, Moonknight.createBossAttributes());
        FabricDefaultAttributeRegistry.register(FROST_GIANT, FrostGiant.createGiantAttributes());
        FabricDefaultAttributeRegistry.register(RIME_SPECTRE, RimeSpectre.createSpectreAttributes());
        FabricDefaultAttributeRegistry.register(WARMTH_ENTITY, WarmthEntity.createEntityAttributes());
        FabricDefaultAttributeRegistry.register(DAY_STALKER, DayStalker.createBossAttributes());
        FabricDefaultAttributeRegistry.register(NIGHT_PROWLER, NightProwler.createBossAttributes());
        FabricDefaultAttributeRegistry.register(NIGHTS_EDGE, NightsEdge.createAttributes());

        ItemRegistry.registerItem(WITHERED_DEMON_SPAWN_EGG, "withered_demon_spawn_egg");
        ItemRegistry.registerItem(ACCURSED_LORD_BOSS_SPAWN_EGG, "accursed_lord_boss_spawn_egg");
        ItemRegistry.registerItem(DRAUGR_BOSS_SPAWN_EGG, "draugr_boss_spawn_egg");
        ItemRegistry.registerItem(NIGHT_SHADE_SPAWN_EGG, "night_shade_spawn_egg");
        ItemRegistry.registerItem(RETURNING_KNIGHT_SPAWN_EGG, "returning_knight_spawn_egg");
        ItemRegistry.registerItem(BIG_CHUNGUS_SPAWN_EGG, "big_chungus_spawn_egg");
        ItemRegistry.registerItem(REMNANT_SPAWN_EGG, "remnant_spawn_egg");
        ItemRegistry.registerItem(DARK_SORCERER_SPAWN_EGG, "dark_sorcerer_spawn_egg");
        ItemRegistry.registerItem(SOUL_REAPER_GHOST_SPAWN_EGG, "soul_reaper_ghost_spawn_egg");
        ItemRegistry.registerItem(FORLORN_SPAWN_EGG, "forlorn_spawn_egg");
        ItemRegistry.registerItem(EVIL_FORLORN_SPAWN_EGG, "evil_forlorn_spawn_egg");
        ItemRegistry.registerItem(SOULMASS_SPAWN_EGG, "soulmass_spawn_egg");
        ItemRegistry.registerItem(CHAOS_MONARCH_SPAWN_EGG, "chaos_monarch_spawn_egg");
        ItemRegistry.registerItem(MOONKNIGHT_SPAWN_EGG, "moonknight_spawn_egg");
        ItemRegistry.registerItem(FROST_GIANT_SPAWN_EGG, "frost_giant_spawn_egg");
        ItemRegistry.registerItem(RIME_SPECTRE_SPAWN_EGG, "rime_spectre_spawn_egg");
    }

    private static <I extends PathAwareEntity> EntityType<I> registerWithSpawnEgg(EntityType<I> type, String id, int primaryColor, int secondaryColor) {
        Item egg = new SpawnEggItem(type, primaryColor, secondaryColor, new Item.Settings().group(MAIN_GROUP));
        ItemRegistry.registerItem(egg, id + "_spawn_egg");
        /*
         * Due to bug regarding Valhelsia Core redirecting the mixin used in the model provider, the generation of model jsons has been deprecated.
         * ModelProvider.ITEMS.put(egg, ModelProvider.SPAWN_EGG);
         */
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, id), type);
    }

    private static <E extends Entity> EntityType<E> registerEntity(String id, EntityType<E> entity) {
        return Registry.register(Registry.ENTITY_TYPE, new Identifier(ModId, id), entity);
    }
}
