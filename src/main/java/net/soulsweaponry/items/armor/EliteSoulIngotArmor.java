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
import net.soulsweaponry.client.renderer.armor.EliteSoulIngotArmorRenderer;
import net.soulsweaponry.config.ArmorConfig;
import net.soulsweaponry.items.abilities.inventorytick.BasicInventoryTickAbility;
import net.soulsweaponry.items.abilities.predicate.FullSetEquipped;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class EliteSoulIngotArmor extends ModdedArmor implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final FullSetEquipped SET_BONUS = new FullSetEquipped(
            () -> ArmorRegistry.ELITE_SOUL_INGOT_HELMET,
            () -> ArmorRegistry.SOUL_INGOT_CHESTPLATE,
            () -> ArmorRegistry.SOUL_INGOT_LEGGINGS,
            () -> ArmorRegistry.SOUL_INGOT_BOOTS
    );
    private static final BasicInventoryTickAbility RESISTANCE = new BasicInventoryTickAbility(
            (stack, world, entity, slot, selected) -> entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,
                    (int) ArmorConfig.elite_soul_ingot_armor_fortified_resistance_duration,
                    (int) (ArmorConfig.elite_soul_ingot_armor_fortified_resistance_amp
                            + ArmorConfig.elite_soul_ingot_armor_fortified_resistance_amp_per_level * WeaponUtil.getUpgradeLevel(stack)),
                    false, false)),
            List.of(
                    Text.translatable("tooltip.soulsweapons.fortified").formatted(Formatting.BLUE),
                    Text.translatable("tooltip.soulsweapons.fortified.1", StatusEffects.RESISTANCE.value().getName()).formatted(Formatting.GRAY)
            ), 20
    );

    public EliteSoulIngotArmor(RegistryEntry<ArmorMaterial> material, Type slot, Settings settings) {
        super(material, slot, settings);
        this.addAbility(SET_BONUS, RESISTANCE);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> BipedEntityModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable BipedEntityModel<T> original) {
                if (this.renderer == null) {
                    this.renderer = new EliteSoulIngotArmorRenderer<>();
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

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ArmorConfig.disable_use_elite_soul_ingot_armor;
    }

    @Override
    public float[] getBleedBuildupResistances() {
        return ArmorConfig.elite_soul_ingot_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ArmorConfig.elite_soul_ingot_bleed_damage_resistances;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ArmorConfig.elite_soul_ingot_posture_buildup_resistances;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ArmorConfig.elite_soul_ingot_base_posture_increase;
    }
}