package net.soulsweaponry.items.armor;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.items.IConfigDisable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ModdedArmor extends ArmorItem implements IConfigDisable {

    public ModdedArmor(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.isDisabled(stack)) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}