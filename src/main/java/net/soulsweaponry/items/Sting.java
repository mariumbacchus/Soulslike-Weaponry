package net.soulsweaponry.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Sting extends SwordItem {

    private static final String ACTIVE = "active_glowing";

    public Sting(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.STING_DAMAGE.get(), pAttackSpeedModifier, pProperties);
    }
    //TODO make it shine like a torch with shaders

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        this.setActive(stack, this.isHostileAround(world, entity));
    }

    private boolean isHostileAround(Level world, Entity holder) {
        for (Entity around : world.getEntities(holder, holder.getBoundingBox().inflate(16))) {
            if (around instanceof Monster) {
                return true;
            }
        }
        return false;
    }

    private void setActive(ItemStack stack, boolean bl) {
        if (stack.hasTag()) {
            stack.getTag().putBoolean(ACTIVE, bl);
        }
    }

    public boolean isActive(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(ACTIVE)) {
            return stack.getTag().getBoolean(ACTIVE);
        } else {
            return false;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.LUMINATE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SPIDERS_BANE, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }
}
