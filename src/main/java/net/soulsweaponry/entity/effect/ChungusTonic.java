package net.soulsweaponry.entity.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.DespawnTimerData;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.registry.EffectRegistry;

import java.util.Set;
import java.util.stream.Collectors;

public class ChungusTonic extends StatusEffect {

    public ChungusTonic() {
        super(StatusEffectCategory.BENEFICIAL, 0x29ff90);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        Set<String> excludedStr = Set.of(ConfigConstructor.chungus_tonic_excluded_entities_to_become);
        Set<EntityType<?>> excluded = excludedStr.stream().map((str) -> {
            Identifier entityId = new Identifier(str.contains(":") ? str : "minecraft:" + str);
            return Registries.ENTITY_TYPE.get(entityId);
        }).collect(Collectors.toSet());
        if (!(entity instanceof PlayerEntity) && DespawnTimerData.getDespawnTicks(entity) == 0 && !entity.getWorld().isClient) {
            EntityType<?> type;
            do {
                type = Registries.ENTITY_TYPE.get(entity.getRandom().nextInt(Registries.ENTITY_TYPE.size()));
            } while (excluded.contains(type));
            Entity randomEntity = type.create(entity.getWorld());
            if (randomEntity != null) {
                randomEntity.setPosition(entity.getPos());
                DespawnTimerData.setDespawnTicks((IEntityDataSaver) randomEntity, 1);
                if (randomEntity instanceof LivingEntity living) {
                    living.addStatusEffect(new StatusEffectInstance(EffectRegistry.CHUNGUS_TONIC_EFFECT, 1000, 0));
                }
                if (randomEntity instanceof PersistentProjectileEntity projectile) {
                    projectile.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                }
                entity.getWorld().spawnEntity(randomEntity);
            }
            entity.remove(Entity.RemovalReason.DISCARDED);
        }
    }
}
