package net.soulsweaponry.items.hammer;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.client.renderer.item.NightfallRenderer;
import net.soulsweaponry.config.WeaponConfig;
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
            WeaponConfig.nightfall_calculated_fall_base_radius,
            WeaponConfig.nightfall_calculated_fall_height_increase_radius_modifier,
            WeaponConfig.nightfall_calculated_fall_target_launch_modifier,
            WeaponConfig.nightfall_calculated_fall_target_max_launch_power,
            WeaponConfig.nightfall_calculated_fall_max_radius,
            WeaponConfig.nightfall_calculated_fall_max_damage,
            WeaponConfig.nightfall_calculated_fall_height_increase_damage_modifier,
            WeaponConfig.nightfall_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleTypes.SOUL_FIRE_FLAME, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {},
            (user, fallDistance, stack) -> {}
    );
    private static final Unbreakable UNBREAKABLE = new Unbreakable(
            (int) WeaponConfig.nightfall_shield_min_cooldown, (int) WeaponConfig.nightfall_shield_cooldown,
            (int) WeaponConfig.nightfall_shield_reduced_cooldown_per_level,
            (int) WeaponConfig.nightfall_shield_duration,
            (int) WeaponConfig.nightfall_shield_absorption_amp,
            (int) WeaponConfig.nightfall_shield_resistance_amp
    );
    private static final Obliterate OBLITERATE = new Obliterate(
            WeaponConfig.nightfall_obliterate_base_damage,
            WeaponConfig.nightfall_obliterate_bonus_damage_per_level,
            WeaponConfig.nightfall_obliterate_enchant_bonus_damage_modifier,
            WeaponConfig.nightfall_obliterate_y_velocity_launch_power,
            WeaponConfig.nightfall_obliterate_aoe_expansion,
            WeaponConfig.nightfall_obliterate_range_outwards,
            (int) WeaponConfig.nightfall_obliterate_min_cooldown,
            (int) WeaponConfig.nightfall_obliterate_cooldown,
            (int) WeaponConfig.nightfall_obliterate_reduced_cooldown_per_level
    );
    private static final SummonRemnant SUMMON_REMNANT = new SummonRemnant(
            WeaponConfig.nightfall_allow_non_undead_to_maybe_be_summoned, WeaponConfig.nightfall_summon_chance,
            (int) WeaponConfig.nightfall_summoned_allies_cap, "NightfallSummons"
    );

    public Nightfall(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.nightfall_damage, WeaponConfig.nightfall_attack_speed, settings, (int) WeaponConfig.nightfall_posture_loss, ATTRIBUTES);
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
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_nightfall;
    }

    @Override
    public List<Text> getItemLore() {
        return List.of(
                Text.translatable("tooltip.soulsweapons.nightfall.part_1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.nightfall.part_2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.nightfall.part_3").formatted(Formatting.DARK_GRAY)
        );
    }
}