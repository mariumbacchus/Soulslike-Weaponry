package net.soulsweaponry.items.sword;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.EmpoweredDawnbreakerRenderer;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.abilitykeybind.VeilOfFireAbility;
import net.soulsweaponry.items.abilities.posthit.BlazingBlade;
import net.soulsweaponry.items.abilities.stoppedusing.ChaosStorm;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

/**
 * Also known as "Genesis Fracture"
 */
public class EmpoweredDawnbreaker extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final BlazingBlade BLAZING_BLADE = new BlazingBlade(
            WeaponConfig.empowered_dawnbreaker_post_hit_base_fire_seconds,
            WeaponConfig.empowered_dawnbreaker_post_hit_bonus_fire_seconds_per_level,
            WeaponConfig.empowered_dawnbreaker_post_hit_bonus_fire_seconds_per_fire_aspect_level
    );
    private static final VeilOfFireAbility VEIL_OF_FIRE_ABILITY = new VeilOfFireAbility(
            WeaponConfig.empowered_dawnbreaker_explosion_affect_all_entities,
            (int) WeaponConfig.empowered_dawnbreaker_post_hit_base_retribution_amp,
            WeaponConfig.empowered_dawnbreaker_post_hit_bonus_retribution_amp_per_level,
            WeaponConfig.empowered_dawnbreaker_explosion_percent_chance_addition,
            WeaponConfig.empowered_dawnbreaker_explosion_range,
            WeaponConfig.empowered_dawnbreaker_explosion_base_fire_seconds,
            WeaponConfig.empowered_dawnbreaker_explosion_bonus_fire_seconds_per_level,
            WeaponConfig.empowered_dawnbreaker_explosion_base_damage,
            WeaponConfig.empowered_dawnbreaker_explosion_bonus_damage_per_level,
            (int) WeaponConfig.empowered_dawnbreaker_explosion_fear_duration,

            (int) WeaponConfig.empowered_dawnbreaker_veil_of_fire_duration,
            WeaponConfig.empowered_dawnbreaker_veil_of_fire_bonus_duration_per_level,
            (int) WeaponConfig.empowered_dawnbreaker_veil_of_fire_amp,
            WeaponConfig.empowered_dawnbreaker_veil_of_fire_bonus_amp_per_level,
            (int) WeaponConfig.empowered_dawnbreaker_veil_of_fire_min_cooldown,
            (int) WeaponConfig.empowered_dawnbreaker_veil_of_fire_cooldown,
            (int) WeaponConfig.empowered_dawnbreaker_veil_of_fire_reduced_cooldown_per_level
    );
    private static final ChaosStorm CHAOS_STORM = new ChaosStorm(
            WeaponConfig.empowered_dawnbreaker_chaos_storm_damage,
            WeaponConfig.empowered_dawnbreaker_chaos_storm_bonus_damage_per_level,
            (int) WeaponConfig.empowered_dawnbreaker_chaos_storm_fire_resistance_duration,

            (int) WeaponConfig.empowered_dawnbreaker_chaos_storm_pillar_spawn_range,
            (int) WeaponConfig.empowered_dawnbreaker_chaos_storm_pillars_amount,
            (int) WeaponConfig.empowered_dawnbreaker_chaos_storm_bonus_pillars_per_level,
            WeaponConfig.empowered_dawnbreaker_chaos_storm_pillar_size,

            (int) WeaponConfig.empowered_dawnbreaker_chaos_storm_min_cooldown,
            (int) WeaponConfig.empowered_dawnbreaker_chaos_storm_cooldown,
            (int) WeaponConfig.empowered_dawnbreaker_chaos_storm_reduced_cooldown_per_level
    );

    public EmpoweredDawnbreaker(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.empowered_dawnbreaker_damage, WeaponConfig.empowered_dawnbreaker_attack_speed, settings);
        this.addAbility(BLAZING_BLADE, VEIL_OF_FIRE_ABILITY, CHAOS_STORM);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private EmpoweredDawnbreakerRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new EmpoweredDawnbreakerRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_empowered_dawnbreaker;
    }
}