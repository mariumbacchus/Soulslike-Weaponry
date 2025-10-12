package net.soulsweaponry.items.hammer;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.MjolnirItemRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ChargeToUseItem;
import net.soulsweaponry.items.abilities.abilitykeybind.CircleLightningCall;
import net.soulsweaponry.items.abilities.statboost.RainBoostsStats;
import net.soulsweaponry.items.abilities.stoppedusing.sneaking.Riptide;
import net.soulsweaponry.items.abilities.stoppedusing.ThrowMjolnir;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Mjolnir extends ChargeToUseItem implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final RainBoostsStats RAIN_BOOSTS_STATS = new RainBoostsStats(ConfigConstructor.mjolnir_rain_bonus_damage, ConfigConstructor.mjolnir_rain_bonus_attack_speed);
    private static final Riptide RIPTIDE = new Riptide(
            5f, 1f, 15f,
            (int) ConfigConstructor.mjolnir_riptide_min_cooldown,
            (int) ConfigConstructor.mjolnir_riptide_cooldown,
            (int) ConfigConstructor.mjolnir_riptide_reduced_cooldown_per_level,
            Riptide.RAINING
    );
    private static final ThrowMjolnir THROW_MJOLNIR = new ThrowMjolnir(2.5f, 0.2f);
    private static final CircleLightningCall LIGHTNING_CALL = new CircleLightningCall(
            ConfigConstructor.mjolnir_lightning_base_smash_damage,
            ConfigConstructor.mjolnir_lightning_bonus_smash_damage_per_level,
            ConfigConstructor.mjolnir_lightning_enchant_bonus_smash_damage_multiplier,
            (int) ConfigConstructor.mjolnir_lightning_circle_amount,
            (int) ConfigConstructor.mjolnir_lightning_smash_min_cooldown,
            (int) ConfigConstructor.mjolnir_lightning_smash_cooldown,
            (int) ConfigConstructor.mjolnir_lightning_smash_reduced_cooldown_per_level
    );

    public Mjolnir(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.mjolnir_damage, ConfigConstructor.mjolnir_attack_speed, settings);
        this.addAbility(RAIN_BOOSTS_STATS, RIPTIDE, THROW_MJOLNIR, LIGHTNING_CALL);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_mjolnir;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private MjolnirItemRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new MjolnirItemRenderer();

                return this.renderer;
            }
        });
    }
}