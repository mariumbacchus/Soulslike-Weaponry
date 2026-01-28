package net.soulsweaponry.items.spear;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.renderer.item.CometSpearItemRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAbility;
import net.soulsweaponry.items.abilities.stoppedusing.sneaking.Riptide;
import net.soulsweaponry.items.abilities.stoppedusing.ThrowCometSpear;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAttributes;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CometSpear extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    public static final DetonateGroundAttributes METEOR_STRIKE_ATTRIBUTES = new DetonateGroundAttributes(
            ConfigConstructor.comet_spear_calculated_fall_base_radius,
            ConfigConstructor.comet_spear_calculated_fall_height_increase_radius_modifier,
            ConfigConstructor.comet_spear_calculated_fall_target_launch_modifier,
            ConfigConstructor.comet_spear_calculated_fall_target_max_launch_power,
            ConfigConstructor.comet_spear_calculated_fall_max_radius,
            ConfigConstructor.comet_spear_calculated_fall_max_damage,
            ConfigConstructor.comet_spear_calculated_fall_height_increase_damage_modifier,
            ConfigConstructor.comet_spear_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleTypes.FLAME, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {},
            (user, fallDistance, stack) -> {}
    );
    public static final DetonateGroundAbility METEOR_STRIKE = new DetonateGroundAbility(METEOR_STRIKE_ATTRIBUTES);
    private static final ThrowCometSpear THROW_COMET_SPEAR = new ThrowCometSpear(
            5f,
            (int) ConfigConstructor.comet_spear_throw_ability_min_cooldown,
            (int) ConfigConstructor.comet_spear_throw_ability_cooldown,
            (int) ConfigConstructor.comet_spear_throw_ability_reduced_cooldown_per_level
    );
    private static final Riptide RIPTIDE = new Riptide(
            ConfigConstructor.comet_spear_riptide_launch_power,
            ConfigConstructor.comet_spear_riptide_bonus_launch_power_per_level,
            ConfigConstructor.comet_spear_riptide_collision_damage,
            (int) ConfigConstructor.comet_spear_riptide_calculated_fall_duration,
            (int) ConfigConstructor.comet_spear_riptide_calculated_fall_amp,
            ConfigConstructor.comet_spear_riptide_calculated_fall_bonus_amp_per_level,
            (int) ConfigConstructor.comet_spear_riptide_min_cooldown,
            (int) ConfigConstructor.comet_spear_riptide_cooldown,
            (int) ConfigConstructor.comet_spear_riptide_reduced_cooldown_per_level,
            Riptide.ALWAYS_COOLDOWN
    );

    public CometSpear(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.comet_spear_damage, ConfigConstructor.comet_spear_attack_speed, settings);
        this.addAbility(THROW_COMET_SPEAR, RIPTIDE, METEOR_STRIKE);
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
            private final CometSpearItemRenderer renderer = new CometSpearItemRenderer();

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
        return ConfigConstructor.disable_use_comet_spear;
    }
}