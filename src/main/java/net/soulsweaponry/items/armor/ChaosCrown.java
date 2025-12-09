package net.soulsweaponry.items.armor;

import net.minecraft.client.render.entity.model.BipedEntityModel;
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
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.armor.ChaosSetRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;
import java.util.function.Consumer;

public class ChaosCrown extends ModdedArmor implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final UUID LUCK_MODIFIER_UUID = UUID.fromString("ea8c740d-dd7c-4e5e-80aa-41bf5a250f5a");
    private static final EntityAttributeModifier LUCK_MODIFIER = new EntityAttributeModifier(
            LUCK_MODIFIER_UUID, "Helmet Luck Bonus", ConfigConstructor.chaos_crown_luck_given, EntityAttributeModifier.Operation.ADDITION);
    /**
     * Will contain harmful effects as the key, and the opposite beneficial effect as value
     */
    private static final HashMap<StatusEffect, StatusEffect> FLIPPABLE_EFFECTS = new HashMap<>();

    public ChaosCrown(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
        this.addTooltipAbility(TooltipAbilities.EMPEROR, TooltipAbilities.EFFECT_REVERSAL);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player) {
            if (this.isSlotActive(player, EquipmentSlot.HEAD)) {
                if (!player.getAttributeInstance(EntityAttributes.GENERIC_LUCK).hasModifier(LUCK_MODIFIER)) {
                    player.getAttributeInstance(EntityAttributes.GENERIC_LUCK).addPersistentModifier(LUCK_MODIFIER);
                }
                if (!player.getItemCooldownManager().isCoolingDown(ArmorRegistry.CHAOS_CROWN.get()) && !player.getItemCooldownManager().isCoolingDown(ArmorRegistry.CHAOS_HELMET.get())) {
                    this.flipEffects(player);
                }
            } else {
                Objects.requireNonNull(player.getAttributeInstance(EntityAttributes.GENERIC_LUCK)).removeModifier(LUCK_MODIFIER);
            }
        }
    }

    private void flipEffects(PlayerEntity player) {
        List<StatusEffectInstance> statusEffectsCopy = new ArrayList<>(player.getStatusEffects());
        List<StatusEffect> effectsToRemove = new ArrayList<>();
        boolean triggered = false;
        for (StatusEffectInstance instance : statusEffectsCopy) {
            StatusEffect effect = instance.getEffectType();
            if (effect.getCategory() == StatusEffectCategory.HARMFUL) {
                int duration = (int) (instance.getDuration() / 3f);
                int amplifier = (int) (instance.getAmplifier() / 2f);
                StatusEffect newEffect = StatusEffects.REGENERATION;
                for (StatusEffect harmful : FLIPPABLE_EFFECTS.keySet()) {
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
        for (StatusEffect effectToRemove : effectsToRemove) {
            player.removeStatusEffect(effectToRemove);
        }
        if (triggered && !player.isCreative()) {
            player.getItemCooldownManager().set(ArmorRegistry.CHAOS_CROWN.get(), (int) Math.max(ConfigConstructor.chaos_crown_flip_effect_min_cooldown, ConfigConstructor.chaos_crown_flip_effect_cooldown
                    - this.getReduceCooldownEnchantLevel(player.getEquippedStack(EquipmentSlot.HEAD)) * 40));
            player.getItemCooldownManager().set(ArmorRegistry.CHAOS_HELMET.get(), (int) Math.max(ConfigConstructor.chaos_crown_flip_effect_min_cooldown, ConfigConstructor.chaos_crown_flip_effect_cooldown
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
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_chaos_crown;
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
}