package net.soulsweaponry.items.abilities.userdamaged;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record Mirror(
        float percentHealthThreshold, float increasedThresholdPerLvl
) implements IAbility {

    @Override
    public boolean onUserDamaged(DamageSource source, float amount, ItemStack stack, LivingEntity user) {
        if (user.getHealth() <= user.getMaxHealth() * this.getThreshold(WeaponUtil.getUpgradeLevel(stack))
                && (source.isIn(DamageTypeTags.IS_PROJECTILE) || source.getSource() instanceof ProjectileEntity)) {
            Entity entity = source.getSource();
            if (entity != null) {
                Vec3d playerPos = user.getPos();
                Vec3d projectilePos = entity.getPos();
                Vec3d projectileMotion = entity.getVelocity();
                Vec3d reflectionVector = this.calculateReflectionVector(playerPos, projectilePos, projectileMotion);
                // Reflect the projectile back
                entity.setVelocity(reflectionVector);
            }
            return false;
        }
        return true;
    }

    private Vec3d calculateReflectionVector(Vec3d playerPos, Vec3d projectilePos, Vec3d projectileMotion) {
        Vec3d vectorToPlayer = playerPos.subtract(projectilePos).normalize();
        return projectileMotion.subtract(vectorToPlayer.multiply(projectileMotion.dotProduct(vectorToPlayer) * 2.0D));
    }

    public float getThreshold(int lvl) {
        return this.percentHealthThreshold + this.increasedThresholdPerLvl * lvl;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        MutableText trigger = Text.of(String.format("%.0f", this.getThreshold(WeaponUtil.getUpgradeLevel(stack)) * 100) + "%").copy().formatted(Formatting.RED);
        return List.of(
                Text.translatable("tooltip.soulsweapons.mirror").formatted(Formatting.DARK_PURPLE),
                Text.translatable("tooltip.soulsweapons.mirror.1", trigger).formatted(Formatting.GRAY)
        );
    }
}
