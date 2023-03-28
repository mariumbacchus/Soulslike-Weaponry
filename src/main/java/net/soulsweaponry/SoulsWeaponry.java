package net.soulsweaponry;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.config.MidnightConfig;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.EventRegistry;
import net.soulsweaponry.registry.GunRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.EnchantRegistry;
import net.soulsweaponry.registry.OreSpawnRegistry;
import net.soulsweaponry.registry.PacketsServer;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.registry.RecipeRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.SpawnInit;
import net.soulsweaponry.registry.WeaponRegistry;
import software.bernie.geckolib3.GeckoLib;

public class SoulsWeaponry implements ModInitializer {

    public static final String ModId = "soulsweapons";
    public static final Logger LOGGER = LoggerFactory.getLogger("Soulslike Weaponry");
    public static final ItemGroup MAIN_GROUP = FabricItemGroupBuilder.build(new Identifier(ModId, "general"), () -> new ItemStack(ItemRegistry.MOONSTONE));

    @Override
    public void onInitialize() {
        MidnightConfig.init(ModId, ConfigConstructor.class);
        LOGGER.info("Config initialized!");
        BlockRegistry.init();
        ItemRegistry.init();
        EffectRegistry.init();
        EnchantRegistry.init();
        EntityRegistry.init();
        EventRegistry.init();
        SoundRegistry.init();
        SpawnInit.init();
        WeaponRegistry.init();
        ArmorRegistry.init();
        GunRegistry.init();
        OreSpawnRegistry.init();
        LOGGER.info("Successfully registered SoulsWeapons content!");
        GeckoLib.initialize();
        LOGGER.info("Successfully initialized Geckolib!");
        RecipeRegistry.init();
        LOGGER.info("Successfully registered recipes!");
        PacketsServer.initServer();
        ParticleRegistry.init();

        FabricLoader.getInstance().getModContainer(ModId).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(ModId, "2d_weapons"), modContainer, "2D Weapon Models", ResourcePackActivationType.NORMAL);
            LOGGER.info("Successfully registered built-in 2D model resourcepack!");
        });

        LOGGER.info("Initializing done!");
    }
}
