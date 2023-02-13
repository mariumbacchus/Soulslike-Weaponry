package net.soulsweaponry.items;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;

public class BluemoonGreatsword extends SwordItem {
    
    public BluemoonGreatsword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.bluemoon_greatsword_damage, attackSpeed, settings);
    }
}
