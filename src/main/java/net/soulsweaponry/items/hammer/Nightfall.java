package net.soulsweaponry.items.hammer;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.renderer.item.NightfallRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.UltraHeavyWeapon;
import net.soulsweaponry.items.abilities.abilitykeybind.Unbreakable;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAttributes;
import net.soulsweaponry.items.abilities.stoppedusing.Obliterate;
import net.soulsweaponry.items.abilities.targetdeath.SummonRemnant;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;
import java.util.function.Consumer;

public class Nightfall extends UltraHeavyWeapon implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final DetonateGroundAttributes ATTRIBUTES = new DetonateGroundAttributes(
            ConfigConstructor.nightfall_calculated_fall_base_radius,
            ConfigConstructor.nightfall_calculated_fall_height_increase_radius_modifier,
            ConfigConstructor.nightfall_calculated_fall_target_launch_modifier,
            ConfigConstructor.nightfall_calculated_fall_target_max_launch_power,
            ConfigConstructor.nightfall_calculated_fall_max_radius,
            ConfigConstructor.nightfall_calculated_fall_max_damage,
            ConfigConstructor.nightfall_calculated_fall_height_increase_damage_modifier,
            ConfigConstructor.nightfall_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleTypes.SOUL_FIRE_FLAME, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {},
            (user, fallDistance, stack) -> {}
    );
    private static final Unbreakable UNBREAKABLE = new Unbreakable(
            (int) ConfigConstructor.nightfall_shield_min_cooldown, (int) ConfigConstructor.nightfall_shield_cooldown,
            (int) ConfigConstructor.nightfall_shield_reduced_cooldown_per_level,
            (int) ConfigConstructor.nightfall_shield_duration,
            (int) ConfigConstructor.nightfall_shield_absorption_amp,
            (int) ConfigConstructor.nightfall_shield_resistance_amp
    );
    private static final Obliterate OBLITERATE = new Obliterate(
            ConfigConstructor.nightfall_obliterate_base_damage,
            ConfigConstructor.nightfall_obliterate_bonus_damage_per_level,
            ConfigConstructor.nightfall_obliterate_enchant_bonus_damage_modifier,
            ConfigConstructor.nightfall_obliterate_y_velocity_launch_power,
            (int) ConfigConstructor.nightfall_obliterate_min_cooldown,
            (int) ConfigConstructor.nightfall_obliterate_cooldown,
            (int) ConfigConstructor.nightfall_obliterate_reduced_cooldown_per_level
    );
    private static final SummonRemnant SUMMON_REMNANT = new SummonRemnant(
            ConfigConstructor.nightfall_allow_non_undead_to_maybe_be_summoned, ConfigConstructor.nightfall_summon_chance,
            (int) ConfigConstructor.nightfall_summoned_allies_cap, "NightfallSummons"
    );

    public Nightfall(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.nightfall_damage, ConfigConstructor.nightfall_attack_speed, settings, (int) ConfigConstructor.nightfall_posture_loss, ATTRIBUTES);
        this.addAbility(OBLITERATE, UNBREAKABLE, SUMMON_REMNANT);
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
            private NightfallRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new NightfallRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[] {
                Text.translatable("tooltip.soulsweapons.nightfall.part_1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.nightfall.part_2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.nightfall.part_3").formatted(Formatting.DARK_GRAY)
        };
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_nightfall;
    }
}