package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.ISummonAlliesAbility;
import net.soulsweaponry.items.abilities.targetdeath.ISoulHarvest;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;

/**
 * Use to spawn entities based on the amount of souls the item has harvested by fetching {@link #getSouls(ItemStack)}.
 * The ability will prioritize the best entity based on power needed.
 * <p>
 * For example: if the required for 3 entities is 5, 10 and 50, and the power is 67, first entity is the one requiring 50 and
 * so 50 souls is deducted. 17 souls remain, so then it would choose the one requiring 10. 7 souls remain, and so entity
 * needing 5 is chosen. 2 souls remain so nothing happens.
 * <p>
 * If the player sneaks, then the {@code second best} entity will always be chosen.
 * <p>
 * Being in creative will always spawn the best entity.
 *
 * @param maxSummons max amount of allies that can exist at once
 * @param entityPowerMap tree map containing the mappings of the entity and how many souls that are required
 * @param bonusHealthPerPower bonus health per power (per soul)
 * @param bonusHealthIncreasePerLvl added to {@code bonusHealthPerPower} before the bonus health per soul calculation, is based on weapon level
 * @param maxBonusHealth max bonus health the summon can get
 * @param bonusAttackPerPower bonus attack damage per power (per soul)
 * @param bonusAttackIncreasePerLvl added to {@code bonusAttackPerPower} before the bonus attack per soul calculation, is based on weapon level
 * @param maxBonusAttack max bonus attack damage the summon can get
 */
public record SoulReleasePowerBased(int maxSummons, String summonListId, NavigableMap<Integer, EntityType<?>> entityPowerMap,
                                    float bonusHealthPerPower, float bonusHealthIncreasePerLvl, float maxBonusHealth,
                                    float bonusAttackPerPower, float bonusAttackIncreasePerLvl, float maxBonusAttack
) implements ISoulHarvest, ISummonAlliesAbility {

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand, ItemStack stack) {
        int power = player.isCreative() ? this.entityPowerMap.lastKey() : this.getSouls(stack);
        if (entityPowerMap.isEmpty() || power < entityPowerMap.firstKey()) {
            return ActionResult.FAIL;
        }
        boolean sneaking = player.isSneaking();
        Map.Entry<Integer, EntityType<?>> chosen = sneaking ? secondBestEntry(power) : bestEntry(power);
        if (chosen == null) {
            return ActionResult.FAIL;
        }
        EntityType<?> type = chosen.getValue();
        int cost = chosen.getKey();
        Vec3d vecBlocksAway = player.getRotationVector().multiply(3).add(player.getPos());
        ParticleHandler.particleOutburstMap(world, 50, vecBlocksAway.getX(), vecBlocksAway.getY(), vecBlocksAway.getZ(), ParticleEvents.CONJURE_ENTITY_MAP, 1f);
        world.playSound(null, player.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.PLAYERS, 0.8f, 1f);
        Entity e = type.create(world, SpawnReason.SPAWN_ITEM_USE);
        if (e != null) {
            e.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
            if (e instanceof TameableEntity t) {
                t.setOwner(player);
            }
            this.updateStats(e, power, stack, this.bonusHealthPerPower, this.bonusHealthIncreasePerLvl, this.maxBonusHealth, this.bonusAttackPerPower, this.bonusAttackIncreasePerLvl, this.maxBonusAttack);
            world.spawnEntity(e);
            this.saveSummonUuid(player, e.getUuid());
            if (!player.isCreative()) {
                this.addAmount(stack, - cost);
            }
        }
        stack.damage(3, player, LivingEntity.getSlotForHand(hand));
        return ActionResult.SUCCESS;
    }

    /**
     * Gets the best entry based on power.
     * Returns null if the power is under the smallest threshold.
     */
    @Nullable
    private Map.Entry<Integer, EntityType<?>> bestEntry(int power) {
        return entityPowerMap.floorEntry(power);
    }

    /**
     * Gets the second-best entry based on power.
     * Returns null if the power is under the smallest threshold.
     */
    @Nullable
    private Map.Entry<Integer, EntityType<?>> secondBestEntry(int power) {
        var best = entityPowerMap.floorEntry(power);
        return (best == null) ? null : Objects.requireNonNullElse(entityPowerMap.lowerEntry(best.getKey()), best);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.soul_release").formatted(Formatting.DARK_BLUE),
                Text.translatable("tooltip.soulsweapons.soul_release.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.soul_release.2").formatted(Formatting.GRAY)
        );
    }

    @Override
    public int getMaxSummons() {
        return this.maxSummons;
    }

    @Override
    public String getSummonsListId() {
        return this.summonListId;
    }
}
