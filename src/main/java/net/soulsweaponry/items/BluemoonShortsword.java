package net.soulsweaponry.items;

import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.CommonConfig;

public class BluemoonShortsword extends MoonlightShortsword {

    public BluemoonShortsword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, CommonConfig.BLUEMOON_SHORTSWORD_DAMAGE.get(), attackSpeed, settings);
    }

    @Override
    public boolean isDisabled() {
        return CommonConfig.DISABLE_USE_BLUEMOON_SHORTSWORD.get();
    }
}
