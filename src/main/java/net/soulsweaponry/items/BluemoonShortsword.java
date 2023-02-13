package net.soulsweaponry.items;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;

public class BluemoonShortsword extends SwordItem {
    public BluemoonShortsword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.bluemoon_shortsword_damage, attackSpeed, settings);
    }
}
