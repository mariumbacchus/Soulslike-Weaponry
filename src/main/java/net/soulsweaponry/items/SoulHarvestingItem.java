package net.soulsweaponry.items;

import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.items.abilities.targetdeath.SoulHarvest;

public abstract class SoulHarvestingItem extends ModdedSword {

    private static final SoulHarvest SOUL_HARVEST = new SoulHarvest();

    public SoulHarvestingItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.addAbility(SOUL_HARVEST);
    }
}
