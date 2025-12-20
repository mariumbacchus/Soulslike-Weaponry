package net.soulsweaponry.items.abilities.use;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.entitydata.UmbralTrespassData;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record UmbralTrespass(float baseDamage, float bonusDamagePerLvl, float bonusEnchantDamageModifier,
                             int minCooldown, int cooldown, int reducedCooldownPerLvl,
                             float healMod, int ticksBeforeDismount, double maxHealthBonusDamage, float maxRange) implements IAbility {

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (user.hasStatusEffect(EffectRegistry.COOLDOWN)) {
            this.notifyCooldown(user);
            return ActionResult.FAIL;
        }
        if (user.getAttacking() != null && user.squaredDistanceTo(user.getAttacking()) < this.maxRange && world instanceof ServerWorld serverWorld) {
            LivingEntity target = user.getAttacking();
            if (user.startRiding(target, true)) {
                int lvl = WeaponUtil.getUpgradeLevel(stack);
                if (!UmbralTrespassData.shouldDamageRiding(user)) {
                    UmbralTrespassData.setShouldDamageRiding(user, true);
                    UmbralTrespassData.setOtherStats(user, this.baseDamage + lvl * this.bonusDamagePerLvl
                                    + this.bonusEnchantDamageModifier * EnchantmentHelper.getDamage(
                                            serverWorld, stack, target, user.getDamageSources().playerAttack(user), 0),
                            Math.max(this.minCooldown, this.cooldown - lvl * this.reducedCooldownPerLvl), this.healMod, this.maxHealthBonusDamage);
                    user.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, this.ticksBeforeDismount, 0));
                    user.addStatusEffect(new StatusEffectInstance(EffectRegistry.GHOSTLY, this.ticksBeforeDismount, 0));
                }
                world.playSound(null, user.getBlockPos(), SoundRegistry.UMBRAL_TRESPASS_EVENT, SoundCategory.PLAYERS, 0.8f, 1f);
                ParticleHandler.particleOutburstMap(world, 150, user.getX(), user.getEyeY(), user.getZ(), ParticleEvents.SOUL_FLAME_SMALL_OUTBURST_MAP, 1f);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAIL;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.umbral_trespass").formatted(Formatting.DARK_RED),
                Text.translatable("tooltip.soulsweapons.umbral_trespass_description_1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.umbral_trespass_description_2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.umbral_trespass_description_3").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.umbral_trespass_description_4").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.umbral_trespass_description_5").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.umbral_trespass_description_6").formatted(Formatting.DARK_GRAY)
        );
    }
}
