package net.soulsweaponry.items.axe;

import net.minecraft.item.AxeItem;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;

import java.util.ArrayList;
import java.util.List;

public abstract class ModdedAxe extends AxeItem implements IHasAbilities {

    protected final List<IAbility> abilities = new ArrayList<>();

    public ModdedAxe(ToolMaterial material, float attackDamage, float ingameAttackSpeed, Settings settings) {
        super(material, attackDamage, - (4f - ingameAttackSpeed), settings);
    }

    @Override
    public List<IAbility> getAbilities() {
        return this.abilities;
    }
}