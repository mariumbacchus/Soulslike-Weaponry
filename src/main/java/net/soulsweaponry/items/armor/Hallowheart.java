package net.soulsweaponry.items.armor;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.client.renderer.armor.WitheredArmorRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.abilitykeybind.LifeLeach;
import net.soulsweaponry.items.abilities.immunity.EffectImmunity;
import net.soulsweaponry.items.abilities.predicate.Equipped;
import net.soulsweaponry.items.abilities.userdamaged.Infectious;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class Hallowheart extends ModdedArmor implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final EffectImmunity WITHER_IMMUNITY = new EffectImmunity(Set.of(StatusEffects.WITHER));
    private static final LifeLeach LIFE_LEACH = new LifeLeach(
            (int) ConfigConstructor.withered_chest_unceasing_life_leach_duration,
            ConfigConstructor.withered_chest_unceasing_life_leach_duration_per_level,
            (int) ConfigConstructor.withered_chest_unceasing_life_leach_amplifier,
            ConfigConstructor.withered_chest_unceasing_life_leach_amplifier_per_level,
            (int) ConfigConstructor.withered_chest_unceasing_min_cooldown,
            (int) ConfigConstructor.withered_chest_unceasing_cooldown,
            (int) ConfigConstructor.withered_chest_unceasing_reduced_cooldown_per_level
    );
    private static final Infectious INFECTIOUS = new Infectious(
            ConfigConstructor.withered_chest_infectious_damage,
            ConfigConstructor.withered_chest_infectious_damage_per_level,
            ConfigConstructor.withered_chest_infectious_knockback,
            ConfigConstructor.withered_chest_infectious_knockback_per_level,
            List.of(StatusEffects.WITHER),
            (int) ConfigConstructor.withered_chest_infectious_apply_wither_duration,
            ConfigConstructor.withered_chest_infectious_apply_wither_duration_per_level,
            (int) ConfigConstructor.withered_chest_infectious_apply_wither_amplifier,
            ConfigConstructor.withered_chest_infectious_apply_wither_amplifier_per_level,
            (int) ConfigConstructor.withered_chest_infectious_apply_fire_seconds,
            (int) ConfigConstructor.withered_chest_infectious_apply_fire_seconds_per_level
    );

    public Hallowheart(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.addAbility(Equipped.CHEST_SLOT, WITHER_IMMUNITY, LIFE_LEACH, INFECTIOUS);
    }

    @Override
    public List<Text> getItemLore() {
        return List.of(
                Text.translatable("tooltip.soulsweapons.withered_chest.lore.1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.withered_chest.lore.2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.withered_chest.lore.3").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.withered_chest.lore.4").formatted(Formatting.DARK_GRAY)
        );
    }

    public PlayState soulsAnimation(AnimationState<?> event) {
        event.getController().setAnimation(RawAnimation.begin().thenPlay("no_souls"));
        return PlayState.CONTINUE;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_hallowheart;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> BipedEntityModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable BipedEntityModel<T> original) {
                if (this.renderer == null) {
                    this.renderer = new WitheredArmorRenderer<Hallowheart>();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "souls", 0, this::soulsAnimation));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ConfigConstructor.withered_armor_base_posture_increase;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ConfigConstructor.withered_armor_posture_buildup_resistances;
    }

    @Override
    public float[] getBleedBuildupResistances() {
        return ConfigConstructor.withered_armor_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ConfigConstructor.withered_armor_bleed_damage_resistances;
    }
}
