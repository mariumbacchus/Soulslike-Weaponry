package net.soulsweaponry.items;

import java.util.Optional;

import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.soulsweaponry.util.ModTags;

public class BossCompass extends Item {

    public BossCompass(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!world.isClient) {
            this.updatePos((ServerWorld)world, entity.getBlockPos(), stack);
        }
    }

    public void updatePos(ServerWorld world, BlockPos center, ItemStack stack) {
        Optional<RegistryEntryList.Named<ConfiguredStructureFeature<?, ?>>> optional;
        if (world.getDimension().getEffects() == DimensionType.THE_NETHER_ID) {
            optional = world.getRegistryManager().get(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY).getEntryList(ModTags.Structures.DECAYING_KINGDOM);
        } else if (world.getDimension().getEffects() == DimensionType.OVERWORLD_ID) {
            optional = world.getRegistryManager().get(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY).getEntryList(ModTags.Structures.CHAMPIONS_GRAVES);
        } else {
            optional = null;
        }
        if (optional != null) {
            Pair<BlockPos, RegistryEntry<ConfiguredStructureFeature<?, ?>>> pair = world.getChunkManager().getChunkGenerator().locateStructure(world, optional.get(), center, 100, false);
            if (stack.getOrCreateNbt() != null) {
                if (pair != null) {
                    stack.getNbt().put("structurePos", NbtHelper.fromBlockPos(pair.getFirst()));
                }
            }
        }
        //structurePos = world.locateStructure(ModTags.Structures.DECAYING_KINGDOM, center, 100, false);
    }
    
    public BlockPos getStructurePos(ItemStack stack) {
        if (stack.hasNbt()) {
            return NbtHelper.toBlockPos(stack.getNbt().getCompound("structurePos"));
        } else {
            return null;
        }
    }
}
