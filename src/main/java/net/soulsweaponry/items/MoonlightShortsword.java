package net.soulsweaponry.items;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class MoonlightShortsword extends ModdedSword {

    public MoonlightShortsword(ToolMaterial toolMaterial, Settings settings) {
        this(toolMaterial, ConfigConstructor.moonlight_shortsword_damage, ConfigConstructor.moonlight_shortsword_attack_speed, settings);
    }

    public MoonlightShortsword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.MOONLIGHT_ATTACK);
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_moonlight_shortsword;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_moonlight_shortsword;
    }

    public static void summonSmallProjectile(World world, PlayerEntity user) {
        for (Hand hand : Hand.values()) {
            ItemStack itemStack = user.getStackInHand(hand);
            if (!user.getItemCooldownManager().isCoolingDown(WeaponRegistry.MOONLIGHT_SHORTSWORD.get()) && itemStack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD.get())
                    || user.hasStatusEffect(EffectRegistry.MOON_HERALD.get())
                    || (itemStack.isOf(WeaponRegistry.BLUEMOON_SHORTSWORD.get()) && !user.getItemCooldownManager().isCoolingDown(WeaponRegistry.BLUEMOON_SHORTSWORD.get()))) {
                boolean bl = itemStack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD.get()) || itemStack.isOf(WeaponRegistry.BLUEMOON_SHORTSWORD.get());
                if (user.hasStatusEffect(EffectRegistry.MOON_HERALD.get()) && bl && user.getStatusEffect(EffectRegistry.MOON_HERALD.get()).getDuration() % 4 != 0) {
                    return;
                }
                float damage = ConfigConstructor.moonlight_shortsword_projectile_damage;
                MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.MOONLIGHT_ENTITY_TYPE.get(), world, user);
                if (user.hasStatusEffect(EffectRegistry.MOON_HERALD.get()) && !itemStack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD.get())) {
                    damage += user.getStatusEffect(EffectRegistry.MOON_HERALD.get()).getAmplifier() * 2f;
                }
                projectile.setAgeAndPoints(15, 30, 1);
                projectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 0f);
                projectile.setDamage(damage);
                if (itemStack.isOf(WeaponRegistry.MOONLIGHT_SHORTSWORD.get())) projectile.setItemStack(itemStack);
                world.spawnEntity(projectile);

                //Damaging the itemstack messes with Better Combat, therefore postHit damages weapon twice instead
                /* itemStack.damage(1, user, (p_220045_0_) -> {
                    p_220045_0_.sendToolBreakStatus(hand);
                }); */
                if (itemStack.getItem() instanceof BluemoonShortsword) {
                    user.getItemCooldownManager().set(WeaponRegistry.BLUEMOON_SHORTSWORD.get(), ConfigConstructor.bluemoon_shortsword_projectile_cooldown);
                }

                world.playSound(null, user.getBlockPos(), SoundRegistry.MOONLIGHT_SMALL_EVENT.get(), SoundCategory.PLAYERS, 1f, 1f);
                user.getItemCooldownManager().set(WeaponRegistry.MOONLIGHT_SHORTSWORD.get(), ConfigConstructor.moonlight_shortsword_projectile_cooldown);
                user.swingHand(Hand.MAIN_HAND, true);
            }
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public int getReduceCooldownEnchantLevel(ItemStack stack) {
        return 0;
    }
}
