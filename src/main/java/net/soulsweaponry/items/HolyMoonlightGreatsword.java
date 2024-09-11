package net.soulsweaponry.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.invisible.HolyMoonlightPillar;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.WeaponUtil;

public class HolyMoonlightGreatsword extends TrickWeapon implements IChargeNeeded {

    public HolyMoonlightGreatsword(ToolMaterial toolMaterial, Settings settings, int switchWeaponIndex) {
        super(toolMaterial, 3, settings, switchWeaponIndex, 3, false, true);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.NEED_CHARGE, WeaponUtil.TooltipAbilities.LUNAR_HERALD_NO_CHARGE, WeaponUtil.TooltipAbilities.CHARGE, WeaponUtil.TooltipAbilities.MOONFALL);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            int chargeTime = this.getMaxUseTime(stack) - remainingUseTicks;
            if (chargeTime >= 10) {
                int emp = player.hasStatusEffect(EffectRegistry.MOON_HERALD) ? 20 * player.getStatusEffect(EffectRegistry.MOON_HERALD).getAmplifier() : 0;
                this.applyItemCooldown(player, Math.max(ConfigConstructor.holy_moonlight_ability_min_cooldown, ConfigConstructor.holy_moonlight_ability_cooldown - this.getReduceCooldownEnchantLevel(stack) * 30 - emp));
                stack.damage(5, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));
                int ruptures = ConfigConstructor.holy_moonlight_ruptures_amount + WeaponUtil.getEnchantDamageBonus(stack);
                Vec3d vecBlocksAway = player.getRotationVector().multiply(3).add(player.getPos());
                BlockPos targetArea = new BlockPos(vecBlocksAway.x, user.getY(), vecBlocksAway.z);
                float power = ConfigConstructor.holy_moonlight_ability_damage;
                for (Entity entity : world.getOtherEntities(player, new Box(targetArea).expand(3))) {
                    if (entity instanceof LivingEntity) {
                        entity.damage(CustomDamageSource.obliterateDamageSource(player), power + 2 * EnchantmentHelper.getAttackDamage(stack, ((LivingEntity) entity).getGroup()));
                        entity.addVelocity(0, this.getKnockup(stack), 0);
                    }
                }
                if (!world.isClient) {
                    this.castSpell(player, world, stack, ruptures, this.getAbilityDamage(), this.getKnockup(stack), user.getYaw() + 90);
                }
                if (stack.hasNbt() && !player.isCreative()) {
                    stack.getNbt().putInt(IChargeNeeded.CHARGE, 0);
                }
                player.world.playSound(player, targetArea, SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                player.world.playSound(player, targetArea, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
                if (!world.isClient) {
                    ParticleHandler.particleOutburstMap(player.getWorld(), 150, vecBlocksAway.getX(), user.getY(), vecBlocksAway.getZ(), ParticleEvents.MOONFALL_MAP, 1f);
                }
            }
        }
    }

    public void castSpell(LivingEntity user, World world, ItemStack stack, Vec3d startPos, int amount, float damage, float knockup, float yaw, float particleMod, float radius) {
        double maxY = startPos.getY();
        double y = startPos.getY() + 1.0;
        float f = (float) Math.toRadians(yaw);
        for (int i = 0; i < amount; i++) {
            double h = 1.75 * (double)(i + 1);
            this.summonPillars(user, world, stack, startPos.getX() + (double)MathHelper.cos(f) * h, startPos.getZ() + (double)MathHelper.sin(f) * h, maxY, y, -6 + i * 2, damage, knockup, particleMod, radius);
        }
    }

    private void summonPillars(LivingEntity user, World world, ItemStack stack, double x, double z, double maxY, double y, int warmup, float damage, float knockup, float particleMod, float radius) {
        BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
        boolean bl = false;
        double d = 0.0;
        do {
            VoxelShape voxelShape;
            BlockPos blockPos2;
            if (!world.getBlockState(blockPos2 = blockPos.down()).isSideSolidFullSquare(world, blockPos2, Direction.UP)) continue;
            if (!world.isAir(blockPos) && !(voxelShape = world.getBlockState(blockPos).getCollisionShape(world, blockPos)).isEmpty()) {
                d = voxelShape.getMax(Direction.Axis.Y);
            }
            bl = true;
            break;
        } while ((blockPos = blockPos.down()).getY() >= MathHelper.floor(maxY) - 1);
        if (bl) {
            HolyMoonlightPillar pillar = new HolyMoonlightPillar(EntityRegistry.HOLY_MOONLIGHT_PILLAR, world);
            pillar.setOwner(user);
            pillar.setStack(stack);
            pillar.setParticleMod(particleMod);
            pillar.setRadius(radius);
            pillar.setDamage(damage);
            pillar.setKnockUp(knockup);
            pillar.setWarmup(warmup);
            pillar.setPos(x, (double)blockPos.getY() + d, z);
            world.spawnEntity(pillar);
        }
    }

    public void castSpell(LivingEntity user, World world, ItemStack stack, int amount, float damage, float knockup, float yaw) {
        this.castSpell(user, world, stack, user.getPos(), amount, damage, knockup, yaw, 1f, 1.85f);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) this.addCharge(stack, this.getAddedCharge(stack));
        return super.postHit(stack, target, attacker);
    }

    private float getAbilityDamage() {
        return ConfigConstructor.holy_moonlight_ability_damage;
    }

    private float getKnockup(ItemStack stack) {
        return ConfigConstructor.holy_moonlight_ability_knockup + (float)WeaponUtil.getEnchantDamageBonus(stack)/10;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (this.isDisabled(itemStack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(itemStack);
        }
        if (itemStack.getDamage() < itemStack.getMaxDamage() - 1 && (this.isCharged(itemStack) || user.isCreative() || user.hasStatusEffect(EffectRegistry.MOON_HERALD))) {
            user.setCurrentHand(hand);
            return TypedActionResult.success(itemStack);
        }
        else {
            return TypedActionResult.fail(itemStack);
        }
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public int getMaxCharge() {
        return ConfigConstructor.holy_moonlight_ability_charge_needed;
    }

    @Override
    public int getAddedCharge(ItemStack stack) {
        int base = ConfigConstructor.holy_moonlight_greatsword_charge_added_post_hit;
        return base + WeaponUtil.getEnchantDamageBonus(stack) * 2;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.holy_moonlight_ability_enchant_reduces_cooldown;
    }

    @Override
    public String getReduceCooldownEnchantId(ItemStack stack) {
        return ConfigConstructor.holy_moonlight_ability_enchant_reduces_cooldown_id;
    }
}