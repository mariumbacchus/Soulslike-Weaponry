package net.soulsweaponry.items.abilities.use;

import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.SilverBulletEntity;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.util.ModTags;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class ShootSilverBullet implements IAbility {

    public final float damage;
    public final float velocity;
    public final float divergence;
    public final int postureLoss;
    public final float postureLossPerVisceral;
    public final int projectileCount;
    public final float projectileCountPerLvl;
    public final int bulletsNeededWithInfinity;
    public final int bulletsNeeded;
    public final int levelToUnlockInfinity;
    public final int stackDamage;
    public final int maxProjectileAge;
    public final int maxProjectileAgeEthereal;
    public final int minCooldown;
    public final int cooldown;
    public final int reducedCooldownPerFastHands;
    public final int particleAmount;
    public final float particleSpread;

    public ShootSilverBullet(
            float damage, float velocity, float divergence, int postureLoss, float postureLossPerVisceral,
            int projectileCount, float projectileCountPerLvl, int bulletsNeededWithInfinity, int bulletsNeeded,
            int levelToUnlockInfinity, int stackDamage, int maxProjectileAge, int maxProjectileAgeEthereal,
            int minCooldown, int cooldown, int reducedCooldownPerFastHands, int particleAmount, float particleSpread
    ) {
        this.damage = damage;
        this.velocity = velocity;
        this.divergence = divergence;
        this.postureLoss = postureLoss;
        this.postureLossPerVisceral = postureLossPerVisceral;
        this.projectileCount = projectileCount;
        this.projectileCountPerLvl = projectileCountPerLvl;
        this.bulletsNeededWithInfinity = bulletsNeededWithInfinity;
        this.bulletsNeeded = bulletsNeeded;
        this.levelToUnlockInfinity = levelToUnlockInfinity;
        this.stackDamage = stackDamage;
        this.maxProjectileAge = maxProjectileAge;
        this.maxProjectileAgeEthereal = maxProjectileAgeEthereal;
        this.minCooldown = minCooldown;
        this.cooldown = cooldown;
        this.reducedCooldownPerFastHands = reducedCooldownPerFastHands;
        this.particleAmount = particleAmount;
        this.particleSpread = particleSpread;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        ItemStack bulletStack = this.canShoot(user, stack);
        if (bulletStack != null) {
            this.shootProjectiles(world, user, stack);
            this.spawnShotParticles(world, user, this.particleAmount, this.particleSpread);
            this.postShot(world, user, stack);
            return ActionResult.CONSUME;
        }
        return ActionResult.FAIL;
    }

    public void shootProjectiles(World world, PlayerEntity user, ItemStack stack) {
        int projectileCount = (int) (this.projectileCount + this.projectileCountPerLvl * WeaponUtil.getUpgradeLevel(stack));
        for (int i = 0; i < projectileCount; i++) {
            PersistentProjectileEntity entity = this.createSilverBulletEntity(world, user, stack);
            world.spawnEntity(entity);
        }
    }

    public PersistentProjectileEntity createSilverBulletEntity(World world, LivingEntity shooter, ItemStack gunStack) {
        if (WeaponUtil.getLevel(gunStack, EnchantRegistry.MISFIRE_CURSE) > 0 && !world.isClient && shooter.getRandom().nextDouble() < ConfigConstructor.misfire_curse_enchant_trigger_chance) {
            world.createExplosion(null, shooter.getX(), shooter.getBodyY(0.5f), shooter.getZ(), 3f, true, World.ExplosionSourceType.MOB);
            shooter.setOnFireFor(3);
        }
        float power = this.getCalculatedDamage(this.damage, gunStack);
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
        entity.setMaxAge(this.maxProjectileAge);
        if (ethereal > 0) {
            entity.setNoClip(true);
            entity.setEthereal(true);
            entity.setMaxAge(this.maxProjectileAgeEthereal);
        }
        entity.setVelocity(shooter, shooter.getPitch(), shooter.getYaw(), 0.0F, this.velocity, this.divergence);
        entity.setPostureLoss(this.getPostureLoss(gunStack));
        entity.setDamage(power);
        if (explosivePower > 0) {
            entity.setExplosionPower(explosivePower);
        }
        if (chainLightningLevel > 0) {
            entity.setChainLightningDamage(this.damage * chainLightningLevel * ConfigConstructor.chain_lightning_enchant_damage_mod_per_level);
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
                copy.setDamage(this.getCalculatedDamage(this.damage * ConfigConstructor.phantom_trace_enchant_phantom_projectile_damage_mod, gunStack));
                world.spawnEntity(copy);
            }
        }
        return entity;
    }

    @Nullable
    public ItemStack canShoot(PlayerEntity user, ItemStack stack) {
        boolean infinity = this.hasInfinity(stack);
        boolean bl = user.getAbilities().creativeMode || (infinity && this.bulletsNeededWithInfinity <= 0);
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
        ItemStack held = RangedWeaponItem.getHeldProjectile(player, this.getProjectiles());
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
        stack.damage(this.stackDamage, user, WeaponUtil.getActiveHandSlot(user));
        user.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        if (!user.isCreative()) {
            this.applyItemCooldown(stack, user, this.getCooldown(stack));
        }
    }

    public int getCooldown(ItemStack stack) {
        return Math.max(this.minCooldown, this.cooldown - this.getReducedCooldown(stack));
    }

    public int getPostureLoss(ItemStack stack) {
        int lvl = WeaponUtil.getLevel(stack, EnchantRegistry.VISCERAL);
        return (int) (this.postureLoss + lvl * this.postureLossPerVisceral);
    }

    /**
     * Calculates the damage so that the input damage equals the damage dealt, factoring velocity damage increase so that the config reflects correct sum.
     * @return damage value the projectile needs to afflict the resultDamage parameter inputted
     */
    public float getCalculatedDamage(float damage, ItemStack gunStack) {
        // double attr = shooter.getAttributeValue(EntityAttributes_RangedWeapon.DAMAGE.entry); If weapon with bonus was held in offhand (with ANY attribute bonus), it would apply to main hand weapon >:(
        damage += gunStack.getOrDefault(ComponentRegistry.GUN_BONUS_DAMAGE, 0f); // Damage per level is from the upgrade recipe file instead
        return damage / this.velocity;
    }

    public int getReducedCooldown(ItemStack stack) {
        return WeaponUtil.getLevel(stack, EnchantRegistry.FAST_HANDS) * this.reducedCooldownPerFastHands;
    }

    public boolean hasInfinity(ItemStack stack) {
        return EnchantmentHelper.hasAnyEnchantmentsIn(stack, ModTags.Enchantments.PREVENTS_AMMO_CONSUME) || WeaponUtil.getUpgradeLevel(stack) >= this.levelToUnlockInfinity;
    }

    public int getBulletsNeeded(ItemStack stack) {
        return this.hasInfinity(stack) ? this.bulletsNeededWithInfinity : this.bulletsNeeded;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return (stack) -> stack.isOf(ItemRegistry.SILVER_BULLET) && stack.getCount() >= this.getBulletsNeeded(stack);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        float bonus = stack.getOrDefault(ComponentRegistry.GUN_BONUS_DAMAGE, 0f);
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        MutableText damage = Text.literal(String.format("%.1f", (this.damage + bonus)));
        MutableText postureLoss = Text.literal(String.valueOf(this.getPostureLoss(stack)));
        MutableText cooldown = Text.literal(String.valueOf(this.getCooldown(stack)));
        if (bonus > 0) {
            damage.formatted(Formatting.BLUE);
        }
        if (this.getPostureLoss(stack) > this.postureLoss) {
            postureLoss.formatted(Formatting.BLUE);
        }
        if (this.getCooldown(stack) < this.cooldown) {
            cooldown.formatted(Formatting.BLUE);
        }
        tooltip.add(Text.translatable("tooltip.soulsweapons.gun_posture_loss").append(postureLoss).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.gun_posture_loss_on_players", MathHelper.floor(ConfigConstructor.silver_bullet_posture_loss_on_player_modifier * 100f) + "%").formatted(Formatting.DARK_GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.gun_damage").formatted(Formatting.GRAY).append(damage));
        tooltip.add(Text.translatable("tooltip.soulsweapons.gun_cooldown").append(cooldown).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.soulsweapons.gun_bullets_used").append(Text.literal(String.valueOf(this.bulletsNeeded))).formatted(Formatting.GRAY));
        if (lvl >= this.levelToUnlockInfinity) {
            tooltip.add(Text.translatable("enchantment.minecraft.infinity").formatted(Formatting.BLUE));
        }
        return tooltip;
    }
}
