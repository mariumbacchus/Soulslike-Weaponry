package net.soulsweaponry.items;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;

public class Draugr extends SwordItem {
    
    public static final String NIGHT = "night";

    public Draugr(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, 1, attackSpeed, settings);  
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        this.refreshDayTime(world, stack);
    }

    private void refreshDayTime(World world, ItemStack stack) {
        if (stack.hasNbt()) {
            if (world.getDimension().hasSkyLight() && world.getTimeOfDay() % 24000 > 13000 && world.getTimeOfDay() % 24000 < 23000) {
                stack.getNbt().putBoolean(NIGHT, true);
            } else {
                stack.getNbt().putBoolean(NIGHT, false);
            }
        }
    }

    private boolean isNight(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(NIGHT)) {
            return stack.getNbt().getBoolean(NIGHT);
        } else {
            return false;
        }
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.isNight(stack) ? ConfigConstructor.draugr_damage_at_night - 1 : 0, EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -2.4D, EntityAttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.night_prowler").formatted(Formatting.DARK_AQUA));
            tooltip.add(Text.translatable("tooltip.soulsweapons.night_prowler_description").formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        
        super.appendTooltip(stack, world, tooltip, context);
    }
}
