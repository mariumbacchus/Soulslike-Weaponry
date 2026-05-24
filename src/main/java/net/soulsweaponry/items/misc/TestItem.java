package net.soulsweaponry.items.misc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class TestItem extends SwordItem {

    public TestItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        /*if (!world.isClient) {
            Vec3d vec = user.getRotationVector().multiply(3).add(user.getPos());
            ParticleHandler.particleOutburstMap(world, 200, vec.getX(), vec.getY(), vec.getZ(), ParticleEvents.FLAME_RUPTURE_MAP, 1f);
        }*/
        user.setCurrentHand(hand);
        return super.use(world, user, hand);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }
}
