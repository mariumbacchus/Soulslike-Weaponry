package net.soulsweaponry.items.sword;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.renderer.item.DarkinBladeRenderer;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.UltraHeavyWeapon;
import net.soulsweaponry.items.abilities.stoppedusing.SwordLeap;
import net.soulsweaponry.items.abilities.targetdamaged.Omnivamp;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAttributes;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.function.Consumer;

public class DarkinBlade extends UltraHeavyWeapon implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final DetonateGroundAttributes ATTRIBUTES = new DetonateGroundAttributes(
            WeaponConfig.darkin_blade_calculated_fall_base_radius,
            WeaponConfig.darkin_blade_calculated_fall_height_increase_radius_modifier,
            WeaponConfig.darkin_blade_calculated_fall_target_launch_modifier,
            WeaponConfig.darkin_blade_calculated_fall_target_max_launch_power,
            WeaponConfig.darkin_blade_calculated_fall_max_radius,
            WeaponConfig.darkin_blade_calculated_fall_max_damage,
            WeaponConfig.darkin_blade_calculated_fall_height_increase_damage_modifier,
            WeaponConfig.darkin_blade_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleTypes.FLAME, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {},
            (user, fallDistance, stack) -> {}
    );
    private static final SwordLeap SWORD_LEAP = new SwordLeap(
            WeaponConfig.darkin_blade_sword_leap_damage,
            WeaponConfig.darkin_blade_sword_leap_bonus_damage_per_level,
            (int) WeaponConfig.darkin_blade_sword_leap_calculated_fall_duration,
            WeaponConfig.darkin_blade_sword_leap_y_velocity,
            (int) WeaponConfig.darkin_blade_sword_leap_min_cooldown,
            (int) WeaponConfig.darkin_blade_sword_leap_cooldown,
            (int) WeaponConfig.darkin_blade_sword_leap_reduced_cooldown_per_level,
            WeaponConfig.darkin_blade_sword_leap_cooldown_mod_fully_charged,
            WeaponConfig.darkin_blade_sword_leap_cooldown_mod_not_fully_charged
    );
    private static final Omnivamp OMNIVAMP = new Omnivamp(
            WeaponConfig.darkin_blade_omnivamp_base_heal,
            WeaponConfig.darkin_blade_omnivamp_bonus_heal_per_level,
            (int) WeaponConfig.darkin_blade_omnivamp_min_cooldown,
            (int) WeaponConfig.darkin_blade_omnivamp_cooldown,
            (int) WeaponConfig.darkin_blade_omnivamp_reduced_cooldown_per_level
    );

    public DarkinBlade(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.darkin_blade_damage, WeaponConfig.darkin_blade_attack_speed, settings, (int) WeaponConfig.darkin_blade_posture_loss, ATTRIBUTES);
        this.addAbility(SWORD_LEAP, OMNIVAMP);
    }

    private PlayState predicate(AnimationState<?> event){
        event.getController().setAnimation(RawAnimation.begin().then("heartbeat", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "controller", 20, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private DarkinBladeRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new DarkinBladeRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_darkin_blade;
    }
}