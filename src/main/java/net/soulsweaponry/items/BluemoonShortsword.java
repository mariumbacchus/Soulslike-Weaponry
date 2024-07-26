package net.soulsweaponry.items;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.CommonConfig;

public class BluemoonShortsword extends MoonlightShortsword {

    public BluemoonShortsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, CommonConfig.BLUEMOON_SHORTSWORD_DAMAGE.get(), CommonConfig.BLUEMOON_SHORTSWORD_ATTACK_SPEED.get(), settings);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return CommonConfig.DISABLE_USE_BLUEMOON_SHORTSWORD.get();
    }
}
