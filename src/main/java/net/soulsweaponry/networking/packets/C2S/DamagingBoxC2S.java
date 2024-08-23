package net.soulsweaponry.networking.packets.C2S;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class DamagingBoxC2S {

    private final BlockPos blockPos;
    private final double expansion;
    private final float damage;
    private final float knockbackStrength;
    private final float knockbackX;
    private final float knockbackZ;
    private final SoundEvent sound;
    private final BlockPos soundPos;
    private final UUID attackerUUID;

    public DamagingBoxC2S(BlockPos blockPos, double expansion, float damage, float knockbackStrength, float knockbackX, float knockbackZ, SoundEvent sound, BlockPos soundPos, UUID attackerUUID) {
        this.blockPos = blockPos;
        this.expansion = expansion;
        this.damage = damage;
        this.knockbackStrength = knockbackStrength;
        this.knockbackX = knockbackX;
        this.knockbackZ = knockbackZ;
        this.sound = sound;
        this.soundPos = soundPos;
        this.attackerUUID = attackerUUID;
    }

    //Same as decode
    public DamagingBoxC2S(PacketByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        this.expansion = buf.readDouble();
        this.damage = buf.readFloat();
        this.knockbackStrength = buf.readFloat();
        this.knockbackX = buf.readFloat();
        this.knockbackZ = buf.readFloat();
        this.sound = Registries.SOUND_EVENT.get(buf.readIdentifier());
        this.soundPos = buf.readBlockPos();
        this.attackerUUID = buf.readUuid();
    }

    // Same as encode
    public void toBytes(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeDouble(this.expansion);
        buf.writeFloat(this.damage);
        buf.writeFloat(this.knockbackStrength);
        buf.writeFloat(this.knockbackX);
        buf.writeFloat(this.knockbackZ);
        buf.writeIdentifier(this.sound.getId());
        buf.writeBlockPos(this.soundPos);
        buf.writeUuid(this.attackerUUID);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            this.handlePacket(player, this);
        });
        context.setPacketHandled(true);
    }

    private void handlePacket(ServerPlayerEntity player, DamagingBoxC2S packet) {
        ServerWorld serverWorld = (ServerWorld) player.getWorld();
        Entity attacker = serverWorld.getEntity(packet.attackerUUID);
        DamageSource source;
        if (attacker instanceof LivingEntity living) {
            source = serverWorld.getDamageSources().mobAttack(living);
        } else {
            source = serverWorld.getDamageSources().generic();
        }
        for (Entity entity : serverWorld.getOtherEntities(player, new Box(packet.blockPos).expand(packet.expansion))) {
            if (entity instanceof LivingEntity target) {
                target.damage(source, packet.damage);
                if (packet.knockbackX == 0 && packet.knockbackZ == 0) {
                    double x = packet.blockPos.getX() - target.getX();
                    double z = packet.blockPos.getZ() - target.getZ();
                    target.takeKnockback(packet.knockbackStrength, x, z);
                } else {
                    target.takeKnockback(packet.knockbackStrength, packet.knockbackX, packet.knockbackZ);
                }
            }
        }
        serverWorld.playSound(null, packet.soundPos, packet.sound, SoundCategory.HOSTILE, 1f, 1f);
    }
}
