package net.soulsweaponry.client.registry;

import net.minecraft.client.render.entity.DragonFireballEntityRenderer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.client.render.entity.WitherSkullEntityRenderer;
import net.soulsweaponry.client.renderer.entity.mobs.*;
import net.soulsweaponry.client.renderer.entity.projectile.*;
import net.soulsweaponry.registry.EntityRegistry;

public class EntityModelRegistry {

    public static void register() {
        EntityRenderers.register(EntityRegistry.WITHERED_DEMON.get(), WitheredDemonRenderer::new);
        EntityRenderers.register(EntityRegistry.ACCURSED_LORD_BOSS.get(), AccursedLordBossRenderer::new);
        EntityRenderers.register(EntityRegistry.CHAOS_MONARCH.get(), ChaosMonarchRenderer::new);
        EntityRenderers.register(EntityRegistry.DRAUGR_BOSS.get(), DraugrBossRenderer::new);
        EntityRenderers.register(EntityRegistry.NIGHT_SHADE.get(), NightShadeRenderer::new);
        EntityRenderers.register(EntityRegistry.RETURNING_KNIGHT.get(), ReturningKnightRenderer::new);
        EntityRenderers.register(EntityRegistry.SOULMASS.get(), SoulmassRenderer::new);
        EntityRenderers.register(EntityRegistry.REMNANT.get(), RemnantRenderer::new);
        EntityRenderers.register(EntityRegistry.FORLORN.get(), ForlornRenderer::new);
        EntityRenderers.register(EntityRegistry.EVIL_FORLORN.get(), ForlornRenderer::new);
        EntityRenderers.register(EntityRegistry.DARK_SORCERER.get(), DarkSorcererRenderer::new);
        EntityRenderers.register(EntityRegistry.COMET_SPEAR_ENTITY_TYPE.get(), CometSpearRenderer::new);
        EntityRenderers.register(EntityRegistry.BIG_CHUNGUS.get(), BigChungusRenderer::new);
        EntityRenderers.register(EntityRegistry.MOONLIGHT_ENTITY_TYPE.get(), MoonlightProjectileRenderer::new);
        EntityRenderers.register(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE.get(), MoonlightProjectileBigRenderer::new);
        EntityRenderers.register(EntityRegistry.VERTICAL_MOONLIGHT_ENTITY_TYPE.get(), VerticalMoonlightProjectileRenderer::new);
        EntityRenderers.register(EntityRegistry.SWORDSPEAR_ENTITY_TYPE.get(), DragonslayerSwordspearRenderer::new);
        EntityRenderers.register(EntityRegistry.CHARGED_ARROW_ENTITY_TYPE.get(), ChargedArrowRenderer::new);
        EntityRenderers.register(EntityRegistry.SILVER_BULLET_ENTITY_TYPE.get(), SilverBulletRenderer::new);
        EntityRenderers.register(EntityRegistry.CANNONBALL.get(), CannonballRenderer::new);
        EntityRenderers.register(EntityRegistry.LEVIATHAN_AXE_ENTITY_TYPE.get(), LeviathanAxeEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.MJOLNIR_ENTITY_TYPE.get(), MjolnirProjectileRenderer::new);
        EntityRenderers.register(EntityRegistry.FREYR_SWORD_ENTITY_TYPE.get(), FreyrSwordEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.SHADOW_ORB.get(), ShadowOrbRenderer::new);
        EntityRenderers.register(EntityRegistry.MOONKNIGHT.get(), MoonknightRenderer::new);
        EntityRenderers.register(EntityRegistry.DRAUPNIR_SPEAR_TYPE.get(), DraupnirSpearEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.FROST_GIANT.get(), FrostGiantRenderer::new);
        EntityRenderers.register(EntityRegistry.RIME_SPECTRE.get(), RimeSpectreRenderer::new);
        EntityRenderers.register(EntityRegistry.AREA_EFFECT_SPHERE.get(), EmptyEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.DRAGON_STAFF_PROJECTILE.get(), DragonFireballEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.WITHERED_WABBAJACK_PROJECTILE.get(), WitherSkullEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.CHAOS_SKULL.get(), WitherSkullEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.CHAOS_ORB_ENTITY.get(), ChaosOrbRenderer::new);
        EntityRenderers.register(EntityRegistry.SUNLIGHT_PROJECTILE_SMALL.get(), SunlightProjectileSmallRenderer::new);
        EntityRenderers.register(EntityRegistry.SUNLIGHT_PROJECTILE_BIG.get(), SunlightProjectileBigRenderer::new);
        EntityRenderers.register(EntityRegistry.VERTICAL_SUNLIGHT_PROJECTILE.get(), VerticalSunlightProjectileRenderer::new);
        EntityRenderers.register(EntityRegistry.GROWING_FIREBALL_ENTITY.get(), GrowingFireballRenderer::new);
        EntityRenderers.register(EntityRegistry.WARMTH_ENTITY.get(), WarmthEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.DAY_STALKER.get(), DayStalkerRenderer::new);
        EntityRenderers.register(EntityRegistry.NIGHT_PROWLER.get(), NightProwlerRenderer::new);
        EntityRenderers.register(EntityRegistry.NIGHT_SKULL.get(), NightSkullRenderer::new);
        EntityRenderers.register(EntityRegistry.FOG_ENTITY.get(), EmptyEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.BLACKFLAME_SNAKE_ENTITY.get(), EmptyEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.NO_DRAG_WITHER_SKULL.get(), WitherSkullEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.DEATH_SPIRAL_ENTITY.get(), EmptyEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.NIGHTS_EDGE.get(), NightsEdgeRenderer::new);
        EntityRenderers.register(EntityRegistry.NIGHT_WAVE.get(), EmptyEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.FLAME_PILLAR.get(), EmptyEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.KRAKEN_SLAYER_PROJECTILE.get(), KrakenSlayerProjectileRenderer::new);
        EntityRenderers.register(EntityRegistry.MOONLIGHT_ARROW.get(), MoonlightArrowRenderer::new);
        EntityRenderers.register(EntityRegistry.ARROW_STORM_ENTITY.get(), EmptyEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.HOLY_MOONLIGHT_PILLAR.get(), EmptyEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.WARMUP_LIGHTNING.get(), EmptyEntityRenderer::new);
        EntityRenderers.register(EntityRegistry.SOUL_REAPER_GHOST.get(), SoulReaperGhostRenderer::new);
    }
}
