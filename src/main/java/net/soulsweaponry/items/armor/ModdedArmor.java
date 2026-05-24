package net.soulsweaponry.items.armor;

import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.armorattributes.BleedResistance;
import net.soulsweaponry.items.abilities.armorattributes.PostureResistance;

import java.util.ArrayList;
import java.util.List;

public abstract class ModdedArmor extends ArmorItem implements IHasAbilities {

    protected final List<IAbility> abilities = new ArrayList<>();

    public ModdedArmor(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
        PostureResistance postureResistances = new PostureResistance(this.getPostureBuildupResistances(), this.getBasePostureIncrease());
        BleedResistance bleedResistances = new BleedResistance(this.getBleedBuildupResistances(), this.getBleedDamageResistances());
        this.addAbility(postureResistances, bleedResistances);
    }

    @Override
    public List<IAbility> getAbilities() {
        return this.abilities;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot != this.getSlotType()) {
            return super.getAttributeModifiers(slot);
        }
        return this.applyArmorAttributeModifiers(super.getAttributeModifiers(slot), slot).build();
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