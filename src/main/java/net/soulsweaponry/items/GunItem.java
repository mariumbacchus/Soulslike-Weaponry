package net.soulsweaponry.items;

import java.util.List;
import java.util.function.Predicate;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.EnchantRegistry;
import org.jetbrains.annotations.Nullable;

public abstract class GunItem extends BowItem implements IConfigDisable {

    public static final Predicate<ItemStack> SILVER_PROJECTILE = (stack) -> stack.isOf(ItemRegistry.SILVER_BULLET);

    public GunItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public Predicate<ItemStack> getProjectiles() {
        return SILVER_PROJECTILE;
    }

    public int getReducedCooldown(ItemStack stack) {
        return EnchantmentHelper.getLevel(EnchantRegistry.FAST_HANDS, stack) * 8;
    }

    public abstract int getPostureLoss(ItemStack stack);
    public abstract float getBulletDamage(ItemStack stack);
    public abstract float getBulletVelocity(ItemStack stack);
    public abstract float getBulletDivergence(ItemStack stack);
    public abstract int getCooldown(ItemStack stack);
    public abstract int bulletsNeeded();
    @Override
    public abstract boolean isFireproof();
    public int getMaxUseTime(ItemStack stack) {
        return 0;
    }

    public PersistentProjectileEntity createSilverBulletEntity(World world, LivingEntity shooter, ItemStack gunStack) {
        float power = (this.getBulletDamage(gunStack) / this.getBulletVelocity(gunStack)) + EnchantmentHelper.getLevel(Enchantments.POWER, gunStack) / 2f;
        int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, gunStack);
        SilverBulletEntity entity = this.getModdedProjectile(world, shooter, gunStack);
        if (gunStack.isOf(GunRegistry.GATLING_GUN)) {
            entity.setPos(shooter.getX(), shooter.getEyeY() - 0.2f, shooter.getZ());
        } else {
            entity.setPos(shooter.getX(), shooter.getEyeY(), shooter.getZ());
        }
        entity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, this.getBulletVelocity(gunStack), this.getBulletDivergence(gunStack));
        entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        entity.setPostureLoss(this.getPostureLoss(gunStack));
        entity.setDamage(power);
        if (punch > 0) {
            entity.setPunch(punch);
        }
        if (EnchantmentHelper.getLevel(Enchantments.FLAME, gunStack) > 0) {
            entity.setOnFireFor(8);
        }
        return entity;
    }

    public SilverBulletEntity getModdedProjectile(World world, LivingEntity shooter, ItemStack gunStack) {
        return new SilverBulletEntity(world, shooter);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.isDisabled(stack)) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.gun_posture_loss").append(Text.literal(String.valueOf(this.getPostureLoss(stack)))).formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.gun_posture_loss_on_players", MathHelper.floor(ConfigConstructor.silver_bullet_posture_loss_on_player_modifier * 100f) + "%").formatted(Formatting.DARK_GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.gun_damage").append(Text.literal(String.format("%.1f", this.getBulletDamage(stack)))).formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.gun_cooldown").append(Text.literal(String.valueOf(this.getCooldown(stack)))).formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.gun_bullets_used").append(Text.literal(String.valueOf(this.bulletsNeeded()))).formatted(Formatting.GRAY));
            if (this.getMaxUseTime(stack) != 0) {
                tooltip.add(Text.translatable("tooltip.soulsweapons.gun_max_use_time").append(Text.literal(String.valueOf(this.getMaxUseTime(stack)))).formatted(Formatting.GRAY));
            }
        }
        else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
