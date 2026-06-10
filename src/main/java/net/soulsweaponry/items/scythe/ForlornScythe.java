package net.soulsweaponry.items.scythe;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.ForlornScytheRenderer;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.SoulHarvestingItem;
import net.soulsweaponry.items.abilities.use.NightSkullSoulRelease;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class ForlornScythe extends SoulHarvestingItem implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final NightSkullSoulRelease WITHER_SOUL_RELEASE = new NightSkullSoulRelease(
            WeaponConfig.forlorn_scythe_night_skull_explosion_power,
            WeaponConfig.forlorn_scythe_charged_night_skull_explosion_power,
            WeaponConfig.forlorn_scythe_night_skull_damage,
            WeaponConfig.forlorn_scythe_night_skull_bonus_damage_per_level,
            WeaponConfig.forlorn_scythe_charged_night_skull_damage,
            WeaponConfig.forlorn_scythe_charged_night_skull_bonus_damage_per_level,
            WeaponConfig.forlorn_scythe_night_skull_velocity,
            WeaponConfig.forlorn_scythe_night_skull_explosion_destroy_blocks,
            (int) WeaponConfig.forlorn_scythe_night_skull_max_age_ticks,
            (int) WeaponConfig.forlorn_scythe_night_skull_base_count,
            (int) WeaponConfig.forlorn_scythe_night_skull_levels_needed_for_2_more_skulls
    );

    public ForlornScythe(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.forlorn_scythe_damage, WeaponConfig.forlorn_scythe_attack_speed, settings);
        this.addAbility(WITHER_SOUL_RELEASE);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private ForlornScytheRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new ForlornScytheRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_forlorn_scythe;
    }
}