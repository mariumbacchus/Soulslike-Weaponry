package net.soulsweaponry.items.armor;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.registry.entry.RegistryEntry;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.armorattributes.BleedResistance;
import net.soulsweaponry.items.abilities.armorattributes.PostureResistance;

import java.util.ArrayList;
import java.util.List;

public abstract class ModdedArmor extends ArmorItem implements IHasAbilities {

    protected final List<IAbility> abilities = new ArrayList<>();

    public ModdedArmor(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        PostureResistance postureResistances = new PostureResistance(this.getPostureBuildupResistances(), this.getBasePostureIncrease());
        BleedResistance bleedResistances = new BleedResistance(this.getBleedBuildupResistances(), this.getBleedDamageResistances());
        this.addAbility(postureResistances, bleedResistances);
    }

    @Override
    public List<IAbility> getAbilities() {
        return this.abilities;
    }

    private final Supplier<AttributeModifiersComponent> attributeModifiers = Suppliers.memoize(() -> this.applyArmorAttributeModifiers(super.getAttributeModifiers(), this.type.getEquipmentSlot()).build());

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