package net.soulsweaponry.items;

import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAbility;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAttributes;
import net.soulsweaponry.items.abilities.posthit.UltraHeavy;

public abstract class UltraHeavyWeapon extends ModdedSword {

    public UltraHeavyWeapon(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings, int postureLossPostHit, DetonateGroundAttributes detonateGroundAttributes) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        UltraHeavy heavyAbility = new UltraHeavy(postureLossPostHit, 200, 1);
        DetonateGroundAbility detonateGroundAbility = new DetonateGroundAbility(detonateGroundAttributes);
        this.addAbility(heavyAbility, detonateGroundAbility);
    }
}
