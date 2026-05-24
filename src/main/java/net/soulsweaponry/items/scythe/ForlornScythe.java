package net.soulsweaponry.items.scythe;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.item.ForlornScytheRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.SoulHarvestingItem;
import net.soulsweaponry.items.abilities.use.WitherSoulRelease;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class ForlornScythe extends SoulHarvestingItem implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final WitherSoulRelease WITHER_SOUL_RELEASE = new WitherSoulRelease(
            ConfigConstructor.forlorn_scythe_wither_skull_explosion_power,
            ConfigConstructor.forlorn_scythe_empowered_wither_skull_explosion_power
    );

    public ForlornScythe(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.forlorn_scythe_damage, ConfigConstructor.forlorn_scythe_attack_speed, settings);
        this.addAbility(WITHER_SOUL_RELEASE);
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
            private final ForlornScytheRenderer renderer = new ForlornScytheRenderer();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_forlorn_scythe;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_forlorn_scythe;
    }
}