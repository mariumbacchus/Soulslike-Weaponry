package net.soulsweaponry.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.recipe.ItemUpgradeRecipe;
import net.soulsweaponry.registry.EnchantRegistry;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class WeaponUtil {

    public static final String ITEM_UPGRADE_LEVEL_KEY = "ItemUpgradeLevel";

    /**
     * Returns the upgrade level of the item. One can upgrade it by mixing the item with
     * a Twinkling Titanite in the Smithing Table with a Netherite Upgrade Template.
     */
    public static int getUpgradeLevel(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return (nbt != null && nbt.contains(ITEM_UPGRADE_LEVEL_KEY, NbtCompound.INT_TYPE)) ? nbt.getInt(ITEM_UPGRADE_LEVEL_KEY) : 0;
    }

    public static void setUpgradeLevel(ItemStack stack, int level) {
        stack.getOrCreateNbt().putInt(ITEM_UPGRADE_LEVEL_KEY, level);
    }

    /**
     * Copy over default item stack components such as enchants, damage or stack size.
     * Also copies over {@link ComponentRegistry#ITEM_UPGRADE_LEVEL} and bonus damage/speed
     * attributes gotten from it.
     * Mainly used in {@link net.soulsweaponry.api.trickweapon.TrickWeaponUtil} and
     * {@link net.soulsweaponry.items.abilities.targetdeath.SoulHarvestTransform}.
     * @param prevStack previous stack to copy from
     * @param newStack new stack to copy to from the prev stack
     */
    public static void copyOverItemComponents(World world, ItemStack prevStack, ItemStack newStack) {
        int lvl = WeaponUtil.getUpgradeLevel(prevStack);
        float nextDamage = WeaponUtil.getBaseAttackDamage(newStack);
        float nextAttackSpeed = WeaponUtil.getBaseAttackSpeed(newStack);
        newStack.applyComponentsFrom(prevStack.getComponents());
        WeaponUtil.modifyStackAttributes(newStack, nextDamage, nextAttackSpeed);
        newStack.set(ComponentRegistry.ITEM_UPGRADE_LEVEL, lvl);
        newStack.setCount(prevStack.getCount());

        // Rebuild the upgrade level bonuses based on the new item type (so ranged bonuses for bows instead of melee, etc.)
        // Ignore if no upgrade recipe is found and keep old attributes
        ItemUpgradeRecipe recipe = UpgradeUtil.findItemUpgradeRecipeForBase(world, newStack);
        if (recipe != null) {
            float primaryPerLevel = recipe.primaryBonus();
            float secondaryPerLevel = recipe.secondaryBonus();
            UpgradeUtil.rebuildUpgradeAttributesForCurrentForm(newStack, lvl, primaryPerLevel, secondaryPerLevel);
        }
    }

    /**
     * Returns level of the damage enchant, for example {@code 5} for Sharpness V or {@code 4} for Smite IV
     */
    public static int getEnchantDamageBonus(ItemStack stack) {
        return getHighestEnchantInTag(stack, EnchantmentTags.DAMAGE_EXCLUSIVE_SET);
    }

    /**
     * Get the highest level out of the enchants the stack has that are within the given tag.
     * For example: searching for enchants within {@code EnchantmentTags.DAMAGE_EXCLUSIVE_SET}
     * will return the highest level of sharpness, smite or whatever damage enchant the item
     * has.
     */
    public static int getHighestEnchantInTag(ItemStack stack, TagKey<Enchantment> tag) {
        return EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries().stream()
                .filter(e -> e.getKey().isIn(tag))
                .mapToInt(Map.Entry::getValue)
                .max()
                .orElse(0);
    }

    /**
     * Gets the level of a specific enchant based on the RegistryKey for simplicity.
     */
    public static int getLevel(ItemStack stack, RegistryKey<Enchantment> enchantKey) {
        Boolean disable = EnchantRegistry.DISABLED_ENCHANTMENTS.get(enchantKey);
        if (ConfigConstructor.disable_all_enchantments || (disable != null && disable)) {
            return 0;
        }
        for (Map.Entry<RegistryEntry<Enchantment>, Integer> e : EnchantmentHelper.getEnchantments(stack).getEnchantmentEntries()) {
            if (e.getKey().getKey().filter(key -> key.equals(enchantKey)).isPresent()) {
                return e.getValue();
            }
        }
        return 0;
    }

    public static void applyEnchantment(World world, ItemStack stack, RegistryKey<Enchantment> enchantKey, int level) {
        var lookup = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
        lookup.getEntry(enchantKey).ifPresentOrElse(
                entry -> stack.addEnchantment(lookup.getEntry(entry.value()), level),
                () -> SoulsWeaponry.LOGGER.warn("Enchantment {} not found when trying to apply to {}", enchantKey, stack)
        );
    }

    public static float getBaseAttackDamage(ItemStack stack) {
        AttributeModifiersComponent src = stack.getItem().getComponents()
                .getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);

        for (var e : src.modifiers()) {
            if (e.slot() == AttributeModifierSlot.MAINHAND
                    && e.attribute().equals(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                    && e.modifier().operation() == EntityAttributeModifier.Operation.ADD_VALUE
                    && BASE_ATTACK_DAMAGE_MODIFIER_ID.equals(e.modifier().id())) {
                return (float) e.modifier().value();
            }
        }
        return 0f;
    }


    public static float getBaseAttackSpeed(ItemStack stack) {
        AttributeModifiersComponent src = stack.getItem().getComponents()
                .getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);

        for (var e : src.modifiers()) {
            if (e.slot() == AttributeModifierSlot.MAINHAND
                    && e.attribute().equals(EntityAttributes.GENERIC_ATTACK_SPEED)
                    && e.modifier().operation() == EntityAttributeModifier.Operation.ADD_VALUE
                    && BASE_ATTACK_SPEED_MODIFIER_ID.equals(e.modifier().id())) {
                return (float) e.modifier().value();
            }
        }
        return 0f;
    }

    /**
     * Override the damage and attack speed inside the {@code DataComponentTypes.ATTRIBUTE_MODIFIERS} component
     * while keeping all other attributes as is. Call this in a tick method to update dynamically.
     * @param stack item stack
     * @param damage damage
     * @param attackSpeed attack speed, this is not pre-calculated so you need to enter {@code - (4f - 1.6f)}
     *                    if you want 1.6 in attack speed as a result
     */
    public static void modifyStackAttributes(ItemStack stack, float damage, float attackSpeed) {
        AttributeModifiersComponent source = stack.contains(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                ? stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                : stack.getItem().getComponents().getOrDefault(
                DataComponentTypes.ATTRIBUTE_MODIFIERS,
                AttributeModifiersComponent.DEFAULT
        );

        AttributeModifiersComponent.Builder b = AttributeModifiersComponent.builder();

        // copy everything EXCEPT the two base MAINHAND rows we want to replace
        for (AttributeModifiersComponent.Entry e : source.modifiers()) {
            Identifier id = e.modifier().id();
            boolean isMainhand = e.slot() == AttributeModifierSlot.MAINHAND;

            boolean isBaseDamage = isMainhand
                    && e.attribute().equals(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                    && BASE_ATTACK_DAMAGE_MODIFIER_ID.equals(id);

            boolean isBaseSpeed = isMainhand
                    && e.attribute().equals(EntityAttributes.GENERIC_ATTACK_SPEED)
                    && BASE_ATTACK_SPEED_MODIFIER_ID.equals(id);

            if (isBaseDamage || isBaseSpeed) continue; // drop those, replace later instead

            b.add(e.attribute(), e.modifier(), e.slot());
        }

        // re-add the base rows with the dynamic values
        b.add(
                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(BASE_ATTACK_DAMAGE_MODIFIER_ID, damage, EntityAttributeModifier.Operation.ADD_VALUE),
                AttributeModifierSlot.MAINHAND
        );
        b.add(
                EntityAttributes.GENERIC_ATTACK_SPEED,
                new EntityAttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, EntityAttributeModifier.Operation.ADD_VALUE),
                AttributeModifierSlot.MAINHAND
        );

        stack.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, b.build());
    }

    public static EquipmentSlot getActiveHandSlot(PlayerEntity player) {
        return LivingEntity.getSlotForHand(player.getActiveHand());
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
    public static EntityAttributeModifier makeAttribute(RegistryEntry<EntityAttribute> attr, EquipmentSlot slot, float amount) {
        return makeAttribute(attr, slot.getName().toLowerCase(), amount);
    }

    /**
     * Helper method to make attributes with the identifier being a combination of the mod id, attribute id and custom name.
     * Add an Operation parameter to replace ADDITION later if you feel like it.
     */
    @Nullable
    public static EntityAttributeModifier makeAttribute(RegistryEntry<EntityAttribute> attr, String name, float amount) {
        // Don't display attributes with 0
        if (amount == 0) {
            return null;
        }
        // e.g. "soulsweapons:bleed_buildup:chungus"
        // Any non [a-z0-9/._-] character will be replaced with "-" to satisfy Identifier class (Looking at you, Mjölnir)
        Identifier id = Identifier.of(SoulsWeaponry.ModId, String.format("%s.%s", attr.value().getTranslationKey(), name));
        return new EntityAttributeModifier(id, amount, EntityAttributeModifier.Operation.ADD_VALUE);
    }

    /**
     * Helper method to make attributes with the identifier being a combination of the mod id, attribute id and equipment slot.
     * This takes in an array of doubles that is used to map the values to the armor equipment slot (head to feet).
     * No values beyond the 4th (3) index will be used.
     * Add an Operation parameter to replace ADDITION later if you feel like it.
     */
    @Nullable
    public static EntityAttributeModifier makeAttribute(RegistryEntry<EntityAttribute> attr, EquipmentSlot slot, float[] perSlotValues) {
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
            Identifier entityId = Identifier.of(str.contains(":") ? str : "minecraft:" + str);
            return Registries.ENTITY_TYPE.get(entityId);
        }).collect(Collectors.toList());
    }

    /**
     * Create a new builder with all the attributes from the previous component {@code toCopyFrom}.
     * Should usually take in {@code super.getAttributeModifiers()} from {@link ArmorItem#getAttributeModifiers}
     * when it comes to armor items.
     */
    public static AttributeModifiersComponent.Builder createAndCopyAttributes(AttributeModifiersComponent toCopyFrom) {
        AttributeModifiersComponent.Builder builder = AttributeModifiersComponent.builder();
        for (AttributeModifiersComponent.Entry e : toCopyFrom.modifiers()) {
            builder.add(e.attribute(), e.modifier(), e.slot());
        }
        return builder;
    }
}