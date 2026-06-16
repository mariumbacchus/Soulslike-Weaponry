package net.soulsweaponry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.soulsweaponry.api.entitystats.EntityStatsUtil;
import net.soulsweaponry.api.trickweapon.TrickWeaponUtil;
import net.soulsweaponry.config.*;
import net.soulsweaponry.networking.PacketReceiverRegistry;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.world.gen.WorldGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SoulsWeaponry implements ModInitializer {

    public static final String ModId = "soulsweapons";
    public static final String CONFIG_FOLDER = "soulsweapons/";
    public static final Logger LOGGER = LoggerFactory.getLogger("Soulslike Weaponry");
    public static final ArrayList<Item> ITEM_GROUP_LIST = new ArrayList<>();

    @Override
    public void onInitialize() { //TODO add low bleed resistance and stuff to zombies so the stat doesnt go unnoticed
        long start = System.currentTimeMillis();
        MidnightConfig.init(CONFIG_FOLDER + ModId + "_weapon_config", WeaponConfig.class);
        MidnightConfig.init(CONFIG_FOLDER + ModId + "_armor_config", ArmorConfig.class);
        MidnightConfig.init(CONFIG_FOLDER + ModId + "_gun_config", GunConfig.class);
        MidnightConfig.init(CONFIG_FOLDER + ModId + "_enchantment_config", EnchantmentConfig.class);
        MidnightConfig.init(CONFIG_FOLDER + ModId + "_chungus_tonic_whitelist", ChungusTonicWhitelist.class);
        MidnightConfig.init(CONFIG_FOLDER + ModId + "_client", ClientConfig.class);
        MidnightConfig.init(CONFIG_FOLDER + ModId + "_entity_config", EntityConfig.class);
        MidnightConfig.init(CONFIG_FOLDER + ModId + "_entity_stats_config", EntityStatsConfig.class);
        LOGGER.info("Configs initialized!");

        AttributeRegistry.init();
        DamageSourceRegistry.init();
        ComponentRegistry.init();
        BlockRegistry.init();
        ItemRegistry.init();
        FluidRegistry.init();
        FluidRegistry.registerCauldronBehavior();
        EffectRegistry.init();
        EnchantRegistry.init();
        EntityRegistry.init();
        EventRegistry.init();
        ParticleRegistry.init();
        SpawnInit.init();
        WeaponRegistry.init();
        ArmorRegistry.init();
        RecipeSerializerRegistry.init();
        GunRegistry.init();
        WorldGen.generateCustomWorldGen();
        LOGGER.info("Successfully registered SoulsWeapons content!");
        PacketRegistry.registerPackets();
        PacketReceiverRegistry.registerC2SReceivers();

        FabricLoader.getInstance().getModContainer(ModId).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(ModId, "2d_weapons"), modContainer, Text.literal("2D Weapon Models"), ResourcePackActivationType.NORMAL);
            LOGGER.info("Successfully registered built-in 2D model resourcepack!");
        });
        FabricLoader.getInstance().getModContainer(ModId).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(ModId, "legacy_2d"), modContainer, Text.literal("Legacy 2D Models"), ResourcePackActivationType.NORMAL);
            LOGGER.info("Successfully registered built-in Legacy 2D Models resourcepack!");
        });
        FabricLoader.getInstance().getModContainer(ModId).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(ModId, "legacy_3d"), modContainer, Text.literal("Legacy 3D Models"), ResourcePackActivationType.NORMAL);
            LOGGER.info("Successfully registered built-in Legacy 3D Models resourcepack!");
        });
        FabricLoader.getInstance().getModContainer(ModId).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(ModId, "fresh_animations_compat"), modContainer, Text.literal("Fresh Animations Compat."), ResourcePackActivationType.NORMAL);
            LOGGER.info("Successfully registered built-in Fresh Animations Compat. resourcepack!");
        });
        FabricLoader.getInstance().getModContainer(ModId).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(ModId, "enhanced_gow"), modContainer, Text.literal("Szombie's 3D GOW Weapons"), ResourcePackActivationType.DEFAULT_ENABLED);
            LOGGER.info("Successfully registered built-in Szombie's Enhanced 3D GOW Weapons resourcepack!");
        });
        FabricLoader.getInstance().getModContainer(ModId).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(Identifier.of(ModId, "remove_item_shaders"), modContainer, Text.literal("Remove Item Shaders"), ResourcePackActivationType.NORMAL);
            LOGGER.info("Successfully registered built-in Remove Item Shaders resourcepack!");
        });

        DefaultItemComponentEvents.MODIFY.register(context -> context.modify(
                ItemRegistry.FIREPROOF_ITEMS::contains,
                (builder, item) -> builder.add(DataComponentTypes.FIRE_RESISTANT, Unit.INSTANCE)
        ));

        Registry.register(Registries.ITEM_GROUP, Identifier.of(ModId, "general"),
                FabricItemGroup.builder().displayName(Text.translatable("itemGroup.soulsweapons.general"))
                        .icon(() -> new ItemStack(ItemRegistry.MOONSTONE)).entries(((displayContext, entries) -> {
                            for (Item item : ITEM_GROUP_LIST) {
                                entries.add(item);
                            }
                        })).build());

        ServerLifecycleEvents.SERVER_STARTING.register(TrickWeaponUtil::loadMappings);
        EntityStatsUtil.register();

        long end = System.currentTimeMillis();
        LOGGER.info("Initializing done, time taken: " + (end - start) + "ms");
    }
}
