package net.soulsweaponry.client.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.soulsweaponry.client.renderer.entity.mobs.*;
import net.soulsweaponry.client.renderer.entity.projectile.*;
import net.soulsweaponry.registry.EntityRegistry;
import net.minecraft.client.render.entity.DragonFireballEntityRenderer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.WitherSkullEntityRenderer;

public class EntityModelRegistry {
    
    public static void initClient() {
        EntityRendererRegistry.register(EntityRegistry.WITHERED_DEMON, WitheredDemonRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.ACCURSED_LORD_BOSS, AccursedLordBossRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CHAOS_MONARCH, ChaosMonarchRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.DRAUGR_BOSS, DraugrBossRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.NIGHT_SHADE, NightShadeRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.RETURNING_KNIGHT, ReturningKnightRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SOULMASS, SoulmassRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.REMNANT, RemnantRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FORLORN, ForlornRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.EVIL_FORLORN, ForlornRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.DARK_SORCERER, DarkSorcererRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.COMET_SPEAR_ENTITY_TYPE, CometSpearRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BIG_CHUNGUS, BigChungusRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MOONLIGHT_ENTITY_TYPE, MoonlightProjectileRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE, MoonlightProjectileBigRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.VERTICAL_MOONLIGHT_ENTITY_TYPE, VerticalMoonlightProjectileRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SWORDSPEAR_ENTITY_TYPE, DragonslayerSwordspearRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CHARGED_ARROW_ENTITY_TYPE, ChargedArrowRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SILVER_BULLET_ENTITY_TYPE, SilverBulletRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CANNONBALL, CannonballRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.LEVIATHAN_AXE_ENTITY_TYPE, LeviathanAxeEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MJOLNIR_ENTITY_TYPE, MjolnirProjectileRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FREYR_SWORD_ENTITY_TYPE, FreyrSwordEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SHADOW_ORB, ShadowOrbRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.MOONKNIGHT, MoonknightRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.DRAUPNIR_SPEAR_TYPE, DraupnirSpearEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FROST_GIANT, FrostGiantRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.RIME_SPECTRE, RimeSpectreRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.AREA_EFFECT_SPHERE, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.DRAGON_STAFF_PROJECTILE, DragonFireballEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.WITHERED_WABBAJACK_PROJECTILE, WitherSkullEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CHAOS_SKULL, WitherSkullEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.CHAOS_ORB_ENTITY, ChaosOrbRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SUNLIGHT_PROJECTILE_SMALL, SunlightProjectileSmallRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SUNLIGHT_PROJECTILE_BIG, SunlightProjectileBigRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.VERTICAL_SUNLIGHT_PROJECTILE, VerticalSunlightProjectileRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.GROWING_FIREBALL_ENTITY, GrowingFireballRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.WARMTH_ENTITY, WarmthEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.DAY_STALKER, DayStalkerRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.NIGHT_PROWLER, NightProwlerRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.NIGHT_SKULL, NightSkullRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FOG_ENTITY, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.BLACKFLAME_SNAKE_ENTITY, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.NO_DRAG_WITHER_SKULL, WitherSkullEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.DEATH_SPIRAL_ENTITY, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.NIGHTS_EDGE, NightsEdgeRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.NIGHT_WAVE, EmptyEntityRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.FLAME_PILLAR, EmptyEntityRenderer::new);
    }
}
