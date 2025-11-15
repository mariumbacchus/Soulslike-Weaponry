package net.soulsweaponry.items.gun;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.tooltip.TooltipType;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.ModTags;
import net.soulsweaponry.util.WeaponUtil;
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
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

// NOTE: Remember to add the item to ConventionalItemTags.BOW_TOOLS or something like that to make UseAction.BOW animation work
public abstract class GunItem extends RangedWeaponItem implements IConfigDisable {

    protected final List<TooltipAbilities> tooltipAbilities = new ArrayList<>();

    public GunItem(Settings settings) {
        super(settings);
        //this.addTooltipAbility(TooltipAbilities.GUN_ITEM);TODO
    }
    
    @Override
    public Predicate<ItemStack> getProjectiles() {
        return (stack) -> stack.isOf(ItemRegistry.SILVER_BULLET) && stack.getCount() >= this.getBulletsNeeded(stack);
    }

    public int getReducedCooldown(ItemStack stack) {
        return WeaponUtil.getLevel(stack, EnchantRegistry.FAST_HANDS) * 8;
    }

    public boolean hasInfinity(ItemStack stack) {
        return EnchantmentHelper.hasAnyEnchantmentsIn(stack, ModTags.Enchantments.PREVENTS_AMMO_CONSUME);
    }

