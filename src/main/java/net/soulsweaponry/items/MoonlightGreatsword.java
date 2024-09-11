package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class MoonlightGreatsword extends ChargeToUseItem {

    public MoonlightGreatsword(ToolMaterial toolMaterial, Settings settings) {
        this(toolMaterial, ConfigConstructor.moonlight_greatsword_damage, ConfigConstructor.moonlight_greatsword_attack_speed, settings);
    }

    public MoonlightGreatsword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.MOONLIGHT);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_moonlight_greatsword;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                stack.damage(3, (LivingEntity)playerEntity, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                MoonlightProjectile entity = new MoonlightProjectile(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE, world, user);
                entity.setAgeAndPoints(30, 150, 4);
                //float damage = (float) user.getAttributes().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 1.5F, 1.0F);
                entity.setDamage(ConfigConstructor.moonlight_greatsword_projectile_damage);
                entity.setItemStack(stack);
                world.spawnEntity(entity);
                world.playSound(null, user.getBlockPos(), SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                if (this instanceof BluemoonGreatsword) {
                    if (stack.hasNbt() && !playerEntity.isCreative()) {
                        stack.getNbt().putInt(IChargeNeeded.CHARGE, 0);
                    }
                }
            }
        }
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[] {
                Text.translatable("tooltip.soulsweapons.moonlight_greatsword.part_1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.moonlight_greatsword.part_2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.moonlight_greatsword.part_3").formatted(Formatting.DARK_GRAY)
        };
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_moonlight_greatsword;
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
