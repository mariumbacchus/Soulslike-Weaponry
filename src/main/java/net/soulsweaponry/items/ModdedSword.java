package net.soulsweaponry.items;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;

import java.util.ArrayList;
import java.util.List;

public abstract class ModdedSword extends SwordItem implements IHasAbilities {

    protected final List<IAbility> abilities = new ArrayList<>();

    public ModdedSword(ToolMaterial toolMaterial, int attackDamage, float ingameAttackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, - (4f - ingameAttackSpeed), settings);
    }

    @Override
    public List<IAbility> getAbilities() {
        return this.abilities;
    }
}