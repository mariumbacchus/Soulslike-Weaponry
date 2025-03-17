package net.soulsweaponry;

import com.mojang.logging.LogUtils;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.config.MidnightConfig;
import net.soulsweaponry.entity.mobs.BigChungus;
import net.soulsweaponry.entity.mobs.DarkSorcerer;
import net.soulsweaponry.entity.mobs.EvilForlorn;
import net.soulsweaponry.items.staff.WitheredWabbajack;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.util.BetterBrewingRecipe;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

import java.util.ArrayList;
import java.util.List;

@Mod(SoulsWeaponry.ModId)
public class SoulsWeaponry {

    public static final String ModId = "soulsweapons";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final List<RegistryObject<? extends Item>> ITEM_GROUP_LIST = new ArrayList<>();

    public SoulsWeaponry() {
        // Forge config is loaded after all other registration, meaning it's useless in my case.
        // ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC, "soulsweapons-common-forge.toml");
        MidnightConfig.init(ModId, ConfigConstructor.class);

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        GeckoLib.initialize();

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
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FluidRegistry.registerCauldronBehavior();

            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, BlockRegistry.HYDRANGEA.get().asItem(), EffectRegistry.WARDING.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, BlockRegistry.OLEANDER.get().asItem(), EffectRegistry.TAINTED_AMBROSIA.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(EffectRegistry.WARDING.get(), Items.GLOWSTONE_DUST, EffectRegistry.STRONG_WARDING.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(EffectRegistry.WARDING.get(), Items.REDSTONE, EffectRegistry.LONG_WARDING.get()));

            BrewingRecipeRegistry.addRecipe(Ingredient.ofItems(Items.POTION), Ingredient.ofItems(ItemRegistry.CHUNGUS_EMERALD.get()), ItemRegistry.CHUNGUS_TONIC_POTION.get().getDefaultStack());
            BrewingRecipeRegistry.addRecipe(Ingredient.ofItems(ItemRegistry.CHUNGUS_TONIC_POTION.get()), Ingredient.ofItems(BlockRegistry.CHUNGUS_EMERALD_BLOCK.get().asItem()), ItemRegistry.CHUNGUS_TONIC_SPLASH.get().getDefaultStack());
            BrewingRecipeRegistry.addRecipe(Ingredient.ofItems(ItemRegistry.CHUNGUS_TONIC_SPLASH.get()), Ingredient.ofItems(Items.DRAGON_BREATH), ItemRegistry.CHUNGUS_TONIC_LINGERING.get().getDefaultStack());

            SpawnRestriction.register(EntityRegistry.WITHERED_DEMON.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
            SpawnRestriction.register(EntityRegistry.BIG_CHUNGUS.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BigChungus::canSpawnInDark);
            SpawnRestriction.register(EntityRegistry.EVIL_FORLORN.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EvilForlorn::canSpawn);
            SpawnRestriction.register(EntityRegistry.DARK_SORCERER.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DarkSorcerer::canSpawn);

            WitheredWabbajack.initProjectileList();
        });
    }

    //TODO:
    // Fix forge specific issues on github
}
