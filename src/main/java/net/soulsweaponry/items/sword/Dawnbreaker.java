package net.soulsweaponry.items.sword;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.DawnbreakerRenderer;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.posthit.BlazingBlade;
import net.soulsweaponry.items.abilities.posthit.DawnbreakerExplosion;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Dawnbreaker extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final BlazingBlade BLAZING_BLADE = new BlazingBlade(
            WeaponConfig.dawnbreaker_post_hit_base_fire_seconds,
            WeaponConfig.dawnbreaker_post_hit_bonus_fire_seconds_per_level,
            WeaponConfig.dawnbreaker_post_hit_bonus_fire_seconds_per_fire_aspect_level
    );
    private static final DawnbreakerExplosion DAWNBREAKER_EXPLOSION = new DawnbreakerExplosion(
            WeaponConfig.dawnbreaker_explosion_affect_all_entities,
            (int) WeaponConfig.dawnbreaker_post_hit_base_retribution_amp,
            WeaponConfig.dawnbreaker_post_hit_bonus_retribution_amp_per_level,
            WeaponConfig.dawnbreaker_explosion_percent_chance_addition,
            WeaponConfig.dawnbreaker_explosion_range,
            WeaponConfig.dawnbreaker_explosion_base_fire_seconds,
            WeaponConfig.dawnbreaker_explosion_bonus_fire_seconds_per_level,
            WeaponConfig.dawnbreaker_explosion_base_damage,
            WeaponConfig.dawnbreaker_explosion_bonus_damage_per_level,
            (int) WeaponConfig.dawnbreaker_explosion_fear_duration
    );

    public Dawnbreaker(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.dawnbreaker_damage, WeaponConfig.dawnbreaker_attack_speed, settings);
        this.addAbility(BLAZING_BLADE, DAWNBREAKER_EXPLOSION);
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
            private DawnbreakerRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new DawnbreakerRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_dawnbreaker;
    }
}