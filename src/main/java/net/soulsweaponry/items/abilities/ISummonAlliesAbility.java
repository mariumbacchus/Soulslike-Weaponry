package net.soulsweaponry.items.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.SummonsData;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface ISummonAlliesAbility extends IAbility {

    int getMaxSummons();
    String getSummonsListId();

    default void saveSummonUuid(LivingEntity user, UUID summonUuid) {
        SummonsData.addSummonUUID((IEntityDataSaver) user, summonUuid, this.getSummonsListId());
    }

    default boolean canSummonEntity(ServerWorld world, LivingEntity user, String listId) {
        List<UUID> toRemove = new ArrayList<>();
        for (UUID prevUuid : SummonsData.getAliveSummons(user, listId)) {
            Entity entity = world.getEntity(prevUuid);
            if (entity == null || !entity.isAlive()) {
                toRemove.add(prevUuid);
            }
        }
        toRemove.forEach(uuid -> SummonsData.removeSummonUUID((IEntityDataSaver) user, uuid, listId));
        return SummonsData.getAliveSummons(user, listId).length < this.getMaxSummons();
    }

    /**
     * Increases the {@link EntityAttributes#GENERIC_MAX_HEALTH} and {@link EntityAttributes#GENERIC_ATTACK_DAMAGE}
     * based on {@code power} (souls).
     */
    default void updateStats(Entity entity, int power, ItemStack stack,
                             float bonusHealthPerPower, float bonusHealthIncreasePerLvl, float maxBonusHealth,
                             float bonusAttackPerPower, float bonusAttackIncreasePerLvl, float maxBonusAttack
    ) {
        if (entity instanceof LivingEntity living) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            var maxHealth = living.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            if (maxHealth != null) {
                double newMax = power * (bonusHealthPerPower + bonusHealthIncreasePerLvl * lvl);
                newMax = living.getMaxHealth() + Math.min(maxBonusHealth, newMax);
                maxHealth.setBaseValue(newMax);
                living.setHealth((float) newMax);
            }
            var atk = living.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            if (atk != null) {
                double newDmg = power * (bonusAttackPerPower + bonusAttackIncreasePerLvl * lvl);
                newDmg = atk.getValue() + Math.min(maxBonusAttack, newDmg);
                atk.setBaseValue(newDmg);
            }
        }
    }
}