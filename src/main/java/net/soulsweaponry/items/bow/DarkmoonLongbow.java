package net.soulsweaponry.items.bow;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.arrow.MoonlightArrow;
import net.soulsweaponry.entity.projectile.noclip.ArrowStormEntity;
import net.soulsweaponry.mixin.PersistentProjectileEntityInvoker;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.function.Supplier;

public class DarkmoonLongbow extends ModdedBow implements IKeybindAbility {

    public DarkmoonLongbow(Settings settings, Supplier<Ingredient> repairIngredientSupplier) {
        super(settings, createConfig((int) ConfigConstructor.darkmoon_longbow_pull_time_ticks,
                ConfigConstructor.darkmoon_longbow_damage, ConfigConstructor.darkmoon_longbow_bonus_velocity),
                repairIngredientSupplier);
        this.addTooltipAbility( TooltipAbilities.SLOW_PULL, TooltipAbilities.MOONLIGHT_ARROW, TooltipAbilities.ARROW_STORM);
    }

    @Override
    public PersistentProjectileEntity getModifiedProjectile(World world, ItemStack bowStack, ItemStack arrowStack, LivingEntity shooter, PersistentProjectileEntity originalArrow) {
        MoonlightArrow projectile = new MoonlightArrow(world, shooter, arrowStack, bowStack);
        ((PersistentProjectileEntityInvoker)projectile).invokeSetPierceLevel((byte) 4);
        projectile.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return projectile;
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(this)) {
            world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, 1f, 1f);
            ArrowStormEntity entity = new ArrowStormEntity(EntityRegistry.ARROW_STORM_ENTITY, world);
            entity.setPos(player.getX(), player.getY() + 4.5F, player.getZ());
            entity.setVelocity(player, 0, player.getYaw(), 0.0F, 1f, 1.0F);
            entity.setOwner(player);
            double power = WeaponUtil.getLevel(stack, Enchantments.POWER);
            entity.setDamage(ConfigConstructor.darkmoon_longbow_ability_damage / 2.6f + power * 1.25f);
            entity.setMaxArrowAge(40);
            world.spawnEntity(entity);
            this.applyItemCooldown(player, (int) Math.max(ConfigConstructor.darkmoon_longbow_ability_min_cooldown_ticks,
                    ConfigConstructor.darkmoon_longbow_ability_cooldown_ticks - this.getReduceCooldownEnchantLevel(stack) * 30));
            stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_darkmoon_longbow;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.darkmoon_longbow_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.darkmoon_longbow_enchant_reduces_cooldown_ids;
    }
}