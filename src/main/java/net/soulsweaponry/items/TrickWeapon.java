package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrickWeapon extends ModdedSword implements IUltraHeavy {

    private static final int[] DAMAGE = {
            CommonConfig.KIRKHAMMER_DAMAGE.get(),
            CommonConfig.KIRKHAMMER_SILVER_SWORD_DAMAGE.get(),
            CommonConfig.LUDWIGS_HOLY_GREATSWORD.get(),
            CommonConfig.HOLY_MOON_GREAT_DAMAGE.get(),
            CommonConfig.HOLY_MOON_SWORD_DAMAGE.get(),
    };

    private static final boolean[] DISABLE = {
            CommonConfig.DISABLE_USE_KIRKHAMMER.get(),
            CommonConfig.DISABLE_USE_SILVER_SWORD.get(),
            CommonConfig.DISABLE_USE_LUDWIGS_HOLY_GREATSWORD.get(),
            CommonConfig.DISABLE_USE_HOLY_MOONLIGHT_GREATSWORD.get(),
            CommonConfig.DISABLE_USE_HOLY_MOONLIGHT_SWORD.get(),
    };

    private final int switchWeaponIndex;
    private final int ownWeaponIndex;
    private final boolean undeadBonus;
    private final boolean isHeavy;
    private final int arrayIndex;

    public TrickWeapon(ToolMaterial toolMaterial, int damageIndex, float attackSpeed, Settings settings, int switchWeaponIndex, int ownWeaponIndex, boolean isHeavy, boolean undeadBonus) {
        super(toolMaterial, DAMAGE[damageIndex], attackSpeed, settings);
        this.switchWeaponIndex = switchWeaponIndex;
        this.ownWeaponIndex = ownWeaponIndex;
        this.undeadBonus = undeadBonus;
        this.isHeavy = isHeavy;
        this.arrayIndex = damageIndex;
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
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled()) {
            this.notifyDisabled(attacker);
            return super.postHit(stack, target, attacker);
        }
        if (this.isHeavy) {
            this.gainStrength(attacker);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.TRICK_WEAPON, stack, tooltip);
            if (this.isHeavy()) {
                WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.HEAVY, stack, tooltip);
            }
            if (this.undeadBonus) {
                WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.RIGHTEOUS, stack, tooltip);
            }
            if (stack.isOf(WeaponRegistry.HOLY_MOONLIGHT_SWORD.get())) {
                WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.CHARGE, stack, tooltip);
                WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.CHARGE_BONUS_DAMAGE, stack, tooltip);
            }
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
    }

    @Override
    public boolean isHeavy() {
        return this.isHeavy;
    }

    @Override
    public boolean isDisabled() {
        return DISABLE[this.arrayIndex];
    }
}
