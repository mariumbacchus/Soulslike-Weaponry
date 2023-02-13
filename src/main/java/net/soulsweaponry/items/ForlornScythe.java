package net.soulsweaponry.items;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ForlornScythe extends SwordItem implements IAnimatable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public ForlornScythe(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.forlorn_scythe_damage, attackSpeed, settings);
    }

    @Override
    public void registerControllers(AnimationData data) {        
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
    
}
