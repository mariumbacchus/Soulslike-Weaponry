package net.soulsweaponry.items;

import net.fabric_extras.ranged_weapon.api.RangedConfig;
import net.minecraft.client.network.ClientPlayerEntity;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.ChargedArrow;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class Galeforce extends ModdedBow implements IKeybindAbility {

    public Galeforce(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, repairIngredientSupplier);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.GALEFORCE);
        this.configure(new RangedConfig(ConfigConstructor.galeforce_pull_time_ticks, ConfigConstructor.galeforce_damage, ConfigConstructor.galeforce_max_velocity));
    }

    @Override
    @Nullable
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        shooter.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, ConfigConstructor.galeforce_speed_effect_duration_ticks, ConfigConstructor.galeforce_speed_effect_amplifier - 1));
        return new ChargedArrow(world, shooter, arrowStack, false);
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.hasStatusEffect(EffectRegistry.COOLDOWN)) {
            if (!player.isCreative())
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.COOLDOWN, ConfigConstructor.galeforce_dash_cooldown, 0));
            if (player.getAttacking() != null) {
                LivingEntity target = player.getAttacking();
                double x = target.getX() - player.getX();
                double y = target.getEyeY() - player.getBodyY(1f);
                double z = target.getZ() - player.getZ();
                this.shootArrow(world, stack, new ItemStack(Items.ARROW), player, new Vec3d(x, y, z));
            } else {
                this.shootArrow(world, stack, new ItemStack(Items.ARROW), player, null);
            }
        }
    }

    private void shootArrow(ServerWorld world, ItemStack stack, ItemStack arrowStack, PlayerEntity player, @Nullable Vec3d currentTargetPos) {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, ConfigConstructor.galeforce_speed_effect_duration_ticks, ConfigConstructor.galeforce_speed_effect_amplifier - 1));
        ChargedArrow chargedArrow = new ChargedArrow(world, player, arrowStack, true);
        chargedArrow.setPos(player.getX(), player.getY() + 1.5F, player.getZ());
        if (currentTargetPos != null) {
            chargedArrow.setVelocity(currentTargetPos.getX(), currentTargetPos.getY(), currentTargetPos.getZ(), ConfigConstructor.galeforce_max_velocity, 1f);
        } else {
            chargedArrow.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, ConfigConstructor.galeforce_max_velocity, 1.0F);
        }
        chargedArrow.setCritical(true);
        double damage = EnchantmentHelper.getLevel(Enchantments.POWER, stack) * 0.6f + ConfigConstructor.galeforce_damage / ConfigConstructor.galeforce_max_velocity;
        chargedArrow.setDamage(damage);
        int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
        if (punch > 0) {
            chargedArrow.setPunch(punch);
        }
        if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
            chargedArrow.setOnFireFor(8);
        }
        stack.damage(1, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));

        boolean creativeAndInfinity = player.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
        boolean bl2 = creativeAndInfinity && arrowStack.isOf(Items.ARROW);
        if (bl2 || player.getAbilities().creativeMode && (arrowStack.isOf(Items.SPECTRAL_ARROW) || arrowStack.isOf(Items.TIPPED_ARROW))) {
            chargedArrow.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        }
        world.spawnEntity(chargedArrow);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
        if (!bl2 && !player.getAbilities().creativeMode) {
            arrowStack.decrement(1);
            if (arrowStack.isEmpty()) {
                player.getInventory().removeOne(arrowStack);
            }
        }
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
        if (!player.hasStatusEffect(EffectRegistry.COOLDOWN)) {
            float f = player.getYaw();
            float g = player.getPitch();
            float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
            float k = -MathHelper.sin(g * 0.017453292F);
            float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
            float m = MathHelper.sqrt(h * h + k * k + l * l);
            float n = 3.0F * ((1.0F + 1F) / 4.0F);
            h *= n / m;
            k *= n / m;
            l *= n / m;
            player.addVelocity(h, k, l);
        }
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_galeforce;
    }
}