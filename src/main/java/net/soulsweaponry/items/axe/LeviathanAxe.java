package net.soulsweaponry.items.axe;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.LeviathanAxeRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.posthit.Permafrost;
import net.soulsweaponry.items.abilities.stoppedusing.ThrowLeviathanAxe;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class LeviathanAxe extends ModdedAxe implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
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
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private final LeviathanAxeRenderer renderer = new LeviathanAxeRenderer();

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
}