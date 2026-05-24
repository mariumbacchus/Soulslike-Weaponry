package net.soulsweaponry.items.axe;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.item.LeviathanAxeRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.posthit.Permafrost;
import net.soulsweaponry.items.abilities.stoppedusing.ThrowLeviathanAxe;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private LeviathanAxeRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new LeviathanAxeRenderer();

                return renderer;
            }
        });
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_leviathan_axe;
    }
}