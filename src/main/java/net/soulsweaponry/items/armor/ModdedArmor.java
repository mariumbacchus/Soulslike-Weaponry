package net.soulsweaponry.items.armor;

import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.Item;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.armorattributes.BleedResistance;
import net.soulsweaponry.items.abilities.armorattributes.PostureResistance;
import net.soulsweaponry.util.VanillaArmorAttributes;

import java.util.ArrayList;
import java.util.List;

public abstract class ModdedArmor extends Item implements IHasAbilities {

    protected final List<IAbility> abilities = new ArrayList<>();
    protected final EquipmentType type;

    /**
     * Item Attributes such as armor values or posture resistance is applied statically with
     * {@link #buildSettings(ArmorMaterial, EquipmentType, Settings, List)} so it is important
     * to add abilities changing armor attributes within the constructor and not after.
     * @param material armor material
     * @param type equipment type/slot
     * @param settings item settings
     * @param abilities abilities adding armor attributes should go here
     */
    public ModdedArmor(ArmorMaterial material, EquipmentType type, Settings settings, List<IAbility> abilities) {
        super(buildSettings(material, type, settings, abilities));
        this.type = type;
        this.abilities.addAll(abilities);
    }

    @Override
    public List<IAbility> getAbilities() {
        return this.abilities;
    }

    public static Settings buildSettings(
            ArmorMaterial material,
            EquipmentType type,
            Settings settings,
            List<IAbility> abilities
    ) {
        // Vanilla components like stack damage
        settings = material.applySettings(settings, type);
        // Basic armor values like toughness
        AttributeModifiersComponent vanilla = VanillaArmorAttributes.create(material, type);
        // Ability attributes
        AttributeModifiersComponent merged = IHasAbilities.applyArmorAttributeModifiers(vanilla, type.getEquipmentSlot(), abilities).build();
        return settings.attributeModifiers(merged);
    }

    public static List<IAbility> applyCustomAttributeAbilities(float[] postureBuildupResistances, float[] basePostureIncrease, float[] bleedBuildupResistances, float[] bleedDamageResistances) {
        List<IAbility> abilities = new ArrayList<>();
        abilities.add(new PostureResistance(postureBuildupResistances, basePostureIncrease));
        abilities.add(new BleedResistance(bleedBuildupResistances, bleedDamageResistances));
        return abilities;
    }
}