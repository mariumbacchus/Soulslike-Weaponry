package net.soulsweaponry.items.dagger;

import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.mehrunes_razor.BonusCritHitDamage;

public abstract class ModdedDagger extends ModdedSword {

    public ModdedDagger(ToolMaterial toolMaterial, int attackDamage, float ingameAttackSpeed, Settings settings, double critHitBonus) {
        super(toolMaterial, attackDamage, ingameAttackSpeed, settings);
        this.addAbility(new BonusCritHitDamage(critHitBonus));
    }
}
