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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class LeviathanAxeEntity extends PersistentProjectileEntity implements IAnimatable, IAnimationTickable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
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

    @Override
    public ItemStack asItemStack() {
        return this.stack;
    }

    @Override
    public int tickTimer() {
        return 0;
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
    
}
