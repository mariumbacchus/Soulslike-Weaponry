package net.soulsweaponry.items.spear;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.item.DraupnirSpearItemRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.abilitykeybind.ExplodeSavedEntities;
import net.soulsweaponry.items.abilities.abilitykeybind.sneaking.SummonDraupnirSpears;
import net.soulsweaponry.items.abilities.stoppedusing.ThrowDraupnirSpear;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class DraupnirSpear extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final ThrowDraupnirSpear THROW_DRAUPNIR_SPEAR = new ThrowDraupnirSpear(
            5f, (int) ConfigConstructor.draupnir_spear_throw_min_cooldown,
            (int) ConfigConstructor.draupnir_spear_throw_cooldown,
            (int) ConfigConstructor.draupnir_spear_throw_reduced_cooldown_per_level
    );
    private static final SummonDraupnirSpears SUMMON_DRAUPNIR_SPEARS = new SummonDraupnirSpears(
            ConfigConstructor.draupnir_spear_summon_spears_range_out,
            (int) ConfigConstructor.draupnir_spear_summon_spears_amount,
            (int) ConfigConstructor.draupnir_spear_summon_spears_min_cooldown,
            (int) ConfigConstructor.draupnir_spear_summon_spears_cooldown,
            (int) ConfigConstructor.draupnir_spear_summon_spears_reduced_cooldown_per_level
    );
    private static final ExplodeSavedEntities EXPLODE_SAVED_ENTITIES = new ExplodeSavedEntities(
            ConfigConstructor.draupnir_spear_user_explosion_radius,
            ConfigConstructor.draupnir_spear_user_explosion_base_damage,
            ConfigConstructor.draupnir_spear_user_explosion_bonus_damage_per_level,
            ConfigConstructor.draupnir_spear_user_explosion_bonus_enchant_damage_mod,
            ConfigConstructor.draupnir_spear_user_explosion_knockup,

            ConfigConstructor.draupnir_spear_explode_spears_base_explosion_power,
            ConfigConstructor.draupnir_spear_explode_spears_bonus_explosion_power_per_level,
            ConfigConstructor.draupnir_spear_explode_spears_power_needed_to_apply_weakness,
            (int) ConfigConstructor.draupnir_spear_explode_spears_weakness_duration,
            (int) ConfigConstructor.draupnir_spear_explode_spears_weakness_base_amp,
            ConfigConstructor.draupnir_spear_explode_spears_weakness_bonus_amp_per_power,

            (int) ConfigConstructor.draupnir_spear_explode_spears_min_cooldown,
            (int) ConfigConstructor.draupnir_spear_explode_spears_cooldown,
            (int) ConfigConstructor.draupnir_spear_explode_spears_reduced_cooldown_per_level
    );

    public DraupnirSpear(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.draupnir_spear_damage, ConfigConstructor.draupnir_spear_attack_speed, settings);
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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final DraupnirSpearItemRenderer renderer = new DraupnirSpearItemRenderer();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_draupnir_spear;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_draupnir_spear;
    }
}