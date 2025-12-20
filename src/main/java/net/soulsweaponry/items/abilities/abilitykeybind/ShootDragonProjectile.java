package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.DragonStaffProjectile;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ShootDragonProjectile(
        float projectileSpeed, int projectileMaxAge,
        float cloudBaseRadius, float cloudBonusRadiusPerLvl,
        int cloudDuration, int cloudBonusDurationPerLvl,
        float cloudRadiusGrowth, float cloudBonusRadiusGrowthPerLvl,
        int effectDuration, int effectAmp,
        int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IKeybindAbility {

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity user, @Nullable Hand hand) {
        if (this.isCoolingDown(user, stack)) {
            return;
        }
        if (!user.isCreative()) {
            this.applyItemCooldown(stack, user, this.getCooldown(stack));
        }
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        DragonStaffProjectile fireball = this.getDragonStaffProjectile(world, user, lvl);
        fireball.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, this.projectileSpeed, 0f);
        world.spawnEntity(fireball);
        stack.damage(1, user, WeaponUtil.getActiveHandSlot(user));
        world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_DRAGON_SHOOT,
                SoundCategory.NEUTRAL, 0.5f, 2f / (world.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity user, @Nullable Hand hand) {
        if (this.isCoolingDown(user, stack)) {
            return;
        }
        user.swingHand(hand == null ? Hand.MAIN_HAND : hand);
    }

    private DragonStaffProjectile getDragonStaffProjectile(World world, PlayerEntity user, int lvl) {
        DragonStaffProjectile fireball = new DragonStaffProjectile(world, user);
        fireball.setProjectileMaxAge(this.projectileMaxAge);
        fireball.setCloudRadius(this.cloudBaseRadius + this.cloudBonusRadiusPerLvl * lvl);
        fireball.setCloudDuration(this.cloudDuration + this.cloudBonusDurationPerLvl * lvl);
        fireball.setCloudRadiusGrowth(this.cloudRadiusGrowth + this.cloudBonusRadiusGrowthPerLvl * lvl);
        fireball.setEffectDuration(this.effectDuration);
        fireball.setEffectAmp(this.effectAmp);
        fireball.setPos(user.getX(), user.getY() + 1.0f, user.getZ());
        return fireball;
    }

    private int getCooldown(ItemStack stack) {
        return Math.max(this.minCooldown, this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.purified_mist").formatted(Formatting.DARK_PURPLE),
                Text.translatable("tooltip.soulsweapons.purified_mist.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.purified_mist.2").formatted(Formatting.GRAY)
        );
    }
}
