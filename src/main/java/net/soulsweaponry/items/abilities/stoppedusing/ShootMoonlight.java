package net.soulsweaponry.items.abilities.stoppedusing;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.MoonlightProjectile;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;

public class ShootMoonlight implements IChargeToUse {

    private final int projectileAmount;
    private final float bonusProjectilesPerLvl;
    private final float speed;
    private final float damage;
    private final float bonusDamagePerLvl;

    public ShootMoonlight(int projectileAmount, float bonusProjectilesPerLvl, float speed, float damage, float bonusDamagePerLvl) {
        this.projectileAmount = projectileAmount;
        this.bonusProjectilesPerLvl = bonusProjectilesPerLvl;
        this.speed = speed;
        this.damage = damage;
        this.bonusDamagePerLvl = bonusDamagePerLvl;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity playerEntity) {
            if (ticksUsed >= 10) {
                stack.damage(3, playerEntity, WeaponUtil.getActiveHandSlot(playerEntity));
                this.shootMoonlightProjectiles(world, user, stack);
                world.playSound(null, user.getBlockPos(), SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.PLAYERS, 1f, 1f);
            }
        }
    }

    public void shootMoonlightProjectiles(World world, LivingEntity user, ItemStack stack) {
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        for (int i = 0; i < this.getProjectileAmount(lvl); i++) {
            MoonlightProjectile entity = this.createMoonlightProjectile(world, user, stack, i, lvl);
            world.spawnEntity(entity);
        }
    }

    public EntityType<? extends MoonlightProjectile> getMoonlightType() {
        return EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE;
    }

    public MoonlightProjectile createMoonlightProjectile(World world, LivingEntity user, ItemStack stack, int projectileNr, int lvl) {
        float stepDegrees = 5.0F;
        MoonlightProjectile entity = new MoonlightProjectile(this.getMoonlightType(), world, user, stack);
        entity.setAgeAndPoints(30, 150, (byte) 4);
        float yawOffset = getYawOffsetForIndex(projectileNr, stepDegrees);
        entity.setVelocity(user, user.getPitch(), user.getYaw() + yawOffset, 0.0F, this.speed, 1.0F);
        entity.setDamage(this.damage + this.bonusDamagePerLvl * lvl);
        return entity;
    }

    public int getProjectileAmount(int lvl) {
        return (int) (this.projectileAmount + this.bonusProjectilesPerLvl * lvl);
    }

    public static float getYawOffsetForIndex(int index, float stepDegrees) {
        if (index == 0) {
            return 0f;
        }
        int pair = (index + 1) / 2;
        boolean isLeft = (index % 2 == 1);
        float offset = pair * stepDegrees;
        return isLeft ? -offset : offset;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight").formatted(Formatting.AQUA));
        tooltip.add(Text.translatable("tooltip.soulsweapons.moonlight.1",
                this.getProjectileAmount(WeaponUtil.getUpgradeLevel(stack))).formatted(Formatting.GRAY));
        return tooltip;
    }
}
