package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrucibleSword extends SwordItem {

    private static final String EMP = "empowered";
    private final float attackSpeed;

    public CrucibleSword(Tier pTier, float attackSpeed, Properties pProperties) {
        super(pTier, CommonConfig.CRUCIBLE_SWORD_DAMAGE.get(), attackSpeed, pProperties);
        this.attackSpeed = attackSpeed;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player) {
            ItemCooldowns cooldownManager = ((Player) attacker).getCooldowns();
            if (!cooldownManager.isOnCooldown(this)) {
                cooldownManager.addCooldown(this, CommonConfig.CRUCIBLE_SWORD_EMP_COOLDOWN.get() - WeaponUtil.getEnchantDamageBonus(stack) * 20);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level pLevel, Entity entity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(stack, pLevel, entity, pSlotId, pIsSelected);
        if (entity instanceof Player player) {
            this.updateEmpowered(stack, !player.getCooldowns().isOnCooldown(this));
        }
    }

    private void updateEmpowered(ItemStack stack, boolean bl) {
        if (stack.hasTag()) {
            stack.getTag().putBoolean(EMP, bl);
        }
    }

    private boolean isEmpowered(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(EMP)) {
            return stack.getTag().getBoolean(EMP);
        } else {
            return false;
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",  (this.isEmpowered(stack) ? CommonConfig.CRUCIBLE_SWORD_EMP_DAMAGE.get() : CommonConfig.CRUCIBLE_SWORD_DAMAGE.get()) - 1, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.DOOM, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }
}
