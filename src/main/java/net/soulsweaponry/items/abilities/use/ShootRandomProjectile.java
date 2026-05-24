package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.DragonStaffProjectile;
import net.soulsweaponry.entity.projectile.GrowingFireball;
import net.soulsweaponry.entity.projectile.WitheredWabbajackProjectile;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.LuckChosenObject;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record ShootRandomProjectile(
        float baseLuckFactor, float bonusLuckFactorPerLvl, float projectileSpeed,
        WitheredWabbajackProjectile.EntityHitAttributes entityHitAttributes,
        WitheredWabbajackProjectile.CollisionAttributes collisionAttributes
) implements IAbility {

    private static final List<LuckChosenObject<EntityType<?>>> PROJECTILES = new ArrayList<>();

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        this.applyItemCooldown(stack.getItem(), user, 1);
        world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_SHOOT, SoundCategory.NEUTRAL,
                0.5f, 2f / (world.getRandom().nextFloat() * 0.4F + 0.8F));
        if (!world.isClient) {
            EntityType<?> type = this.calculateProjectile(user);
            Entity entity = type.create(world);
            int luckFactor = (int) (WeaponUtil.getLuckFactor(user) + this.baseLuckFactor
                                + WeaponUtil.getUpgradeLevel(stack) * this.bonusLuckFactorPerLvl);
            if (entity instanceof ProjectileEntity projectile) {
                projectile.setOwner(user);
                if (entity instanceof DragonStaffProjectile dragonStaffProjectile) {
                    dragonStaffProjectile.setCloudRadius(2f + user.getRandom().nextFloat() * luckFactor);
                }
                projectile.setPos(user.getX(), user.getEyeY(), user.getZ());
                if (entity instanceof GrowingFireball ball) {
                    float power = 1f + user.getRandom().nextFloat() * 10 * luckFactor;
                    int duration = user.getRandom().nextBetween(10, 100 + 20 * luckFactor);
                    float speed = user.getRandom().nextBetween(25, 300 + 20 * luckFactor) / 100f;
                    ball.setMaxAge(duration);
                    ball.setRadiusGrowth(power / (float) duration);
                    ball.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, speed, 0f);
                } else {
                    projectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, this.projectileSpeed, 0f);
                }
                if (projectile instanceof WitheredWabbajackProjectile witheredWabbajackProjectile) {
                    witheredWabbajackProjectile.setCollisionAttributes(this.collisionAttributes);
                    witheredWabbajackProjectile.setEntityHitAttributes(this.entityHitAttributes);
                }
                world.spawnEntity(entity);
                stack.damage(1, user, p -> p.sendToolBreakStatus(hand));
            }
            for (LuckChosenObject<EntityType<?>> luckChosenEntity : PROJECTILES) {
                luckChosenEntity.setLuckFactor(10);
            }
        }
        return TypedActionResult.success(stack, world.isClient());
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
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.wabbajack").formatted(Formatting.DARK_RED, Formatting.OBFUSCATED),
                Text.translatable("tooltip.soulsweapons.wabbajack_description").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.lucky").formatted(Formatting.DARK_GREEN),
                Text.translatable("tooltip.soulsweapons.lucky_description_1").formatted(Formatting.GRAY)
        );
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
        PROJECTILES.add(new LuckChosenObject<>(EntityRegistry.GROWING_FIREBALL_ENTITY.get(), WeaponUtil.LuckType.NEUTRAL));
        PROJECTILES.add(new LuckChosenObject<>(EntityRegistry.DRAGON_STAFF_PROJECTILE.get(), WeaponUtil.LuckType.NEUTRAL));
        PROJECTILES.add(new LuckChosenObject<>(EntityRegistry.WITHERED_WABBAJACK_PROJECTILE.get(), WeaponUtil.LuckType.GOOD));
    }
}
