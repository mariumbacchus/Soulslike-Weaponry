package net.soulsweaponry.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.soulsweaponry.networking.C2S.DamagingBoxC2S;

import java.util.UUID;

public class PacketHelper {

    /**
     * Creates a damaging box at the location. This is an instant box that will not appear in debugging since it will
     * look for all entities within it's expansion range and deal damage to them and apply knockback accordingly.
     * <p></p>
     * This method will knock targets away based on the knockback strength. Use other method if the knockback vector
     * shall be modified. This will also play the sound at the original boxPos, use other method if this must be changed.
     * @param boxPos Position of the box
     * @param expansion Expansion modifier of the box
     * @param damage Damage to be done
     * @param knockbackStrength Knockback strength
     * @param sound Sound to be played
     * @param attackerUUID UUID of the attacker to apply proper damage source
     */
    public static void damagingBox(BlockPos boxPos,
                                   double expansion,
                                   float damage,
                                   float knockbackStrength,
                                   SoundEvent sound,
                                   UUID attackerUUID) {
        damagingBox(boxPos, expansion, damage, knockbackStrength,
                0f, 0f,
                sound, boxPos,
                attackerUUID);
    }

    /**
     * Creates a damaging box at the location. This is an instant box that will not appear in debugging since it will
     * look for all entities within it's expansion range and deal damage to them and apply knockback accordingly.
     * <p></p>
     * This method will knock targets away based on the knockback strength. Use other method if the knockback vector
     * shall be modified.
     * @param boxPos Position of the box
     * @param expansion Expansion modifier of the box
     * @param damage Damage to be done
     * @param knockbackStrength Knockback strength
     * @param sound Sound to be played
     * @param soundPos Where the sound is played from
     * @param attackerUUID UUID of the attacker to apply proper damage source
     */
    public static void damagingBox(BlockPos boxPos,
                                   double expansion,
                                   float damage,
                                   float knockbackStrength,
                                   SoundEvent sound,
                                   BlockPos soundPos,
                                   UUID attackerUUID) {
        damagingBox(boxPos, expansion, damage, knockbackStrength,
                0f, 0f,
                sound, soundPos,
                attackerUUID);
    }

    /**
     * Creates a damaging box at the location. This is an instant box that will not appear in debugging since it will
     * look for all entities within it's expansion range and deal damage to them and apply knockback accordingly.
     * @param boxPos Position of the box
     * @param expansion Expansion modifier of the box
     * @param damage Damage to be done
     * @param knockbackStrength Knockback strength
     * @param knockbackX x coordinate the target should be knocked back towards
     * @param knockbackZ z coordinate the target should be knocked back towards
     * @param sound Sound to be played
     * @param soundPos Where the sound is played from
     * @param attackerUUID UUID of the attacker to apply proper damage source
     */
    public static void damagingBox(BlockPos boxPos,
                                   double expansion,
                                   float damage,
                                   float knockbackStrength,
                                   float knockbackX,
                                   float knockbackZ,
                                   SoundEvent sound,
                                   BlockPos soundPos,
                                   UUID attackerUUID) {
        var pkt = new DamagingBoxC2S(
                boxPos, expansion,
                damage, knockbackStrength,
                knockbackX, knockbackZ,
                sound.getId(), soundPos,
                attackerUUID
        );
        ClientPlayNetworking.send(pkt);
    }

    public static <T extends CustomPayload> void sendToAllPlayersS2C(ServerWorld world, BlockPos pos, T packet) {
        for (ServerPlayerEntity player : PlayerLookup.tracking(world, pos)) {
            ServerPlayNetworking.send(player, packet);
        }
    }
}