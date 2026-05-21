package net.soulsweaponry.util;

import com.google.common.collect.Multimap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.mixin.ItemAccessor;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WeaponUtil {

    public static final String DAMAGE_KEY = "CustomDamage";
    public static final String SPEED_KEY = "CustomSpeed";

    /**
     * Returns the upgrade level of the item. One can upgrade it by mixing the item with
     * a Twinkling Titanite in the Smithing Table with a Netherite Upgrade Template.
     */
    public static int getUpgradeLevel(ItemStack stack) {
        return NbtHelper.getInt(stack, NbtIds.ITEM_UPGRADE_LEVEL, 0);
    }

    public static void setUpgradeLevel(ItemStack stack, int level) {
        NbtHelper.putInt(stack, NbtIds.ITEM_UPGRADE_LEVEL, level);
    }

    /**
     * Copy over default item stack nbt values such as enchants, damage or stack size.
     * Also copies over item upgrade level and bonus damage/speed
     * attributes gotten from it.
     * Mainly used in {@link net.soulsweaponry.api.trickweapon.TrickWeaponUtil} and
     * {@link net.soulsweaponry.items.abilities.targetdeath.SoulHarvestTransform}.
     * @param prevStack previous stack to copy from
     * @param newStack new stack to copy to from the prev stack
     */
    public static void copyOverItemComponents(World world, ItemStack prevStack, ItemStack newStack) {
        int lvl = WeaponUtil.getUpgradeLevel(prevStack);
        newStack.setCount(prevStack.getCount());
        if (prevStack.hasNbt()) {
            newStack.setNbt(prevStack.getNbt().copy());
        }
        WeaponUtil.setUpgradeLevel(newStack, lvl);

        // Recalculate upgrade scaling for the new weapon type
        ItemUpgradeRecipe recipe = UpgradeUtil.findItemUpgradeRecipeForBase(newStack);
        if (recipe != null) {
            float primaryPerLevel = recipe.primaryBonus();
            float secondaryPerLevel = recipe.secondaryBonus();
            NbtHelper.putFloat(newStack, NbtIds.UPGRADE_PRIMARY, primaryPerLevel);
            NbtHelper.putFloat(newStack, NbtIds.UPGRADE_SECONDARY, secondaryPerLevel);
        }
        // Reset dynamic attributes so they rebuild properly
        WeaponUtil.modifyStackAttributes(
                newStack,
                WeaponUtil.getBaseItemAttackDamage(newStack),
                WeaponUtil.getBaseItemAttackSpeed(newStack)
        );
    }

    /**
     * Returns level of the damage enchant, for example {@code 5} for Sharpness V or {@code 4} for Smite IV
     */
    public static int getEnchantDamageBonus(ItemStack stack) {
        return getHighestEnchantInTag(stack, ModTags.Enchantments.DAMAGE_ENCHANTMENTS);
    }

    /**
     * Get the highest level out of the enchants the stack has that are within the given tag.
     * For example: searching for enchants within {@code EnchantmentTags.DAMAGE_EXCLUSIVE_SET}
     * will return the highest level of sharpness, smite or whatever damage enchant the item
     * has.
     */
    public static int getHighestEnchantInTag(ItemStack stack, TagKey<Enchantment> tag) {
        Map<Enchantment, Integer> enchants = EnchantmentHelper.get(stack);
        int max = 0;
        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            if (Registries.ENCHANTMENT.getEntry(entry.getKey()).isIn(tag)) {
                max = Math.max(max, entry.getValue());
            }
        }
        return max;
    }

    /**
     * Returns whether the stack has modified damage or attack speed thanks to abilities.
     */
    public static boolean hasModifiedAttributes(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().contains(DAMAGE_KEY) && stack.getNbt().contains(SPEED_KEY);
    }

    /**
     * Gets the attack damage saved on the nbt to the stack, often times from abilities.
     */
    public static double getStackAttackDamage(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().contains(DAMAGE_KEY) ? stack.getNbt().getDouble(DAMAGE_KEY) : getBaseItemAttackDamage(stack);
    }

    /**
     * Gets the attack speed saved on the nbt to the stack, often times from abilities.
     */
    public static double getStackAttackSpeed(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().contains(SPEED_KEY) ? stack.getNbt().getDouble(SPEED_KEY) : getBaseItemAttackSpeed(stack);
    }

    public static double getBaseItemAttackDamage(ItemStack stack) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = stack.getItem().getAttributeModifiers(stack, EquipmentSlot.MAINHAND);
        for (var mod : map.get(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
            if (mod.getId().equals(ItemAccessor.getAttackDamageModifierId())) {
                return mod.getValue();
            }
        }
        return 0;
    }

    public static double getBaseItemAttackSpeed(ItemStack stack) {
        Multimap<EntityAttribute, EntityAttributeModifier> map = stack.getItem().getAttributeModifiers(stack, EquipmentSlot.MAINHAND);
        for (var mod : map.get(EntityAttributes.GENERIC_ATTACK_SPEED)) {
            if (mod.getId().equals(ItemAccessor.getAttackSpeedModifierId())) {
                return mod.getValue();
            }
        }
        return 0;
    }

    /**
     * Override the damage and attack speed of the item. Call this for consistency between versions.
     * Saves values to a custom nbt that is applied in a mixin for {@link net.minecraft.item.Item#getAttributeModifiers(ItemStack, EquipmentSlot)}.
     * @param stack item stack
     * @param damage damage
     * @param attackSpeed attack speed, this is not pre-calculated so you need to enter {@code - (4f - 1.6f)}
     *                    if you want 1.6 in attack speed as a result
     */
    public static void modifyStackAttributes(ItemStack stack, double damage, double attackSpeed) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putDouble(DAMAGE_KEY, damage);
        nbt.putDouble(SPEED_KEY, attackSpeed);
    }

    public static Consumer<LivingEntity> getActiveHandSlot(LivingEntity user) {
        return p -> p.sendToolBreakStatus(user.getActiveHand());
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
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

    public static int getChargeTime(ItemStack stack, int remainingUseTicks) {
        int i;
        if (WeaponUtil.isModLoaded("epicfight")) {
            i = Integer.MAX_VALUE - remainingUseTicks;
        } else {
            i = stack.getItem().getMaxUseTime(stack) - remainingUseTicks;
        }
        return i;
    }

    public static StatusEffect parseStatusEffectId(String statusEffectId) {
        StatusEffect defaultEntry = StatusEffects.HASTE;
        if (statusEffectId == null || statusEffectId.isBlank()) {
            return defaultEntry;
        }
        Identifier directId = Identifier.tryParse(statusEffectId.toLowerCase(Locale.ROOT));
        if (directId != null) {
            StatusEffect eff = Registries.STATUS_EFFECT.get(directId);
            if (eff != null) {
                return eff;
            }
        }
        return defaultEntry;
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

    /**
     * Helper method to launch a target the way it is facing, used mostly
     * by items using Riptide effect such as Comet Spear or Mjölnir.
     * Keep in mind that the world must be client side to apply movement!
     * @param target target to launch
     */
    public static void launchTarget(LivingEntity target, float launchPower, boolean reverse) {
        float f = target.getYaw();
        float g = target.getPitch();
        float rad = 0.017453292F; // pi / 180
        float h = -MathHelper.sin(f * rad) * MathHelper.cos(g * rad);
        float k = -MathHelper.sin(g * rad);
        float l = MathHelper.cos(f * rad) * MathHelper.cos(g * rad);
        float m = MathHelper.sqrt(h * h + k * k + l * l);
        float n = 3.0F * (launchPower / 4.0F);
        h *= n / m;
        k *= n / m;
        l *= n / m;
        if (reverse) {
            h = -h;
            k = -k;
            l = -l;
        }
        target.addVelocity(h, k, l);
    }

    /**
     * Helper method to make attributes with the identifier being a combination of the mod id, attribute id and equipment slot.
     * Add an Operation parameter to replace ADDITION later if you feel like it.
     */
    @Nullable
    public static EntityAttributeModifier makeAttribute(EntityAttribute attr, EquipmentSlot slot, float amount) {
        return makeAttribute(attr, slot.getName().toLowerCase(), amount);
    }

    /**
     * Helper method to make attributes with the identifier being a combination of the mod id, attribute id and custom name.
     * Add an Operation parameter to replace ADDITION later if you feel like it.
     */
    @Nullable
    public static EntityAttributeModifier makeAttribute(EntityAttribute attr, String name, float amount) {
        // Don't display attributes with 0
        if (amount == 0) {
            return null;
        }
        Identifier attrId = Registries.ATTRIBUTE.getId(attr);
        if (attrId == null) {
            return null;
        }
        String id = SoulsWeaponry.ModId + ":" + attrId.getPath() + "." + name.toLowerCase(Locale.ROOT);
        // 1.20.1 checks for UUID so strings won't work alone
        UUID uuid = UUID.nameUUIDFromBytes(id.getBytes(StandardCharsets.UTF_8));
        return new EntityAttributeModifier(uuid, id, amount, EntityAttributeModifier.Operation.ADDITION);
    }

    /**
     * Helper method to make attributes with the identifier being a combination of the mod id, attribute id and equipment slot.
     * This takes in an array of doubles that is used to map the values to the armor equipment slot (head to feet).
     * No values beyond the 4th (3) index will be used.
     * Add an Operation parameter to replace ADDITION later if you feel like it.
     */
    @Nullable
    public static EntityAttributeModifier makeAttribute(EntityAttribute attr, EquipmentSlot slot, float[] perSlotValues) {
        // Minecraft has feet at index 0 so just follow that pattern
        int idx = switch (slot) {
            case HEAD -> 3;
            case CHEST -> 2;
            case LEGS -> 1;
            case FEET -> 0;
            default -> throw new IllegalArgumentException("Unexpected slot " + slot);
        };
        double amount = perSlotValues[idx];
        return makeAttribute(attr, slot, (float) amount);
    }

    /**
     * Returns a list of all the entity types within an array of identifiers as strings, i.e. "minecraft:chicken".
     * Defaults to minecraft:entity if only the entity name/id was written, so if only "chicken" was
     * mentioned, it would return "minecraft:chicken".
     */
    public static List<EntityType<?>> getEntityListOffArray(String[] array) {
        Set<String> stringSet = Set.of(array);
        return stringSet.stream().map((str) -> {
            Identifier entityId = Identifier.tryParse(str.contains(":") ? str : "minecraft:" + str);
            return Registries.ENTITY_TYPE.get(entityId);
        }).collect(Collectors.toList());
    }

    public static boolean hasAnyEnchantmentsIn(ItemStack stack, TagKey<Enchantment> tag) {
        Map<Enchantment, Integer> enchants = EnchantmentHelper.get(stack);
        for (Enchantment ench : enchants.keySet()) {
            if (Registries.ENCHANTMENT.getEntry(ench).isIn(tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Simulate an explosion without breaking blocks or killing item entities. The range, damage and knockback
     * should match as if it was a legit explosion.
     * @param world world
     * @param explosionCauser entity that caused the explosion and will NOT take damage from it
     * @param power power of the explosion
     * @param x x
     * @param y y
     * @param z z
     */
    public static void simulateExplosion(ServerWorld world, Entity explosionCauser, float power, double x, double y, double z) {
        if (power <= 0.0F) {
            return;
        }
        float radius = power * 2.0F;
        Vec3d explosionPos = new Vec3d(x, y, z);
        Box affectedBox = new Box(
                MathHelper.floor(x - radius - 1.0),
                MathHelper.floor(y - radius - 1.0),
                MathHelper.floor(z - radius - 1.0),
                MathHelper.floor(x + radius + 1.0),
                MathHelper.floor(y + radius + 1.0),
                MathHelper.floor(z + radius + 1.0)
        );
        DamageSource damageSource = world.getDamageSources().explosion(explosionCauser, explosionCauser);
        for (Entity target : world.getOtherEntities(explosionCauser, affectedBox)) {
            if (target instanceof ItemEntity || target.isImmuneToExplosion()) {
                continue;
            }

            double distanceRatio = Math.sqrt(target.squaredDistanceTo(explosionPos)) / radius;
            if (distanceRatio > 1.0D) {
                continue;
            }

            double dirX = target.getX() - x;
            double dirY = (target instanceof TntEntity ? target.getY() : target.getEyeY()) - y;
            double dirZ = target.getZ() - z;

            double distance = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
            if (distance == 0.0D) {
                continue;
            }
            dirX /= distance;
            dirY /= distance;
            dirZ /= distance;

            double exposure = Explosion.getExposure(explosionPos, target);
            double impact = (1.0D - distanceRatio) * exposure;

            float damage = (float)((int)((impact * impact + impact) / 2.0D * 7.0D * radius + 1.0D));
            target.damage(damageSource, damage);

            double knockbackStrength;
            if (target instanceof LivingEntity livingTarget) {
                knockbackStrength = ProtectionEnchantment.transformExplosionKnockback(livingTarget, impact);
            } else {
                knockbackStrength = impact;
            }
            Vec3d knockback = new Vec3d(dirX * knockbackStrength, dirY * knockbackStrength, dirZ * knockbackStrength);
            target.setVelocity(target.getVelocity().add(knockback));
            target.velocityModified = true;
        }
        float pitch = (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F;
        world.playSound(null, BlockPos.ofFloored(x, y, z), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 4.0F, pitch);
        world.spawnParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
    }
}