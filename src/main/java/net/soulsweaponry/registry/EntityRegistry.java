package net.soulsweaponry.registry;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.datagen.DatagenUtil;
import net.soulsweaponry.datagen.loot_tables.EntityLootTablesProvider;
import net.soulsweaponry.entity.AreaEffectSphere;
import net.soulsweaponry.entity.ai.goal.NightProwlerGoal;
import net.soulsweaponry.entity.mobs.*;
import net.soulsweaponry.entity.mobs.boss.*;
import net.soulsweaponry.entity.projectile.*;
import net.soulsweaponry.entity.projectile.arrow.ChargedArrow;
import net.soulsweaponry.entity.projectile.arrow.MoonlightArrow;
import net.soulsweaponry.entity.projectile.arrow.SilverArrow;
import net.soulsweaponry.entity.projectile.arrow.TrueDamageArrow;
import net.soulsweaponry.entity.projectile.noclip.*;

public class EntityRegistry {

    private static final String ModId = SoulsWeaponry.ModId;
    public static final EntityType<WitheredDemon> WITHERED_DEMON = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "withered_demon"), EntityType.Builder.create(WitheredDemon::new, SpawnGroup.MONSTER).dimensions(0.9f, 2f).build());
    public static final EntityType<AccursedLordBoss> ACCURSED_LORD_BOSS = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "accursed_lord_boss"), EntityType.Builder.create(AccursedLordBoss::new, SpawnGroup.MONSTER).dimensions(3F, 6F).build());
    public static final EntityType<DraugrBoss> DRAUGR_BOSS = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "draugr_boss"), EntityType.Builder.create(DraugrBoss::new, SpawnGroup.MONSTER).dimensions(1.5F, 3F).build());
    public static final EntityType<NightShade> NIGHT_SHADE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "night_shade"), EntityType.Builder.create(NightShade::new, SpawnGroup.MONSTER).dimensions(1.5F, 3.5F).build());
    public static final EntityType<ReturningKnight> RETURNING_KNIGHT = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "returning_knight"), EntityType.Builder.create(ReturningKnight::new, SpawnGroup.MONSTER).dimensions(3F, 8F).build());
    public static final EntityType<Remnant> REMNANT = Registry.register(Registries.ENTITY_TYPE ,Identifier.of(ModId, "remnant"), EntityType.Builder.create(Remnant::new, SpawnGroup.CREATURE).dimensions(0.7f, 1.95F).build()); // 0.6F, 1.95F (zombie hitbox)
    public static final EntityType<DarkSorcerer> DARK_SORCERER = Registry.register(Registries.ENTITY_TYPE ,Identifier.of(ModId, "dark_sorcerer"), EntityType.Builder.create(DarkSorcerer::new, SpawnGroup.CREATURE).dimensions(0.7F, 1.95F).build());
    public static final EntityType<BigChungus> BIG_CHUNGUS = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "big_chungus"), EntityType.Builder.create(BigChungus::new, SpawnGroup.MONSTER).dimensions(0.75f, 1f).build());
    public static final EntityType<SoulReaperGhost> SOUL_REAPER_GHOST = Registry.register(Registries.ENTITY_TYPE ,Identifier.of(ModId, "soul_reaper_ghost"), EntityType.Builder.create(SoulReaperGhost::new, SpawnGroup.CREATURE).dimensions(0.7F, 1.95F).build());
    public static final EntityType<Forlorn> FORLORN = Registry.register(Registries.ENTITY_TYPE ,Identifier.of(ModId, "forlorn"), EntityType.Builder.create(Forlorn::new, SpawnGroup.CREATURE).dimensions(0.7F, 1.95F).build());
    public static final EntityType<EvilForlorn> EVIL_FORLORN = Registry.register(Registries.ENTITY_TYPE ,Identifier.of(ModId, "evil_forlorn"), EntityType.Builder.create(EvilForlorn::new, SpawnGroup.MONSTER).dimensions(0.7F, 1.95F).build());
    public static final EntityType<Soulmass> SOULMASS = Registry.register(Registries.ENTITY_TYPE ,Identifier.of(ModId, "soulmass"), EntityType.Builder.create(Soulmass::new, SpawnGroup.CREATURE).dimensions(2.7F, 3.5F).build());
    public static final EntityType<ChaosMonarch> CHAOS_MONARCH = Registry.register(Registries.ENTITY_TYPE ,Identifier.of(ModId, "chaos_monarch"), EntityType.Builder.create(ChaosMonarch::new, SpawnGroup.MONSTER).dimensions(2.5F, 6F).build());
    public static final EntityType<Moonknight> MOONKNIGHT = Registry.register(Registries.ENTITY_TYPE ,Identifier.of(ModId, "moonknight"), EntityType.Builder.create(Moonknight::new, SpawnGroup.MONSTER).dimensions(3F, 8F).build());
    public static final EntityType<FrostGiant> FROST_GIANT = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "frost_giant"), EntityType.Builder.create(FrostGiant::new, SpawnGroup.MONSTER).dimensions(1.25F, 2.6F).build());
    public static final EntityType<RimeSpectre> RIME_SPECTRE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "rime_spectre"), EntityType.Builder.create(RimeSpectre::new, SpawnGroup.MONSTER).dimensions(1F, 2F).build());
    public static final EntityType<DayStalker> DAY_STALKER = registerWithSpawnEgg(EntityType.Builder.create(DayStalker::new, SpawnGroup.MONSTER).dimensions(3.5F, 5.5F).build(), "day_stalker", 0x212121, 0xff8000);
    public static final EntityType<NightProwler> NIGHT_PROWLER = registerWithSpawnEgg(EntityType.Builder.create(NightProwler::new, SpawnGroup.MONSTER).dimensions(3.5F, 5.5F).build(), "night_prowler", 0x345385, 0xff177c);
    public static final EntityType<WarmthEntity> WARMTH_ENTITY = registerWithSpawnEgg(EntityType.Builder.create(WarmthEntity::new, SpawnGroup.MONSTER).dimensions(1f, 1f).build(), "warmth_entity", 0xdb8700, 0xfff7eb);

    public static final EntityType<MoonlightProjectile> MOONLIGHT_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "moonlight_projectile"), EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).dimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<MoonlightProjectile> MOONLIGHT_BIG_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "big_moonlight_projectile"), EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).dimensions(2F, 1F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<MoonlightProjectile> DARK_MOON_PROJECTILE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "dark_moon_projectile"), EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).dimensions(2F, 1F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<MoonlightProjectile> VERTICAL_MOONLIGHT_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "vertical_moonlight_projectile"), EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).dimensions(1F, 3F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<MoonlightProjectile> HORIZONTAL_MOONLIGHT_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "horizontal_moonlight_projectile"), EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).dimensions(3F, 1F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<MoonlightProjectile> SUNLIGHT_PROJECTILE_SMALL = registerEntity("sunlight_projectile_small", EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).dimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<MoonlightProjectile> SUNLIGHT_PROJECTILE_BIG = registerEntity("sunlight_projectile_big", EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).dimensions(2F, 1F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<MoonlightProjectile> VERTICAL_SUNLIGHT_PROJECTILE = registerEntity("vertical_sunlight_projectile", EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).dimensions(1F, 3F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<DragonslayerSwordspearEntity> SWORDSPEAR_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "swordspear_entity"), EntityType.Builder.<DragonslayerSwordspearEntity>create(DragonslayerSwordspearEntity::new, SpawnGroup.MISC).dimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<CometSpearEntity> COMET_SPEAR_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "comet_spear_entity"), EntityType.Builder.<CometSpearEntity>create(CometSpearEntity::new, SpawnGroup.MISC).dimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<ChargedArrow> CHARGED_ARROW_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "charged_arrow_entity"), EntityType.Builder.<ChargedArrow>create(ChargedArrow::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<SilverBulletEntity> SILVER_BULLET_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "silver_bullet_entity"), EntityType.Builder.<SilverBulletEntity>create(SilverBulletEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<Cannonball> CANNONBALL = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "cannonball_entity_type"), EntityType.Builder.<Cannonball>create(Cannonball::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<LeviathanAxeEntity> LEVIATHAN_AXE_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "leviathan_axe_entity"), EntityType.Builder.<LeviathanAxeEntity>create(LeviathanAxeEntity::new, SpawnGroup.MISC).dimensions(.5F, 1F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<MjolnirProjectile> MJOLNIR_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "mjolnir_entity"), EntityType.Builder.<MjolnirProjectile>create(MjolnirProjectile::new, SpawnGroup.MISC).dimensions(.75F, .75F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<FreyrSwordEntity> FREYR_SWORD_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "freyr_sword_entity"), EntityType.Builder.<FreyrSwordEntity>create(FreyrSwordEntity::new, SpawnGroup.MISC).dimensions(.3F, 2F).build());
    public static final EntityType<ShadowOrb> SHADOW_ORB = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "shadow_orb_entity"), EntityType.Builder.<ShadowOrb>create(ShadowOrb::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(30).build());
    public static final EntityType<DraupnirSpearEntity> DRAUPNIR_SPEAR_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "draupnir_spear_entity"), EntityType.Builder.<DraupnirSpearEntity>create(DraupnirSpearEntity::new, SpawnGroup.MISC).dimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(30).build());
    public static final EntityType<AreaEffectSphere> AREA_EFFECT_SPHERE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "area_effect_sphere"), EntityType.Builder.<AreaEffectSphere>create(AreaEffectSphere::new, SpawnGroup.MISC).dimensions(3F, 3F).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build());
    public static final EntityType<DragonStaffProjectile> DRAGON_STAFF_PROJECTILE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "dragon_staff_projectile"), EntityType.Builder.<DragonStaffProjectile>create(DragonStaffProjectile::new, SpawnGroup.MISC).dimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(10).build());
    public static final EntityType<WitheredWabbajackProjectile> WITHERED_WABBAJACK_PROJECTILE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "withered_wabbajack_projectile"), EntityType.Builder.<WitheredWabbajackProjectile>create(WitheredWabbajackProjectile::new, SpawnGroup.MISC).dimensions(0.3125f, 0.3125f).maxTrackingRange(4).trackingTickInterval(10).build());
    public static final EntityType<ChaosSkull> CHAOS_SKULL = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "chaos_skull"), EntityType.Builder.<ChaosSkull>create(ChaosSkull::new, SpawnGroup.MISC).dimensions(0.4f, 0.4f).maxTrackingRange(4).trackingTickInterval(10).build());
    public static final EntityType<ChaosOrbEntity> CHAOS_ORB_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "chaos_orb_entity"), EntityType.Builder.<ChaosOrbEntity>create(ChaosOrbEntity::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(10).build());
    public static final EntityType<GrowingFireball> GROWING_FIREBALL_ENTITY = registerEntity("growing_fireball", EntityType.Builder.<GrowingFireball>create(GrowingFireball::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(20).build());
    public static final EntityType<NightSkull> NIGHT_SKULL = registerEntity("night_skull", EntityType.Builder.<NightSkull>create(NightSkull::new, SpawnGroup.MISC).dimensions(0.75f, 0.9f).maxTrackingRange(15).trackingTickInterval(20).build());
    public static final EntityType<FogEntity> FOG_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "fog_entity"), EntityType.Builder.create(FogEntity::new, SpawnGroup.MISC).dimensions(8f, 3f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build());
    public static final EntityType<BlackflameSnakeEntity> BLACKFLAME_SNAKE_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "blackflame_snake_entity"), EntityType.Builder.<BlackflameSnakeEntity>create(BlackflameSnakeEntity::new, SpawnGroup.MISC).dimensions(2f, 2f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build());
    public static final EntityType<NoDragWitherSkull> NO_DRAG_WITHER_SKULL = registerEntity("no_drag_wither_skull", EntityType.Builder.create(NoDragWitherSkull::new, SpawnGroup.MISC).dimensions(0.3125f, 0.3125f).maxTrackingRange(15).trackingTickInterval(20).build());
    public static final EntityType<NightProwlerGoal.DeathSpiralEntity> DEATH_SPIRAL_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "death_spiral"), EntityType.Builder.create(NightProwlerGoal.DeathSpiralEntity::new, SpawnGroup.MISC).dimensions(2f, 2f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build());
    public static final EntityType<NightsEdge> NIGHTS_EDGE = registerEntity("nights_edge", EntityType.Builder.create(NightsEdge::new, SpawnGroup.MISC).dimensions(2f, 4f).maxTrackingRange(6).trackingTickInterval(Integer.MAX_VALUE).build());
    public static final EntityType<NightWaveEntity> NIGHT_WAVE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "night_wave"), EntityType.Builder.create(NightWaveEntity::new, SpawnGroup.MISC).dimensions(3.5f, 1f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build());
    public static final EntityType<FlamePillar> FLAME_PILLAR = registerEntity("flame_pillar", EntityType.Builder.<FlamePillar>create(FlamePillar::new, SpawnGroup.MISC).dimensions(1.5f, 1.5f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build());
    public static final EntityType<TrueDamageArrow> KRAKEN_SLAYER_PROJECTILE = registerEntity("kraken_slayer_projectile", EntityType.Builder.<TrueDamageArrow>create(TrueDamageArrow::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<MoonlightArrow> MOONLIGHT_ARROW = registerEntity("moonlight_arrow", EntityType.Builder.<MoonlightArrow>create(MoonlightArrow::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<ArrowStormEntity> ARROW_STORM_ENTITY = registerEntity("arrow_storm_entity", EntityType.Builder.create(ArrowStormEntity::new, SpawnGroup.MISC).dimensions(3f, 1.5f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build());
    public static final EntityType<HolyMoonlightPillar> HOLY_MOONLIGHT_PILLAR = registerEntity("holy_moonlight_pillar", EntityType.Builder.create(HolyMoonlightPillar::new, SpawnGroup.MISC).dimensions(1.85f, 1.85f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build());
    public static final EntityType<WarmupLightningEntity> WARMUP_LIGHTNING = registerEntity("warmup_lightning", EntityType.Builder.create(WarmupLightningEntity::new, SpawnGroup.MISC).dimensions(1.5f, 1.5f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build());
    public static final EntityType<GhostGlaiveEntity> GHOST_GLAIVE_TYPE = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "ghost_glaive"), EntityType.Builder.<GhostGlaiveEntity>create(GhostGlaiveEntity::new, SpawnGroup.MISC).dimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<MoonveilWave> MOONVEIL_HORIZONTAL = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "moonveil_horizontal"), EntityType.Builder.<MoonveilWave>create(MoonveilWave::new, SpawnGroup.MISC).dimensions(4.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<MoonveilWave> MOONVEIL_VERTICAL = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "moonveil_vertical"), EntityType.Builder.<MoonveilWave>create(MoonveilWave::new, SpawnGroup.MISC).dimensions(0.5f, 3.0f).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<BlackflameExplosionEntity> BLACKFLAME_EXPLOSION_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "blackflame_explosion_entity"), EntityType.Builder.<BlackflameExplosionEntity>create(BlackflameExplosionEntity::new, SpawnGroup.MISC).dimensions(2f, 2f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build());
    public static final EntityType<AbsorbedProjectilesOrb> ABSORBED_PROJECTILES_ORB_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "absorbed_projectiles_orb_entity"), EntityType.Builder.<AbsorbedProjectilesOrb>create(AbsorbedProjectilesOrb::new, SpawnGroup.MISC).dimensions(1f, 1f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build());
    public static final EntityType<SilverArrow> SILVER_ARROW = registerEntity("silver_arrow", EntityType.Builder.<SilverArrow>create(SilverArrow::new, SpawnGroup.MISC).dimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build());
    public static final EntityType<FrozenLightning> FROZEN_LIGHTNING = registerEntity("frozen_lightning", EntityType.Builder.<FrozenLightning>create(FrozenLightning::new, SpawnGroup.MISC).dimensions(2f, 5f).maxTrackingRange(16).trackingTickInterval(20).build());
    public static final EntityType<TntEntity> CHUNGUS_HEAD = registerEntity("chungus_head", EntityType.Builder.<TntEntity>create(TntEntity::new, SpawnGroup.MISC).dimensions(1f, 1f).maxTrackingRange(16).trackingTickInterval(20).build());
    public static final EntityType<AirCombustion> AIR_COMBUSTION = registerEntity("air_combustion", EntityType.Builder.<AirCombustion>create(AirCombustion::new, SpawnGroup.MISC).dimensions(1f, 1f).maxTrackingRange(16).trackingTickInterval(20).build());
    public static final EntityType<MoltenMetal> MOLTEN_METAL = registerEntity("molten_metal", EntityType.Builder.<MoltenMetal>create(MoltenMetal::new, SpawnGroup.MISC).dimensions(1f, 0.3f).maxTrackingRange(16).trackingTickInterval(20).build());
    public static final EntityType<MoonveilWave> DRAGONWOUND_WAVE_VERTICAL = Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, "dragonwound_wave_vertical"), EntityType.Builder.<MoonveilWave>create(MoonveilWave::new, SpawnGroup.MISC).dimensions(1f, 4.0f).maxTrackingRange(4).trackingTickInterval(20).build());

    public static final Item WITHERED_DEMON_SPAWN_EGG = new SpawnEggItem(WITHERED_DEMON, 10027008, 0, new Item.Settings());
    public static final Item ACCURSED_LORD_BOSS_SPAWN_EGG = new SpawnEggItem(ACCURSED_LORD_BOSS, 0, 10027008, new Item.Settings());
    public static final Item DRAUGR_BOSS_SPAWN_EGG = new SpawnEggItem(DRAUGR_BOSS, 10263708, 7694143, new Item.Settings());
    public static final Item NIGHT_SHADE_SPAWN_EGG = new SpawnEggItem(NIGHT_SHADE, 398638, 16576575, new Item.Settings());
    public static final Item RETURNING_KNIGHT_SPAWN_EGG = new SpawnEggItem(RETURNING_KNIGHT, 2251096, 6554982, new Item.Settings());
    public static final Item REMNANT_SPAWN_EGG = new SpawnEggItem(REMNANT, 6447971, 65514, new Item.Settings());
    public static final Item DARK_SORCERER_SPAWN_EGG = new SpawnEggItem(DARK_SORCERER, 0, 2572343, new Item.Settings());
    public static final Item BIG_CHUNGUS_SPAWN_EGG = new SpawnEggItem(BIG_CHUNGUS, 12636653, 0, new Item.Settings());
    public static final Item SOUL_REAPER_GHOST_SPAWN_EGG = new SpawnEggItem(SOUL_REAPER_GHOST, 13480150, 13200614, new Item.Settings());
    public static final Item FORLORN_SPAWN_EGG = new SpawnEggItem(FORLORN, 4859716, 5701896, new Item.Settings());
    public static final Item EVIL_FORLORN_SPAWN_EGG = new SpawnEggItem(EVIL_FORLORN, 5701896, 4859716, new Item.Settings());
    public static final Item SOULMASS_SPAWN_EGG = new SpawnEggItem(SOULMASS, 4494266, 9658504, new Item.Settings());
    public static final Item CHAOS_MONARCH_SPAWN_EGG = new SpawnEggItem(CHAOS_MONARCH, 4325468, 0, new Item.Settings());
    public static final Item MOONKNIGHT_SPAWN_EGG = new SpawnEggItem(MOONKNIGHT, 13357520, 390585, new Item.Settings());
    public static final Item FROST_GIANT_SPAWN_EGG = new SpawnEggItem(FROST_GIANT, 0x02523f, 0x46dffa, new Item.Settings());
    public static final Item RIME_SPECTRE_SPAWN_EGG = new SpawnEggItem(RIME_SPECTRE, 0x6ae6fc, 0x064854, new Item.Settings());

    public static void init() {
        FabricDefaultAttributeRegistry.register(WITHERED_DEMON, WitheredDemon.createDemonAttributes());
        FabricDefaultAttributeRegistry.register(ACCURSED_LORD_BOSS, AccursedLordBoss.createDemonAttributes());
        FabricDefaultAttributeRegistry.register(DRAUGR_BOSS, DraugrBoss.createBossAttributes());
        FabricDefaultAttributeRegistry.register(NIGHT_SHADE, NightShade.createBossAttributes());
        FabricDefaultAttributeRegistry.register(RETURNING_KNIGHT, ReturningKnight.createBossAttributes());
        FabricDefaultAttributeRegistry.register(BIG_CHUNGUS, BigChungus.createChungusAttributes());
        FabricDefaultAttributeRegistry.register(REMNANT, Remnant.createRemnantAttributes());
        FabricDefaultAttributeRegistry.register(DARK_SORCERER, DarkSorcerer.createSorcererAttributes());
        FabricDefaultAttributeRegistry.register(SOUL_REAPER_GHOST, SoulReaperGhost.createGhostAttributes());
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

        if (DatagenUtil.isDatagenRunning()) {
            registerBossDrops("accursed_lord_boss", ItemRegistry.LORD_SOUL_RED, WeaponRegistry.DARKIN_BLADE, ItemRegistry.WITHERED_DEMON_HEART);
            registerBossDrops("chaos_monarch", WeaponRegistry.WITHERED_WABBAJACK, ItemRegistry.LORD_SOUL_VOID, ArmorRegistry.CHAOS_CROWN, ArmorRegistry.CHAOS_ROBES, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, ItemRegistry.BLOOD_VIAL_RECIPE_PAGE);
            registerBossDrops("day_stalker", WeaponRegistry.DAWNBREAKER, ItemRegistry.LORD_SOUL_DAY_STALKER);
            registerBossDrops("draugr_boss", WeaponRegistry.DRAUGR);
            registerBossDrops("moonknight", WeaponRegistry.MOONLIGHT_GREATSWORD, ItemRegistry.LORD_SOUL_WHITE, ItemRegistry.ESSENCE_OF_LUMINESCENCE, ItemRegistry.MOONSTONE, ItemRegistry.MOONSTONE);
            registerBossDrops("night_prowler", WeaponRegistry.SOUL_REAPER, WeaponRegistry.FORLORN_SCYTHE, ItemRegistry.LORD_SOUL_NIGHT_PROWLER);
            registerBossDrops("returning_knight", WeaponRegistry.NIGHTFALL, ItemRegistry.LORD_SOUL_ROSE, ItemRegistry.ARKENSTONE, ItemRegistry.SOUL_INGOT, ItemRegistry.SOUL_INGOT);
            registerBossDrops("night_shade", ItemRegistry.LORD_SOUL_DARK, ItemRegistry.ESSENCE_OF_EVENTIDE);
        }
    }

    private static <I extends PathAwareEntity> EntityType<I> registerWithSpawnEgg(EntityType<I> type, String id, int primaryColor, int secondaryColor) {
        Item egg = new SpawnEggItem(type, primaryColor, secondaryColor, new Item.Settings());
        ItemRegistry.registerItem(egg, id + "_spawn_egg");
        /*
        * Due to bug regarding Valhelsia Core redirecting the mixin used in the model provider, the generation of model jsons has been deprecated.
        * TODO: this can be re-instated by making sure to only add when running datagen (see DatagenUtil)
        * ModelProvider.ITEMS.put(egg, ModelProvider.SPAWN_EGG);
        */
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, id), type);
    }

    private static <E extends Entity> EntityType<E> registerEntity(String id, EntityType<E> entity) {
        return Registry.register(Registries.ENTITY_TYPE, Identifier.of(ModId, id), entity);
    }

    private static void registerBossDrops(String id, Item... items) {
        EntityLootTablesProvider.BOSS_DROPS.put(id, Lists.newArrayList(items));
    }
}
