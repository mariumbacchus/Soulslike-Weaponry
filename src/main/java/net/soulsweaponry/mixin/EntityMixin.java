package net.soulsweaponry.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.BigChungus;
import net.soulsweaponry.entitydata.DespawnTimerData;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        Entity entity = ((Entity) (Object)this);
        if (!entity.getWorld().isClient) {
            if (DespawnTimerData.getDespawnTicks(entity) > 0) {
                int timer = DespawnTimerData.addDespawnTicks((IEntityDataSaver) entity, 1);
                if (!(entity instanceof PlayerEntity) && timer >= ConfigConstructor.chungus_tonic_ticks_until_chungified) {
                    ParticleHandler.particleSphereList(entity.getWorld(), 10, entity.getX(), entity.getY(), entity.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
                    BigChungus chungus = new BigChungus(EntityRegistry.BIG_CHUNGUS, entity.getWorld());
                    chungus.setPosition(entity.getPos());
                    entity.getWorld().spawnEntity(chungus);
                    entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }
}
