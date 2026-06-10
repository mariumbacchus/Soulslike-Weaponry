package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

/**
 * @param killTriggerCap health cap before switching up one shot chance numbers
 * @param killChanceOverCap one shot chance over the killTriggerCap
 * @param killChanceUnderCap one shot chance under the killTriggerCap
 * @param missingHealthTriggerCap health cap before switching up missing health bonus damage chance numbers
 * @param missingHealthChanceOverCap chance to do bonus damage based on targets missing health over cap
 * @param missingHealthChanceUnderCap chance to do bonus damage based on targets missing health under cap
 * @param missingHealthMod bonus damage based on targets missing health
 * @param missingHealthPlayerMod bonus damage based on targets missing health if the target is a player
 * @param missingHealthMaxBonus max bonus damage the missing health bonus can deal
 */
public record FinalWounds(float killTriggerCap, float killChanceOverCap, float killChanceUnderCap, float missingHealthTriggerCap,
                          float missingHealthChanceOverCap, float missingHealthChanceUnderCap, float missingHealthMod, float missingHealthPlayerMod,
                          float missingHealthMaxBonus) implements IAbility {

    private static final List<EntityType<?>> NO_KILL_ENTITIES = WeaponUtil.getEntityListOffArray(WeaponConfig.mehrunes_razor_no_instakill_entity_blacklist);

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!NO_KILL_ENTITIES.contains(target.getType())) {
            double rand = attacker.getRandom().nextDouble();
            float ratio = target.getMaxHealth() >= this.killTriggerCap ? this.killChanceOverCap : this.killChanceUnderCap;
            if (rand <= ratio) {
                target.kill();
            }
        }
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if (target instanceof LivingEntity living && damageSource.getAttacker() instanceof LivingEntity attacker) {
            float ratio = living.getMaxHealth() >= this.missingHealthTriggerCap ? this.missingHealthChanceOverCap : this.missingHealthChanceUnderCap;
            if (attacker.getRandom().nextDouble() <= ratio) {
                double missing = living.getMaxHealth() - living.getHealth();
                return (float) Math.min(
                        missing * (target instanceof PlayerEntity ? this.missingHealthPlayerMod : this.missingHealthMod),
                        this.missingHealthMaxBonus
                );
            }
        }
        return 0;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.final_wounds").formatted(Formatting.DARK_PURPLE),
                Text.translatable("tooltip.soulsweapons.final_wounds.1", String.format("%.1f", WeaponConfig.mehrunes_razor_missing_health_chance_under_health_cap * 100) + "%").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.final_wounds.2", WeaponConfig.mehrunes_razor_missing_health_max_bonus_damage).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.final_wounds.3", String.format("%.1f", WeaponConfig.mehrunes_razor_missing_health_chance_over_health_cap * 100) + "%", WeaponConfig.mehrunes_razor_missing_health_trigger_cap).formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.final_wounds.4", String.format("%.2f", WeaponConfig.mehrunes_razor_kill_chance_under_health_cap * 100) + "%").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.final_wounds.5", String.format("%.2f", WeaponConfig.mehrunes_razor_kill_chance_over_health_cap * 100) + "%", WeaponConfig.mehrunes_razor_kill_trigger_cap).formatted(Formatting.DARK_GRAY)
        );
    }
}
