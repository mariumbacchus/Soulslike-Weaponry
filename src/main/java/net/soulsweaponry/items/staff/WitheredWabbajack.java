package net.soulsweaponry.items.staff;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.DragonStaffProjectile;
import net.soulsweaponry.entity.projectile.GrowingFireball;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.LuckChosenObject;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class WitheredWabbajack extends ModdedSword {

    private static final List<LuckChosenObject<EntityType<?>>> PROJECTILES = new ArrayList<>();

    public WitheredWabbajack(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.withered_wabbajack_damage, ConfigConstructor.withered_wabbajack_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.WABBAJACK, TooltipAbilities.LUCK_BASED);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (this.isDisabled(itemStack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(itemStack);
        }
        user.getItemCooldownManager().set(this, 1);
        world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.NEUTRAL, 0.5f, 2/(world.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!world.isClient) {
            EntityType<?> type = this.calculateProjectile(user);
            Entity entity = type.create(world);
            if (entity instanceof ProjectileEntity projectile) {
                projectile.setOwner(user);
                if (entity instanceof DragonStaffProjectile dragonStaffProjectile) {
                    dragonStaffProjectile.setCloudRadius(2f + user.getRandom().nextFloat() * WeaponUtil.getLuckFactor(user));
                }
                projectile.setPos(user.getX(), user.getEyeY(), user.getZ());
                if (entity instanceof GrowingFireball ball) {
                    float power = 1f + user.getRandom().nextFloat() * 10 * WeaponUtil.getLuckFactor(user);
                    int duration = user.getRandom().nextBetween(10, 100 + 20 * WeaponUtil.getLuckFactor(user));
                    float speed = user.getRandom().nextBetween(25, 300 + 20 * WeaponUtil.getLuckFactor(user)) / 100f;
                    ball.setMaxAge(duration);
                    ball.setRadiusGrowth(power / (float) duration);
                    ball.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, speed, 0f);
                } else {
                    projectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 0f);
                }
                world.spawnEntity(entity);
                itemStack.damage(1, user, LivingEntity.getSlotForHand(hand));
            }
            for (LuckChosenObject<EntityType<?>> luckChosenEntity : PROJECTILES) {
                luckChosenEntity.setLuckFactor(10);
            }
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    static {
        PROJECTILES.add(new LuckChosenObject<>(EntityType.ARROW, WeaponUtil.LuckType.BAD));
        PROJECTILES.add(new LuckChosenObject<>(EntityType.EGG, WeaponUtil.LuckType.BAD));
        PROJECTILES.add(new LuckChosenObject<>(EntityType.ENDER_PEARL, WeaponUtil.LuckType.BAD));
        PROJECTILES.add(new LuckChosenObject<>(EntityType.EXPERIENCE_BOTTLE, WeaponUtil.LuckType.BAD));
        PROJECTILES.add(new LuckChosenObject<>(EntityType.SMALL_FIREBALL, WeaponUtil.LuckType.BAD));
        PROJECTILES.add(new LuckChosenObject<>(EntityType.SNOWBALL, WeaponUtil.LuckType.BAD));
        PROJECTILES.add(new LuckChosenObject<>(EntityType.WITHER_SKULL, WeaponUtil.LuckType.BAD));
        PROJECTILES.add(new LuckChosenObject<>(EntityType.FIREBALL, WeaponUtil.LuckType.NEUTRAL));
        PROJECTILES.add(new LuckChosenObject<>(EntityRegistry.GROWING_FIREBALL_ENTITY, WeaponUtil.LuckType.NEUTRAL));
        PROJECTILES.add(new LuckChosenObject<>(EntityRegistry.DRAGON_STAFF_PROJECTILE, WeaponUtil.LuckType.NEUTRAL));
        PROJECTILES.add(new LuckChosenObject<>(EntityRegistry.WITHERED_WABBAJACK_PROJECTILE, WeaponUtil.LuckType.GOOD));
    }

    private EntityType<?> calculateProjectile(LivingEntity user) {
        List<LuckChosenObject<EntityType<?>>> projectileList = new ArrayList<>();
        for (LuckChosenObject<EntityType<?>> luckChosen : PROJECTILES) {
            switch (luckChosen.getLuckType()) {
                case BAD -> luckChosen.setLuckFactor(luckChosen.getLuckFactor() - WeaponUtil.getLuckFactor(user));
                case GOOD -> luckChosen.setLuckFactor(luckChosen.getLuckFactor() + WeaponUtil.getLuckFactor(user));
            }
            if (luckChosen.getLuckFactor() > 0) {
                projectileList.add(luckChosen);
            }
        }
        int totalChance = 0;
        for (LuckChosenObject<EntityType<?>> object : projectileList) {
            totalChance += object.getLuckFactor();
        }
        int random = user.getRandom().nextInt(totalChance);
        EntityType<?> chosenProjectile = this.getEntityType(projectileList, random);
        if (chosenProjectile != null) {
            return chosenProjectile;
        }
        return EntityType.ARROW;
    }

    @Nullable
    private EntityType<?> getEntityType(List<LuckChosenObject<EntityType<?>>> projectileList, int random) {
        int cumulativeFactor = 0;
        for (LuckChosenObject<EntityType<?>> luckChosen : projectileList) {
            cumulativeFactor += luckChosen.getLuckFactor();
            if (random < cumulativeFactor) {
                return luckChosen.getObject();
            }
        }
        return null;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_withered_wabbajack;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return null;
    }
}