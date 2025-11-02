package net.soulsweaponry.items.sword;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.FrostGiant;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.entity.mobs.RimeSpectre;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.SummonsData;
import net.soulsweaponry.items.abilities.ISummonAlliesAbility;
import net.soulsweaponry.items.SoulHarvestingItem;
import net.soulsweaponry.items.abilities.posthit.Permafrost;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.TooltipAbilities;

import java.util.UUID;

public class Frostmourne extends SoulHarvestingItem {

    private static final Permafrost PERMAFROST = new Permafrost(
            (int) ConfigConstructor.frostmourne_frost_buildup_post_hit,
            (int) ConfigConstructor.frostmourne_frost_post_hit_permafrost_base_duration,
            (int) ConfigConstructor.frostmourne_frost_post_hit_permafrost_base_amplifier,
            ConfigConstructor.frostmourne_frost_post_hit_permafrost_amp_per_level
    );

    public Frostmourne(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.frostmourne_damage, ConfigConstructor.frostmourne_attack_speed, settings);
        //this.addTooltipAbility(TooltipAbilities.SOUL_RELEASE);
        this.addAbility(PERMAFROST);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.isDisabled(stack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(stack);
        }
        if (/*this.getSouls(stack) >= 5 &&*/ !world.isClient /*&& this.canSummonEntity((ServerWorld) world, user, this.getSummonsListId())*/) {
            Vec3d vecBlocksAway = user.getRotationVector().multiply(3).add(user.getPos());
            BlockPos on = BlockPos.ofFloored(vecBlocksAway);
            Remnant entity = user.getRandom().nextBoolean() ? new FrostGiant(EntityRegistry.FROST_GIANT, world) : new RimeSpectre(EntityRegistry.RIME_SPECTRE, world);
            entity.setPos(vecBlocksAway.x, user.getY() + .1f, vecBlocksAway.z);
            entity.setOwner(user);
            if (entity instanceof RimeSpectre) entity.addVelocity(0, 0.1f, 0);
            entity.setTamed(true, false);
            world.spawnEntity(entity);
            //this.saveSummonUuid(user, entity.getUuid());
            //this.addAmount(stack, -5);
            world.playSound(null, on, SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.PLAYERS, 0.75f, 1f);
            ParticleHandler.particleOutburstMap(world, 50, vecBlocksAway.getX(), vecBlocksAway.getY(), vecBlocksAway.getZ(), ParticleEvents.SOUL_RUPTURE_MAP, 1f);
            return TypedActionResult.success(stack, true);
        }
        return TypedActionResult.fail(stack);
    }

    /*

    @Override
    public void saveSummonUuid(LivingEntity user, UUID summonUuid) {
        SummonsData.addSummonUUID((IEntityDataSaver) user, summonUuid, this.getSummonsListId());
    }*/

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_frostmourne;
    }
}