package net.soulsweaponry.items.armor;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.client.renderer.armor.ChaosSetRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;
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

    /**
     * Will contain harmful effects as the key, and the opposite beneficial effect as value
     */
    private static final HashMap<RegistryEntry<StatusEffect>, RegistryEntry<StatusEffect>> FLIPPABLE_EFFECTS = new HashMap<>();

    public ChaosCrown(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.addTooltipAbility(TooltipAbilities.EMPEROR, TooltipAbilities.EFFECT_REVERSAL);
    }

    private final Supplier<AttributeModifiersComponent> chaosModifiers = Suppliers.memoize(() -> {
        AttributeModifiersComponent vanilla = super.getAttributeModifiers();
        AttributeModifiersComponent.Builder builder = WeaponUtil.createAndCopyAttributes(vanilla);
        EquipmentSlot eqSlot = this.type.getEquipmentSlot();
        AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(eqSlot);
        EntityAttributeModifier luckMod = WeaponUtil.makeAttribute(EntityAttributes.GENERIC_LUCK, eqSlot, ConfigConstructor.chaos_crown_luck_given);
        builder.add(EntityAttributes.GENERIC_LUCK, luckMod, slot);
        return builder.build();
    });//TODO test

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player && this.isSlotActive(player, EquipmentSlot.HEAD)) {
            if (!player.getItemCooldownManager().isCoolingDown(ArmorRegistry.CHAOS_CROWN) && !player.getItemCooldownManager().isCoolingDown(ArmorRegistry.CHAOS_HELMET)) {
                this.flipEffects(player);
            }
        }
    }

    @Override
    public AttributeModifiersComponent getAttributeModifiers() {
        return this.chaosModifiers.get();
    }

    private void flipEffects(PlayerEntity player) {
        List<StatusEffectInstance> statusEffectsCopy = new ArrayList<>(player.getStatusEffects());
        List<RegistryEntry<StatusEffect>> effectsToRemove = new ArrayList<>();
        boolean triggered = false;
        for (StatusEffectInstance instance : statusEffectsCopy) {
            RegistryEntry<StatusEffect> effect = instance.getEffectType();
            if (effect.value().getCategory() == StatusEffectCategory.HARMFUL) {
                int duration = (int) (instance.getDuration() / 3f);
                int amplifier = (int) (instance.getAmplifier() / 2f);
                RegistryEntry<StatusEffect> newEffect = StatusEffects.REGENERATION;
                for (RegistryEntry<StatusEffect> harmful : FLIPPABLE_EFFECTS.keySet()) {
                    if (effect.equals(harmful)) {
                        newEffect = FLIPPABLE_EFFECTS.get(harmful);
                        break;
                    }
                }
                effectsToRemove.add(effect);
                triggered = true;
                player.addStatusEffect(new StatusEffectInstance(newEffect, duration, amplifier));
            }
        }
        for (RegistryEntry<StatusEffect> effectToRemove : effectsToRemove) {
            player.removeStatusEffect(effectToRemove);
        }
        if (triggered && !player.isCreative()) {
            player.getItemCooldownManager().set(ArmorRegistry.CHAOS_CROWN, (int) Math.max(ConfigConstructor.chaos_crown_flip_effect_min_cooldown, ConfigConstructor.chaos_crown_flip_effect_cooldown
                    - this.getReduceCooldownEnchantLevel(player.getEquippedStack(EquipmentSlot.HEAD)) * 40));
            player.getItemCooldownManager().set(ArmorRegistry.CHAOS_HELMET, (int) Math.max(ConfigConstructor.chaos_crown_flip_effect_min_cooldown, ConfigConstructor.chaos_crown_flip_effect_cooldown
                    - this.getReduceCooldownEnchantLevel(player.getEquippedStack(EquipmentSlot.HEAD)) * 40));
        }
    }

    static {
        FLIPPABLE_EFFECTS.put(StatusEffects.SLOWNESS, StatusEffects.SPEED);
        FLIPPABLE_EFFECTS.put(StatusEffects.MINING_FATIGUE, StatusEffects.HASTE);
        FLIPPABLE_EFFECTS.put(StatusEffects.WEAKNESS, StatusEffects.STRENGTH);
        FLIPPABLE_EFFECTS.put(StatusEffects.BLINDNESS, StatusEffects.NIGHT_VISION);
        FLIPPABLE_EFFECTS.put(StatusEffects.HUNGER, StatusEffects.SATURATION);
        FLIPPABLE_EFFECTS.put(StatusEffects.LEVITATION, StatusEffects.SLOW_FALLING);
    }

    @Override
    public Text[] getLoreTooltips() {
        return new Text[] {
                Text.translatable("tooltip.soulsweapons.chaos_crown_lore_1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_crown_lore_2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_crown_lore_3").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_crown_lore_4").formatted(Formatting.DARK_GRAY)
        };
    }

    @Override
    public boolean isSlotActive(PlayerEntity player, EquipmentSlot slot) {
        ItemStack stack = player.getEquippedStack(slot);
        return !stack.isEmpty() && !this.isDisabled(stack) && stack.getItem() instanceof ChaosCrown;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_chaos_crown;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.chaos_crown_flip_effect_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.chaos_crown_flip_effect_enchant_reduces_cooldown_ids;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> BipedEntityModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable BipedEntityModel<T> original) {
                if (this.renderer == null) {
                    this.renderer = new ChaosSetRenderer<ChaosCrown>();
                }
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
}
