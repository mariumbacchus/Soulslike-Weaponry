package net.soulsweaponry.items.sword;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.DawnbreakerRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.posthit.BlazingBlade;
import net.soulsweaponry.items.abilities.posthit.DawnbreakerExplosion;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Dawnbreaker extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    private static final BlazingBlade BLAZING_BLADE = new BlazingBlade(
            ConfigConstructor.dawnbreaker_post_hit_base_fire_seconds,
            ConfigConstructor.dawnbreaker_post_hit_bonus_fire_seconds_per_level,
            ConfigConstructor.dawnbreaker_post_hit_bonus_fire_seconds_per_fire_aspect_level
    );
    private static final DawnbreakerExplosion DAWNBREAKER_EXPLOSION = new DawnbreakerExplosion(
            ConfigConstructor.dawnbreaker_explosion_affect_all_entities,
            (int) ConfigConstructor.dawnbreaker_post_hit_base_retribution_amp,
            ConfigConstructor.dawnbreaker_post_hit_bonus_retribution_amp_per_level,
            ConfigConstructor.dawnbreaker_explosion_percent_chance_addition,
            ConfigConstructor.dawnbreaker_explosion_range,
            ConfigConstructor.dawnbreaker_explosion_base_fire_seconds,
            ConfigConstructor.dawnbreaker_explosion_bonus_fire_seconds_per_level,
            ConfigConstructor.dawnbreaker_explosion_base_damage,
            ConfigConstructor.dawnbreaker_explosion_bonus_damage_per_level,
            (int) ConfigConstructor.dawnbreaker_explosion_fear_duration
    );

    public Dawnbreaker(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.dawnbreaker_damage, ConfigConstructor.dawnbreaker_attack_speed, settings);
        this.addAbility(BLAZING_BLADE, DAWNBREAKER_EXPLOSION);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private final DawnbreakerRenderer renderer = new DawnbreakerRenderer();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_dawnbreaker;
    }
}