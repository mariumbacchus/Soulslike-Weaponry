package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import javax.annotation.Nullable;
import java.util.List;

public class WhirligigSawblade extends ChargeToUseItem {

    public WhirligigSawblade(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, CommonConfig.WHIRLIGIG_SAWBLADE_DAMAGE.get(), attackSpeed, settings);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return CommonConfig.WHIRLIGIG_SAWBLADE_USE_TIME.get() + WeaponUtil.getEnchantDamageBonus(stack) * 10;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        Vec3d vecBlocksAway = user.getRotationVector().multiply(5).add(user.getPos());
        Box chunkBox = new Box(user.getX(), user.getY(), user.getZ(), vecBlocksAway.x, vecBlocksAway.y + 1, vecBlocksAway.z);
        List<Entity> nearbyEntities = world.getOtherEntities(user, chunkBox);
        if (remainingUseTicks > 0) {
            for (Entity nearbyEntity : nearbyEntities) {
                if (nearbyEntity instanceof LivingEntity target) {
                    if (target.damage(DamageSource.mob(user), CommonConfig.WHIRLIGIG_SAWBLADE_ABILITY_DAMAGE.get() + EnchantmentHelper.getAttackDamage(stack, target.getGroup()))) {
                        world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 1f, 1f);
                        target.takeKnockback(1F, 0, 0);
                        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEED.get(), 100, 0));
                    }
                    world.addParticle(ParticleTypes.SWEEP_ATTACK, true, target.getX(), target.getY() + 1F, target.getZ(), target.getRandom().nextInt(10) - 5, target.getRandom().nextInt(10) - 5, target.getRandom().nextInt(10) - 5);
                }
            }
        } else {
            user.stopUsingItem();
            super.usageTick(world, user, stack, remainingUseTicks);
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        this.stop(user, stack);
        return super.finishUsing(stack, world, user);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        this.stop(user, stack);
    }

    private void stop(LivingEntity user, ItemStack stack) {
        if (user instanceof PlayerEntity && !((PlayerEntity)user).isCreative()) ((PlayerEntity) user).getItemCooldownManager().set(this, this.getCooldown(stack));
        stack.damage(3, user, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
    }

    private int getCooldown(ItemStack stack) {
        return CommonConfig.WHIRLIGIG_SAWBLADE_COOLDOWN.get() - (WeaponUtil.getEnchantDamageBonus(stack) * 10);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SAWBLADE, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
