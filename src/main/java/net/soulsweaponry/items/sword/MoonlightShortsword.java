package net.soulsweaponry.items.sword;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.util.TooltipAbilities;

public class MoonlightShortsword extends ModdedSword {

    public MoonlightShortsword(ToolMaterial toolMaterial, Settings settings) {
        this(toolMaterial, (int) ConfigConstructor.moonlight_shortsword_damage, ConfigConstructor.moonlight_shortsword_attack_speed, settings);
    }

    public MoonlightShortsword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        this.addTooltipAbility(TooltipAbilities.MOONLIGHT_ATTACK);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_moonlight_shortsword;
    }

    public static void summonSmallProjectile(World world, PlayerEntity user) {
        for (Hand hand : Hand.values()) {
            ItemStack itemStack = user.getStackInHand(hand);
            boolean effect = user.hasStatusEffect(EffectRegistry.MOON_HERALD);
            if (effect && itemStack.isEmpty()) {
                itemStack = WeaponRegistry.MOONLIGHT_SHORTSWORD.getDefaultStack();
            }
            boolean acceptItem = itemStack.getItem() instanceof MoonlightShortsword;
            if ((acceptItem && !user.getItemCooldownManager().isCoolingDown(itemStack.getItem())) || (effect && !user.getItemCooldownManager().isCoolingDown(ItemRegistry.MOONSTONE_RING))) {
                float damage = itemStack.getItem() instanceof MoonlightShortsword item ? item.getProjectileDamage() : ConfigConstructor.moonlight_shortsword_projectile_damage;
                float velocity = itemStack.getItem() instanceof MoonlightShortsword item ? item.getProjectileVelocity() : ConfigConstructor.moonlight_shortsword_projectile_velocity;
                MoonlightProjectile projectile = new MoonlightProjectile(EntityRegistry.MOONLIGHT_ENTITY_TYPE, world, user, itemStack);
                if (effect && !acceptItem) {
                    damage += user.getStatusEffect(EffectRegistry.MOON_HERALD).getAmplifier() * 2f;
                    user.getItemCooldownManager().set(ItemRegistry.MOONSTONE_RING, 4);
                }
                projectile.setAgeAndPoints(15, 30, (byte) 1);
                projectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, velocity, 0f);
                projectile.setDamage(damage);
                world.spawnEntity(projectile);

                //Damaging the itemstack messes with Better Combat, therefore postHit damages weapon twice instead
                /* itemStack.damage(1, user, (p_220045_0_) -> {
                    p_220045_0_.sendToolBreakStatus(hand);
                }); */
                if (itemStack.getItem() instanceof BluemoonShortsword bluemoon) {
                    bluemoon.applyItemCooldownNoCheck(user, (int) Math.max(ConfigConstructor.bluemoon_shortsword_projectile_min_cooldown, ConfigConstructor.bluemoon_shortsword_projectile_cooldown
                            - bluemoon.getReduceCooldownEnchantLevel(itemStack) * 10));
                }
                world.playSound(null, user.getBlockPos(), SoundRegistry.MOONLIGHT_SMALL_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                user.getItemCooldownManager().set(WeaponRegistry.MOONLIGHT_SHORTSWORD, (int) ConfigConstructor.moonlight_shortsword_projectile_cooldown);
                user.getItemCooldownManager().set(ItemRegistry.MOONSTONE_RING, (int) ConfigConstructor.moonstone_ring_projectile_cooldown);
                user.swingHand(Hand.MAIN_HAND, true);
            }
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            stack.damage(1, attacker, LivingEntity.getSlotForHand(attacker.getActiveHand()));
        }
        return super.postHit(stack, target, attacker);
    }

    public float getProjectileDamage() {
        return ConfigConstructor.moonlight_shortsword_projectile_damage;
    }

    public float getProjectileVelocity() {
        return ConfigConstructor.moonlight_shortsword_projectile_velocity;
    }
}