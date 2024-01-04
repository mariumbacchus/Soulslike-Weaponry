package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.soulsweaponry.config.CommonConfig;

public class Draugr extends SwordItem {

    public static final String NIGHT = "night";

    public Draugr(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, 1, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        this.refreshDayTime(pLevel, pStack);
    }

    private void refreshDayTime(Level world, ItemStack stack) {
        if (stack.hasTag() && !world.isClientSide) {//TODO test
            stack.getTag().putBoolean(NIGHT, world.dimensionType().hasSkyLight() && world.isNight());
        }
    }

    private boolean isNight(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(NIGHT)) {
            return stack.getTag().getBoolean(NIGHT);
        } else {
            return false;
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.isNight(stack) ? CommonConfig.DRAUGR_NIGHT_DAMAGE.get() - 1 : 0, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4D, AttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }
}
