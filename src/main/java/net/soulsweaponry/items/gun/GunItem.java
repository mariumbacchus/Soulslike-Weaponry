package net.soulsweaponry.items.gun;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import net.soulsweaponry.items.IConfigDisable;
import net.soulsweaponry.items.ITooltipInfo;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public abstract class GunItem extends RangedWeaponItem implements IConfigDisable, ITooltipInfo {

    public static final Predicate<ItemStack> SILVER_PROJECTILE = (stack) -> stack.isOf(ItemRegistry.SILVER_BULLET);
    protected final List<TooltipAbilities> tooltipAbilities = new ArrayList<>();

    public GunItem(Settings settings) {
        super(settings);
        this.addTooltipAbility(TooltipAbilities.GUN_ITEM);
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
        if (EnchantmentHelper.getLevel(EnchantRegistry.MISFIRE_CURSE, gunStack) > 0 && !world.isClient && shooter.getRandom().nextDouble() < ConfigConstructor.misfire_curse_enchant_trigger_chance) {
            world.createExplosion(null, shooter.getX(), shooter.getBodyY(0.5f), shooter.getZ(), 3f, true, World.ExplosionSourceType.MOB);
            shooter.setOnFireFor(3);
        }
        //TODO remove power, punch and flame stuff later since people may still have those enchants on them from earlier versions (removed now)
        float power = (this.getBulletDamage(gunStack) / this.getBulletVelocity(gunStack)) + EnchantmentHelper.getLevel(Enchantments.POWER, gunStack) / 2f;
        int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, gunStack);
        int ethereal = EnchantmentHelper.getLevel(EnchantRegistry.ETHEREAL, gunStack);
        int explosivePower = EnchantmentHelper.getLevel(EnchantRegistry.EXPLOSIVE_ROUNDS, gunStack);
        int chainLightningLevel = EnchantmentHelper.getLevel(EnchantRegistry.CHAIN_LIGHTNING, gunStack);
        int blightCarrierLevel = EnchantmentHelper.getLevel(EnchantRegistry.BLIGHT_CARRIER, gunStack);
        SilverBulletEntity entity = this.getModdedProjectile(world, shooter, gunStack);
        entity.setPos(shooter.getX(), shooter.getEyeY() - 0.4f, shooter.getZ());
        entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
        entity.setNoClip(ethereal > 0);
        entity.setEthereal(ethereal > 0);
        entity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, this.getBulletVelocity(gunStack), this.getBulletDivergence(gunStack));
        entity.setPostureLoss(this.getPostureLoss(gunStack));
        entity.setDamage(power);
        if (punch > 0) {
            entity.setPunch(punch);
        }
        if (EnchantmentHelper.getLevel(Enchantments.FLAME, gunStack) > 0) {
            entity.setOnFireFor(8);
        }
        if (explosivePower > 0) {
            entity.setExplosionPower(explosivePower);
        }
        if (chainLightningLevel > 0) {
            entity.setChainLightningDamage(chainLightningLevel * ConfigConstructor.chain_lightning_enchant_damage_per_level);
            entity.setChainLightningRange(chainLightningLevel * ConfigConstructor.chain_lightning_enchant_range_per_level);
        }
        if (blightCarrierLevel > 0) {
            entity.setBlightCarrier(blightCarrierLevel * ConfigConstructor.blight_carrier_enchant_blight_per_level);
        }
        return entity;
    }

    public SilverBulletEntity getModdedProjectile(World world, LivingEntity shooter, ItemStack gunStack) {
        return new SilverBulletEntity(world, shooter, gunStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.isDisabled(stack)) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        this.appendTooltipAbilities(stack, world, tooltip, context);
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public List<TooltipAbilities> getTooltipAbilities() {
        return this.tooltipAbilities;
    }

    @Override
    public void addTooltipAbility(TooltipAbilities... abilities) {
        Collections.addAll(this.tooltipAbilities, abilities);
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public int getRange() {
        return 15;
    }

    @Override
    public int getEnchantability() {
        return 7;
    }
}
