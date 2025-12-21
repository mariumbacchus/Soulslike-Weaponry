package net.soulsweaponry.networking.C2S.receivers;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.soulsweaponry.networking.C2S.packets.DamagingBoxC2S;

import java.util.UUID;

public class DamagingBoxC2SReceiver {

    public static void receive(DamagingBoxC2S pkt, ServerPlayNetworking.Context ctx) {
        MinecraftServer server = ctx.server();
        if (server == null) {
            return;
        }
        ServerPlayerEntity player = ctx.player();
        ServerWorld serverWorld = player.getServerWorld();
        BlockPos blockPos = pkt.blockPos();
        double expansion = pkt.expansion();
        float damage = pkt.damage();
        float knockbackStrength = pkt.knockbackStrength();
        float knockbackX = pkt.knockbackX();
        float knockbackZ = pkt.knockbackZ();
        SoundEvent sound = Registries.SOUND_EVENT.get(pkt.soundId());
        BlockPos soundPos = pkt.soundPos();
        UUID attackerUUID = pkt.attackerUuid();
        server.execute(() -> {
            Entity attacker = serverWorld.getEntity(attackerUUID);
            DamageSource source;
            if (attacker instanceof LivingEntity living) {
                source = serverWorld.getDamageSources().mobAttack(living);
            } else {
                source = serverWorld.getDamageSources().generic();
            }
            for (Entity entity : serverWorld.getOtherEntities(player, new Box(blockPos).expand(expansion))) {
                if (entity instanceof LivingEntity target) {
                    target.damage(serverWorld, source, damage);
                    if (knockbackX == 0 && knockbackZ == 0) {
                        double x = blockPos.getX() - target.getX();
                        double z = blockPos.getZ() - target.getZ();
                        target.takeKnockback(knockbackStrength, x, z);
                    } else {
                        target.takeKnockback(knockbackStrength, knockbackX, knockbackZ);
                    }
                }
            }
            serverWorld.playSound(null, soundPos, sound, SoundCategory.HOSTILE, 1f, 1f);
        });
    }
}
