package net.soulsweaponry.datagen.tags;

import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.ItemTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModItemTagsProvider extends ItemTagProvider {

    public static final List<Supplier<? extends Item>> SWORDS = new ArrayList<>();
    public static final List<Supplier<? extends Item>> BOWS = new ArrayList<>();
    public static final List<Supplier<? extends Item>> CROSSBOWS = new ArrayList<>();
    public static final List<Supplier<? extends Item>> AXES = new ArrayList<>();
    public static final List<Supplier<? extends Item>> ARMORS = new ArrayList<>();

    public static final List<Supplier<? extends Item>> HEAVY_WEAPONS = new ArrayList<>();
    public static final List<Supplier<? extends Item>> SOUL_HARVESTING_WEAPONS = new ArrayList<>();

    public ModItemTagsProvider(DataOutput arg, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, CompletableFuture<TagLookup<Block>> completableFuture2, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, completableFuture2, SoulsWeaponry.ModId, existingFileHelper);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        this.getOrCreateTagBuilder(ModTags.Items.GUNS)
                .add(GunRegistry.BLUNDERBUSS.get())
                .add(GunRegistry.GATLING_GUN.get())
                .add(GunRegistry.HUNTER_CANNON.get())
                .add(GunRegistry.HUNTER_PISTOL.get());

        this.getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(ItemRegistry.MOONSTONE_PICKAXE.get());

        this.getOrCreateTagBuilder(ItemTags.HOES)
                .add(ItemRegistry.MOONSTONE_HOE.get());

        this.getOrCreateTagBuilder(ItemTags.SHOVELS)
                .add(ItemRegistry.MOONSTONE_SHOVEL.get());

        this.getOrCreateTagBuilder(ItemTags.AXES)
                .add(AXES.stream().map(Supplier::get).toArray(Item[]::new))
                .add(ItemRegistry.MOONSTONE_AXE.get());

        this.getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(SWORDS.stream().map(Supplier::get).toArray(Item[]::new));

        this.getOrCreateTagBuilder(Tags.Items.TOOLS_BOWS)
                .add(BOWS.stream().map(Supplier::get).toArray(Item[]::new));

        this.getOrCreateTagBuilder(ModTags.Items.HEAVY_WEAPONS)
                .add(HEAVY_WEAPONS.stream().map(Supplier::get).toArray(Item[]::new));

        this.getOrCreateTagBuilder(ModTags.Items.SCYTHES)
                .add(WeaponRegistry.DARKIN_SCYTHE_PRE.get())
                .add(WeaponRegistry.DARKIN_SCYTHE_PRIME.get())
                .add(WeaponRegistry.SHADOW_ASSASSIN_SCYTHE.get())
                .add(WeaponRegistry.SOUL_REAPER.get())
                .add(WeaponRegistry.FORLORN_SCYTHE.get());

        this.getOrCreateTagBuilder(ModTags.Items.SUMMONABLE_WEAPONS)
                .add(WeaponRegistry.FREYR_SWORD.get());

        this.getOrCreateTagBuilder(ModTags.Items.STAVES)
                .add(WeaponRegistry.DRAGON_STAFF.get())
                .add(WeaponRegistry.WITHERED_WABBAJACK.get());

        this.getOrCreateTagBuilder(ModTags.Items.UPGRADABLE_MOONLIGHT_SWORDS)
                .add(WeaponRegistry.MOONLIGHT_GREATSWORD.get())
                .add(WeaponRegistry.MOONLIGHT_SHORTSWORD.get());

        this.getOrCreateTagBuilder(ModTags.Items.SOUL_HARVESTING_WEAPONS)
                .add(SOUL_HARVESTING_WEAPONS.stream().map(Supplier::get).toArray(Item[]::new));

        this.getOrCreateTagBuilder(ModTags.Items.DRAGONBANE_MATERIAL)
                .add(Items.NETHERITE_INGOT)
                .addOptional(new Identifier("iceandfire", "dragonbone"));

        this.getOrCreateTagBuilder(ModTags.Items.DUO_BOSS_SOULS)
                .add(ItemRegistry.LORD_SOUL_DAY_STALKER.get())
                .add(ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get());

        this.getOrCreateTagBuilder(ModTags.Items.LORD_SOUL)
                .add(ItemRegistry.LORD_SOUL_VOID.get())
                .add(ItemRegistry.LORD_SOUL_DARK.get())
                .add(ItemRegistry.LORD_SOUL_RED.get())
                .add(ItemRegistry.LORD_SOUL_PURPLE.get())
                .add(ItemRegistry.LORD_SOUL_ROSE.get())
                .add(ItemRegistry.LORD_SOUL_WHITE.get())
                .add(ItemRegistry.LORD_SOUL_NIGHT_PROWLER.get())
                .add(ItemRegistry.LORD_SOUL_DAY_STALKER.get());

        this.getOrCreateTagBuilder(ModTags.Items.LOST_SOUL)
                .add(ItemRegistry.LOST_SOUL.get())
                .addOptional(new Identifier("terramity", "lost_soul"));

        this.getOrCreateTagBuilder(ModTags.Items.DEMON_HEARTS)
                .add(ItemRegistry.DEMON_HEART.get())
                .addOptional(new Identifier("bewitchment", "demon_heart"));

        this.getOrCreateTagBuilder(ModTags.Items.LOST_SOUL_REPAIR)
                .add(ItemRegistry.SOUL_INGOT.get())
                .addTag(ModTags.Items.LOST_SOUL);

        this.getOrCreateTagBuilder(ModTags.Items.TRICK_WEAPONS)
                .add(WeaponRegistry.KIRKHAMMER.get())
                .add(WeaponRegistry.HOLY_MOONLIGHT_GREATSWORD.get())
                .add(WeaponRegistry.HOLY_MOONLIGHT_SWORD.get())
                .add(WeaponRegistry.SILVER_SWORD.get())
                .add(WeaponRegistry.SIMONS_BOWBLADE.get())
                .add(WeaponRegistry.SIMONS_BLADE.get())
                .add(WeaponRegistry.HOLY_GREATSWORD.get());

        this.getOrCreateTagBuilder(ItemTags.MUSIC_DISCS)
                .add(ItemRegistry.CHUNGUS_DISC.get())
                .add(ItemRegistry.DRAUGR_BOSS_DISC.get())
                .add(ItemRegistry.FALLEN_ICON_DISC.get());

        this.getOrCreateTagBuilder(ModTags.Items.STICKS)
                .add(Items.STICK);

        this.getOrCreateTagBuilder(ModTags.Items.MELEE_ITEM_UPGRADABLES)
                .addTag(ItemTags.SWORDS)
                .addTag(Tags.Items.TOOLS_TRIDENTS);

        this.getOrCreateTagBuilder(ModTags.Items.RANGED_ITEM_UPGRADABLES)
                .addTag(Tags.Items.TOOLS_BOWS)
                .addTag(Tags.Items.TOOLS_CROSSBOWS)
                .add(CROSSBOWS.stream().map(Supplier::get).toArray(Item[]::new));

        this.getOrCreateTagBuilder(ModTags.Items.HEAD_ARMOR)
                .add(Items.CHAINMAIL_HELMET)
                .add(Items.DIAMOND_HELMET)
                .add(Items.GOLDEN_HELMET)
                .add(Items.IRON_HELMET)
                .add(Items.LEATHER_HELMET)
                .add(Items.NETHERITE_HELMET)
                .add(Items.TURTLE_HELMET)
                .add(ArmorRegistry.CHAOS_CROWN.get())
                .add(ArmorRegistry.CHAOS_HELMET.get())
                .add(ArmorRegistry.FORLORN_HELMET.get())
                .add(ArmorRegistry.SOUL_INGOT_HELMET.get())
                .add(ArmorRegistry.SOUL_ROBES_HELMET.get());
        this.getOrCreateTagBuilder(Tags.Items.ARMORS_HELMETS).addTag(ModTags.Items.HEAD_ARMOR);

        this.getOrCreateTagBuilder(ModTags.Items.CHEST_ARMOR)
                .add(Items.CHAINMAIL_CHESTPLATE)
                .add(Items.DIAMOND_CHESTPLATE)
                .add(Items.GOLDEN_CHESTPLATE)
                .add(Items.IRON_CHESTPLATE)
                .add(Items.LEATHER_CHESTPLATE)
                .add(Items.NETHERITE_CHESTPLATE)
                .add(ArmorRegistry.ARKENPLATE.get())
                .add(ArmorRegistry.CHAOS_ROBES.get())
                .add(ArmorRegistry.ENHANCED_ARKENPLATE.get())
                .add(ArmorRegistry.ENHANCED_WITHERED_CHEST.get())
                .add(ArmorRegistry.WITHERED_CHEST.get())
                .add(ArmorRegistry.FORLORN_CHESTPLATE.get())
                .add(ArmorRegistry.SOUL_INGOT_CHESTPLATE.get())
                .add(ArmorRegistry.SOUL_ROBES_CHESTPLATE.get());
        this.getOrCreateTagBuilder(Tags.Items.ARMORS_CHESTPLATES).addTag(ModTags.Items.CHEST_ARMOR);

        this.getOrCreateTagBuilder(ModTags.Items.LEG_ARMOR)
                .add(Items.CHAINMAIL_LEGGINGS)
                .add(Items.DIAMOND_LEGGINGS)
                .add(Items.GOLDEN_LEGGINGS)
                .add(Items.IRON_LEGGINGS)
                .add(Items.LEATHER_LEGGINGS)
                .add(Items.NETHERITE_LEGGINGS)
                .add(ArmorRegistry.FORLORN_LEGGINGS.get())
                .add(ArmorRegistry.SOUL_INGOT_LEGGINGS.get())
                .add(ArmorRegistry.SOUL_ROBES_LEGGINGS.get());
        this.getOrCreateTagBuilder(Tags.Items.ARMORS_LEGGINGS).addTag(ModTags.Items.LEG_ARMOR);

        this.getOrCreateTagBuilder(ModTags.Items.FOOT_ARMOR)
                .add(Items.CHAINMAIL_BOOTS)
                .add(Items.DIAMOND_BOOTS)
                .add(Items.GOLDEN_BOOTS)
                .add(Items.IRON_BOOTS)
                .add(Items.LEATHER_BOOTS)
                .add(Items.NETHERITE_BOOTS)
                .add(ArmorRegistry.FORLORN_BOOTS.get())
                .add(ArmorRegistry.SOUL_INGOT_BOOTS.get())
                .add(ArmorRegistry.SOUL_ROBES_BOOTS.get());
        this.getOrCreateTagBuilder(Tags.Items.ARMORS_BOOTS).addTag(ModTags.Items.FOOT_ARMOR);
    }
}
