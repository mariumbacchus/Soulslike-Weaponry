package net.soulsweaponry.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DragonStaff extends SwordItem {

    public DragonStaff(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.DRAGON_STAFF_DAMAGE.get(), pAttackSpeedModifier, pProperties);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity user, int remainingUseTicks) {
        if (user.isCrouching() && remainingUseTicks > 0) {
            Vec3 pov = user.getViewVector(1f);
            Vec3 particleSpawn = pov.scale(1);
            Vec3 area = pov.scale(10).add(user.position());
            Level world = user.level;
            for (Entity entity : world.getEntities(user, new AABB(user.blockPosition().offset(0, 2, 0), new BlockPos(area)))) {
                if (entity instanceof LivingEntity) {
                    entity.hurt(CustomDamageSource.dragonMist(user), 2);
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(EffectRegistry.HALLOWED_DRAGON_MIST.get(), 100, CommonConfig.DRAGON_STAFF_AURA_STRENGTH.get()));
                }
            }
            if (world.isClientSide) {
                for (int k = 0; k < 10; k++) {
                    world.addParticle(ParticleRegistry.PURPLE_FLAME.get(), true, particleSpawn.add(user.position()).x(), particleSpawn.add(user.position()).y() + 1.5F, particleSpawn.add(user.position()).z(), pov.x + user.getRandom().nextDouble() - .25, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .25);
                    world.addParticle(ParticleTypes.DRAGON_BREATH, true, particleSpawn.add(user.position()).x(), particleSpawn.add(user.position()).y() + 1.5F, particleSpawn.add(user.position()).z(), pov.x + user.getRandom().nextDouble() - .25, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .25);
                }
            }
        } else {
            user.stopUsingItem();
            super.onUsingTick(stack, user, remainingUseTicks);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level pLevel, LivingEntity user) {
        this.stop(user, stack);
        return super.finishUsingItem(stack, pLevel, user);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level pLevel, LivingEntity user, int pTimeCharged) {
        this.stop(user, stack);
        super.releaseUsing(stack, pLevel, user, pTimeCharged);
    }

    private void stop(LivingEntity user, ItemStack stack) {
        if (user instanceof Player && !((Player)user).isCreative()) ((Player) user).getCooldowns().addCooldown(this, this.getCooldown(stack));
        stack.hurtAndBreak(3, user, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (!user.isCrouching()) {
            if (!user.isCreative()) user.getCooldowns().addCooldown(this, this.getCooldown(itemStack)*2);
            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.NEUTRAL, 0.5f, 2/(world.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!world.isClientSide) {
                /*DragonStaffProjectile fireball = new DragonStaffProjectile(world, user, itemStack);
                fireball.setPos(user.getX(), user.getY() + 1.0f, user.getZ());
                fireball.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 0f);
                world.spawnEntity(fireball);*///TODO add projectile
                itemStack.hurtAndBreak(1, user, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
            }
            return InteractionResultHolder.success(itemStack);
        } else {
            if (itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1) {
                return InteractionResultHolder.fail(itemStack);
            } else {
                user.startUsingItem(hand);
                return InteractionResultHolder.consume(itemStack);
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return CommonConfig.DRAGON_STAFF_USE_TIME.get() + WeaponUtil.getEnchantDamageBonus(stack) * 20;
    }

    private int getCooldown(ItemStack stack) {
        return CommonConfig.DRAGON_STAFF_COOLDOWN.get() - (WeaponUtil.getEnchantDamageBonus(stack) * 10);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.DRAGON_STAFF, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.VENGEFUL_FOG, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }
}
