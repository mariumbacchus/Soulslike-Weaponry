package net.soulsweaponry.items.armor;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentModel;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.client.renderer.armor.ChaosSetRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.armorattributes.Luck;
import net.soulsweaponry.items.abilities.immunity.EffectImmunity;
import net.soulsweaponry.items.abilities.inventorytick.FlipEffects;
import net.soulsweaponry.items.abilities.predicate.Equipped;
import net.soulsweaponry.registry.EffectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;
import java.util.function.Consumer;

public class ChaosCrown extends ModdedArmor implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final Luck LUCK = new Luck(ConfigConstructor.chaos_crown_luck_given);
    private static final FlipEffects FLIP_EFFECTS = new FlipEffects(
            ConfigConstructor.chaos_crown_flip_effect_duration_mod,
            ConfigConstructor.chaos_crown_flip_effect_amp_mod,
            (int) ConfigConstructor.chaos_crown_flip_effect_min_cooldown,
            (int) ConfigConstructor.chaos_crown_flip_effect_cooldown,
            (int) ConfigConstructor.chaos_crown_flip_effect_reduced_cooldown_per_level
    );
    private static final EffectImmunity DECAY_IMMUNITY = new EffectImmunity(
            Set.of(EffectRegistry.DECAY),
            (entity, declinedEffect, stack) -> {
                if (entity.age % 20 == 0) {
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 50, declinedEffect.getAmplifier()));
                }
            }
    );

    public ChaosCrown(ArmorMaterial material, EquipmentType type, Settings settings, List<IAbility> attributeAbilities) {
        super(material, type, settings, attributeAbilities);
        this.addAbility(Equipped.HEAD_SLOT, LUCK, DECAY_IMMUNITY, FLIP_EFFECTS);
    }

    public ChaosCrown(ArmorMaterial material, EquipmentType type, Settings settings) {
        super(material, type, settings, applyCustomAttributeAbilities(
                ConfigConstructor.chaos_set_posture_buildup_resistances,
                ConfigConstructor.chaos_set_base_posture_increase,
                ConfigConstructor.chaos_set_bleed_buildup_resistances,
                ConfigConstructor.chaos_set_bleed_damage_resistances
        ));
        this.addAbility(Equipped.HEAD_SLOT, LUCK, DECAY_IMMUNITY, FLIP_EFFECTS);
    }

    @Override
    public List<Text> getItemLore() {
        return List.of(
                Text.translatable("tooltip.soulsweapons.chaos_crown_lore_1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_crown_lore_2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_crown_lore_3").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_crown_lore_4").formatted(Formatting.DARK_GRAY)
        );
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_chaos_crown;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <E extends LivingEntity, S extends BipedEntityRenderState> @NotNull BipedEntityModel<?> getGeoArmorRenderer(@Nullable E livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, EquipmentModel.LayerType type, BipedEntityModel<S> original) {
                if (this.renderer == null) {
                    this.renderer = new ChaosSetRenderer<ChaosCrown>();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}
