package net.soulsweaponry.items.axe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.LeviathanAxeRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.posthit.Permafrost;
import net.soulsweaponry.items.abilities.stoppedusing.ThrowLeviathanAxe;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class LeviathanAxe extends ModdedAxe implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final Permafrost PERMAFROST = new Permafrost(
            (int) ConfigConstructor.leviathan_axe_frost_buildup_post_hit,
            (int) ConfigConstructor.leviathan_axe_post_hit_permafrost_duration,
            (int) ConfigConstructor.leviathan_axe_post_hit_permafrost_base_amp,
            ConfigConstructor.leviathan_axe_post_hit_permafrost_amp_per_level
    );
    private static final ThrowLeviathanAxe THROW_LEVIATHAN_AXE = new ThrowLeviathanAxe(
            ConfigConstructor.leviathan_axe_projectile_base_speed,
            ConfigConstructor.leviathan_axe_projectile_speed_bonus_per_level
    );

    public LeviathanAxe(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.leviathan_axe_damage, ConfigConstructor.leviathan_axe_attack_speed, settings);
        this.addAbility(PERMAFROST, THROW_LEVIATHAN_AXE);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_leviathan_axe;
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
            private LeviathanAxeRenderer renderer;

            @Override
            public GeoItemRenderer<?> getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new LeviathanAxeRenderer();

                return this.renderer;
            }
        });
    }
}