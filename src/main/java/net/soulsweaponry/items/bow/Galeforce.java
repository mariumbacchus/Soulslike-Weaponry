package net.soulsweaponry.items.bow;

import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.arrow.ChargedArrow;
import net.soulsweaponry.items.ModdedBow;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.ModTags;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class Galeforce extends ModdedBow implements IKeybindAbility {

    public Galeforce(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, new RangedConfig((int) ConfigConstructor.galeforce_pull_time_ticks,
                ConfigConstructor.galeforce_damage, ConfigConstructor.galeforce_max_velocity),
                repairIngredientSupplier);
        this.addTooltipAbility(TooltipAbilities.GALEFORCE);
    }

    @Override
    @Nullable
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        shooter.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, (int) ConfigConstructor.galeforce_speed_effect_duration_ticks, (int) (ConfigConstructor.galeforce_speed_effect_amplifier - 1)));
        return new ChargedArrow(world, shooter, false);
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.hasStatusEffect(EffectRegistry.COOLDOWN)) {
            if (!player.isCreative()) {
                int cooldown = (int) Math.max(ConfigConstructor.galeforce_dash_min_cooldown, ConfigConstructor.galeforce_dash_cooldown - this.getReduceCooldownEnchantLevel(stack) * 8);
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.COOLDOWN, cooldown, 0));
            }
            ItemStack arrowStack = player.getProjectileType(stack);
            if (arrowStack.isEmpty()) {
                arrowStack = new ItemStack(Items.ARROW);
            }
            if (player.getAttacking() != null) {
                LivingEntity target = player.getAttacking();
                double x = target.getX() - player.getX();
                double y = target.getEyeY() - player.getBodyY(1f);
                double z = target.getZ() - player.getZ();
                this.shootArrow(world, stack, arrowStack, player, new Vec3d(x, y, z));
            } else {
                this.shootArrow(world, stack, arrowStack, player, null);
            }
        }
    }

    private void shootArrow(ServerWorld world, ItemStack stack, ItemStack arrowStack, PlayerEntity player, @Nullable Vec3d currentTargetPos) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, (int) ConfigConstructor.galeforce_speed_effect_duration_ticks, (int) (ConfigConstructor.galeforce_speed_effect_amplifier - 1)));
        ChargedArrow chargedArrow = new ChargedArrow(world, player, true);
        chargedArrow.setPos(player.getX(), player.getY() + 1.5F, player.getZ());
        if (currentTargetPos != null) {
            chargedArrow.setVelocity(currentTargetPos.getX(), currentTargetPos.getY(), currentTargetPos.getZ(), ConfigConstructor.galeforce_max_velocity, 1f);
        } else {
            chargedArrow.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, ConfigConstructor.galeforce_max_velocity, 1.0F);
        }
        chargedArrow.setCritical(true);
        double damage = WeaponUtil.getLevel(stack, Enchantments.POWER) * 0.6f + ConfigConstructor.galeforce_damage / ConfigConstructor.galeforce_max_velocity;
        chargedArrow.setDamage(damage); // TODO test to see if power is increased by POWER enchant twice or not due to charged arrow is in tag so it gets enchantment applied
        if (EnchantmentHelper.hasAnyEnchantmentsIn(stack, ModTags.Enchantments.APPLY_FIRE)) {
            chargedArrow.setOnFireFor(8);
        }
        stack.damage(1, player, WeaponUtil.getActiveHandSlot(player));
        chargedArrow.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        world.spawnEntity(chargedArrow);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.hasStatusEffect(EffectRegistry.COOLDOWN)) {
            WeaponUtil.launchTarget(player, 2f, false);
        }
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_galeforce;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.galeforce_dash_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.galeforce_dash_enchant_reduces_cooldown_ids;
    }
}