package net.soulsweaponry.items.armor;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.soulsweaponry.client.renderer.armor.ChaosArmorRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.abilities.inventorytick.HalfHealthResistances;
import net.soulsweaponry.items.abilities.predicate.Equipped;
import net.soulsweaponry.items.abilities.userdamaged.Aftershock;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Arkenplate extends ModdedArmor implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    private static final HalfHealthResistances UNBREAKABLE = new HalfHealthResistances(
            ConfigConstructor.arkenplate_unbreakable_activate_percent_threshold,
            ConfigConstructor.arkenplate_unbreakable_activate_bonus_percent_threshold_per_level,
            (int) ConfigConstructor.arkenplate_unbreakable_resistance_amp,
            ConfigConstructor.arkenplate_unbreakable_resistance_bonus_amp_per_level,
            (int) ConfigConstructor.arkenplate_unbreakable_magic_resistance_amp,
            ConfigConstructor.arkenplate_unbreakable_magic_resistance_bonus_amp_per_level
    );
    private static final Aftershock AFTERSHOCK = new Aftershock(
            ConfigConstructor.arkenplate_aftershock_activate_percent_health_threshold,
            ConfigConstructor.arkenplate_aftershock_activate_bonus_percent_health_threshold_per_level,
            ConfigConstructor.arkenplate_aftershock_knockback,
            ConfigConstructor.arkenplate_aftershock_bonus_knockback_per_level,
            ConfigConstructor.arkenplate_aftershock_damage,
            ConfigConstructor.arkenplate_aftershock_bonus_damage_per_level,
            0, // Armor items can't have sharpness or something like that
            ConfigConstructor.arkenplate_aftershock_expansion_radius,
            (int) ConfigConstructor.arkenplate_aftershock_min_cooldown,
            (int) ConfigConstructor.arkenplate_aftershock_cooldown,
            (int) ConfigConstructor.arkenplate_aftershock_reduced_cooldown_per_level,
            List.of()
    );

    public Arkenplate(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
        this.addAbility(Equipped.CHEST_SLOT, UNBREAKABLE, AFTERSHOCK);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_arkenplate;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                if (this.renderer == null) {
                    this.renderer = new ChaosArmorRenderer<Arkenplate>();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
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