package net.soulsweaponry.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.structure.Structure;
import net.soulsweaponry.util.ModTags;

import java.util.Optional;

public class BossCompass extends Item {

    public BossCompass(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClient && entity.age % 40 == 0) {
            this.updatePos((ServerWorld)world, entity.getBlockPos(), stack);
        }
    }

    public void updatePos(ServerWorld world, BlockPos center, ItemStack stack) {
        Optional<RegistryEntryList.Named<Structure>> optional;
        if (world.getDimensionKey() == DimensionTypes.THE_NETHER) {
            optional = world.getRegistryManager().get(RegistryKeys.STRUCTURE).getEntryList(ModTags.Structures.DECAYING_KINGDOM);
        } else if (world.getDimensionKey() == DimensionTypes.OVERWORLD) {
            optional = world.getRegistryManager().get(RegistryKeys.STRUCTURE).getEntryList(ModTags.Structures.CHAMPIONS_GRAVES);
        } else {
            optional = Optional.empty();
        }
        if (optional.isPresent()) {
            Pair<BlockPos, RegistryEntry<Structure>> pair = world.getChunkManager().getChunkGenerator().locateStructure(world, optional.get(), center, 100, false);
            if (stack.getOrCreateNbt() != null) {
                if (pair != null) {
                    stack.getNbt().put("structurePos", NbtHelper.fromBlockPos(pair.getFirst()));
                }
            }
        }
        //structurePos = world.locateStructure(ModTags.Structures.DECAYING_KINGDOM, center, 100, false);
    }
    
    public GlobalPos getStructurePos(World world, ItemStack stack) {
        if (stack.hasNbt()) {
            return GlobalPos.create(world.getRegistryKey(), NbtHelper.toBlockPos(stack.getNbt().getCompound("structurePos")));
        } else {
            return null;
        }
    }
}
