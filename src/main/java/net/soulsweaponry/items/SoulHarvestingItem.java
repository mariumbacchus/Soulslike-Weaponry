package net.soulsweaponry.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class SoulHarvestingItem extends SwordItem {

    public static final String KILLS = "kills";

    public SoulHarvestingItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        if (target.isDeadOrDying()) {
            if (/*target instanceof BossEntity || */target instanceof WitherBoss) {//TODO add boss entity
                this.addAmount(stack, 50);
            } else {
                this.addKillCounter(stack);
            }
        }
        return true;
    }

    public void addKillCounter(ItemStack stack) {
        this.addAmount(stack, 1);
    }

    public void addAmount(ItemStack stack, int amount) {
        if (stack.hasTag()) {
            if (stack.getTag().contains(KILLS)) {
                stack.getTag().putInt(KILLS, stack.getTag().getInt(KILLS) + amount);
            } else {
                stack.getTag().putInt(KILLS, amount);
            }
        }
    }

    public int getSouls(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(KILLS)) {
            return stack.getTag().getInt(KILLS);
        } else {
            return 0;
        }
    }
}
