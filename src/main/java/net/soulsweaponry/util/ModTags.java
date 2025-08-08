package net.soulsweaponry.util;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import net.soulsweaponry.SoulsWeaponry;

@SuppressWarnings("unused")
public class ModTags {

    public static class Blocks {

        public static final TagKey<Block> NEEDS_IRON_BLOCK_TOOL = createTag("needs_iron_block_tool");
        public static final TagKey<Block> INCORRECT_FOR_IRON_BLOCK_TOOL = createTag("incorrect_for_iron_block_tool");

        public static final TagKey<Block> NEEDS_LOST_SOUL_TOOL = createTag("needs_lost_soul_tool");
        public static final TagKey<Block> INCORRECT_FOR_LOST_SOUL_TOOL = createTag("incorrect_for_lost_soul_tool");

        public static final TagKey<Block> NEEDS_LOST_SOUL_DURABLE_TOOL = createTag("needs_lost_soul_durable_tool");
        public static final TagKey<Block> INCORRECT_FOR_LOST_SOUL_DURABLE_TOOL = createTag("incorrect_for_lost_soul_durable_tool");

        public static final TagKey<Block> NEEDS_MOONSTONE_OR_VERGLAS_TOOL = createTag("needs_moonstone_or_verglas_tool");
        public static final TagKey<Block> INCORRECT_FOR_MOONSTONE_OR_VERGLAS_TOOL = createTag("incorrect_for_moonstone_or_verglas_tool");

        public static final TagKey<Block> NEEDS_CRIMSON_INGOT_TOOL = createTag("needs_crimson_ingot_tool");
        public static final TagKey<Block> INCORRECT_CRIMSON_INGOT_TOOL = createTag("incorrect_crimson_ingot_tool");

        public static final TagKey<Block> NEEDS_MOONSTONE_TOOL = createTag("needs_moonstone_tool");
        public static final TagKey<Block> INCORRECT_FOR_MOONSTONE_TOOL = createTag("incorrect_for_moonstone_tool");

        public static final TagKey<Block> NEEDS_ECHO_SHARD_TOOL = createTag("needs_echo_shard_tool");
        public static final TagKey<Block> INCORRECT_FOR_ECHO_SHARD_TOOL = createTag("incorrect_for_echo_shard_tool");

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of(SoulsWeaponry.ModId, name));
        }

        private static TagKey<Block> createCommonTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, Identifier.of("c", name));
        }
    }

    public static class Items {
        public static final TagKey<Item> LORD_SOUL = createTag("lord_soul");
        public static final TagKey<Item> DEMON_HEARTS = createCommonTag("demon_hearts");
        public static final TagKey<Item> MOONLIGHT_SWORD = createTag("moonlight_sword");
        public static final TagKey<Item> DUO_BOSS_SOULS = createTag("duo_boss_souls");
        public static final TagKey<Item> TRICK_WEAPONS = createTag("trick_weapons");
        public static final TagKey<Item> GUNS = createTag("guns");
        public static final TagKey<Item> LOST_SOUL_REPAIR = createTag("lost_soul_items_repair_ingredients");
        public static final TagKey<Item> DRAGONBANE_MATERIAL = createTag("dragonbane_material");
        public static final TagKey<Item> GUN_ENCHANTABLE = createTag("enchantable/gun");

        public static final TagKey<Item> STICKS = createCommonTag("wood_sticks");
        public static final TagKey<Item> SILVER_INGOTS = createCommonTag("silver_ingots");
        public static final TagKey<Item> IRON_INGOTS = createCommonTag("iron_ingots");
        public static final TagKey<Item> SHIELDS = createCommonTag("shields");
        public static final TagKey<Item> LOST_SOUL = createCommonTag("lost_soul");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(SoulsWeaponry.ModId, name));
        }

        private static TagKey<Item> createCommonTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of("c", name));
        }
    }

    public static class Structures {
        public static final TagKey<Structure> DECAYING_KINGDOM = createTag("decaying_kingdom");
        public static final TagKey<Structure> CHAMPIONS_GRAVES = createTag("champions_graves");

        private static TagKey<Structure> createTag(String id) {
            return TagKey.of(RegistryKeys.STRUCTURE, Identifier.of(SoulsWeaponry.ModId, id));
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> SKELETONS = createCommonTag("skeletons");
        public static final TagKey<EntityType<?>> RANGED_MOBS = createCommonTag("ranged_mobs");
        public static final TagKey<EntityType<?>> DRAGONS = createCommonTag("dragons");

        private static TagKey<EntityType<?>> createCommonTag(String id) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("c", id));
        }
    }

    public static class Effects {
        public static final TagKey<StatusEffect> DAMAGE_OVER_TIME = createCommonTag("damage_over_time");

        private static TagKey<StatusEffect> createCommonTag(String id) {
            return TagKey.of(RegistryKeys.STATUS_EFFECT, Identifier.of("c", id));
        }
    }

    public static class Enchantments {
        public static final TagKey<Enchantment> BULLET_COLLISION_EXCLUSIVE_SET = createTag("exclusive_set/bullet_collision");
        public static final TagKey<Enchantment> APPLY_FIRE = createTag("apply_fire");
        public static final TagKey<Enchantment> PREVENTS_AMMO_CONSUME = createTag("prevents_ammo_consume");

        private static TagKey<Enchantment> createTag(String id) {
            return TagKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(SoulsWeaponry.ModId, id));
        }
    }
}
