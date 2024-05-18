package net.soulsweaponry.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.items.LeviathanAxe;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class LeviathanAxeEntity extends ReturningProjectile implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public LeviathanAxeEntity(EntityType<? extends LeviathanAxeEntity> entityType, World world) {
        super(entityType, world);
        this.stack = new ItemStack(WeaponRegistry.LEVIATHAN_AXE.get());
        this.ignoreCameraFrustum = true;
    }

    public LeviathanAxeEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.LEVIATHAN_AXE_ENTITY_TYPE.get(), owner, world);
        this.stack = stack.copy();
        this.ignoreCameraFrustum = true;
    }

    @Override
    public float getDamage(Entity target) {
        return CommonConfig.LEVIATHAN_AXE_PROJECTILE_DAMAGE.get() + WeaponUtil.getEnchantDamageBonus(this.asItemStack());
    }

    @Override
    public boolean collide(Entity owner, Entity target, float damage) {
        if (!world.isClient && target instanceof MjolnirProjectile) {
            ParticleEvents.mjolnirLeviathanAxeCollision(this.getWorld(), this.getX(), this.getY(), this.getZ());
            this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), 6.0F, true, Explosion.DestructionType.DESTROY);
        }
        DamageSource damageSource = DamageSource.trident(this, owner);
        boolean bl = target.damage(damageSource, damage);
        if (bl) {
            LeviathanAxe.iceExplosion(getWorld(), this.getBlockPos(), this.getOwner(), EnchantmentHelper.getLevel(Enchantments.SHARPNESS, this.stack));
        }
        return bl;
    }

    @Override
    public double getReturnSpeed(ItemStack stack) {
        return CommonConfig.LEVIATHAN_AXE_RETURN_SPEED.get() + (double) EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack)/2f;
    }

    @Override
    protected boolean canHit(Entity entity) {
        if (entity instanceof MjolnirProjectile) {
            return true;
        } else {
            return super.canHit(entity);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
