package net.soulsweaponry.items.staff;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.TooltipAbilities;

public class ChungusStaff extends ModdedSword implements IKeybindAbility {

    public ChungusStaff(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.chungus_staff_damage, ConfigConstructor.chungus_staff_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.CHUNGUS_INFUSED);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!this.isDisabled(stack) && entity.age % 100 == 0 && entity instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.CHUNGUS_TONIC_EFFECT.get(), 120, 0, true, false));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.isDisabled(stack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(stack);
        }
        TntEntity tnt = EntityRegistry.CHUNGUS_HEAD.get().create(world);
        assert tnt != null;
        tnt.setPos(user.getX(), user.getEyeY() - 0.4f, user.getZ());
        tnt.noClip = true;
        Vec3d look = user.getRotationVec(1.0F);
        double speed = 1.5;
        tnt.setVelocity(look.x * speed, look.y * speed, look.z * speed);
        tnt.setPitch(user.getPitch());
        tnt.setYaw(user.getYaw());
        tnt.setFuse(ConfigConstructor.chungus_staff_ticks_before_explosion);
        tnt.setNoGravity(true);
        world.spawnEntity(tnt);
        user.getItemCooldownManager().set(this, ConfigConstructor.chungus_staff_use_cooldown);
        stack.damage(3, user, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
        return TypedActionResult.success(stack);
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_chungus_staff;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_chungus_staff;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.chungus_staff_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.chungus_staff_enchant_reduces_cooldown_ids;
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (this.isDisabled(stack)) {
            this.notifyDisabled(player);
            return;
        }
        if (player.hasStatusEffect(EffectRegistry.COOLDOWN.get())) {
            this.notifyCooldown(player);
            return;
        }
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, 1200, 2));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 1200, 2));
        if (!player.isCreative()) {
            stack.damage(3, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));
            this.applyEffectCooldown(player, ConfigConstructor.chungus_staff_ability_cooldown - this.getReduceCooldownEnchantLevel(stack) * 160);
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
        if (this.isDisabled(stack) || player.hasStatusEffect(EffectRegistry.COOLDOWN.get())) {
            return;
        }
        player.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F);
    }
}