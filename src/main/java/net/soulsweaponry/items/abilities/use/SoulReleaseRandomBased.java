package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.ISummonAlliesAbility;
import net.soulsweaponry.items.abilities.targetdeath.ISoulHarvest;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.List;

/**
 * Use to summon a random entity from the entity type list.
 * Can add weighting later if that is needed.
 */
public record SoulReleaseRandomBased(
        int maxSummons, String summonListId, List<EntityType<?>> entitiesToChoose, int soulCost,
        float bonusHealthPerPower, float bonusHealthIncreasePerLvl, float maxBonusHealth,
        float bonusAttackPerPower, float bonusAttackIncreasePerLvl, float maxBonusAttack
) implements ISoulHarvest, ISummonAlliesAbility {

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        int power = user.isCreative() ? this.soulCost : this.getSouls(stack);
        if (power >= this.soulCost && world instanceof ServerWorld serverWorld && this.canSummonEntity(serverWorld, user, this.getSummonsListId())) {
            Vec3d vecBlocksAway = user.getRotationVector().multiply(3).add(user.getPos());
            BlockPos on = BlockPos.ofFloored(vecBlocksAway);
            EntityType<?> type = this.entitiesToChoose.get(user.getRandom().nextInt(this.entitiesToChoose.size()));
            Entity entity = type.create(world);
            if (entity != null) {
                entity.setPos(vecBlocksAway.x, user.getY() + .1f, vecBlocksAway.z);
                if (entity instanceof TameableEntity tameableEntity) {
                    tameableEntity.setOwner(user);
                    tameableEntity.setTamed(true);
                }
                this.updateStats(entity, power, stack, this.bonusHealthPerPower, this.bonusHealthIncreasePerLvl, this.maxBonusHealth, this.bonusAttackPerPower, this.bonusAttackIncreasePerLvl, this.maxBonusAttack);
                entity.addVelocity(0, 0.1f, 0);
                world.spawnEntity(entity);

                this.saveSummonUuid(user, entity.getUuid());
                if (!user.isCreative()) {
                    this.addAmount(stack, -5);
                }
                world.playSound(null, on, SoundRegistry.NIGHTFALL_SPAWN_EVENT.get(), SoundCategory.PLAYERS, 0.75f, 1f);
                ParticleHandler.particleOutburstMap(world, 50, vecBlocksAway.getX(), vecBlocksAway.getY(), vecBlocksAway.getZ(), ParticleEvents.SOUL_RUPTURE_MAP, 1f);
                return TypedActionResult.success(stack, true);
            }
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public int getMaxSummons() {
        return this.maxSummons;
    }

    @Override
    public String getSummonsListId() {
        return this.summonListId;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.soul_release").formatted(Formatting.DARK_BLUE),
                Text.translatable("tooltip.soulsweapons.soul_release_random.1").formatted(Formatting.GRAY)
        );
    }
}
