package net.soulsweaponry;

import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.world.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.config.ChungusTonicWhitelist;
import net.soulsweaponry.config.ClientConfig;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.config.MidnightConfig;
import net.soulsweaponry.entity.mobs.BigChungus;
import net.soulsweaponry.entity.mobs.DarkSorcerer;
import net.soulsweaponry.entity.mobs.EvilForlorn;
import net.soulsweaponry.items.staff.WitheredWabbajack;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.util.BetterBrewingRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.GeckoLib;

import java.util.ArrayList;
import java.util.List;

@Mod(SoulsWeaponry.ModId)
public class SoulsWeaponry {

    public static final String ModId = "soulsweapons";
    public static final String CONFIG_FOLDER = "soulsweapons/";
    public static final Logger LOGGER = LoggerFactory.getLogger("Soulslike Weaponry");
    public static final List<RegistryObject<? extends Item>> ITEM_GROUP_LIST = new ArrayList<>();

    public SoulsWeaponry() {
        long start = System.currentTimeMillis();
        MidnightConfig.init(CONFIG_FOLDER + ModId, ConfigConstructor.class);
        MidnightConfig.init(CONFIG_FOLDER + "soulsweapons_chungus_tonic_whitelist", ChungusTonicWhitelist.class);
        MidnightConfig.init(CONFIG_FOLDER + ModId + "_client", ClientConfig.class);
        LOGGER.info("Config initialized!");

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        GeckoLib.initialize();
        LOGGER.info("Successfully initialized Geckolib!");

        AttributeRegistry.register(eventBus);
        ItemGroupRegistry.register(eventBus);
        SoundRegistry.register(eventBus);
        ParticleRegistry.register(eventBus);
        EffectRegistry.registerEffects(eventBus);
        EffectRegistry.registerPotions(eventBus);
        EnchantRegistry.register(eventBus);
        BlockRegistry.register(eventBus);
        ArmorRegistry.register();
        WeaponRegistry.register();
        GunRegistry.register();
        EntityRegistry.register(eventBus);
        ItemRegistry.register(eventBus);
        FluidRegistry.register(eventBus);
        FluidTypeRegistry.register(eventBus);
        BlockEntityRegistry.register(eventBus);

        eventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);

        long end = System.currentTimeMillis();
        LOGGER.info("Initializing done, time taken: " + (end - start) + "ms");
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FluidRegistry.registerCauldronBehavior();

            BetterBrewingRecipe.addAwkwardRecipe(BlockRegistry.HYDRANGEA.get().asItem(), EffectRegistry.WARDING.get());
            BetterBrewingRecipe.addAwkwardRecipe(BlockRegistry.OLEANDER.get().asItem(), EffectRegistry.TAINTED_AMBROSIA.get());
            BetterBrewingRecipe.addPotionRecipe(EffectRegistry.WARDING.get(), Items.GLOWSTONE_DUST, EffectRegistry.STRONG_WARDING.get());
            BetterBrewingRecipe.addPotionRecipe(EffectRegistry.WARDING.get(), Items.REDSTONE, EffectRegistry.LONG_WARDING.get());
            BetterBrewingRecipe.addAwkwardRecipe(ItemRegistry.CHUNGUS_EMERALD.get(), EffectRegistry.CHUNGUS_TONIC_POTION.get());

            SpawnRestriction.register(EntityRegistry.WITHERED_DEMON.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
            SpawnRestriction.register(EntityRegistry.BIG_CHUNGUS.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BigChungus::canSpawnInDark);
            SpawnRestriction.register(EntityRegistry.EVIL_FORLORN.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EvilForlorn::canSpawn);
            SpawnRestriction.register(EntityRegistry.DARK_SORCERER.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DarkSorcerer::canSpawn);

            WitheredWabbajack.initProjectileList();
        });
    }
}
