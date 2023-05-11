package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.FrostGiant;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleNetworking;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class Frostmourne extends SoulHarvestingItem {

    public Frostmourne(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.frostmourne_damage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int amp = MathHelper.ceil((float) WeaponUtil.getEnchantDamageBonus(stack)/2f);
        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING, 160, amp));
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.getSouls(stack) >= 5) {
            Vec3d vecBlocksAway = user.getRotationVector().multiply(3).add(user.getPos());
            BlockPos on = BlockPos.ofFloored(vecBlocksAway); // TODO add physical damage immune, but magic sensetive ghost
            FrostGiant entity = new FrostGiant(EntityRegistry.FROST_GIANT, world);//user.getRandom().nextBoolean() ? new SkeletonEntity(EntityType.SKELETON, world) : new ZombieEntity(EntityType.ZOMBIE, world);
            entity.setPos(vecBlocksAway.x, user.getY() + .1f, vecBlocksAway.z);
            entity.setOwner(user);
            world.spawnEntity(entity);
            this.addAmount(stack, -5);
            world.playSound(null, on, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.PLAYERS, 0.75f, 1f);
            if (!world.isClient) {
                ParticleNetworking.sendServerParticlePacket((ServerWorld) world, PacketRegistry.SOUL_RUPTURE_PACKET_ID, on, 50);
            }
            return TypedActionResult.success(stack, world.isClient);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.FREEZE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.PERMAFROST, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SOUL_TRAP, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SOUL_RELEASE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.COLLECT, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
