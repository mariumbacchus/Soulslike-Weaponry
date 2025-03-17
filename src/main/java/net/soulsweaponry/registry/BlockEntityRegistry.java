package net.soulsweaponry.registry;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.blocks.entity.CrimsonObsidianBlockEntity;

import java.util.function.Supplier;

public class BlockEntityRegistry {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SoulsWeaponry.ModId);

    public static final RegistryObject<BlockEntityType<CrimsonObsidianBlockEntity>> CRIMSON_OBSIDIAN_BLOCK_ENTITY = registerBlockEntity("crimson_obsidian_block_entity",
            () -> BlockEntityType.Builder.create(CrimsonObsidianBlockEntity::new, BlockRegistry.CRIMSON_OBSIDIAN.get()).build(null));

    public static <I extends BlockEntityType<?>> RegistryObject<I> registerBlockEntity(String name, Supplier<I> block) {
        return BLOCK_ENTITY.register(name, block);
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY.register(eventBus);
    }
}
