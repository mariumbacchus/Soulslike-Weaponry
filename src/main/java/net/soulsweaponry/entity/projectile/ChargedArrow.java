package net.soulsweaponry.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.registry.EntityRegistry;

public class ChargedArrow extends PersistentProjectileEntity {

    private ItemStack stack;

    public ChargedArrow(EntityType<? extends ChargedArrow> entityType, World world) {
        super(entityType, world);
        this.stack = new ItemStack(Items.ARROW);
    }
  
    public ChargedArrow(World world, double x, double y, double z) {
        super(EntityRegistry.CHARGED_ARROW_ENTITY_TYPE, x, y, z, world);
        this.stack = new ItemStack(Items.ARROW);
    }
  
    public ChargedArrow(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.CHARGED_ARROW_ENTITY_TYPE, owner, world);
        this.stack = stack.copy();
    }
  
    public void tick() {
        if (!this.inGround) {
            Vec3d vec3d = this.getVelocity();
            double e = vec3d.x;
            double f = vec3d.y;
            double g = vec3d.z;
            for (int i = 0; i < 4; ++i) {
                this.getWorld().addParticle(this.getParticleType(), this.getX() + e * (double)i / 4.0D, this.getY() + f * (double)i / 4.0D, this.getZ() + g * (double)i / 4.0D, -e, -f + 0.2D, -g);
            }
        }
        super.tick();
    }

    protected ParticleEffect getParticleType() {
        return ParticleTypes.GLOW; //wax_on
    }

    protected ItemStack asItemStack() {
        this.stack.setCount(1);
        return this.stack;
    }
}
