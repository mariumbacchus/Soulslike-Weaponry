package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.Forlorn;
import net.soulsweaponry.entity.mobs.SoulReaperGhost;
import net.soulsweaponry.entity.mobs.Soulmass;
import net.soulsweaponry.items.abilities.ISummonAlliesAbility;
import net.soulsweaponry.items.abilities.targetdeath.ISoulHarvest;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.List;
import java.util.Map;

public record SoulReleasePowerBased(int maxSummons, Map<Integer, EntityType<?>> entityPowerMap) implements ISoulHarvest, ISummonAlliesAbility {

    private static final SoulReleasePowerBased SOUL_RELEASE = new SoulReleasePowerBased(
            (int) ConfigConstructor.soul_reaper_summoned_allies_cap,
            Map.of(
                    3, EntityRegistry.SOUL_REAPER_GHOST,
                    10, EntityRegistry.FORLORN,
                    30, EntityRegistry.SOULMASS
            )
    );

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand, ItemStack stack) {
        //TODO rewrite the whole thing so that it chooses off of the map based on power
        // TODO also make it choose second best when sneaking
        int power = this.getSouls(stack);
        if (player.isCreative()) power = player.getRandom().nextBetween(5, 50);
        if (power != 0) {
            if (power >= 3 && !world.isClient && this.canSummonEntity((ServerWorld) world, player, this.getSummonsListId())) {
                Vec3d vecBlocksAway = player.getRotationVector().multiply(3).add(player.getPos());
                ParticleHandler.particleOutburstMap(world, 50, vecBlocksAway.getX(), vecBlocksAway.getY(), vecBlocksAway.getZ(), ParticleEvents.CONJURE_ENTITY_MAP, 1f);
                world.playSound(null, player.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.PLAYERS, 0.8f, 1f);
                if (power < 10) {
                    SoulReaperGhost entity = new SoulReaperGhost(EntityRegistry.SOUL_REAPER_GHOST, world);
                    entity.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
                    entity.setOwner(player);
                    world.spawnEntity(entity);
                    this.saveSummonUuid(player, entity.getUuid());
                    if (!player.isCreative()) this.addAmount(stack, -3);
                } else if (player.isSneaking() || power < 30) {
                    Forlorn entity = new Forlorn(EntityRegistry.FORLORN, world);
                    entity.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
                    entity.setOwner(player);
                    world.spawnEntity(entity);
                    this.saveSummonUuid(player, entity.getUuid());
                    if (!player.isCreative()) this.addAmount(stack, -10);
                } else {
                    Soulmass entity = new Soulmass(EntityRegistry.SOULMASS, world);
                    entity.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
                    entity.setOwner(player);
                    world.spawnEntity(entity);
                    this.saveSummonUuid(player, entity.getUuid());
                    if (!player.isCreative()) this.addAmount(stack, -30);
                }

                stack.damage(3, player, LivingEntity.getSlotForHand(hand));
                return TypedActionResult.success(stack, true);
            }
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.soul_release").formatted(Formatting.DARK_BLUE),
                Text.translatable("tooltip.soulsweapons.soul_release_description_1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.soul_release_description_2").formatted(Formatting.GRAY)
        );
    }

    @Override
    public int getMaxSummons() {
        return this.maxSummons;
    }

    @Override
    public String getSummonsListId() {
        return "SoulReaperSummons";
    }
}
