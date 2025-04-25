package net.soulsweaponry.items.sword;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.noclip.MoonveilWave;
import net.soulsweaponry.items.ChargeToUseItem;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

public class Moonveil extends ChargeToUseItem {

    public Moonveil(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.moonveil_damage, ConfigConstructor.moonveil_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.TRANSIENT_MOONLIGHT);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(this) && !world.isClient) {
            int time = WeaponUtil.getChargeTime(stack, remainingUseTicks);
            if (time >= 10) {
                if (player.isSneaking()) {
                    MoonveilWave entity = new MoonveilWave(EntityRegistry.MOONVEIL_VERTICAL, world, user, 15);
                    entity.setPos(player.getX(), player.getEyeY() - 1f, player.getZ());
                    entity.setModelRotation(90);
                    entity.setModelTranslationY(1f);
                    entity.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1f, 1.0F);
                    entity.setDamage(ConfigConstructor.moonveil_vertical_damage + WeaponUtil.getEnchantDamageBonus(stack));
                    world.spawnEntity(entity);
                    world.playSound(null, user.getBlockPos(), SoundRegistry.MOONVEIL_VERTICAL, SoundCategory.PLAYERS, 1f, 1f);
                } else {
                    MoonveilWave entity = new MoonveilWave(world, user, 6);
                    entity.setPos(player.getX(), player.getEyeY() - 0.3f, player.getZ());
                    entity.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 1.0f, 1.0F);
                    entity.setDamage(ConfigConstructor.moonveil_wave_damage + ((float)WeaponUtil.getEnchantDamageBonus(stack) * 0.8f));
                    world.spawnEntity(entity);
                    world.playSound(null, user.getBlockPos(), SoundRegistry.MOONVEIL_HORIZONTAL, SoundCategory.PLAYERS, 1f, 1f);
                }
                stack.damage(3, player, (p) -> p.sendToolBreakStatus(user.getActiveHand()));
                this.applyItemCooldown(player, ConfigConstructor.moonveil_ability_cooldown);
            }
        }
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_moonveil;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_moonveil;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return null;
    }
}
