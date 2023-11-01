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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
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
                            this.shootProjectile(world, stack, itemStack, playerEntity, pullProgress, projectile);
                            stack.getNbt().putInt("firedShots", 0);
                        } else {
                            ArrowItem arrowItem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
                            PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack, playerEntity);
                            this.shootProjectile(world, stack, itemStack, playerEntity, pullProgress, persistentProjectileEntity);
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

    public void shootProjectile(World world, ItemStack stack, ItemStack arrowStack, PlayerEntity player, float pullProgress, PersistentProjectileEntity projectile) {
        projectile.setPos(player.getX(), player.getY() + 1.5F, player.getZ());
        projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, pullProgress * 3.0F, 1.0F);
        if (pullProgress == 1.0F) {
            projectile.setCritical(true);
        }
        double power = EnchantmentHelper.getLevel(Enchantments.POWER, stack); // Normal bow: 2.5 -> 5 (power V) -> 10 with crit
        projectile.setDamage(projectile.getDamage() + power * 0.6f); //This: 3 -> 6 -> 12 with crit
        int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
        if (punch > 0) {
            projectile.setPunch(punch);
        }
        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
            projectile.setOnFireFor(8);
        }
        stack.damage(1, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));

        boolean creativeAndInfinity = player.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
        boolean bl2 = creativeAndInfinity && arrowStack.isOf(Items.ARROW);
        if (bl2 || player.getAbilities().creativeMode && (arrowStack.isOf(Items.SPECTRAL_ARROW) || arrowStack.isOf(Items.TIPPED_ARROW))) {
            projectile.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }
        world.spawnEntity(projectile);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + pullProgress * 0.5F);
        if (!bl2 && !player.getAbilities().creativeMode) {
            arrowStack.decrement(1);
            if (arrowStack.isEmpty()) {
                player.getInventory().removeOne(arrowStack);
            }
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this));
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