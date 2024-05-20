package net.soulsweaponry.registry;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.datagen.loot_tables.BossLootTables;
import net.soulsweaponry.entity.AreaEffectSphere;
import net.soulsweaponry.entity.ai.goal.NightProwlerGoal;
import net.soulsweaponry.entity.mobs.*;
import net.soulsweaponry.entity.projectile.*;
import net.soulsweaponry.entity.projectile.invisible.*;

import static net.soulsweaponry.SoulsWeaponry.ModId;

public class EntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, ModId);

    public static final RegistryObject<EntityType<WitheredDemon>> WITHERED_DEMON = registerWithSpawnEgg("withered_demon", EntityType.Builder.create(WitheredDemon::new, SpawnGroup.MONSTER).setDimensions(1F, 2F).build("withered_demon"), 10027008, 0);
    public static final RegistryObject<EntityType<AccursedLordBoss>> ACCURSED_LORD_BOSS = registerWithSpawnEgg("accursed_lord_boss", EntityType.Builder.create(AccursedLordBoss::new, SpawnGroup.MONSTER).setDimensions(3F, 6F).build("accursed_lord_boss"), 0, 10027008);
    public static final RegistryObject<EntityType<DraugrBoss>> DRAUGR_BOSS = registerWithSpawnEgg("draugr_boss", EntityType.Builder.create(DraugrBoss::new, SpawnGroup.MONSTER).setDimensions(1.5F, 3F).build("draugr_boss"), 10263708, 7694143);
    public static final RegistryObject<EntityType<NightShade>> NIGHT_SHADE = registerWithSpawnEgg("night_shade", EntityType.Builder.create(NightShade::new, SpawnGroup.MONSTER).setDimensions(1.5F, 3.5F).build("night_shade"), 398638, 16576575);
    public static final RegistryObject<EntityType<ReturningKnight>> RETURNING_KNIGHT = registerWithSpawnEgg("returning_knight", EntityType.Builder.create(ReturningKnight::new, SpawnGroup.MONSTER).setDimensions(3.0F, 8.0F).build("returning_knight"), 2251096, 6554982);
    public static final RegistryObject<EntityType<Remnant>> REMNANT = registerWithSpawnEgg("remnant", EntityType.Builder.create(Remnant::new, SpawnGroup.MONSTER).setDimensions(1F, 1.75F).build("remnant"), 6447971, 65514);
    public static final RegistryObject<EntityType<DarkSorcerer>> DARK_SORCERER = registerWithSpawnEgg("dark_sorcerer", EntityType.Builder.create(DarkSorcerer::new, SpawnGroup.MONSTER).setDimensions(1F, 1.75f).build("dark_sorcerer"), 0, 2572343);
    public static final RegistryObject<EntityType<BigChungus>> BIG_CHUNGUS = registerWithSpawnEgg("big_chungus", EntityType.Builder.create(BigChungus::new, SpawnGroup.MONSTER).setDimensions(0.75f, 0.75f).build("big_chungus"), 12636653, 0);
    public static final RegistryObject<EntityType<SoulReaperGhost>> SOUL_REAPER_GHOST = registerWithSpawnEgg("soul_reaper_ghost", EntityType.Builder.create(SoulReaperGhost::new, SpawnGroup.MONSTER).setDimensions(1F, 1.75F).build("soul_reaper_ghost"), 13480150, 13200614);
    public static final RegistryObject<EntityType<Forlorn>> FORLORN = registerWithSpawnEgg("forlorn", EntityType.Builder.create(Forlorn::new, SpawnGroup.MONSTER).setDimensions(1F, 1.75F).build("forlorn"), 4859716, 5701896);
    public static final RegistryObject<EntityType<EvilForlorn>> EVIL_FORLORN = registerWithSpawnEgg("evil_forlorn", EntityType.Builder.create(EvilForlorn::new, SpawnGroup.MONSTER).setDimensions(1F, 1.75F).build("evil_forlorn"), 5701896, 4859716);
    public static final RegistryObject<EntityType<Soulmass>> SOULMASS = registerWithSpawnEgg("soulmass", EntityType.Builder.create(Soulmass::new, SpawnGroup.MONSTER).setDimensions(2.7F, 3.5F).build("soulmass"), 4494266, 9658504);
    public static final RegistryObject<EntityType<ChaosMonarch>> CHAOS_MONARCH = registerWithSpawnEgg("chaos_monarch", EntityType.Builder.create(ChaosMonarch::new, SpawnGroup.MONSTER).setDimensions(2.5F, 6F).build("chaos_monarch"), 4325468, 0);
    public static final RegistryObject<EntityType<Moonknight>> MOONKNIGHT = registerWithSpawnEgg("moonknight", EntityType.Builder.create(Moonknight::new, SpawnGroup.MONSTER).setDimensions(3F, 8F).build("moonknight"), 13357520, 390585);
    public static final RegistryObject<EntityType<FrostGiant>> FROST_GIANT = registerWithSpawnEgg("frost_giant", EntityType.Builder.create(FrostGiant::new, SpawnGroup.MONSTER).setDimensions(1.25F, 2.6F).build("frost_giant"), 0x02523f, 0x46dffa);
    public static final RegistryObject<EntityType<RimeSpectre>> RIME_SPECTRE = registerWithSpawnEgg("rime_spectre", EntityType.Builder.create(RimeSpectre::new, SpawnGroup.MONSTER).setDimensions(1F, 2F).build("rime_spectre"), 0x6ae6fc, 0x064854);
    public static final RegistryObject<EntityType<DayStalker>> DAY_STALKER = registerWithSpawnEgg("day_stalker", EntityType.Builder.create(DayStalker::new, SpawnGroup.MONSTER).setDimensions(3.5F, 5.5F).build("day_stalker"), 0x212121, 0xff8000);
    public static final RegistryObject<EntityType<NightProwler>> NIGHT_PROWLER = registerWithSpawnEgg("night_prowler", EntityType.Builder.create(NightProwler::new, SpawnGroup.MONSTER).setDimensions(3.5F, 5.5F).build("night_prowler"), 0x345385, 0xff177c);
    public static final RegistryObject<EntityType<WarmthEntity>> WARMTH_ENTITY = registerWithSpawnEgg("warmth_entity", EntityType.Builder.create(WarmthEntity::new, SpawnGroup.MONSTER).setDimensions(1f, 1f).build("warmth_entity"), 0xdb8700, 0xfff7eb);

    public static final RegistryObject<EntityType<MoonlightProjectile>> MOONLIGHT_ENTITY_TYPE = registerEntity("moonlight_projectile", EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).setDimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(20).build("moonlight_projectile"));
    public static final RegistryObject<EntityType<MoonlightProjectile>> MOONLIGHT_BIG_ENTITY_TYPE = registerEntity("big_moonlight_projectile", EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).setDimensions(2F, 1F).maxTrackingRange(4).trackingTickInterval(20).build("big_moonlight_projectile"));
    public static final RegistryObject<EntityType<MoonlightProjectile>> VERTICAL_MOONLIGHT_ENTITY_TYPE = registerEntity("vertical_moonlight_projectile", EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).setDimensions(1F, 3F).maxTrackingRange(4).trackingTickInterval(20).build("vertical_moonlight_projectile"));
    public static final RegistryObject<EntityType<MoonlightProjectile>> SUNLIGHT_PROJECTILE_SMALL = registerEntity("sunlight_projectile_small", EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).setDimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(20).build("sunlight_projectile_small"));
    public static final RegistryObject<EntityType<MoonlightProjectile>> SUNLIGHT_PROJECTILE_BIG = registerEntity("sunlight_projectile_big", EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).setDimensions(2F, 1F).maxTrackingRange(4).trackingTickInterval(20).build("sunlight_projectile_big"));
    public static final RegistryObject<EntityType<MoonlightProjectile>> VERTICAL_SUNLIGHT_PROJECTILE = registerEntity("vertical_sunlight_projectile", EntityType.Builder.<MoonlightProjectile>create(MoonlightProjectile::new, SpawnGroup.MISC).setDimensions(1F, 3F).maxTrackingRange(4).trackingTickInterval(20).build("vertical_sunlight_projectile"));
    public static final RegistryObject<EntityType<DragonslayerSwordspearEntity>> SWORDSPEAR_ENTITY_TYPE = registerEntity("swordspear_entity", EntityType.Builder.<DragonslayerSwordspearEntity>create(DragonslayerSwordspearEntity::new, SpawnGroup.MISC).setDimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(20).build("swordspear_entity"));
    public static final RegistryObject<EntityType<CometSpearEntity>> COMET_SPEAR_ENTITY_TYPE = registerEntity("comet_spear_entity", EntityType.Builder.<CometSpearEntity>create(CometSpearEntity::new, SpawnGroup.MISC).setDimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(20).build("comet_spear_entity"));
    public static final RegistryObject<EntityType<ChargedArrow>> CHARGED_ARROW_ENTITY_TYPE = registerEntity("charged_arrow_entity", EntityType.Builder.<ChargedArrow>create(ChargedArrow::new, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build("charged_arrow_entity"));
    public static final RegistryObject<EntityType<SilverBulletEntity>> SILVER_BULLET_ENTITY_TYPE = registerEntity("silver_bullet_entity", EntityType.Builder.<SilverBulletEntity>create(SilverBulletEntity::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).build("silver_bullet_entity"));
    public static final RegistryObject<EntityType<Cannonball>> CANNONBALL = registerEntity("cannonball_entity_type", EntityType.Builder.<Cannonball>create(Cannonball::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(40).build("cannonball_entity_type"));
    public static final RegistryObject<EntityType<LeviathanAxeEntity>> LEVIATHAN_AXE_ENTITY_TYPE = registerEntity("leviathan_axe_entity", EntityType.Builder.<LeviathanAxeEntity>create(LeviathanAxeEntity::new, SpawnGroup.MISC).setDimensions(.5F, 1F).maxTrackingRange(4).trackingTickInterval(20).build("leviathan_axe_entity"));
    public static final RegistryObject<EntityType<MjolnirProjectile>> MJOLNIR_ENTITY_TYPE = registerEntity("mjolnir_entity", EntityType.Builder.<MjolnirProjectile>create(MjolnirProjectile::new, SpawnGroup.MISC).setDimensions(.75F, .75F).maxTrackingRange(4).trackingTickInterval(20).build("mjolnir_entity"));
    public static final RegistryObject<EntityType<FreyrSwordEntity>> FREYR_SWORD_ENTITY_TYPE = registerEntity("freyr_sword_entity", EntityType.Builder.<FreyrSwordEntity>create(FreyrSwordEntity::new, SpawnGroup.MISC).setDimensions(.3F, 2F).build("freyr_sword_entity"));
    public static final RegistryObject<EntityType<ShadowOrb>> SHADOW_ORB = registerEntity("shadow_orb_entity", EntityType.Builder.<ShadowOrb>create(ShadowOrb::new, SpawnGroup.MISC).setDimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(30).build("shadow_orb_entity"));
    public static final RegistryObject<EntityType<DraupnirSpearEntity>> DRAUPNIR_SPEAR_TYPE = registerEntity("draupnir_spear_entity", EntityType.Builder.<DraupnirSpearEntity>create(DraupnirSpearEntity::new, SpawnGroup.MISC).setDimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(30).build("draupnir_spear_entity"));
    public static final RegistryObject<EntityType<AreaEffectSphere>> AREA_EFFECT_SPHERE = registerEntity("area_effect_sphere", EntityType.Builder.<AreaEffectSphere>create(AreaEffectSphere::new, SpawnGroup.MISC).setDimensions(3F, 3F).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build("area_effect_sphere"));
    public static final RegistryObject<EntityType<DragonStaffProjectile>> DRAGON_STAFF_PROJECTILE = registerEntity("dragon_staff_projectile", EntityType.Builder.<DragonStaffProjectile>create(DragonStaffProjectile::new, SpawnGroup.MISC).setDimensions(1F, 1F).maxTrackingRange(4).trackingTickInterval(10).build("dragon_staff_projectile"));
    public static final RegistryObject<EntityType<WitheredWabbajackProjectile>> WITHERED_WABBAJACK_PROJECTILE = registerEntity("withered_wabbajack_projectile", EntityType.Builder.<WitheredWabbajackProjectile>create(WitheredWabbajackProjectile::new, SpawnGroup.MISC).setDimensions(0.3125f, 0.3125f).maxTrackingRange(4).trackingTickInterval(10).build("withered_wabbajack_projectile"));
    public static final RegistryObject<EntityType<ChaosSkull>> CHAOS_SKULL = registerEntity("chaos_skull", EntityType.Builder.<ChaosSkull>create(ChaosSkull::new, SpawnGroup.MISC).setDimensions(0.4f, 0.4f).maxTrackingRange(4).trackingTickInterval(10).build("chaos_skull"));
    public static final RegistryObject<EntityType<ChaosOrbEntity>> CHAOS_ORB_ENTITY = registerEntity("chaos_orb_entity", EntityType.Builder.<ChaosOrbEntity>create(ChaosOrbEntity::new, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(10).build("chaos_orb_entity"));
    public static final RegistryObject<EntityType<GrowingFireball>> GROWING_FIREBALL_ENTITY = registerEntity("growing_fireball", EntityType.Builder.<GrowingFireball>create(GrowingFireball::new, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(10).trackingTickInterval(20).build("growing_fireball"));
    public static final RegistryObject<EntityType<NightSkull>> NIGHT_SKULL = registerEntity("night_skull", EntityType.Builder.create(NightSkull::new, SpawnGroup.MISC).setDimensions(0.5f, 0.75f).maxTrackingRange(15).trackingTickInterval(20).build("night_skull"));
    public static final RegistryObject<EntityType<FogEntity>> FOG_ENTITY = registerEntity("fog_entity", EntityType.Builder.create(FogEntity::new, SpawnGroup.MISC).setDimensions(8f, 3f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build("fog_entity"));
    public static final RegistryObject<EntityType<BlackflameSnakeEntity>> BLACKFLAME_SNAKE_ENTITY = registerEntity("blackflame_snake_entity", EntityType.Builder.create(BlackflameSnakeEntity::new, SpawnGroup.MISC).setDimensions(2f, 2f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build("blackflame_snake_entity"));
    public static final RegistryObject<EntityType<NoDragWitherSkull>> NO_DRAG_WITHER_SKULL = registerEntity("no_drag_wither_skull", EntityType.Builder.create(NoDragWitherSkull::new, SpawnGroup.MISC).setDimensions(0.3125f, 0.3125f).maxTrackingRange(15).trackingTickInterval(20).build("no_drag_wither_skull"));
    public static final RegistryObject<EntityType<NightProwlerGoal.DeathSpiralEntity>> DEATH_SPIRAL_ENTITY = registerEntity("death_spiral", EntityType.Builder.create(NightProwlerGoal.DeathSpiralEntity::new, SpawnGroup.MISC).setDimensions(2f, 2f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build("death_spiral"));
    public static final RegistryObject<EntityType<NightsEdge>> NIGHTS_EDGE = registerEntity("nights_edge", EntityType.Builder.create(NightsEdge::new, SpawnGroup.MISC).setDimensions(0.75f, 2f).maxTrackingRange(6).trackingTickInterval(Integer.MAX_VALUE).build("nights_edge"));
    public static final RegistryObject<EntityType<NightWaveEntity>> NIGHT_WAVE = registerEntity("night_wave", EntityType.Builder.create(NightWaveEntity::new, SpawnGroup.MISC).setDimensions(3.5f, 1f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build("night_wave"));
    public static final RegistryObject<EntityType<FlamePillar>> FLAME_PILLAR = registerEntity("flame_pillar", EntityType.Builder.create(FlamePillar::new, SpawnGroup.MISC).setDimensions(1.5f, 1.5f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build("flame_pillar"));
    public static final RegistryObject<EntityType<KrakenSlayerProjectile>> KRAKEN_SLAYER_PROJECTILE = registerEntity("kraken_slayer_projectile", EntityType.Builder.<KrakenSlayerProjectile>create(KrakenSlayerProjectile::new, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build("kraken_slayer_projectile"));
    public static final RegistryObject<EntityType<MoonlightArrow>> MOONLIGHT_ARROW = registerEntity("moonlight_arrow", EntityType.Builder.<MoonlightArrow>create(MoonlightArrow::new, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).maxTrackingRange(4).trackingTickInterval(20).build("moonlight_arrow"));
    public static final RegistryObject<EntityType<ArrowStormEntity>> ARROW_STORM_ENTITY = registerEntity("arrow_storm_entity", EntityType.Builder.create(ArrowStormEntity::new, SpawnGroup.MISC).setDimensions(3f, 1.5f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build("arrow_storm_entity"));
    public static final RegistryObject<EntityType<HolyMoonlightPillar>> HOLY_MOONLIGHT_PILLAR = registerEntity("holy_moonlight_pillar", EntityType.Builder.create(HolyMoonlightPillar::new, SpawnGroup.MISC).setDimensions(1.85f, 1.85f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build("holy_moonlight_pillar"));
    public static final RegistryObject<EntityType<WarmupLightningEntity>> WARMUP_LIGHTNING = registerEntity("warmup_lightning", EntityType.Builder.create(WarmupLightningEntity::new, SpawnGroup.MISC).setDimensions(1.5f, 1.5f).makeFireImmune().maxTrackingRange(4).trackingTickInterval(Integer.MAX_VALUE).build("warmup_lightning"));

    public static <E extends EntityType<? extends MobEntity>> RegistryObject<E> registerWithSpawnEgg(String id, E entity, int primaryColor, int secondaryColor) {
        RegistryObject<E> returnable = registerEntity(id, entity);
        ItemRegistry.registerItem(id + "_spawn_egg", new ForgeSpawnEggItem(returnable, primaryColor, secondaryColor, new Item.Settings().group(SoulsWeaponry.MAIN_GROUP)));
        return returnable;
    }

    public static <E extends EntityType<?>> RegistryObject<E> registerEntity(String id, E entity) {
        return ENTITIES.register(id, () -> entity);
    }

    private static void registerBossDrops(String id, Item... items) {
        BossLootTables.BOSS_DROPS.put(id, Lists.newArrayList(items));
    }

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }

    public static void registerBossDrops() {
        registerBossDrops("accursed_lord_boss", ItemRegistry.LORD_SOUL_RED.get(), WeaponRegistry.DARKIN_BLADE.get(), ItemRegistry.WITHERED_DEMON_HEART.get());
        registerBossDrops("chaos_monarch", WeaponRegistry.WITHERED_WABBAJACK.get(), ItemRegistry.LORD_SOUL_VOID.get(), ItemRegistry.CHAOS_CROWN.get(), ItemRegistry.CHAOS_ROBES.get());
        registerBossDrops("day_stalker", WeaponRegistry.DAWNBREAKER.get(), ItemRegistry.LORD_SOUL_DAY_STALKER.get());
        registerBossDrops("draugr_boss", WeaponRegistry.DRAUGR.get());
        registerBossDrops("moonknight", WeaponRegistry.MOONLIGHT_GREATSWORD.get(), ItemRegistry.LORD_SOUL_WHITE.get(), ItemRegistry.ESSENCE_OF_LUMINESCENCE.get(), ItemRegistry.MOONSTONE.get(), ItemRegistry.MOONSTONE.get());
        registerBossDrops("night_prowler", WeaponRegistry.SOUL_REAPER.get(), WeaponRegistry.FORLORN_SCYTHE.get(), ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get());
        registerBossDrops("returning_knight", WeaponRegistry.NIGHTFALL.get(), ItemRegistry.LORD_SOUL_ROSE.get(), ItemRegistry.ARKENSTONE.get(), ItemRegistry.SOUL_INGOT.get(), ItemRegistry.SOUL_INGOT.get());
        registerBossDrops("night_shade", ItemRegistry.LORD_SOUL_DARK.get(), ItemRegistry.ESSENCE_OF_EVENTIDE.get());
    }
}
