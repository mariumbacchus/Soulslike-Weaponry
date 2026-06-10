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
import net.soulsweaponry.client.renderer.armor.ChaosSetRenderer;
import net.soulsweaponry.config.ArmorConfig;
import net.soulsweaponry.items.abilities.immunity.EffectImmunity;
import net.soulsweaponry.items.abilities.inventorytick.CorruptGround;
import net.soulsweaponry.items.abilities.predicate.Equipped;
import net.soulsweaponry.registry.EffectRegistry;
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

public class ChaosRobes extends ModdedArmor implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final EffectImmunity DECAY_IMMUNITY = new EffectImmunity(Set.of(EffectRegistry.DECAY));
    public static final CorruptGround CORRUPT_GROUND = new CorruptGround(
            (int) ArmorConfig.chaos_cape_corrupt_ground_range,
            ArmorConfig.chaos_cape_corrupt_ground_range_per_level,
            ArmorConfig.chaos_cape_corrupt_ground_status_effect_range,
            ArmorConfig.chaos_cape_corrupt_ground_status_effect_range_per_level,
            List.of(
                    new StatusEffectInstance(StatusEffects.WITHER,
                            (int) ArmorConfig.chaos_cape_corrupt_ground_status_effect_wither_duration,
                            (int) ArmorConfig.chaos_cape_corrupt_ground_status_effect_wither_amp
                    )
            )
    );

    public ChaosRobes(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.addAbility(Equipped.CHEST_SLOT, DECAY_IMMUNITY, CORRUPT_GROUND);
    }

    @Override
    public List<Text> getItemLore() {
        return List.of(
                Text.translatable("tooltip.soulsweapons.chaos_robes_lore_1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_robes_lore_2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_robes_lore_3").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_robes_lore_4").formatted(Formatting.DARK_GRAY)
        );
    }

    private PlayState predicate(AnimationState<?> event) {
        event.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ArmorConfig.disable_use_chaos_robes;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> BipedEntityModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable BipedEntityModel<T> original) {
                if (this.renderer == null) {
                    this.renderer = new ChaosSetRenderer<ChaosRobes>();
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

    @Override
    public float[] getBleedBuildupResistances() {
        return ArmorConfig.chaos_set_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ArmorConfig.chaos_set_bleed_damage_resistances;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ArmorConfig.chaos_set_posture_buildup_resistances;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ArmorConfig.chaos_set_base_posture_increase;
    }
}
