package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.WeaponUtil;

public class TrickWeapon extends ModdedSword implements IUltraHeavy {

    private static final int[] DAMAGE = {
            CommonConfig.KIRKHAMMER_DAMAGE.get(),
            CommonConfig.KIRKHAMMER_SILVER_SWORD_DAMAGE.get(),
            CommonConfig.LUDWIGS_HOLY_GREATSWORD_DAMAGE.get(),
            CommonConfig.HOLY_MOON_GREAT_DAMAGE.get(),
            CommonConfig.HOLY_MOON_SWORD_DAMAGE.get(),
    };

    private static final float[] ATTACK_SPEED = {
            CommonConfig.KIRKHAMMER_ATTACK_SPEED.get(),
            CommonConfig.KIRKHAMMER_SILVER_SWORD_ATTACK_SPEED.get(),
            CommonConfig.LUDWIGS_HOLY_GREATSWORD_ATTACK_SPEED.get(),
            CommonConfig.HOLY_MOON_GREAT_ATTACK_SPEED.get(),
            CommonConfig.HOLY_MOON_SWORD_ATTACK_SPEED.get(),
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

    public TrickWeapon(ToolMaterial toolMaterial, int damageIndex, Settings settings, int switchWeaponIndex, int ownWeaponIndex, boolean isHeavy, boolean undeadBonus) {
        super(toolMaterial, DAMAGE[damageIndex], ATTACK_SPEED[damageIndex], settings);
        this.switchWeaponIndex = switchWeaponIndex;
        this.ownWeaponIndex = ownWeaponIndex;
        this.undeadBonus = undeadBonus;
        this.isHeavy = isHeavy;
        this.arrayIndex = damageIndex;
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.TRICK_WEAPON);
        if (this.isHeavy()) {
            this.addTooltipAbility(WeaponUtil.TooltipAbilities.HEAVY);
        }
        if (this.undeadBonus) {
            this.addTooltipAbility(WeaponUtil.TooltipAbilities.RIGHTEOUS);
        }
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
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (this.isHeavy) {
            this.gainStrength(attacker);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isHeavy() {
        return this.isHeavy;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return DISABLE[this.arrayIndex];
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }
}
