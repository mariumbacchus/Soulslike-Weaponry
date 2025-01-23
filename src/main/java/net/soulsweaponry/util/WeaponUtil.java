package net.soulsweaponry.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
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

    public static List<Integer> arrayToList(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int t : array) {
            list.add(t);
        }
        return list;
    }

    /**
     * Use this method when doing something in circles, rippling outwards from the center
     * @param world world
     * @param yaw yaw of the player (often times needs to be plussed by 90)
     * @param start start position (for example the position of the user)
     * @param maxY maxY level the positions can go to
     * @param ripples amount of ripples outwards from start position
     * @param radiusAndMod vec2f containing x for base radius and y for radius modifier, often times base = 1.5 and mod = 1.75
     * @param consumer consumer accepting {@code Vec3d, Integer and Float}, where the vec3d is the position, integer is the delayed warmup and float is the yaw output for rotating the entity
     */
    public static void doConsumerOnCircle(World world, float yaw, Vec3d start, double maxY, int ripples, Vec2f radiusAndMod, TriConsumer<Vec3d, Integer, Float> consumer) {
        double y = maxY + 1.0;
        float f = (float) Math.toRadians(yaw);
        for (int waves = 0; waves < ripples; waves++) {
            for (int i = 0; i < 360; i += MathHelper.floor((80f) / (waves + 1f))) {
                float r = radiusAndMod.x + waves * radiusAndMod.y;
                yaw = (float) (f + i * Math.PI / 180f);
                double x0 = start.getX();
                double z0 = start.getZ();
                double x = x0 + r * Math.cos(i * Math.PI / 180);
                double z = z0 + r * Math.sin(i * Math.PI / 180);
                doConsumerOnPoint(world, x, z, maxY, y, 3 * (waves + 1), yaw, consumer);
            }
        }
    }

    /**
     * Use this method when doing something in a line, for example spawn multiple {@code HolyMoonlightPillar} with delayed warmup
     * in a straight line from the user.
     * @param world world
     * @param yaw yaw of the user (often times needs to be plussed by 90)
     * @param startPos start position (for example the position of the user)
     * @param maxY maxY level the positions can go to
     * @param amount amount of positions to generate/range outwards => amount of entities in the line
     * @param spacingModifier spacing modifier between each point, normally 1.75 or 1.25
     * @param consumer consumer accepting {@code Vec3d, Integer and Float}, where the vec3d is the position, integer is the delayed warmup and float is the yaw output for rotating the entity
     */
    public static void doConsumerOnLine(World world, float yaw, Vec3d startPos, double maxY, int amount, float spacingModifier, TriConsumer<Vec3d, Integer, Float> consumer) {
        double y = maxY + 1.0;
        float f = (float) Math.toRadians(yaw);
        for (int i = 0; i < amount; i++) {
            double h = spacingModifier * (double)(i + 1);
            doConsumerOnPoint(world, startPos.getX() + (double)MathHelper.cos(f) * h, startPos.getZ() + (double)MathHelper.sin(f) * h, maxY, y, -6 + i * 2, yaw, consumer);
        }
    }

    /**
     * Used in {@code doConsumerOnLine} method, do something on the position if valid (at right height, not inside solids blocks, etc.)
     * @param world world
     * @param x x position
     * @param z z position
     * @param maxY max height/offset of y
     * @param y y position with offset
     * @param warmup warmup/delay for the entity, meant for entities such as {@code HolyMoonlightPillar} that should explode after a delay
     * @param yaw yaw of the entity for rotating the entity in the consumer
     * @param consumer consumer accepting {@code Vec3d, Integer and Float}, where the vec3d is the position, integer is the delayed warmup and float is the yaw output for rotating the entity
     */
    public static void doConsumerOnPoint(World world, double x, double z, double maxY, double y, int warmup, float yaw, TriConsumer<Vec3d, Integer, Float> consumer) {
        BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
        boolean bl = false;
        double d = 0.0;
        do {
            VoxelShape voxelShape;
            BlockPos blockPos2;
            if (!world.getBlockState(blockPos2 = blockPos.down()).isSideSolidFullSquare(world, blockPos2, Direction.UP)) continue;
            if (!world.isAir(blockPos) && !(voxelShape = world.getBlockState(blockPos).getCollisionShape(world, blockPos)).isEmpty()) {
                d = voxelShape.getMax(Direction.Axis.Y);
            }
            bl = true;
            break;
        } while ((blockPos = blockPos.down()).getY() >= MathHelper.floor(maxY) - 1);
        if (bl) {
            consumer.accept(new Vec3d(x, (double)blockPos.getY() + d, z), warmup, yaw);
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
