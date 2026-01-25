package net.soulsweaponry.mixin;

import net.minecraft.client.render.item.property.numeric.NumericProperties;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.predicate.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NumericProperties.class)
public class NumericPropertiesMixin {

    @Unique
    private static final Identifier BETTERCOMBAT_LOADED_ID = Identifier.of(SoulsWeaponry.ModId, "bettercombat");
    @Unique
    private static final Identifier EPIC_FIGHT_LOADED_ID = Identifier.of(SoulsWeaponry.ModId, "epicfight");
    @Unique
    private static final Identifier CHARGED_ESSENCE_ID = Identifier.of(SoulsWeaponry.ModId, "charged_essence");
    @Unique
    private static final Identifier INVISIBLE_ID = Identifier.of(SoulsWeaponry.ModId, "invisible");
    @Unique
    private static final Identifier NIGHT_ID = Identifier.of(SoulsWeaponry.ModId, "night");
    @Unique
    private static final Identifier BOSS_COMPASS_ANGLE_ID = Identifier.of(SoulsWeaponry.ModId, "boss_compass_angle");
    @Unique
    private static final Identifier SHARPENED_ID = Identifier.of(SoulsWeaponry.ModId, "sharpened");
    @Unique
    private static final Identifier ENEMY_NEARBY_ID = Identifier.of(SoulsWeaponry.ModId, "enemy_nearby");
    @Unique
    private static final Identifier MAX_HEALTH_ID = Identifier.of(SoulsWeaponry.ModId, "max_health");
    @Unique
    private static final Identifier STORMVEIL_ACTIVE_ID = Identifier.of(SoulsWeaponry.ModId, "stormveil_active");
    @Unique
    private static final Identifier CHUNGUS_TONIC_ID = Identifier.of(SoulsWeaponry.ModId, "chungus_tonic");

    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void sw_registerBowPull(CallbackInfo ci) {
        var mapper = NumericPropertiesAccessor.sw_idMapper();
        mapper.put(BETTERCOMBAT_LOADED_ID, BetterCombatProperty.CODEC);
        mapper.put(EPIC_FIGHT_LOADED_ID, EpicFightProperty.CODEC);
        mapper.put(CHARGED_ESSENCE_ID, ChargedEssenceProperty.CODEC);
        mapper.put(INVISIBLE_ID, InvisibleProperty.CODEC);
        mapper.put(NIGHT_ID, NightProperty.CODEC);
        mapper.put(BOSS_COMPASS_ANGLE_ID, BossCompassAngleProperty.CODEC);
        mapper.put(SHARPENED_ID, SharpenedProperty.CODEC);
        mapper.put(ENEMY_NEARBY_ID, EnemyNearbyProperty.CODEC);
        mapper.put(MAX_HEALTH_ID, MaxHealthProperty.CODEC);
        mapper.put(STORMVEIL_ACTIVE_ID, StormveilActiveProperty.CODEC);
        mapper.put(CHUNGUS_TONIC_ID, ChungusTonicProperty.CODEC);
    }
}
