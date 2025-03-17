package net.soulsweaponry.registry;

import net.minecraft.util.Identifier;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;

import java.util.function.Consumer;

public class FluidTypeRegistry {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, SoulsWeaponry.ModId);

    public static final RegistryObject<FluidType> PURIFIED_BLOOD_FLUID_TYPE = FLUID_TYPES.register("purified_blood", () ->
            new FluidType(FluidType.Properties.create()
                    .lightLevel(10).canDrown(false)
            ) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        @Override
                        public Identifier getStillTexture() {
                            return new Identifier(SoulsWeaponry.ModId, "block/purified_blood_still");
                        }
                        @Override
                        public Identifier getFlowingTexture() {
                            return new Identifier(SoulsWeaponry.ModId, "block/purified_blood_flow");
                        }
                    });
                }
            }
    );

    public static void register(IEventBus bus) {
        FLUID_TYPES.register(bus);
    }
}
