package net.soulsweaponry.items.misc;

import java.util.Optional;

import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.ModTags;

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
        if (world.getRegistryKey() == World.NETHER) {
            optional = world.getRegistryManager().getOrThrow(RegistryKeys.STRUCTURE).getOptional(ModTags.Structures.DECAYING_KINGDOM);
        } else if (world.getRegistryKey() == World.OVERWORLD) {
            optional = world.getRegistryManager().getOrThrow(RegistryKeys.STRUCTURE).getOptional(ModTags.Structures.CHAMPIONS_GRAVES);
        } else {
            optional = Optional.empty();
        }
        if (optional.isPresent()) {
            Pair<BlockPos, RegistryEntry<Structure>> pair = world.getChunkManager().getChunkGenerator().locateStructure(world, optional.get(), center, 100, false);
            if (pair != null) {
                stack.set(ComponentRegistry.SAVED_BLOCK_POS, pair.getFirst());
            }
        }
        //structurePos = world.locateStructure(ModTags.Structures.DECAYING_KINGDOM, center, 100, false);
    }

    public GlobalPos getStructurePos(World world, ItemStack stack) {
        return GlobalPos.create(world.getRegistryKey(), Optional.ofNullable(stack.get(ComponentRegistry.SAVED_BLOCK_POS)).orElse(BlockPos.ORIGIN));
    }
}
