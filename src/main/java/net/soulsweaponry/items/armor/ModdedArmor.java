package net.soulsweaponry.items.armor;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.items.ICooldownItem;
import net.soulsweaponry.items.ITooltipInfo;
import net.soulsweaponry.registry.AttributeRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ModdedArmor extends ArmorItem implements IConfigDisable, ICooldownItem, ITooltipInfo {

    protected final List<TooltipAbilities> tooltipAbilities = new ArrayList<>();

    public ModdedArmor(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    private final Supplier<AttributeModifiersComponent> attributeModifiers = Suppliers.memoize(() -> { //TODO test this
        AttributeModifiersComponent vanilla = super.getAttributeModifiers();
        AttributeModifiersComponent.Builder builder = WeaponUtil.createAndCopyAttributes(vanilla);
        EquipmentSlot eqSlot = this.type.getEquipmentSlot();
        AttributeModifierSlot slot = AttributeModifierSlot.forEquipmentSlot(eqSlot);

        // Custom ones
        float[] bleedArr = this.getBleedBuildupResistances();
        if (bleedArr != null) {
            EntityAttributeModifier bleedMod = WeaponUtil.makeAttribute(AttributeRegistry.BLEED_BUILDUP_RESISTANCE, eqSlot, bleedArr);
            if (bleedMod != null) {
                builder.add(
                        AttributeRegistry.BLEED_BUILDUP_RESISTANCE,
                        bleedMod,
                        slot
                );
            }
        }

        float[] bleedDamage = this.getBleedDamageResistances();
        if (bleedDamage != null) {
            EntityAttributeModifier attr = WeaponUtil.makeAttribute(AttributeRegistry.BLEED_DAMAGE_RESISTANCE, eqSlot, bleedDamage);
            if (attr != null) {
                builder.add(AttributeRegistry.BLEED_DAMAGE_RESISTANCE, attr, slot);
            }
        }

        float[] postureBuildup = this.getPostureBuildupResistances();
        if (postureBuildup != null) {
            EntityAttributeModifier attr = WeaponUtil.makeAttribute(AttributeRegistry.POSTURE_BUILDUP_RESISTANCE, eqSlot, postureBuildup);
            if (attr != null) {
                builder.add(AttributeRegistry.POSTURE_BUILDUP_RESISTANCE, attr, slot);
            }
        }

        float[] basePostureIncrease = this.getBasePostureIncrease();
        if (basePostureIncrease != null) {
            EntityAttributeModifier attr = WeaponUtil.makeAttribute(AttributeRegistry.BASE_POSTURE_INCREASE, eqSlot, basePostureIncrease);
            if (attr != null) {
                builder.add(AttributeRegistry.BASE_POSTURE_INCREASE, attr, slot);
            }
        }

        return builder.build();
    });

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (this.isDisabled(stack)) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        this.appendTooltipAbilities(stack, context, tooltip, type);
        super.appendTooltip(stack, context, tooltip, type);
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

    public abstract boolean isSlotActive(PlayerEntity player, EquipmentSlot slot);

    @Override
    public AttributeModifiersComponent getAttributeModifiers() {
        return this.attributeModifiers.get();
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