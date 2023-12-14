package net.soulsweapons;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.soulsweapons.registry.ItemRegistry;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SoulsWeaponry.MOD_ID)
public class SoulsWeaponry {

    public static final String MOD_ID = "soulsweapons";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final CreativeModeTab MAIN_GROUP = new CreativeModeTab("general") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemRegistry.MOONSTONE.get());
        }
    };

    public SoulsWeaponry() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemRegistry.register(eventBus);

        eventBus.addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {

    }
}
