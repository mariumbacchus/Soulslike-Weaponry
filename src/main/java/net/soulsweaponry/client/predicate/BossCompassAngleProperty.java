package net.soulsweaponry.client.predicate;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.items.misc.BossCompass;
import net.soulsweaponry.registry.ItemRegistry;
import org.jetbrains.annotations.Nullable;

public class BossCompassAngleProperty implements NumericProperty {

    public static final MapCodec<BossCompassAngleProperty> CODEC = MapCodec.unit(new BossCompassAngleProperty());

    @Override
    public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity holder, int seed) {
        if (world == null || holder == null) return 0.0f;

        if (!stack.isOf(ItemRegistry.BOSS_COMPASS)) return 0.0f;
        BossCompass item = (BossCompass) stack.getItem();

        BlockPos target = item.getStructurePos(world, stack).pos();
        if (target == null) return 0.0f;

        double dx = (target.getX() + 0.5) - holder.getX();
        double dz = (target.getZ() + 0.5) - holder.getZ();

        // If too close, behave like "invalid target"
        if (dx * dx + dz * dz < 1.0e-4) return 0.0f;

        // Holder yaw in radians, angle to target in radians
        double yaw = Math.toRadians(holder.getYaw());
        double angleToTarget = Math.atan2(dz, dx);

        // Vanilla-like: return 0..1
        double angle = (angleToTarget - yaw) / (Math.PI * 2.0);
        return MathHelper.floorMod((float)angle, 1.0f);
    }

    @Override
    public MapCodec<? extends NumericProperty> getCodec() {
        return CODEC;
    }
}
