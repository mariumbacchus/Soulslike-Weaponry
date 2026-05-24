package net.soulsweaponry.items.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.DamageSourceRegistry;

import java.util.function.Consumer;

public class ChainLightning {

    public static void trigger(World world, LivingEntity target, LivingEntity user, float damage, double boxExpansion, Consumer<LivingEntity> targetConsumer) {
        if (!world.isClient) {
            Vec3d toPrimary  = new Vec3d(target.getX(), target.getBodyY(0.5f), target.getZ());
            targetConsumer.accept(target);

            // Lightning from player to target, was visually just in the way so disabling it for now
            //Vec3d fromPlayer = new Vec3d(user.getX(), user.getEyeY(), user.getZ());
            //ParticleHandler.chainLightning(world, fromPlayer, toPrimary);

            for (Entity e : world.getOtherEntities(target,
                    target.getBoundingBox().expand(boxExpansion),
                    ent -> ent instanceof LivingEntity && !ent.equals(user) && !user.isTeammate(ent)
                            && (!(ent instanceof Tameable tameable) // exclude the users owned mobs such as wolves
                            || tameable.getOwner() == null
                            || !tameable.getOwner().equals(user)))) {
                LivingEntity secondary = (LivingEntity)e;
                world.playSound(null, secondary.getBlockPos(), SoundRegistry.SHOCK, SoundCategory.PLAYERS, 1f, 1f);
                secondary.damage(DamageSourceRegistry.create(world, DamageSourceRegistry.PLAYER_LIGHTNING, user), damage);
                Vec3d toSecondary = new Vec3d(secondary.getX(), secondary.getBodyY(0.5f), secondary.getZ());
                ParticleHandler.chainLightning(world, toPrimary, toSecondary);
                targetConsumer.accept(secondary);
            }
        }
    }

    public static void trigger(World world, LivingEntity target, LivingEntity user, float damage, double boxExpansion) {
        trigger(world, target, user, damage, boxExpansion, (e) -> {});
    }
}
