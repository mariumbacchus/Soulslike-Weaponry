package net.soulsweaponry.items.sword;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.BloodthirsterRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.posthit.LifeSteal;
import net.soulsweaponry.items.abilities.posthit.Overheal;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Bloodthirster extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    private static final LifeSteal LIFE_STEAL = new LifeSteal(
            ConfigConstructor.bloodthirster_life_steal_base_heal,
            ConfigConstructor.bloodthirster_life_steal_bonus_heal_per_level,
            (int) ConfigConstructor.bloodthirster_life_steal_min_cooldown,
            (int) ConfigConstructor.bloodthirster_life_steal_cooldown,
            (int) ConfigConstructor.bloodthirster_life_steal_reduced_cooldown_per_level
    );
    private static final Overheal OVERHEAL = new Overheal(
            (int) ConfigConstructor.bloodthirster_overheal_base_absorption_amp,
            ConfigConstructor.bloodthirster_overheal_bonus_absorption_amp_per_level,
            (int) ConfigConstructor.bloodthirster_overheal_base_absorption_duration,
            ConfigConstructor.bloodthirster_overheal_bonus_absorption_duration_per_level,
            (int) ConfigConstructor.bloodthirster_overheal_min_cooldown,
            (int) ConfigConstructor.bloodthirster_overheal_cooldown,
            (int) ConfigConstructor.bloodthirster_overheal_reduced_cooldown_per_level
    );

    public Bloodthirster(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.bloodthirster_damage, ConfigConstructor.bloodthirster_attack_speed, settings);
        this.addAbility(LIFE_STEAL, OVERHEAL);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_bloodthirster;
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
            private final BloodthirsterRenderer renderer = new BloodthirsterRenderer();

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