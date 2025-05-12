package net.soulsweaponry.items.gun;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
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

    protected final List<TooltipAbilities> tooltipAbilities = new ArrayList<>();

    public GunItem(Settings settings) {
        super(settings);
        this.addTooltipAbility(TooltipAbilities.GUN_ITEM);
    }
    
    @Override
    public Predicate<ItemStack> getProjectiles() {
        return (stack) -> stack.isOf(ItemRegistry.SILVER_BULLET) && stack.getCount() >= this.getBulletsNeeded();
    }

    public int getReducedCooldown(ItemStack stack) {
        return EnchantmentHelper.getLevel(EnchantRegistry.FAST_HANDS, stack) * 8;
    }

    public abstract int getPostureLoss(ItemStack stack);
    public abstract float getBulletDamage(ItemStack stack);
    public abstract float getBulletVelocity(ItemStack stack);
    public abstract float getBulletDivergence(ItemStack stack);
    public abstract int getCooldown(ItemStack stack);
    public abstract int getBulletsNeeded();
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
        float power = (this.getBulletDamage(gunStack) / this.getBulletVelocity(gunStack)) + EnchantmentHelper.getLevel(Enchantments.POWER, gunStack) / 2f;
        int ethereal = EnchantmentHelper.getLevel(EnchantRegistry.ETHEREAL, gunStack);
        int explosivePower = EnchantmentHelper.getLevel(EnchantRegistry.EXPLOSIVE_ROUNDS, gunStack);
        int chainLightningLevel = EnchantmentHelper.getLevel(EnchantRegistry.CHAIN_LIGHTNING, gunStack);
        int blightCarrierLevel = EnchantmentHelper.getLevel(EnchantRegistry.BLIGHT_CARRIER, gunStack);
        int freezeLevel = EnchantmentHelper.getLevel(EnchantRegistry.FROSTSILVER, gunStack);
        SilverBulletEntity entity = this.getModdedProjectile(world, shooter, gunStack);
        entity.setPos(shooter.getX(), shooter.getEyeY() - 0.4f, shooter.getZ());
        entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
        entity.setNoClip(ethereal > 0);
        entity.setEthereal(ethereal > 0);
        entity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, this.getBulletVelocity(gunStack) / 8, this.getBulletDivergence(gunStack));
        entity.setPostureLoss(this.getPostureLoss(gunStack));
        entity.setDamage(power);
        if (explosivePower > 0) {
            entity.setExplosionPower(explosivePower);
        }
        if (chainLightningLevel > 0) {
            entity.setChainLightningDamage(this.getBulletDamage(gunStack) * chainLightningLevel * ConfigConstructor.chain_lightning_enchant_damage_mod_per_level);
            entity.setChainLightningRange(chainLightningLevel * ConfigConstructor.chain_lightning_enchant_range_per_level);
        }
        if (blightCarrierLevel > 0) {
            entity.setBlightCarrier(blightCarrierLevel * ConfigConstructor.blight_carrier_enchant_blight_per_level);
        }
        if (freezeLevel > 0) {
            entity.setFreezeAmplifier(freezeLevel * ConfigConstructor.frostsilver_enchant_permafrost_per_level);
        }
        return entity;
    }

    @Nullable
    public ItemStack canShoot(PlayerEntity user, ItemStack stack) {
        boolean bl = user.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
        ItemStack bullet = this.getProjectileType(user);
        if (!bullet.isEmpty() || bl) {
            if (bullet.isEmpty()) {
                return new ItemStack(ItemRegistry.SILVER_BULLET);
            }
            int toRemove = this.getBulletsNeeded();
            Item bulletItem = ItemRegistry.SILVER_BULLET;

            for (int slot = 0; slot < user.getInventory().size() && toRemove > 0; slot++) {
                ItemStack slotStack = user.getInventory().getStack(slot);
                if (slotStack.isOf(bulletItem)) {
                    int removed = Math.min(slotStack.getCount(), toRemove);
                    slotStack.decrement(removed);
                    toRemove -= removed;
                }
            }
            return bullet;
        }
        return null;
    }

    public ItemStack getProjectileType(PlayerEntity player) {
        int needed = this.getBulletsNeeded();
        Item bulletItem = ItemRegistry.SILVER_BULLET;
        Predicate<ItemStack> heldPred = this.getHeldProjectiles();
        ItemStack held = RangedWeaponItem.getHeldProjectile(player, heldPred);
        if (!held.isEmpty()) return held;

        int totalFound = 0;
        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            ItemStack slotStack = player.getInventory().getStack(slot);
            if (slotStack.isOf(bulletItem)) {
                totalFound += slotStack.getCount();
                if (totalFound >= needed) break;
            }
        }
        if (totalFound < needed) {
            if (player.getAbilities().creativeMode) {
                return new ItemStack(bulletItem, needed);
            }
            else {
                return ItemStack.EMPTY;
            }
        }
        return new ItemStack(bulletItem, needed);
    }

    public SilverBulletEntity getModdedProjectile(World world, LivingEntity shooter, ItemStack gunStack) {
        return new SilverBulletEntity(world, shooter, gunStack);
    }

    public void spawnShotParticles(World world, PlayerEntity user, int amount, float spread) {
        Vec3d look = user.getRotationVector();
        Vec3d eyePos = user.getEyePos();
        Vec3d muzzle = eyePos.add(look);

        Random rand = user.getRandom();
        for (int i = 0; i < amount; i++) {
            double vx = look.x + rand.nextGaussian() * spread;
            double vy = look.y + rand.nextGaussian() * spread;
            double vz = look.z + rand.nextGaussian() * spread;
            if (i % 2 == 0) {
                world.addParticle(ParticleTypes.FLAME, true,
                        muzzle.x, muzzle.y, muzzle.z,
                        vx, vy, vz);
            } else {
                world.addParticle(ParticleTypes.SMOKE, true,
                        muzzle.x, muzzle.y, muzzle.z,
                        vx, vy, vz);
            }
            world.addParticle(ParticleTypes.SMOKE, true,
                    muzzle.x, muzzle.y, muzzle.z,
                    vx, vy, vz);
        }
    }

    public void postShot(World world, PlayerEntity user, ItemStack stack) {
        world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f,1f);
        stack.damage(this.getStackDamageToApply(), user, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.isCreative()) user.getItemCooldownManager().set(this, this.getCooldown(stack));
    }

    public int getStackDamageToApply() {
        return 1;
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
