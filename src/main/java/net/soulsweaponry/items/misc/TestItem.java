package net.soulsweaponry.items.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.collision.RotatableHitbox;
import net.soulsweaponry.collision.RotatableHitboxDebugRegistry;

import java.util.List;

public class TestItem extends Item {

    public TestItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 99999999;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) {
            return;
        }

        Vec3d look = player.getRotationVec(1.0F).normalize();
        Vec3d center = player.getEyePos().add(look.multiply(3.0));
        Vec3d size = new Vec3d(1.0, 1.0, 3.0);

        RotatableHitbox hitbox = RotatableHitbox.fromLook(center, size, look);
        if (world.isClient()) {
            String debugId = "test_item_" + player.getUuidAsString();
            RotatableHitboxDebugRegistry.put(world, debugId, hitbox);
            return;
        }

        List<Entity> hitEntities = hitbox.getIntersectingEntities(world, player, entity -> !entity.isSpectator() && entity.canHit() && entity.isAttackable());
        for (Entity entity : hitEntities) {
            System.out.println("Hit: " + entity.getName().getString());
        }
    }
}
