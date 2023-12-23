package net.soulsweaponry.items;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.soulsweaponry.config.CommonConfig;

public class BluemoonGreatsword extends SwordItem {

    public BluemoonGreatsword(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.BLUEMOON_GREATSWORD_DAMAGE.get(), pAttackSpeedModifier, pProperties);
    }
    //TODO would be cool if the sword has the same charge mechanic as holy moonlight greatsword and at max it can shoot one moonlight projectile
}
