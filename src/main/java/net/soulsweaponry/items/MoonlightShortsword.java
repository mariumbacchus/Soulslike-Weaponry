package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.WeaponUtil;

import javax.annotation.Nullable;
import java.util.List;

public class MoonlightShortsword extends ModdedSword {
    
    public MoonlightShortsword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, CommonConfig.MOONLIGHT_SHORTSWORD_DAMAGE.get(), attackSpeed, settings);
    }

    public MoonlightShortsword(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean isDisabled() {
        return CommonConfig.DISABLE_USE_MOONLIGHT_SHORTSWORD.get();
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
                float damage = CommonConfig.MOONLIGHT_SHORTSWORD_PROJECTILE_DAMAGE.get();
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
                    user.getItemCooldownManager().set(WeaponRegistry.BLUEMOON_SHORTSWORD.get(), CommonConfig.BLUEMOON_SHORTSWORD_PROJECTILE_COOLDOWN.get());
                }

                world.playSound(null, user.getBlockPos(), SoundRegistry.MOONLIGHT_SMALL_EVENT.get(), SoundCategory.PLAYERS, 1f, 1f);
                user.getItemCooldownManager().set(WeaponRegistry.MOONLIGHT_SHORTSWORD.get(), CommonConfig.MOONLIGHT_SHORTSWORD_PROJECTILE_COOLDOWN.get());
                user.swingHand(Hand.MAIN_HAND, true);
            }
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled()) {
            stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.MOONLIGHT_ATTACK, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
    }
}
