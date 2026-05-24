package net.soulsweaponry.items.armor;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.armor.ChaosSetRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.armorattributes.Luck;
import net.soulsweaponry.items.abilities.immunity.EffectImmunity;
import net.soulsweaponry.items.abilities.inventorytick.FlipEffects;
import net.soulsweaponry.items.abilities.predicate.Equipped;
import net.soulsweaponry.registry.EffectRegistry;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Set;
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
            Set.of(EffectRegistry.DECAY.get()),
            (entity, declinedEffect, stack) -> {
                if (entity.age % 20 == 0) {
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 50, declinedEffect.getAmplifier()));
                }
            }
    );

    public ChaosCrown(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new ChaosSetRenderer<>();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public float[] getBleedBuildupResistances() {
        return ConfigConstructor.chaos_set_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ConfigConstructor.chaos_set_bleed_damage_resistances;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ConfigConstructor.chaos_set_posture_buildup_resistances;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ConfigConstructor.chaos_set_base_posture_increase;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_chaos_crown;
    }
}