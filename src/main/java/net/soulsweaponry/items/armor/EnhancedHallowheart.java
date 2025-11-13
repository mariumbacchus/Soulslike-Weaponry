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
import net.soulsweaponry.client.renderer.armor.WitheredArmorRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.abilitykeybind.LifeLeach;
import net.soulsweaponry.items.abilities.immunity.EffectImmunity;
import net.soulsweaponry.items.abilities.inventorytick.BasicInventoryTickAbility;
import net.soulsweaponry.items.abilities.inventorytick.Exalt;
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

public class EnhancedHallowheart extends ModdedArmor implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final EffectImmunity WITHER_IMMUNITY = new EffectImmunity(Set.of(StatusEffects.WITHER));
    private static final LifeLeach LIFE_LEACH = new LifeLeach(
            (int) ConfigConstructor.enhanced_withered_chest_unceasing_life_leach_duration,
            ConfigConstructor.enhanced_withered_chest_unceasing_life_leach_duration_per_level,
            (int) ConfigConstructor.enhanced_withered_chest_unceasing_life_leach_amplifier,
            ConfigConstructor.enhanced_withered_chest_unceasing_life_leach_amplifier_per_level,
            (int) ConfigConstructor.enhanced_withered_chest_unceasing_min_cooldown,
            (int) ConfigConstructor.enhanced_withered_chest_unceasing_cooldown,
            (int) ConfigConstructor.enhanced_withered_chest_unceasing_reduced_cooldown_per_level
    );
    private static final Infectious INFECTIOUS = new Infectious(
            ConfigConstructor.enhanced_withered_chest_infectious_damage,
            ConfigConstructor.enhanced_withered_chest_infectious_damage_per_level,
            ConfigConstructor.enhanced_withered_chest_infectious_knockback,
            ConfigConstructor.enhanced_withered_chest_infectious_knockback_per_level,
            List.of(StatusEffects.WITHER),
            (int) ConfigConstructor.enhanced_withered_chest_infectious_apply_wither_duration,
            ConfigConstructor.enhanced_withered_chest_infectious_apply_wither_duration_per_level,
            (int) ConfigConstructor.enhanced_withered_chest_infectious_apply_wither_amplifier,
            ConfigConstructor.enhanced_withered_chest_infectious_apply_wither_amplifier_per_level,
            (int) ConfigConstructor.enhanced_withered_chest_infectious_apply_fire_seconds,
            (int) ConfigConstructor.enhanced_withered_chest_infectious_apply_fire_seconds_per_level
    );
    private static final BasicInventoryTickAbility FIRE_RESISTANCE = new BasicInventoryTickAbility(
            (stack, world, entity, slot, equipped) -> entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 100, 0, false, false)),
            List.of(
                    Text.translatable("tooltip.soulsweapons.fire_immune").formatted(Formatting.GOLD),
                    Text.translatable("tooltip.soulsweapons.fire_immune.1").formatted(Formatting.GRAY)
            ), 20
    );
    private static final Exalt EXALT = new Exalt(
            ConfigConstructor.enhanced_withered_chest_exalt_amp_per_missing_health_percent,
            ConfigConstructor.enhanced_withered_chest_exalt_amp_per_missing_health_percent_bonus_per_level,
            (int) ConfigConstructor.enhanced_withered_chest_exalt_amp_max,
            ConfigConstructor.enhanced_withered_chest_exalt_amp_max_increase_per_level,
            (int) ConfigConstructor.enhanced_withered_chest_exalt_duration
    );

    public EnhancedHallowheart(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.addAbility(Equipped.CHEST_SLOT, WITHER_IMMUNITY, LIFE_LEACH, INFECTIOUS, FIRE_RESISTANCE, EXALT);
    }

    @Override
    public List<Text> getItemLore() {
        return List.of(
                Text.translatable("tooltip.soulsweapons.withered_chest.lore.enhanced.1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.withered_chest.lore.enhanced.2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.withered_chest.lore.enhanced.3").formatted(Formatting.DARK_GRAY)
        );
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_enhanced_hallowheart;
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

    public PlayState heartAnimation(AnimationState<?> event) {
        //Note: maybe figure out how to use ISyncable and add different animations (already in .animations.json).
        event.getController().setAnimation(RawAnimation.begin().thenPlay("idle_heartbeat"));
        return PlayState.CONTINUE;
    }

    public PlayState soulsAnimation(AnimationState<?> event) {
        event.getController().setAnimation(RawAnimation.begin().thenPlay("soul_spin"));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "souls", 0, this::soulsAnimation));
        controllerRegistrar.add(new AnimationController<>(this, "heart", 0, this::heartAnimation));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ConfigConstructor.enhanced_withered_armor_base_posture_increase;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ConfigConstructor.enhanced_withered_armor_posture_buildup_resistances;
    }

    @Override
    public float[] getBleedBuildupResistances() {
        return ConfigConstructor.enhanced_withered_armor_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ConfigConstructor.enhanced_withered_armor_bleed_damage_resistances;
    }
}
