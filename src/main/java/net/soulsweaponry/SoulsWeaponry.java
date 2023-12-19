package net.soulsweaponry;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.soulsweaponry.client.renderer.armor.ChaosSetRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.items.ChaosSet;
import net.soulsweaponry.registry.*;
import net.soulsweaponry.util.BetterBrewingRecipe;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod(SoulsWeaponry.MOD_ID)
public class SoulsWeaponry {

    public static final String MOD_ID = "soulsweapons";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CreativeModeTab MAIN_GROUP = new CreativeModeTab(MOD_ID + ".general") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.MOONSTONE.get());
        }
    };

    public SoulsWeaponry() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BlockRegistry.register(eventBus);
        ItemRegistry.register(eventBus);
        EffectRegistry.register(eventBus);
        SoundRegistry.register(eventBus);
        ParticleRegistry.register(eventBus);

        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        GeckoLib.initialize();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC, "soulsweapons-common-forge.toml");

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.ALTAR_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.BLACKSTONE_PEDESTAL.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.CHUNGUS_MONOLITH.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.VERGLAS_BLOCK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WITHERED_GRASS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WITHERED_BERRY_BUSH.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WITHERED_FERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WITHERED_LARGE_FERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.WITHERED_TALL_GRASS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.OLEANDER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BlockRegistry.HYDRANGEA.get(), RenderType.cutout());

        GeoArmorRenderer.registerArmorRenderer(ChaosSet.class, ChaosSetRenderer::new);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, BlockRegistry.HYDRANGEA.get().asItem(), EffectRegistry.WARDING.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, BlockRegistry.OLEANDER.get().asItem(), EffectRegistry.TAINTED_AMBROSIA.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(EffectRegistry.WARDING.get(), Items.GLOWSTONE_DUST, EffectRegistry.STRONG_WARDING.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(EffectRegistry.WARDING.get(), Items.REDSTONE, EffectRegistry.LONG_WARDING.get()));
        });
    }
}
