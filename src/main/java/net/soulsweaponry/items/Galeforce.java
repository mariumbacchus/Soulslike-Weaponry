package net.soulsweaponry.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.ChargedArrow;
import net.soulsweaponry.util.WeaponUtil;

public class Galeforce extends BowItem {

    public Galeforce(Settings settings) {
        super(settings);
    }
    
    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity)user;
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 50, 3));
            if (playerEntity.getOffHandStack().isOf(this)) {
                float f = playerEntity.getYaw();
                float g = playerEntity.getPitch();
                float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
                float k = -MathHelper.sin(g * 0.017453292F);
                float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
                float m = MathHelper.sqrt(h * h + k * k + l * l);
                float n = 3.0F * ((1.0F + 1F) / 4.0F);
                h *= n / m;
                k *= n / m;
                l *= n / m;
                playerEntity.addVelocity((double)h, (double)k, (double)l);
                playerEntity.getItemCooldownManager().set(this, ConfigConstructor.galeforce_dash_cooldown);
            }
            boolean creativeAndInfinity = playerEntity.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemStack = playerEntity.getArrowType(stack);
            if (!itemStack.isEmpty() || creativeAndInfinity) {
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(Items.ARROW);
                }

                int maxUseTime = this.getMaxUseTime(stack) - remainingUseTicks;
                float pullProgress = getPullProgress(maxUseTime);
                if (!((double)pullProgress < 0.1D)) {
                    boolean bl2 = creativeAndInfinity && itemStack.isOf(Items.ARROW);
                    if (!world.isClient) {
                        ChargedArrow chargedArrow = new ChargedArrow(world, user, itemStack);
                        chargedArrow.setPos(user.getX(), user.getY() + 1.5F, user.getZ());
                        chargedArrow.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, pullProgress * 3.0F, 1.0F);
                        if (pullProgress == 1.0F) {
                            chargedArrow.setCritical(true);
                        }
       
                        double power = EnchantmentHelper.getLevel(Enchantments.POWER, stack); // Normal bow: 2.5 -> 5 (power V) -> 10 with crit
                        chargedArrow.setDamage(chargedArrow.getDamage() + power * 0.6f + ConfigConstructor.galeforce_bonus_damage); //This: 3 -> 6 -> 12 with crit
                        int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
                        if (punch > 0) {
                            chargedArrow.setPunch(punch);
                        }
       
                        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
                            chargedArrow.setOnFireFor(8);
                        }
        
                        stack.damage(1, user, (p_220045_0_) -> {
                            p_220045_0_.sendToolBreakStatus(user.getActiveHand());
                        });
        
                        if (bl2 || playerEntity.getAbilities().creativeMode && (itemStack.isOf(Items.SPECTRAL_ARROW) || itemStack.isOf(Items.TIPPED_ARROW))) {
                            chargedArrow.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                        }                    
                        world.spawnEntity(chargedArrow);               
                    }
                    world.playSound((PlayerEntity)null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + pullProgress * 0.5F);
                    if (!bl2 && !playerEntity.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            playerEntity.getInventory().removeOne(itemStack);
                        }
                    }
                    playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.GALEFORCE, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
