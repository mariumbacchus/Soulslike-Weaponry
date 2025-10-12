package net.soulsweaponry.items.scythe;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.use.UmbralTrespass;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

public class DarkinScythePrime extends ModdedSword {

    private static final UmbralTrespass UMBRAL_TRESPASS = new UmbralTrespass(
            ConfigConstructor.darkin_scythe_prime_umbral_trespass_damage,
            ConfigConstructor.darkin_scythe_prime_umbral_trespass_bonus_damage_per_level,
            ConfigConstructor.darkin_scythe_prime_umbral_trespass_bonus_enchant_damage_modifier,
            (int) ConfigConstructor.darkin_scythe_prime_umbral_trespass_min_cooldown,
            (int) ConfigConstructor.darkin_scythe_prime_umbral_trespass_cooldown,
            (int) ConfigConstructor.darkin_scythe_prime_umbral_trespass_reduced_cooldown_per_level,
            ConfigConstructor.darkin_scythe_prime_umbral_trespass_heal_modifier,
            (int) ConfigConstructor.darkin_scythe_prime_umbral_trespass_ticks_before_dismount,
            ConfigConstructor.darkin_scythe_prime_umbral_trespass_bonus_percent_max_health_damage
    );

    public DarkinScythePrime(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) (ConfigConstructor.darkin_scythe_damage + ConfigConstructor.darkin_scythe_bonus_damage), ConfigConstructor.darkin_scythe_prime_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.OMNIVAMP);
        this.addAbility(UMBRAL_TRESPASS);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        //TODO make ability off of this, also make Omnivamp ability actually heal the player off of any ability somehow instead of hardcoding to one ability (maybe on target damaged?)
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (attacker instanceof PlayerEntity player) {
            if (!player.getItemCooldownManager().isCoolingDown(stack.getItem()) && !(player.getHealth() >= player.getMaxHealth())) {
                this.applyItemCooldown(player, (int) Math.max(ConfigConstructor.lifesteal_item_min_cooldown, ConfigConstructor.lifesteal_item_cooldown - this.getReduceLifeStealCooldownEnchantLevel(stack) * 6));
                float healing = ConfigConstructor.lifesteal_item_base_healing;
                if (ConfigConstructor.lifesteal_item_heal_scales) {
                    healing += MathHelper.ceil(((float) WeaponUtil.getEnchantDamageBonus(stack))/2);
                }
                attacker.heal(healing);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_darkin_scythe_prime;
    }
}