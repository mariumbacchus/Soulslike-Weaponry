package net.soulsweaponry.items.spear;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.DraupnirSpearItemRenderer;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.abilitykeybind.ExplodeSavedEntities;
import net.soulsweaponry.items.abilities.abilitykeybind.sneaking.SummonDraupnirSpears;
import net.soulsweaponry.items.abilities.stoppedusing.ThrowDraupnirSpear;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class DraupnirSpear extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final ThrowDraupnirSpear THROW_DRAUPNIR_SPEAR = new ThrowDraupnirSpear(
            5f, (int) WeaponConfig.draupnir_spear_throw_min_cooldown,
            (int) WeaponConfig.draupnir_spear_throw_cooldown,
            (int) WeaponConfig.draupnir_spear_throw_reduced_cooldown_per_level
    );
    private static final SummonDraupnirSpears SUMMON_DRAUPNIR_SPEARS = new SummonDraupnirSpears(
            WeaponConfig.draupnir_spear_summon_spears_range_out,
            (int) WeaponConfig.draupnir_spear_summon_spears_amount,
            (int) WeaponConfig.draupnir_spear_summon_spears_min_cooldown,
            (int) WeaponConfig.draupnir_spear_summon_spears_cooldown,
            (int) WeaponConfig.draupnir_spear_summon_spears_reduced_cooldown_per_level
    );
    private static final ExplodeSavedEntities EXPLODE_SAVED_ENTITIES = new ExplodeSavedEntities(
            WeaponConfig.draupnir_spear_user_explosion_radius,
            WeaponConfig.draupnir_spear_user_explosion_base_damage,
            WeaponConfig.draupnir_spear_user_explosion_bonus_damage_per_level,
            WeaponConfig.draupnir_spear_user_explosion_bonus_enchant_damage_mod,
            WeaponConfig.draupnir_spear_user_explosion_knockup,

            WeaponConfig.draupnir_spear_explode_spears_base_explosion_power,
            WeaponConfig.draupnir_spear_explode_spears_bonus_explosion_power_per_level,
            WeaponConfig.draupnir_spear_explode_spears_power_needed_to_apply_weakness,
            (int) WeaponConfig.draupnir_spear_explode_spears_weakness_duration,
            (int) WeaponConfig.draupnir_spear_explode_spears_weakness_base_amp,
            WeaponConfig.draupnir_spear_explode_spears_weakness_bonus_amp_per_power,

            (int) WeaponConfig.draupnir_spear_explode_spears_min_cooldown,
            (int) WeaponConfig.draupnir_spear_explode_spears_cooldown,
            (int) WeaponConfig.draupnir_spear_explode_spears_reduced_cooldown_per_level
    );

    public DraupnirSpear(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.draupnir_spear_damage, WeaponConfig.draupnir_spear_attack_speed, settings);
        this.addAbility(THROW_DRAUPNIR_SPEAR, SUMMON_DRAUPNIR_SPEARS, EXPLODE_SAVED_ENTITIES);
    }

    private PlayState predicate(AnimationState<?> event){
        event.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private DraupnirSpearItemRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new DraupnirSpearItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_draupnir_spear;
    }
}