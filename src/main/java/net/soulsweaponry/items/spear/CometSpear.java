package net.soulsweaponry.items.spear;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.renderer.item.CometSpearItemRenderer;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAbility;
import net.soulsweaponry.items.abilities.stoppedusing.sneaking.Riptide;
import net.soulsweaponry.items.abilities.stoppedusing.ThrowCometSpear;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAttributes;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.function.Consumer;

public class CometSpear extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    public static final DetonateGroundAttributes METEOR_STRIKE_ATTRIBUTES = new DetonateGroundAttributes(
            WeaponConfig.comet_spear_calculated_fall_base_radius,
            WeaponConfig.comet_spear_calculated_fall_height_increase_radius_modifier,
            WeaponConfig.comet_spear_calculated_fall_target_launch_modifier,
            WeaponConfig.comet_spear_calculated_fall_target_max_launch_power,
            WeaponConfig.comet_spear_calculated_fall_max_radius,
            WeaponConfig.comet_spear_calculated_fall_max_damage,
            WeaponConfig.comet_spear_calculated_fall_height_increase_damage_modifier,
            WeaponConfig.comet_spear_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleTypes.FLAME, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {},
            (user, fallDistance, stack) -> {}
    );
    public static final DetonateGroundAbility METEOR_STRIKE = new DetonateGroundAbility(METEOR_STRIKE_ATTRIBUTES);
    private static final ThrowCometSpear THROW_COMET_SPEAR = new ThrowCometSpear(
            5f,
            (int) WeaponConfig.comet_spear_throw_ability_min_cooldown,
            (int) WeaponConfig.comet_spear_throw_ability_cooldown,
            (int) WeaponConfig.comet_spear_throw_ability_reduced_cooldown_per_level
    );
    private static final Riptide RIPTIDE = new Riptide(
            WeaponConfig.comet_spear_riptide_launch_power,
            WeaponConfig.comet_spear_riptide_bonus_launch_power_per_level,
            WeaponConfig.comet_spear_riptide_collision_damage,
            (int) WeaponConfig.comet_spear_riptide_calculated_fall_duration,
            (int) WeaponConfig.comet_spear_riptide_calculated_fall_amp,
            WeaponConfig.comet_spear_riptide_calculated_fall_bonus_amp_per_level,
            (int) WeaponConfig.comet_spear_riptide_min_cooldown,
            (int) WeaponConfig.comet_spear_riptide_cooldown,
            (int) WeaponConfig.comet_spear_riptide_reduced_cooldown_per_level,
            Riptide.ALWAYS_COOLDOWN
    );

    public CometSpear(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.comet_spear_damage, WeaponConfig.comet_spear_attack_speed, settings);
        this.addAbility(THROW_COMET_SPEAR, RIPTIDE, METEOR_STRIKE);
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
            private CometSpearItemRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new CometSpearItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_comet_spear;
    }
}