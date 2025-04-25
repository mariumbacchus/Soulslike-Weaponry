package net.soulsweaponry.items.sword;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.NightsEdge;
import net.soulsweaponry.items.ChargeToUseItem;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

public class NightsEdgeItem extends ChargeToUseItem implements IKeybindAbility {

    public NightsEdgeItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.nights_edge_weapon_damage, ConfigConstructor.nights_edge_weapon_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.NIGHTS_EDGE, TooltipAbilities.BLIGHT);
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_nights_edge;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (target.hasStatusEffect(EffectRegistry.BLIGHT)) {
            int amp = target.getStatusEffect(EffectRegistry.BLIGHT).getAmplifier();
            target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLIGHT, 60, amp + 1));
            if (amp >= 10) {
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.DECAY, 80, 0));
            }
        } else {
            target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLIGHT, 60, MathHelper.floor(WeaponUtil.getEnchantDamageBonus(stack)/2f)));
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(this)) {
            int i = WeaponUtil.getChargeTime(stack, remainingUseTicks);
            if (i >= 10) {
                stack.damage(1, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                WeaponUtil.doConsumerOnLine(world, player.getYaw() + 90, player.getPos(), 4, 10 + 2 * WeaponUtil.getEnchantDamageBonus(stack), 1.25f,
                        (Vec3d position, Integer warmup, Float yaw) -> this.spawnNightsEdge(world, player, stack, position, warmup, yaw));
                this.applyItemCooldown(player, this.getScaledCooldown(stack));
            }
        }
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(this)) {
            WeaponUtil.doConsumerOnCircle(world, player.getYaw() + 90, player.getPos(), player.getY(), 3 + MathHelper.floor(WeaponUtil.getEnchantDamageBonus(stack)/2f), new Vec2f(1.5f, 1.75f),
                    (Vec3d position, Integer warmup, Float yaw) -> this.spawnNightsEdge(world, player, stack, position, warmup, yaw * 57.295776F));
            this.applyItemCooldown(player, this.getScaledCooldown(stack));
            stack.damage(1, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));
        }
    }

    public void spawnNightsEdge(World world, LivingEntity user, ItemStack stack, Vec3d position, int warmup, float yaw) {
        NightsEdge edge = new NightsEdge(EntityRegistry.NIGHTS_EDGE, world);
        edge.setOwner(user);
        edge.setDamage(ConfigConstructor.nights_edge_ability_damage + 2 * WeaponUtil.getEnchantDamageBonus(stack));
        edge.setWarmup(warmup);
        edge.setYaw(yaw);
        edge.setPos(position.x, position.y, position.z);
        world.spawnEntity(edge);
    }

    protected int getScaledCooldown(ItemStack stack) {
        int base = ConfigConstructor.nights_edge_ability_cooldown;
        return Math.max(ConfigConstructor.nights_edge_ability_min_cooldown, base - this.getReduceCooldownEnchantLevel(stack) * 8);
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.nights_edge_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.nights_edge_enchant_reduces_cooldown_ids;
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_nights_edge;
    }
}