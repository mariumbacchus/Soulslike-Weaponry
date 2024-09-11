package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class PureMoonlightGreatsword extends ChargeToUseItem {

    public PureMoonlightGreatsword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.pure_moonlight_greatsword_damage, ConfigConstructor.pure_moonlight_greatsword_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.TRIPLE_MOONLIGHT);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                if (!world.isClient) {
                    stack.damage(5, (LivingEntity)playerEntity, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                    for (int j = -1; j < 2; j++) {
                        MoonlightProjectile entity = new MoonlightProjectile(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE, world, user);
                        entity.setAgeAndPoints(30, 75, 4);
                        entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw() + j*5, 0.0F, 1.5F, 1.0F);
                        entity.setDamage(ConfigConstructor.moonlight_greatsword_projectile_damage + 1f);
                        world.spawnEntity(entity);
                    }
                    world.playSound(null, user.getBlockPos(), SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                }
            }
        }
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String getReduceCooldownEnchantId(ItemStack stack) {
        return null;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_pure_moonlight_greatsword;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_pure_moonlight_greatsword;
    }
}