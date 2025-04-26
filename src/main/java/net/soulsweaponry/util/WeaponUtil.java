package net.soulsweaponry.util;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WeaponUtil {

    public static final Enchantment[] DAMAGE_ENCHANTS = {Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.BANE_OF_ARTHROPODS};

    /**
     * Returns level of the damage enchant, for example {@code 5} for Sharpness V or {@code 4} for Smite IV
     */
    public static int getEnchantDamageBonus(ItemStack stack) {
        for (Enchantment ench : DAMAGE_ENCHANTS) {
            if (EnchantmentHelper.getLevel(ench, stack) > 0) {
                return EnchantmentHelper.getLevel(ench, stack);
            }
        }
        return 0;
    }

    // NOTE: May be changed in future versions.
    public static boolean isModLoaded(String modId) {
        return FMLLoader.getLoadingModList().getModFileById(modId) != null;
    }

    public static boolean isFightModLoaded() {
        return isModLoaded("bettercombat") || isModLoaded("epicfight");
    }

    public static List<Integer> arrayToList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int t : array) {
            list.add(t);
        }
        return list;
    }

    /**
     * Spawn/run something in a series of concentric circles around the start point,
     * rippling outwards. Ground‐detection is done using your doConsumerOnPoint logic.
     * @param world        world
     * @param yaw          players yaw in degrees
     * @param startPos     center point
     * @param maxYOffset   max vertical search offset, we look from startPos.y - maxYOffset to startPos.y + maxYOffset
     * @param ripples      how many rings to spawn
     * @param radiusAndMod vec2f: .x = base radius, .y = per‐ripples increment
     * @param consumer     tri‐consumer taking (position, warmupDelay, spawnYaw (degrees))
     */
    public static void doConsumerOnCircle(World world, float yaw, Vec3d startPos, double maxYOffset, int ripples, Vec2f radiusAndMod, TriConsumer<Vec3d, Integer, Float> consumer) {
        double minY = startPos.getY() - maxYOffset;
        double maxY = startPos.getY() + maxYOffset;
        float yawRad = (float) Math.toRadians(yaw);
        for (int wave = 0; wave < ripples; wave++) {
            double radius = radiusAndMod.x + wave * radiusAndMod.y;
            int step = MathHelper.floor(80f / (wave + 1f));
            for (int angleDeg = 0; angleDeg < 360; angleDeg += step) {
                float totalRad = yawRad + (angleDeg * (float)Math.PI / 180f);
                double x = startPos.getX() + radius * Math.cos(totalRad);
                double z = startPos.getZ() + radius * Math.sin(totalRad);
                int warmup = 3 * (wave + 1);
                float spawnYaw = yaw + angleDeg;
                doConsumerOnPoint(world, x, z, minY, maxY, warmup, spawnYaw, consumer);
            }
        }
    }

    /**
     * Use this method when doing something in a line, for example spawn multiple {@code HolyMoonlightPillar} with delayed warmup
     * in a straight line from the user.
     * @param world world
     * @param yaw yaw of the user (often times needs to be plussed by 90)
     * @param startPos start position (for example the position of the user)
     * @param maxYOffset max y offset from the original y point the points can go through to find valid spot
     * @param amount amount of positions to generate/range outwards => amount of entities in the line
     * @param spacingModifier spacing modifier between each point, normally 1.75 or 1.25
     * @param consumer consumer accepting {@code Vec3d, Integer and Float}, where the vec3d is the position, integer is the delayed warmup and float is the yaw output for rotating the entity
     */
    public static void doConsumerOnLine(World world, float yaw, Vec3d startPos, double maxYOffset, int amount, float spacingModifier, TriConsumer<Vec3d, Integer, Float> consumer) {
        double minY = startPos.getY() - maxYOffset;
        double maxY = startPos.getY() + maxYOffset;
        float f = (float) Math.toRadians(yaw);
        for (int i = 0; i < amount; i++) {
            double h = spacingModifier * (double)(i + 1);
            doConsumerOnPoint(world, startPos.getX() + (double)MathHelper.cos(f) * h, startPos.getZ() + (double)MathHelper.sin(f) * h, minY, maxY, -6 + i * 2, yaw, consumer);
        }
    }

    /**
     * Used in {@code doConsumerOnLine} method, do something on the position if valid (at right height, not inside solids blocks, etc.)
     * @param world world
     * @param x x position
     * @param z z position
     * @param minY min y with offset
     * @param maxY max y with offset
     * @param warmup warmup/delay for the entity, meant for entities such as {@code HolyMoonlightPillar} that should explode after a delay
     * @param yaw yaw of the entity for rotating the entity in the consumer
     * @param consumer consumer accepting {@code Vec3d, Integer and Float}, where the vec3d is the position, integer is the delayed warmup and float is the yaw output for rotating the entity
     */
    public static void doConsumerOnPoint(World world, double x, double z, double minY, double maxY, int warmup, float yaw, TriConsumer<Vec3d, Integer, Float> consumer) {
        BlockPos blockPos = BlockPos.ofFloored(x, maxY, z);
        boolean valid = false;
        double shapeOffset = 0.0;
        do {
            BlockPos blockPos2 = blockPos.down();
            BlockState blockState = world.getBlockState(blockPos2);
            if (blockState.isSideSolidFullSquare(world, blockPos2, Direction.UP)) {
                if (!world.isAir(blockPos)) {
                    BlockState blockState2 = world.getBlockState(blockPos);
                    VoxelShape voxelShape = blockState2.getCollisionShape(world, blockPos);
                    if (!voxelShape.isEmpty()) {
                        shapeOffset = voxelShape.getMax(Direction.Axis.Y);
                    }
                }
                valid = true;
                break;
            }
            blockPos = blockPos.down();
        } while (blockPos.getY() >= MathHelper.floor(minY) - 1);
        if (valid) {
            consumer.accept(new Vec3d(x, (double)blockPos.getY() + shapeOffset, z), warmup, yaw);
        }
    }

    /**
     * Get the luck attribute of the entity.
     * Luck = 1 * effect_amplifier, is normally at 0 without other effects or interactions.
     */
    public static int getLuckFactor(LivingEntity entity) {
        return MathHelper.floor(entity.getAttributeValue(EntityAttributes.GENERIC_LUCK) * 2 + 2);
    }

    /**
     *
     * @param user user (checks this entity for luck attribute)
     * @param list list of LuckChosenObjects that should be picked at random based on LuckType
     * @return the chosen object, such as a random entity type or random enum
     * @param <T> the object type stored in LuckChosenObject
     */
    @Nullable
    public static <T> T getRandomlyChosenObject(LivingEntity user, List<LuckChosenObject<T>> list, boolean flipLuckTypes) {
        List<LuckChosenObject<T>> projectileList = new ArrayList<>();
        int modifier = flipLuckTypes ? -1 : 1;
        for (LuckChosenObject<T> luckChosen : list) {
            switch (luckChosen.getLuckType()) {
                case BAD -> luckChosen.setLuckFactor(luckChosen.getLuckFactor() + (modifier * - WeaponUtil.getLuckFactor(user)));
                case GOOD -> luckChosen.setLuckFactor(luckChosen.getLuckFactor() + (modifier * WeaponUtil.getLuckFactor(user)));
            }
            if (luckChosen.getLuckFactor() > 0) {
                projectileList.add(luckChosen);
            }
        }
        int totalChance = 0;
        for (LuckChosenObject<T> object : projectileList) {
            totalChance += object.getLuckFactor();
        }
        int random = user.getRandom().nextInt(totalChance);
        int cumulativeFactor = 0;
        for (LuckChosenObject<T> luckChosen : projectileList) {
            cumulativeFactor += luckChosen.getLuckFactor();
            if (random < cumulativeFactor) {
                return luckChosen.getObject();
            }
        }
        return null;
    }

    public enum LuckType {
        GOOD, NEUTRAL, BAD
    }
}