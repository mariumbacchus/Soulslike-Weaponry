package net.soulsweaponry.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.IUltraHeavy;
import net.soulsweaponry.entitydata.PostureData;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.posthit.UltraHeavy;

public class StaggerEnchantment extends Enchantment {

    public StaggerEnchantment(Rarity weight, EquipmentSlot... slotTypes) {
        super(weight, EnchantmentTarget.WEAPON, slotTypes);
    }
    
    @Override
    public int getMinPower(int level) {
        return 10 + level * 10;
    }

    @Override
    public int getMaxLevel() {
        return (int) ConfigConstructor.stagger_enchant_max_level;
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (ConfigConstructor.disable_enchantment_stagger) {
            return;
        }
        if (target instanceof LivingEntity living && !living.isDead()) {
            int postureLoss = MathHelper.floor(ConfigConstructor.stagger_enchant_posture_loss_on_player_modifier * ConfigConstructor.stagger_enchant_posture_loss_applied_per_level);
            if (user != null) {
                ItemStack stack = user.getStackInHand(Hand.MAIN_HAND);
                if (IHasAbilities.getAbility(stack, UltraHeavy.class).isPresent()) {
                    postureLoss = MathHelper.floor(postureLoss * ConfigConstructor.ultra_heavy_posture_loss_modifier_when_stagger_enchant);
                }
            }
            postureLoss *= level;
            PostureData.addPostureLoss(living, postureLoss);
        }
        super.onTargetDamaged(user, target, level);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof IUltraHeavy || super.isAcceptableItem(stack);
    }
}
