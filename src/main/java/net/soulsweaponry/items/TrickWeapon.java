package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.WeaponUtil;

public class TrickWeapon extends ModdedSword implements IUltraHeavy {
    //TODO now that trick weapons can be made from jsons, rewrite this to make
    // the weapons have right values instead of referring to another list
    private static final int[] DAMAGE = {
            ConfigConstructor.kirkhammer_damage,
            ConfigConstructor.kirkhammer_silver_sword_damage,
            ConfigConstructor.ludwigs_holy_greatsword_damage,
            ConfigConstructor.holy_moonlight_greatsword_damage,
            ConfigConstructor.holy_moonlight_sword_damage,
    };

    private static final float[] ATTACK_SPEED = {
            ConfigConstructor.kirkhammer_attack_speed,
            ConfigConstructor.kirkhammer_silver_sword_attack_speed,
            ConfigConstructor.ludwigs_holy_greatsword_attack_speed,
            ConfigConstructor.holy_moonlight_greatsword_attack_speed,
            ConfigConstructor.holy_moonlight_sword_attack_speed,
    };

    private static final boolean[] DISABLE = {
            ConfigConstructor.disable_use_kirkhammer,
            ConfigConstructor.disable_use_silver_sword,
            ConfigConstructor.disable_use_ludwigs_holy_greatsword,
            ConfigConstructor.disable_use_holy_moonlight_greatsword,
            ConfigConstructor.disable_use_holy_moonlight_sword,
    };

    private static final boolean[] FIREPROOF = {
            ConfigConstructor.is_fireproof_kirkhammer,
            ConfigConstructor.is_fireproof_silver_sword,
            ConfigConstructor.is_fireproof_ludwigs_holy_blade,
            ConfigConstructor.is_fireproof_holy_moonlight_greatsword,
            ConfigConstructor.is_fireproof_holy_moonlight_sword,
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
    public boolean isFireproof() {
        return FIREPROOF[this.arrayIndex];
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String getReduceCooldownEnchantId(ItemStack stack) {
        return null;
    }
}
