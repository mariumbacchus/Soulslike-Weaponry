package net.soulsweaponry.entity.logic;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ParticleRegistry;

import java.util.ArrayList;
import java.util.List;

public class DeathSpiralLogic {
    private final Vec3d pos;
    private final float radius;
    private int age;
    private boolean switchParticle = true;
    private final List<List<Vec3d>> spirals;

    public DeathSpiralLogic(Vec3d pos, float radius) {
        this.pos = pos;
        this.radius = radius;
        this.spirals = this.getSpirals();
    }

    public void tick(World world, Vec3d origin) {
        this.age++;
        if (world.isClient) {
            for (List<Vec3d> list : spirals) {
                if (list.size() != 0) {
                    ParticleEffect type = this.switchParticle ? ParticleRegistry.DAZZLING_PARTICLE : ParticleRegistry.DARK_STAR;
                    Vec3d vec = new Vec3d(list.get(0).getX(), list.get(0).getY(), list.get(0).getZ()).add(origin);
                    world.addParticle(type, vec.getX(), vec.getY(), vec.getZ(),
                            0, 0, 0);
                    this.switchParticle = !switchParticle;
                    list.remove(0);
                }
            }
        }
    }

    private List<List<Vec3d>> getSpirals() {
        List<Vec3d> list1 = new ArrayList<>();
        List<Vec3d> list2 = new ArrayList<>();
        List<List<Vec3d>> list3 = new ArrayList<>();
        float r = this.radius;
        for (int theta = 0; theta < 360; theta++) {
            if (theta % 2 == 0) {
                double x0 = this.pos.getX();
                double y0 = this.pos.getY() + 3;
                double z0 = this.pos.getZ();
                double x = x0 + r * Math.cos(theta * Math.PI / 180);
                double z = z0 + r * Math.sin(theta * Math.PI / 180);
                list1.add(new Vec3d(x, this.pos.getY() + theta * Math.PI/180, z));
                list2.add(new Vec3d(x, y0 + r * Math.tan(theta * Math.PI / 180), z));
            }
        }
        list3.add(list1);
        list3.add(list2);
        return list3;
    }

    public boolean isFinished() {
        int i = 0;
        for (List<Vec3d> list : this.spirals) {
            i += list.size();
        }
        return i == 0;
    }
}