package net.soulsweaponry.items.axe;

import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;

import java.util.ArrayList;
import java.util.List;

public abstract class ModdedAxe extends AxeItem implements IHasAbilities {

    protected final List<IAbility> abilities = new ArrayList<>();

    public ModdedAxe(ToolMaterial toolMaterial, int attackDamage, float ingameAttackSpeed, Settings settings) {
        super(toolMaterial, settings.attributeModifiers(AxeItem.createAttributeModifiers(toolMaterial, attackDamage, - (4f - ingameAttackSpeed))));
    }

    @Override
    public List<IAbility> getAbilities() {
        return this.abilities;
    }
}