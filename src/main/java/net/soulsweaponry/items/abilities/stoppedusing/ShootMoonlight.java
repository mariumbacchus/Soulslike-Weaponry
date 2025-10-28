package net.soulsweaponry.items.abilities.stoppedusing;

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

public record ShootMoonlight(int projectileAmount, float bonusProjectilesPerLvl, float speed, float damage, float bonusDamagePerLvl) implements IChargeToUse {

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity playerEntity) {
            if (ticksUsed >= 10) {
                stack.damage(3, playerEntity, WeaponUtil.getActiveHandSlot(playerEntity));
                this.createMoonlightProjectiles(world, user, stack);
                world.playSound(null, user.getBlockPos(), SoundRegistry.MOONLIGHT_BIG_EVENT, SoundCategory.PLAYERS, 1f, 1f);
            }
        }
    }

    public void createMoonlightProjectiles(World world, LivingEntity user, ItemStack stack) {
        float stepDegrees = 5.0F;
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        for (int i = 0; i < this.getProjectileAmount(lvl); i++) {
            MoonlightProjectile entity = new MoonlightProjectile(EntityRegistry.MOONLIGHT_BIG_ENTITY_TYPE, world, user, stack);
            entity.setAgeAndPoints(30, 150, (byte) 4);
            float yawOffset = getYawOffsetForIndex(i, stepDegrees);
            entity.setVelocity(user, user.getPitch(), user.getYaw() + yawOffset, 0.0F, this.speed, 1.0F);
            entity.setDamage(this.damage + this.bonusDamagePerLvl * lvl);
            world.spawnEntity(entity);
        }
    }

    public int getProjectileAmount(int lvl) {
        return (int) (this.projectileAmount + this.bonusProjectilesPerLvl * lvl);
    }

    private float getYawOffsetForIndex(int index, float stepDegrees) {
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
