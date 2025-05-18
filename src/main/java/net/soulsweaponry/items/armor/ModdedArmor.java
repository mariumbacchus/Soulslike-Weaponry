package net.soulsweaponry.items.armor;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.items.ICooldownItem;
import net.soulsweaponry.items.ITooltipInfo;
import net.soulsweaponry.util.TooltipAbilities;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ModdedArmor extends ArmorItem implements IConfigDisable, ICooldownItem, ITooltipInfo {

    protected final List<TooltipAbilities> tooltipAbilities = new ArrayList<>();

    public ModdedArmor(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.isDisabled(stack)) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        this.appendTooltipAbilities(stack, world, tooltip, context);
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (this.isDisabled(stack)) {
            return;
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public List<TooltipAbilities> getTooltipAbilities() {
        return this.tooltipAbilities;
    }

    @Override
    public void addTooltipAbility(TooltipAbilities... abilities) {
        Collections.addAll(this.tooltipAbilities, abilities);
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public abstract boolean isFireproof();

    public abstract boolean isSlotActive(PlayerEntity player, EquipmentSlot slot);
}