package net.soulsweaponry.items;

import java.util.List;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.DragonStaffProjectile;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

public class DragonStaff extends SwordItem {

    public DragonStaff(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.dragon_staff_damage, attackSpeed, settings);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (user.isSneaking() && remainingUseTicks > 0) {
            Vec3d pov = user.getRotationVector();
            Vec3d particleSpawn = pov.multiply(1);
            Vec3d area = pov.multiply(10).add(user.getPos());
            for (Entity entity : world.getOtherEntities(user, new Box(user.getBlockPos().add(0, 2, 0), new BlockPos(area)))) {
                if (entity instanceof LivingEntity) {
                    entity.damage(CustomDamageSource.dragonMist(user), 2);
                    ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(EffectRegistry.HALLOWED_DRAGON_MIST, 100, ConfigConstructor.dragon_staff_aura_strength));
                }
            }
            if (world.isClient) {
                for (int k = 0; k < 10; k++) {
                    world.addParticle(ParticleRegistry.PURPLE_FLAME, true, particleSpawn.add(user.getPos()).getX(), particleSpawn.add(user.getPos()).getY() + 1.5F, particleSpawn.add(user.getPos()).getZ(), pov.x + user.getRandom().nextDouble() - .25, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .25);
                    world.addParticle(ParticleTypes.DRAGON_BREATH, true, particleSpawn.add(user.getPos()).getX(), particleSpawn.add(user.getPos()).getY() + 1.5F, particleSpawn.add(user.getPos()).getZ(), pov.x + user.getRandom().nextDouble() - .25, pov.y + user.getRandom().nextDouble() - .5, pov.z + user.getRandom().nextDouble() - .25);
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

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        this.stop(user, stack);
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    private void stop(LivingEntity user, ItemStack stack) {
        if (user instanceof PlayerEntity && !((PlayerEntity)user).isCreative()) ((PlayerEntity) user).getItemCooldownManager().set(this, this.getCooldown(stack));
        stack.damage(3, user, (p_220045_0_) -> {
            p_220045_0_.sendToolBreakStatus(user.getActiveHand());
        });
    }
    
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!user.isSneaking()) {
            if (!user.isCreative()) user.getItemCooldownManager().set(this, this.getCooldown(itemStack)*2);
            world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.NEUTRAL, 0.5f, 2/(world.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!world.isClient) {
                Vec3d look = user.getRotationVector();
                DragonStaffProjectile fireball = new DragonStaffProjectile(world, user, look.getX(), look.getY(), look.getZ());
                fireball.setPos(user.getX(), user.getY() + 1.0f, user.getZ());
                fireball.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 0f);
                world.spawnEntity(fireball);
                itemStack.damage(1, user, (p_220045_0_) -> {
                    p_220045_0_.sendToolBreakStatus(hand);
                });
            }
            return TypedActionResult.success(itemStack, world.isClient());
        } else {
            if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
                return TypedActionResult.fail(itemStack);
            } else {
                user.setCurrentHand(hand);
                return TypedActionResult.consume(itemStack);
            }
        }
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return ConfigConstructor.dragon_staff_use_time + WeaponUtil.getEnchantDamageBonus(stack) * 20;
    }

    private int getCooldown(ItemStack stack) {
        return ConfigConstructor.dragon_staff_cooldown - (WeaponUtil.getEnchantDamageBonus(stack) * 10);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.DRAGON_STAFF, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.VENGEFUL_FOG, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
