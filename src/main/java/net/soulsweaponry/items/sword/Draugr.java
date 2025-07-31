package net.soulsweaponry.items.sword;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

public class Draugr extends ModdedSword {

    public Draugr(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, 1, ConfigConstructor.draugr_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.NIGHT_PROWLER);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (this.isDisabled(stack)) return;
        float bonus = world.getDimension().hasSkyLight() && world.isNight() ? ConfigConstructor.draugr_damage_at_night : this.getAttackDamage();
        WeaponUtil.modifyStackAttributes(stack, bonus, this.getAttackSpeed());
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_draugr;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return null;
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[] {
                Text.translatable("tooltip.soulsweapons.draugr_info.part_1").formatted(Formatting.DARK_GRAY)
        };
    }
}