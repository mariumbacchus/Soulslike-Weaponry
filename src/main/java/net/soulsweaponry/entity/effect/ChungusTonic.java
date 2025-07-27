package net.soulsweaponry.entity.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.soulsweaponry.config.ChungusTonicWhitelist;
import net.soulsweaponry.entitydata.DespawnTimerData;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class ChungusTonic extends StatusEffect {

    public ChungusTonic() {
        super(StatusEffectCategory.BENEFICIAL, 0x29ff90 /*, ParticleTypes.LARGE_SMOKE */);//TODO custom "rainbow" particle that has randomly adjusting colors
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        List<EntityType<?>> whitelist = WeaponUtil.getEntityListOffArray(ChungusTonicWhitelist.chungus_tonic_whitelist);
        if (!(entity instanceof PlayerEntity) && DespawnTimerData.getDespawnTicks(entity) == 0 && !entity.getWorld().isClient) {
            EntityType<?> type = whitelist.get(entity.getRandom().nextInt(whitelist.size()));
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
