package net.soulsweaponry.util;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;
import net.soulsweaponry.SoulsWeaponry;

@SuppressWarnings("unused")
public class ModTags {

    public static class Blocks {

        private static TagKey<Block> createTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier(SoulsWeaponry.ModId, name));
        }

        private static TagKey<Block> createCommonTag(String name) {
            return TagKey.of(RegistryKeys.BLOCK, new Identifier("c", name));
        }
    }

    public static class Items {
        public static final TagKey<Item> LORD_SOUL = createTag("lord_soul");
        public static final TagKey<Item> DEMON_HEARTS = createCommonTag("demon_hearts");
        public static final TagKey<Item> MOONLIGHT_SWORD = createTag("moonlight_sword");

        public static final TagKey<Item> STICKS = createCommonTag("wood_sticks");
        public static final TagKey<Item> SILVER_INGOTS = createCommonTag("silver_ingots");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(SoulsWeaponry.ModId, name));
        }

        private static TagKey<Item> createCommonTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier("c", name));
        }
    }

    public static class Structures {
        public static final TagKey<Structure> DECAYING_KINGDOM = createTag("decaying_kingdom");
        public static final TagKey<Structure> CHAMPIONS_GRAVES = createTag("champions_graves");

        private static TagKey<Structure> createTag(String id) {
            return TagKey.of(RegistryKeys.STRUCTURE, new Identifier(SoulsWeaponry.ModId, id));
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> SKELETONS = createCommonTag("skeletons");

        private static TagKey<EntityType<?>> createCommonTag(String id) {
            return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("c", id));
        }
    }
}
