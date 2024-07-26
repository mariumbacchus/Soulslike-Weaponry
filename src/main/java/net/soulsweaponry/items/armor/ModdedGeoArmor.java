package net.soulsweaponry.items.armor;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import net.soulsweaponry.items.IConfigDisable;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.item.GeoArmorItem;

import java.util.List;

public abstract class ModdedGeoArmor extends GeoArmorItem implements IConfigDisable {

    public ModdedGeoArmor(ArmorMaterial materialIn, EquipmentSlot slot, Settings builder) {
        super(materialIn, slot, builder);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.isDisabled(stack)) {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.disabled"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
