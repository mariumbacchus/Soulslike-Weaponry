package net.soulsweaponry.items.sword;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.BloodthirsterRenderer;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.posthit.LifeSteal;
import net.soulsweaponry.items.abilities.posthit.Overheal;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Bloodthirster extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final LifeSteal LIFE_STEAL = new LifeSteal(
            WeaponConfig.bloodthirster_life_steal_base_heal,
            WeaponConfig.bloodthirster_life_steal_bonus_heal_per_level,
            (int) WeaponConfig.bloodthirster_life_steal_min_cooldown,
            (int) WeaponConfig.bloodthirster_life_steal_cooldown,
            (int) WeaponConfig.bloodthirster_life_steal_reduced_cooldown_per_level
    );
    private static final Overheal OVERHEAL = new Overheal(
            (int) WeaponConfig.bloodthirster_overheal_base_absorption_amp,
            WeaponConfig.bloodthirster_overheal_bonus_absorption_amp_per_level,
            (int) WeaponConfig.bloodthirster_overheal_base_absorption_duration,
            WeaponConfig.bloodthirster_overheal_bonus_absorption_duration_per_level,
            (int) WeaponConfig.bloodthirster_overheal_min_cooldown,
            (int) WeaponConfig.bloodthirster_overheal_cooldown,
            (int) WeaponConfig.bloodthirster_overheal_reduced_cooldown_per_level
    );

    public Bloodthirster(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.bloodthirster_damage, WeaponConfig.bloodthirster_attack_speed, settings);
        this.addAbility(LIFE_STEAL, OVERHEAL);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_bloodthirster;
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
            private BloodthirsterRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new BloodthirsterRenderer();

                return this.renderer;
            }
        });
    }
}