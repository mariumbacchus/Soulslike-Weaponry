package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.WeaponRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrickWeapon extends UltraHeavyWeapon {

    private static final int[] DAMAGE = {
            ConfigConstructor.kirkhammer_damage,
            ConfigConstructor.kirkhammer_silver_sword_damage,
            ConfigConstructor.ludwigs_holy_greatsword,
    };

    private final int switchWeaponIndex;
    private final int ownWeaponIndex;
    private final boolean undeadBonus;

    public TrickWeapon(ToolMaterial toolMaterial, int damageIndex, float attackSpeed, Settings settings, int switchWeaponIndex, int ownWeaponIndex, boolean isHeavy, boolean undeadBonus) {
        super(toolMaterial, DAMAGE[damageIndex], attackSpeed, settings, isHeavy);
        this.switchWeaponIndex = switchWeaponIndex;
        this.ownWeaponIndex = ownWeaponIndex;
        this.undeadBonus = undeadBonus;
    }

    public int getSwitchWeaponIndex() {
        return this.switchWeaponIndex;
    }

    public int getOwnWeaponIndex() {
        return this.ownWeaponIndex;
    }

    public boolean hasUndeadBonus() {
        return this.undeadBonus;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon").formatted(Formatting.WHITE));
            tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon_description_1").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.trick_weapon_description_2").formatted(Formatting.DARK_GRAY));
            if (this.isHeavy()) {
                tooltip.add(Text.translatable("tooltip.soulsweapons.heavy_weapon").formatted(Formatting.RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.heavy_weapon_description").formatted(Formatting.GRAY));
            }
            if (this.undeadBonus) {
                int amount = MathHelper.floor(EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack) + ConfigConstructor.righteous_undead_bonus_damage);
                tooltip.add(Text.translatable("tooltip.soulsweapons.righteous").formatted(Formatting.GOLD));
                tooltip.add(Text.translatable("tooltip.soulsweapons.righteous_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.righteous_description_2").formatted(Formatting.DARK_GRAY).append(Text.literal(String.valueOf(amount))));
            }
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
