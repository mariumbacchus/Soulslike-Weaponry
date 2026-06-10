package net.soulsweaponry.items.armor;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.client.renderer.armor.EChaosArmorRenderer;
import net.soulsweaponry.config.ArmorConfig;
import net.soulsweaponry.items.abilities.inventorytick.HalfHealthResistances;
import net.soulsweaponry.items.abilities.predicate.Equipped;
import net.soulsweaponry.items.abilities.userdamaged.Aftershock;
import net.soulsweaponry.items.abilities.userdamaged.Mirror;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class EnhancedArkenplate extends ModdedArmor implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final HalfHealthResistances UNBREAKABLE = new HalfHealthResistances(
            ArmorConfig.enhanced_arkenplate_unbreakable_activate_percent_threshold,
            ArmorConfig.enhanced_arkenplate_unbreakable_activate_bonus_percent_threshold_per_level,
            (int) ArmorConfig.enhanced_arkenplate_unbreakable_resistance_amp,
            ArmorConfig.enhanced_arkenplate_unbreakable_resistance_bonus_amp_per_level,
            (int) ArmorConfig.enhanced_arkenplate_unbreakable_magic_resistance_amp,
            ArmorConfig.enhanced_arkenplate_unbreakable_magic_resistance_bonus_amp_per_level
    );
    private static final Aftershock AFTERSHOCK = new Aftershock(
            ArmorConfig.enhanced_arkenplate_aftershock_activate_percent_health_threshold,
            ArmorConfig.enhanced_arkenplate_aftershock_activate_bonus_percent_health_threshold_per_level,
            ArmorConfig.enhanced_arkenplate_aftershock_knockback,
            ArmorConfig.enhanced_arkenplate_aftershock_bonus_knockback_per_level,
            ArmorConfig.enhanced_arkenplate_aftershock_damage,
            ArmorConfig.enhanced_arkenplate_aftershock_bonus_damage_per_level,
            0, // Armor items can't have sharpness or something like that
            ArmorConfig.enhanced_arkenplate_aftershock_expansion_radius,
            (int) ArmorConfig.enhanced_arkenplate_aftershock_min_cooldown,
            (int) ArmorConfig.enhanced_arkenplate_aftershock_cooldown,
            (int) ArmorConfig.enhanced_arkenplate_aftershock_reduced_cooldown_per_level,
            List.of(
                    new StatusEffectInstance(StatusEffects.WEAKNESS,
                    (int) ArmorConfig.enhanced_arkenplate_aftershock_weakness_duration,
                    (int) ArmorConfig.enhanced_arkenplate_aftershock_weakness_amp)
            )
    );
    private static final Mirror MIRROR = new Mirror(
            ArmorConfig.enhanced_arkenplate_mirror_trigger_percent,
            ArmorConfig.enhanced_arkenplate_mirror_bonus_trigger_percent_per_level
    );

    public EnhancedArkenplate(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.addAbility(Equipped.CHEST_SLOT, UNBREAKABLE, AFTERSHOCK, MIRROR);
    }

    @Override
    public List<Text> getItemLore() {
        return List.of(
                Text.translatable("tooltip.soulsweapons.arkenplate_lore_1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.arkenplate_lore_2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.arkenplate_lore_3").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.arkenplate_lore_4").formatted(Formatting.DARK_GRAY)
        );
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ArmorConfig.disable_use_enhanced_arkenplate;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> BipedEntityModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable BipedEntityModel<T> original) {
                if (this.renderer == null) {
                    this.renderer = new EChaosArmorRenderer<EnhancedArkenplate>();
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
        event.getController().setAnimation(RawAnimation.begin().thenPlay("soul_spin"));
        return PlayState.CONTINUE;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ArmorConfig.enhanced_chaos_armor_base_posture_increase;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ArmorConfig.enhanced_chaos_armor_posture_buildup_resistances;
    }

    @Override
    public float[] getBleedBuildupResistances() {
        return ArmorConfig.enhanced_chaos_armor_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ArmorConfig.enhanced_chaos_armor_bleed_damage_resistances;
    }
}
