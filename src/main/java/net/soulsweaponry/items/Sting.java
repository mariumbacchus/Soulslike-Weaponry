package net.soulsweaponry.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.WeaponUtil;

/**
 * TODO for another time: Make it light up if it is active like how Dynamic Lights or shaders do it
 */
public class Sting extends ModdedSword {

    private static final String ACTIVE = "active_glowing";

    public Sting(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.sting_damage, ConfigConstructor.sting_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.LUMINATE, WeaponUtil.TooltipAbilities.SPIDERS_BANE);
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
    public boolean isDisabled(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_sting;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String getReduceCooldownEnchantId(ItemStack stack) {
        return null;
    }
}