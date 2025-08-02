package net.soulsweaponry.items.sword;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.TooltipAbilities;

import java.util.Optional;

/**
 * TODO for another time: Make it light up if it is active like how Dynamic Lights or shaders do it
 */
public class Sting extends ModdedSword {

    public Sting(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.sting_damage, ConfigConstructor.sting_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.LUMINATE, TooltipAbilities.SPIDERS_BANE);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if (!this.isDisabled(null) && target.getType().isIn(EntityTypeTags.ARTHROPOD)) {
            return ConfigConstructor.sting_bonus_arthropod_damage;
        }
        return super.getBonusAttackDamage(target, baseAttackDamage, damageSource);
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
        stack.set(ComponentRegistry.EMPOWERED, bl);
    }

    public boolean isActive(ItemStack stack) {
        return Optional.ofNullable(stack.get(ComponentRegistry.EMPOWERED)).orElse(false);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return null;
    }
}
