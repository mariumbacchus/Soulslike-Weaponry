package net.soulsweaponry.items.sword;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.renderer.item.DarkinBladeRenderer;
import net.soulsweaponry.config.ConfigConstructor;
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
            ConfigConstructor.darkin_blade_calculated_fall_base_radius,
            ConfigConstructor.darkin_blade_calculated_fall_height_increase_radius_modifier,
            ConfigConstructor.darkin_blade_calculated_fall_target_launch_modifier,
            ConfigConstructor.darkin_blade_calculated_fall_target_max_launch_power,
            ConfigConstructor.darkin_blade_calculated_fall_max_radius,
            ConfigConstructor.darkin_blade_calculated_fall_max_damage,
            ConfigConstructor.darkin_blade_calculated_fall_height_increase_damage_modifier,
            ConfigConstructor.darkin_blade_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleTypes.FLAME, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {},
            (user, fallDistance, stack) -> {}
    );
    private static final SwordLeap SWORD_LEAP = new SwordLeap(
            ConfigConstructor.darkin_blade_sword_leap_damage,
            ConfigConstructor.darkin_blade_sword_leap_bonus_damage_per_level,
            (int) ConfigConstructor.darkin_blade_sword_leap_calculated_fall_duration,
            ConfigConstructor.darkin_blade_sword_leap_y_velocity,
            (int) ConfigConstructor.darkin_blade_sword_leap_min_cooldown,
            (int) ConfigConstructor.darkin_blade_sword_leap_cooldown,
            (int) ConfigConstructor.darkin_blade_sword_leap_reduced_cooldown_per_level,
            ConfigConstructor.darkin_blade_sword_leap_cooldown_mod_fully_charged,
            ConfigConstructor.darkin_blade_sword_leap_cooldown_mod_not_fully_charged
    );
    private static final Omnivamp OMNIVAMP = new Omnivamp(
            ConfigConstructor.darkin_blade_omnivamp_base_heal,
            ConfigConstructor.darkin_blade_omnivamp_bonus_heal_per_level,
            (int) ConfigConstructor.darkin_blade_omnivamp_min_cooldown,
            (int) ConfigConstructor.darkin_blade_omnivamp_cooldown,
            (int) ConfigConstructor.darkin_blade_omnivamp_reduced_cooldown_per_level
    );

    public DarkinBlade(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.darkin_blade_damage, ConfigConstructor.darkin_blade_attack_speed, settings, (int) ConfigConstructor.darkin_blade_posture_loss, ATTRIBUTES);
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
        return ConfigConstructor.disable_use_darkin_blade;
    }
}