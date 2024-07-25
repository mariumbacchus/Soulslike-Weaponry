package net.soulsweaponry.items;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;

public class BluemoonShortsword extends MoonlightShortsword {
    public BluemoonShortsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.bluemoon_shortsword_damage, ConfigConstructor.bluemoon_shortsword_attack_speed, settings);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_bluemoon_shortsword;
    }
}
