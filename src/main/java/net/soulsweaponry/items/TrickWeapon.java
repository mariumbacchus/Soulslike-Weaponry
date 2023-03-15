package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.WeaponRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrickWeapon extends UltraHeavyWeapon {

    private static final int[] DAMAGE = {
            ConfigConstructor.kirkhammer_damage,
            ConfigConstructor.kirkhammer_silver_sword_damage,
    };

    private final int switchWeaponIndex;

    public TrickWeapon(ToolMaterial toolMaterial, int damageIndex, float attackSpeed, Settings settings, int switchWeaponIndex, boolean isHeavy) {
        super(toolMaterial, DAMAGE[damageIndex], attackSpeed, settings, isHeavy);
        this.switchWeaponIndex = switchWeaponIndex;
    }

    public int getSwitchWeaponIndex() {
        return this.switchWeaponIndex;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.trick_weapon").formatted(Formatting.WHITE));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.trick_weapon_description_1").formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.trick_weapon_description_2").formatted(Formatting.DARK_GRAY));
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
