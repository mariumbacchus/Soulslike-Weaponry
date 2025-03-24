package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.ModTags;
import net.soulsweaponry.util.WeaponUtil;

public abstract class SoulHarvestingItem extends ModdedSword {

    public static final String KILLS = "kills";

    public SoulHarvestingItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.SOUL_TRAP, WeaponUtil.TooltipAbilities.COLLECT);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (target.isDead()) {
            if (target.getType().isIn(ModTags.Entities.BOSSES)) {
                this.addAmount(stack, 50);
            } else {
                this.addKillCounter(stack);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    public void addKillCounter(ItemStack stack) {
        this.addAmount(stack, 1);
    }

    public void addAmount(ItemStack stack, int amount) {
        stack.apply(ComponentRegistry.KILLS, 0, i -> i + amount);
    }

    public int getSouls(ItemStack stack) {
        return stack.getOrDefault(ComponentRegistry.KILLS, 0);
    }
}