package net.soulsweaponry.items;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.abilities.use.InvisibleItem;

public class TranslucentWeapon extends ModdedSword {

    private static final InvisibleItem INVISIBLE_ITEM = new InvisibleItem();

    public TranslucentWeapon(ToolMaterial toolMaterial, int attackDamage, float ingameAttackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, ingameAttackSpeed, settings);
        this.addAbility(INVISIBLE_ITEM);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_translucent_weapons;
    }
}
