package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.WeaponUtil;

import javax.annotation.Nullable;
import java.util.List;

public class Sting extends SwordItem {

    private static final String ACTIVE = "active_glowing";
    //private static final String POS = "active_positions";

    public Sting(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.sting_damage, attackSpeed, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        this.setActive(stack, this.isHostileAround(world, entity));
    }

    private boolean isHostileAround(World world, Entity holder) {
        for (Entity around : world.getOtherEntities(holder, holder.getBoundingBox().expand(16))) {
            if (around instanceof HostileEntity) {
                return true;
            }
        }
        return false;
    }

    /* These methods would help the world remove and add light sources to and from blockstates.
     * Dynamic lights already does this better, therefore these are commented.
     */
    /*private void addPosToNBT(ItemStack stack, BlockPos pos) {
        if (stack.hasNbt()) {
            int[] arr = {pos.getX(), pos.getY(), pos.getZ()};
            stack.getNbt().putIntArray(POS, arr);
        }
    }
    private void clearPrevPos(World world, ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(POS)) {
            int[] arr = stack.getNbt().getIntArray(POS);
            BlockPos pos = new BlockPos(arr[0], arr[1], arr[2]);
            this.setActive(stack, false);
        }
    }*/

    private void setActive(ItemStack stack, boolean bl) {
        if (stack.hasNbt()) {
            stack.getNbt().putBoolean(ACTIVE, bl);
        }
    }

    public boolean isActive(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(ACTIVE)) {
            return stack.getNbt().getBoolean(ACTIVE);
        } else {
            return false;
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.LUMINATE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SPIDERS_BANE, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}