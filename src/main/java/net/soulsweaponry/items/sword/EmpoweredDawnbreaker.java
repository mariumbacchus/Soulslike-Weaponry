package net.soulsweaponry.items.sword;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.item.EmpoweredDawnbreakerRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.abilitykeybind.VeilOfFireAbility;
import net.soulsweaponry.items.abilities.posthit.BlazingBlade;
import net.soulsweaponry.items.abilities.stoppedusing.ChaosStorm;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

/**
 * Also known as "Genesis Fracture"
 */
public class EmpoweredDawnbreaker extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final BlazingBlade BLAZING_BLADE = new BlazingBlade(
            ConfigConstructor.empowered_dawnbreaker_post_hit_base_fire_seconds,
            ConfigConstructor.empowered_dawnbreaker_post_hit_bonus_fire_seconds_per_level,
            ConfigConstructor.empowered_dawnbreaker_post_hit_bonus_fire_seconds_per_fire_aspect_level
    );
    private static final VeilOfFireAbility VEIL_OF_FIRE_ABILITY = new VeilOfFireAbility(
            ConfigConstructor.empowered_dawnbreaker_explosion_affect_all_entities,
            (int) ConfigConstructor.empowered_dawnbreaker_post_hit_base_retribution_amp,
            ConfigConstructor.empowered_dawnbreaker_post_hit_bonus_retribution_amp_per_level,
            ConfigConstructor.empowered_dawnbreaker_explosion_percent_chance_addition,
            ConfigConstructor.empowered_dawnbreaker_explosion_range,
            ConfigConstructor.empowered_dawnbreaker_explosion_base_fire_seconds,
            ConfigConstructor.empowered_dawnbreaker_explosion_bonus_fire_seconds_per_level,
            ConfigConstructor.empowered_dawnbreaker_explosion_base_damage,
            ConfigConstructor.empowered_dawnbreaker_explosion_bonus_damage_per_level,
            (int) ConfigConstructor.empowered_dawnbreaker_explosion_fear_duration,

            (int) ConfigConstructor.empowered_dawnbreaker_veil_of_fire_duration,
            ConfigConstructor.empowered_dawnbreaker_veil_of_fire_bonus_duration_per_level,
            (int) ConfigConstructor.empowered_dawnbreaker_veil_of_fire_amp,
            ConfigConstructor.empowered_dawnbreaker_veil_of_fire_bonus_amp_per_level,
            (int) ConfigConstructor.empowered_dawnbreaker_veil_of_fire_min_cooldown,
            (int) ConfigConstructor.empowered_dawnbreaker_veil_of_fire_cooldown,
            (int) ConfigConstructor.empowered_dawnbreaker_veil_of_fire_reduced_cooldown_per_level
    );
    private static final ChaosStorm CHAOS_STORM = new ChaosStorm(
            ConfigConstructor.empowered_dawnbreaker_chaos_storm_damage,
            ConfigConstructor.empowered_dawnbreaker_chaos_storm_bonus_damage_per_level,
            (int) ConfigConstructor.empowered_dawnbreaker_chaos_storm_fire_resistance_duration,

            (int) ConfigConstructor.empowered_dawnbreaker_chaos_storm_pillar_spawn_range,
            (int) ConfigConstructor.empowered_dawnbreaker_chaos_storm_pillars_amount,
            (int) ConfigConstructor.empowered_dawnbreaker_chaos_storm_bonus_pillars_per_level,
            ConfigConstructor.empowered_dawnbreaker_chaos_storm_pillar_size,

            (int) ConfigConstructor.empowered_dawnbreaker_chaos_storm_min_cooldown,
            (int) ConfigConstructor.empowered_dawnbreaker_chaos_storm_cooldown,
            (int) ConfigConstructor.empowered_dawnbreaker_chaos_storm_reduced_cooldown_per_level
    );

    public EmpoweredDawnbreaker(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.empowered_dawnbreaker_damage, ConfigConstructor.empowered_dawnbreaker_attack_speed, settings);
        this.addAbility(BLAZING_BLADE, VEIL_OF_FIRE_ABILITY, CHAOS_STORM);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final EmpoweredDawnbreakerRenderer renderer = new EmpoweredDawnbreakerRenderer();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_empowered_dawnbreaker;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_empowered_dawnbreaker;
    }
}