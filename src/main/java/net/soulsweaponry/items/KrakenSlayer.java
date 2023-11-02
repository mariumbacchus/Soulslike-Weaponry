package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.KrakenSlayerProjectile;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KrakenSlayer extends ModdedBow {

    public KrakenSlayer(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            boolean creativeAndInfinity = playerEntity.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemStack = playerEntity.getProjectileType(stack);
            if (!itemStack.isEmpty() || creativeAndInfinity) {
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(Items.ARROW);
                }

                int maxUseTime = this.getMaxUseTime(stack) - remainingUseTicks;
                float pullProgress = this.getModdedPullProgress(maxUseTime);
                if (!((double)pullProgress < 0.1D)) {
                    if (!world.isClient) {
                        if (stack.hasNbt() && stack.getNbt().contains("firedShots") && stack.getNbt().getInt("firedShots") >= 2) {
                            KrakenSlayerProjectile projectile = new KrakenSlayerProjectile(EntityRegistry.KRAKEN_SLAYER_PROJECTILE, world);
                            projectile.setTrueDamage(ConfigConstructor.kraken_slayer_bonus_true_damage + EnchantmentHelper.getLevel(Enchantments.POWER, stack));
                            this.shootProjectile(world, stack, itemStack, playerEntity, pullProgress, projectile, 0.6f, 3.0f);
                            stack.getNbt().putInt("firedShots", 0);
                        } else {
                            ArrowItem arrowItem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                            PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack, playerEntity);
                            this.shootProjectile(world, stack, itemStack, playerEntity, pullProgress, persistentProjectileEntity, 0.6f, 3.0f);
                            if (stack.hasNbt()) {
                                if (stack.getNbt().contains("firedShots")) {
                                    stack.getNbt().putInt("firedShots", stack.getNbt().getInt("firedShots") + 1);
                                } else {
                                    stack.getNbt().putInt("firedShots", 1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.FAST_PULL, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.THIRD_SHOT, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public float getReducedPullTime() {
        return 10;
    }
}
