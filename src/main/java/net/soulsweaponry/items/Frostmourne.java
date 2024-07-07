package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
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
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.entity.mobs.RimeSpectre;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.SummonsData;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;
import java.util.UUID;

public class Frostmourne extends SoulHarvestingItem implements ISummonAllies {

    public Frostmourne(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.frostmourne_damage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled()) {
            stack.damage(1, attacker, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            this.notifyDisabled(attacker);
            return true;
        }
        int amp = MathHelper.ceil((float) WeaponUtil.getEnchantDamageBonus(stack)/2f);
        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING, 160, amp));
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.isDisabled()) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(stack);
        }
        if (this.getSouls(stack) >= 5 && !world.isClient && this.canSummonEntity((ServerWorld) world, user, this.getSummonsListId())) {
            Vec3d vecBlocksAway = user.getRotationVector().multiply(3).add(user.getPos());
            BlockPos on = BlockPos.ofFloored(vecBlocksAway);
            Remnant entity = user.getRandom().nextBoolean() ? new FrostGiant(EntityRegistry.FROST_GIANT, world) : new RimeSpectre(EntityRegistry.RIME_SPECTRE, world);
            entity.setPos(vecBlocksAway.x, user.getY() + .1f, vecBlocksAway.z);
            entity.setOwner(user);
            if (entity instanceof RimeSpectre) entity.addVelocity(0, 0.1f, 0);
            entity.setTamed(true);
            world.spawnEntity(entity);
            this.saveSummonUuid(user, entity.getUuid());
            this.addAmount(stack, -5);
            world.playSound(null, on, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.PLAYERS, 0.75f, 1f);
            ParticleHandler.particleOutburstMap(world, 50, vecBlocksAway.getX(), vecBlocksAway.getY(), vecBlocksAway.getZ(), ParticleEvents.SOUL_RUPTURE_MAP, 1f);
            return TypedActionResult.success(stack, true);
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.FREEZE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.PERMAFROST, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SOUL_TRAP, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SOUL_RELEASE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.COLLECT, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
    }

    @Override
    public int getMaxSummons() {
        return ConfigConstructor.frostmourne_summoned_allies_cap;
    }

    @Override
    public String getSummonsListId() {
        return "FrostmourneSummons";
    }

    @Override
    public void saveSummonUuid(LivingEntity user, UUID summonUuid) {
        SummonsData.addSummonUUID((IEntityDataSaver) user, summonUuid, this.getSummonsListId());
    }

    @Override
    public boolean isDisabled() {
        return ConfigConstructor.disable_use_frostmourne;
    }
}