    public abstract int getPostureLoss(ItemStack stack);
    public abstract float getBulletDamage(ItemStack stack);
    public abstract float getBulletVelocity(ItemStack stack);
    public abstract float getBulletDivergence(ItemStack stack);
    public abstract int getCooldown(ItemStack stack);

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 0;
    }

    public int getBulletsNeeded(ItemStack stack) {
        return 1;
    }

    public int getBulletsNeededWithInfinity(ItemStack stack) {
        return 1;
    }

    // TODO instead of all of these get enchant level calls, replace with the new enchant system that applies to silver bullets
    public PersistentProjectileEntity createSilverBulletEntity(World world, LivingEntity shooter, ItemStack gunStack) {
        if (WeaponUtil.getLevel(gunStack, EnchantRegistry.MISFIRE_CURSE) > 0 && !world.isClient && shooter.getRandom().nextDouble() < ConfigConstructor.misfire_curse_enchant_trigger_chance) {
            world.createExplosion(null, shooter.getX(), shooter.getBodyY(0.5f), shooter.getZ(), 3f, true, World.ExplosionSourceType.MOB);
            shooter.setOnFireFor(3);
        }
        float power = this.getCalculatedDamage(this.getBulletDamage(gunStack), gunStack, shooter);
        int ethereal = WeaponUtil.getLevel(gunStack, EnchantRegistry.ETHEREAL);
        int explosivePower = WeaponUtil.getLevel(gunStack, EnchantRegistry.EXPLOSIVE_ROUNDS);
        int chainLightningLevel = WeaponUtil.getLevel(gunStack, EnchantRegistry.CHAIN_LIGHTNING);
        int blightCarrierLevel = WeaponUtil.getLevel(gunStack, EnchantRegistry.BLIGHT_CARRIER);
        int freezeLevel = WeaponUtil.getLevel(gunStack, EnchantRegistry.FROSTSILVER);
        int phantomTraceLevel = WeaponUtil.getLevel(gunStack, EnchantRegistry.PHANTOM_TRACE);
        int tetherLevel = WeaponUtil.getLevel(gunStack, EnchantRegistry.TETHER);
        int ricochetLevel = WeaponUtil.getLevel(gunStack, EnchantRegistry.RICOCHET);
        SilverBulletEntity entity = this.getModdedProjectile(world, shooter, gunStack);
        entity.setPos(shooter.getX(), shooter.getEyeY() - 0.4f, shooter.getZ());
        entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
        entity.setMaxAge(this.getProjectileMaxAge());
        if (ethereal > 0) {
            entity.setNoClip(true);
            entity.setEthereal(true);
            entity.setMaxAge(this.getProjectileMaxAgeEthereal());
        }
        entity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, this.getBulletVelocity(gunStack), this.getBulletDivergence(gunStack));
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
            entity.setBlightCarrier((int) (blightCarrierLevel * ConfigConstructor.blight_carrier_enchant_blight_per_level));
        }
        if (freezeLevel > 0) {
            entity.setFreezeAmplifier((int) (freezeLevel * ConfigConstructor.frostsilver_enchant_permafrost_per_level));
        }
        if (tetherLevel > 0 && ricochetLevel == 0) {
            entity.setTether(tetherLevel);
        }
        if (ricochetLevel > 0) {
            entity.setRicochetBounces((int) (ricochetLevel * ConfigConstructor.ricochet_enchant_bounce_per_level));
        }
        if (phantomTraceLevel > 0) {
            for (int i = 1; i < phantomTraceLevel + 1; i++) {
                SilverBulletEntity copy = this.getModdedProjectile(world, shooter, gunStack);
                copy.copyFrom(entity);
                copy.setNoClip(ethereal > 0);
                copy.setEthereal(ethereal > 0);
                copy.setUuid(UUID.randomUUID());
                copy.setEchoCopy(true);
                copy.setEchoCopyTimer(10 * i);
                copy.setMaxEchoDelay(10 * i);
                copy.setDamage(this.getCalculatedDamage(this.getBulletDamage(gunStack) * ConfigConstructor.phantom_trace_enchant_phantom_projectile_damage_mod, gunStack, shooter));
                world.spawnEntity(copy);
            }
        }
        return entity;
    }

    @Nullable
    public ItemStack canShoot(PlayerEntity user, ItemStack stack) {
        boolean infinity = this.hasInfinity(stack);
        boolean bl = user.getAbilities().creativeMode || (infinity && this.getBulletsNeededWithInfinity(stack) <= 0);
        ItemStack bullet = this.getProjectileType(user, stack);
        if (!bullet.isEmpty() || bl) {
            if (bullet.isEmpty()) {
                return new ItemStack(ItemRegistry.SILVER_BULLET);
            }
            int toRemove = bl ? 0 : this.getBulletsNeeded(stack);
            Item bulletItem = ItemRegistry.SILVER_BULLET;
            if (infinity) {
                return bullet;
            }
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

    public ItemStack getProjectileType(PlayerEntity player, ItemStack stack) {
        int needed = this.getBulletsNeeded(stack);
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
        world.playSound(user, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1f,1f);
        stack.damage(this.getStackDamageToApply(), user, WeaponUtil.getActiveHandSlot(user));
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.isCreative()) user.getItemCooldownManager().set(this, this.getCooldown(stack));
    }

    /**
     * Calculates the damage so that the input damage equals the damage dealt, factoring velocity damage increase so that the config reflects correct sum.
     * @return damage value the projectile needs to afflict the resultDamage parameter inputted
     */
    public float getCalculatedDamage(float resultDamage, ItemStack gunStack, LivingEntity shooter) {
        // double attr = shooter.getAttributeValue(EntityAttributes_RangedWeapon.DAMAGE.entry); If weapon with bonus was held in offhand (with ANY attribute bonus), it would apply to main hand weapon >:(
        resultDamage += gunStack.getOrDefault(ComponentRegistry.GUN_BONUS_DAMAGE, 0f);
        return (resultDamage / this.getBulletVelocity(gunStack)) + WeaponUtil.getLevel(gunStack, Enchantments.POWER) / 2f;
    }

    public int getStackDamageToApply() {
        return 1;
    }

    public int getProjectileMaxAge() {
        return 60;
    }

    public int getProjectileMaxAgeEthereal() {
        return 25;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (this.isDisabled(stack)) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public int getEnchantability() {
        return 7;
    }

    @Override
    public int getRange() {
        return 15;
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {

    }
}