package net.soulsweaponry.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class UltraHeavyWeapon extends SwordItem {

    private final boolean isHeavy;

    public UltraHeavyWeapon(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, boolean isHeavy) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        this.isHeavy = isHeavy;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity attacker) {
        if (this.isHeavy && attacker.hasEffect(MobEffects.DAMAGE_BOOST)) {
            attacker.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 200, 1));
        }
        return super.hurtEnemy(pStack, pTarget, attacker);
    }

    public boolean isHeavy() {
        return this.isHeavy;
    }
}
