package net.soulsweaponry.items.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.items.ICooldownItem;
import net.soulsweaponry.items.ITooltipInfo;
import net.soulsweaponry.registry.AttributeRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ModdedArmor extends ArmorItem implements IConfigDisable, ICooldownItem, ITooltipInfo {

    protected final List<TooltipAbilities> tooltipAbilities = new ArrayList<>();

    public ModdedArmor(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.isDisabled(stack)) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        this.appendTooltipAbilities(stack, world, tooltip, context);
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (this.isDisabled(stack)) {
            return;
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public List<TooltipAbilities> getTooltipAbilities() {
        return this.tooltipAbilities;
    }

    @Override
    public void addTooltipAbility(TooltipAbilities... abilities) {
        Collections.addAll(this.tooltipAbilities, abilities);
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public abstract boolean isFireproof();

    public abstract boolean isSlotActive(PlayerEntity player, EquipmentSlot slot);

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> vanilla = super.getAttributeModifiers(slot);
        if (slot == this.type.getEquipmentSlot()) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(vanilla);

            float[] bleedBuildup = this.getBleedBuildupResistances();
            if (bleedBuildup != null) {
                EntityAttributeModifier attr = WeaponUtil.makeAttribute(AttributeRegistry.BLEED_BUILDUP_RESISTANCE.get(), slot, bleedBuildup);
                if (attr != null) {
                    builder.put(AttributeRegistry.BLEED_BUILDUP_RESISTANCE.get(), attr);
                }
            }

            float[] bleedDamage = this.getBleedDamageResistances();
            if (bleedDamage != null) {
                EntityAttributeModifier attr = WeaponUtil.makeAttribute(AttributeRegistry.BLEED_DAMAGE_RESISTANCE.get(), slot, bleedDamage);
                if (attr != null) {
                    builder.put(AttributeRegistry.BLEED_DAMAGE_RESISTANCE.get(), attr);
                }
            }

            float[] postureBuildup = this.getPostureBuildupResistances();
            if (postureBuildup != null) {
                EntityAttributeModifier attr = WeaponUtil.makeAttribute(AttributeRegistry.POSTURE_BUILDUP_RESISTANCE.get(), slot, postureBuildup);
                if (attr != null) {
                    builder.put(AttributeRegistry.POSTURE_BUILDUP_RESISTANCE.get(), attr);
                }
            }

            float[] basePostureIncrease = this.getBasePostureIncrease();
            if (basePostureIncrease != null) {
                EntityAttributeModifier attr = WeaponUtil.makeAttribute(AttributeRegistry.BASE_POSTURE_INCREASE.get(), slot, basePostureIncrease);
                if (attr != null) {
                    builder.put(AttributeRegistry.BASE_POSTURE_INCREASE.get(), attr);
                }
            }
            return builder.build();
        }
        return vanilla;
    }

    /**
     * Gets the bleed buildup resistances for each armor piece, example: {@code {35, 50, 75, 40}} (feet at index 0).
     * <p>Override this to give the custom armor different values.</p>
     */
    public float[] getBleedBuildupResistances() {
        return null;
    }

    /**
     * Gets the bleed damage resistances for each armor piece, example: {@code {35, 50, 75, 40}} (feet at index 0).
     * <p>Override this to give the custom armor different values.</p>
     */
    public float[] getBleedDamageResistances() {
        return null;
    }

    /**
     * Gets the bleed damage resistances for each armor piece, example: {@code {35, 50, 75, 40}} (feet at index 0).
     * <p>Override this to give the custom armor different values.</p>
     */
    public float[] getPostureBuildupResistances() {
        return null;
    }

    /**
     * Gets the base posture increase for each armor piece, example: {@code {35, 50, 75, 40}} (feet at index 0).
     * It is used to increase the max posture of the entity with the attribute.
     * <p>Override this to give the custom armor different values.</p>
     */
    public float[] getBasePostureIncrease() {
        return null;
    }
}