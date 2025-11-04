package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.entity.projectile.NightsEdge;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record NightsEdgeAbility(
        int rippleAmount, float bonusRipplesPerLvl,
        float rippleDamage, float rippleBonusDmgPerLvl,
        int lineAmount, float bonusLineAmountPerLvl,
        float lineDamage, float lineBonusDmgPerLvl,
        int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IKeybindAbility {

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (!this.isCoolingDown(player, stack)) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            double verticalSearch = 3.0;
            int ripples = (int) (this.rippleAmount + this.bonusRipplesPerLvl * lvl);
            Vec2f radii = new Vec2f(1.5f, 1.75f);
            float ringYaw = player.getYaw() + 90f;
            float damage = this.rippleDamage + this.rippleBonusDmgPerLvl * lvl;
            WeaponUtil.doConsumerOnCircle(world, ringYaw, player.getPos(), verticalSearch, ripples, radii, (position, warmup, yawDeg) -> this.spawnNightsEdge(world, player, position, warmup, yawDeg, damage));
            this.applyItemCooldown(stack, player, this.getScaledCooldown(stack));
            stack.damage(1, player, WeaponUtil.getActiveHandSlot(player));
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int ticksUsed) {
        if (user instanceof PlayerEntity player && !this.isCoolingDown(player, stack)) {
            if (ticksUsed >= 10) {
                int lvl = WeaponUtil.getUpgradeLevel(stack);
                int lineAmount = (int) (this.lineAmount + this.bonusLineAmountPerLvl * lvl);
                float damage = this.lineDamage + this.lineBonusDmgPerLvl * lvl;
                stack.damage(1, player, WeaponUtil.getActiveHandSlot(player));
                WeaponUtil.doConsumerOnLine(world, player.getYaw() + 90, player.getPos(), 4, lineAmount, 1.25f,
                        (Vec3d position, Integer warmup, Float yaw) -> this.spawnNightsEdge(world, player, position, warmup, yaw, damage));
                this.applyItemCooldown(stack, player, this.getScaledCooldown(stack));
            }
        }
    }

    public void spawnNightsEdge(World world, LivingEntity user, Vec3d position, int warmup, float yaw, float damage) {
        NightsEdge edge = new NightsEdge(EntityRegistry.NIGHTS_EDGE, world);
        edge.setOwner(user);
        edge.setDamage(damage);
        edge.setWarmup(warmup);
        edge.setYaw(yaw);
        edge.setPos(position.x, position.y, position.z);
        world.spawnEntity(edge);
    }

    private int getScaledCooldown(ItemStack stack) {
        return (int) Math.max(this.minCooldown, (float) this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl);
    }

    @Override
    public boolean isChargeToUse() {
        return true;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.nights_edge").formatted(Formatting.DARK_PURPLE),
                Text.translatable("tooltip.soulsweapons.nights_edge.1").formatted(Formatting.GRAY),//TODO can maybe clean up translations better
                Text.translatable("tooltip.soulsweapons.nights_edge.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.nights_edge.3").formatted(Formatting.GRAY)
        );
    }
}
