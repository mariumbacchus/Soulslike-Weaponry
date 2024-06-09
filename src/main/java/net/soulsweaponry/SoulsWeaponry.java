package net.soulsweaponry;

import com.mojang.logging.LogUtils;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.world.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.entity.mobs.DarkSorcerer;
import net.soulsweaponry.entity.mobs.EvilForlorn;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.util.BetterBrewingRecipe;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod(SoulsWeaponry.ModId)
public class SoulsWeaponry {

    public static final String ModId = "soulsweapons";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final ItemGroup MAIN_GROUP = new ItemGroup(ModId + ".general") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ItemRegistry.MOONSTONE.get());
        }
    };

    public SoulsWeaponry() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        GeckoLib.initialize();

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
        //TODO armor rendering
        eventBus.addListener(this::setup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC, "soulsweapons-common-forge.toml");

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();

            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, BlockRegistry.HYDRANGEA.get().asItem(), EffectRegistry.WARDING.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, BlockRegistry.OLEANDER.get().asItem(), EffectRegistry.TAINTED_AMBROSIA.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(EffectRegistry.WARDING.get(), Items.GLOWSTONE_DUST, EffectRegistry.STRONG_WARDING.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(EffectRegistry.WARDING.get(), Items.REDSTONE, EffectRegistry.LONG_WARDING.get()));

            SpawnRestriction.register(EntityRegistry.WITHERED_DEMON.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
            SpawnRestriction.register(EntityRegistry.BIG_CHUNGUS.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
            SpawnRestriction.register(EntityRegistry.EVIL_FORLORN.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EvilForlorn::canSpawn);
            SpawnRestriction.register(EntityRegistry.DARK_SORCERER.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DarkSorcerer::canSpawn);
        });
    }
}
