package net.soulsweaponry.items.armor;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.soulsweaponry.client.renderer.armor.ChaosArmorRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.inventorytick.HalfHealthResistances;
import net.soulsweaponry.items.abilities.predicate.Equipped;
import net.soulsweaponry.items.abilities.userdamaged.Aftershock;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class Arkenplate extends ModdedArmor implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final HalfHealthResistances UNBREAKABLE = new HalfHealthResistances(
            ConfigConstructor.arkenplate_unbreakable_activate_percent_threshold,
            (int) ConfigConstructor.arkenplate_unbreakable_resistance_amp,
            ConfigConstructor.arkenplate_unbreakable_resistance_bonus_amp_per_level,
            (int) ConfigConstructor.arkenplate_unbreakable_magic_resistance_amp,
            ConfigConstructor.arkenplate_unbreakable_magic_resistance_bonus_amp_per_level
    );
    private static final Aftershock AFTERSHOCK = new Aftershock(
            ConfigConstructor.arkenplate_aftershock_activate_percent_health_threshold,
            ConfigConstructor.arkenplate_aftershock_knockback,
            ConfigConstructor.arkenplate_aftershock_bonus_knockback_per_level,
            ConfigConstructor.arkenplate_aftershock_damage,
            ConfigConstructor.arkenplate_aftershock_bonus_damage_per_level,
            0, // Armor items can't have sharpness or something like that
            ConfigConstructor.arkenplate_aftershock_expansion_radius,
            (int) ConfigConstructor.arkenplate_aftershock_min_cooldown,
            (int) ConfigConstructor.arkenplate_aftershock_cooldown,
            (int) ConfigConstructor.arkenplate_aftershock_reduced_cooldown_per_level,
            List.of(
                    new StatusEffectInstance(StatusEffects.WEAKNESS, 160, 2),//TODO weakness is only meant for enhanced arkenplate
                    new StatusEffectInstance(StatusEffects.STRENGTH, 160, 2)//TODO only for testing
            )
    );

    public Arkenplate(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.addAbility(Equipped.CHEST_SLOT, UNBREAKABLE, AFTERSHOCK);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_arkenplate;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> BipedEntityModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable BipedEntityModel<T> original) {
                if (this.renderer == null) {
                    this.renderer = new ChaosArmorRenderer<Arkenplate>();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    public PlayState predicate(AnimationState<?> event) {
        event.getController().setAnimation(RawAnimation.begin().thenPlay("no_souls"));
        return PlayState.CONTINUE;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ConfigConstructor.chaos_armor_base_posture_increase;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ConfigConstructor.chaos_armor_posture_buildup_resistances;
    }

    @Override
    public float[] getBleedBuildupResistances() {
        return ConfigConstructor.chaos_armor_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ConfigConstructor.chaos_armor_bleed_damage_resistances;
    }
}