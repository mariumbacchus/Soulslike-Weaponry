package net.soulsweaponry.entity.projectile;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.soulsweaponry.items.LeviathanAxe;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class LeviathanAxeEntity extends PersistentProjectileEntity implements GeoEntity {

    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);
    private ItemStack stack;

    public LeviathanAxeEntity(EntityType<? extends LeviathanAxeEntity> entityType, World world) {
        super(entityType, world);
        this.stack = new ItemStack(WeaponRegistry.LEVIATHAN_AXE);
    }

    public LeviathanAxeEntity(EntityType<? extends LeviathanAxeEntity> type, double x, double y, double z,
            World world) {
        super(type, x, y, z, world);
        this.stack = new ItemStack(WeaponRegistry.LEVIATHAN_AXE);
    }

    public LeviathanAxeEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityRegistry.LEVIATHAN_AXE_ENTITY_TYPE, owner, world);
        this.stack = stack.copy();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.setUpRemove();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        this.setUpRemove();
    }

    private void setUpRemove() {
        LeviathanAxe.iceExplosion(world, this.getBlockPos(), this.getOwner(), EnchantmentHelper.getLevel(Enchantments.SHARPNESS, this.stack));
        this.discard();
    }

    private PlayState predicate(AnimationState state) {
        state.getController().setAnimation(RawAnimation.begin().then("spin", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public ItemStack asItemStack() {
        return this.stack;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }
    
}
