package net.soulsweapons.items;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BossCompass extends Item {

    public BossCompass(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(stack, world, entity, pSlotId, pIsSelected);
        if (!world.isClientSide && entity instanceof Player && entity.tickCount % 40 == 0) {
            this.updatePos((ServerLevel)world, entity.getOnPos(), stack, entity);
        }
    }

    public void updatePos(ServerLevel world, BlockPos center, ItemStack stack, Entity entity) {
//        Optional<RegistryEntryList.Named<ConfiguredStructureFeature<?, ?>>> optional;
//        if (world.getDimension().getEffects() == DimensionType.THE_NETHER_ID) { TODO lag structures og tags
//            optional = world.getRegistryManager().get(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY).getEntryList(ModTags.Structures.DECAYING_KINGDOM);
//        } else if (world.getDimension().getEffects() == DimensionType.OVERWORLD_ID) {
//            optional = world.getRegistryManager().get(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY).getEntryList(ModTags.Structures.CHAMPIONS_GRAVES);
//        } else {
//            optional = null;
//        }
//        if (optional != null) {
//            BlockPos blockpos = world.findNearestMapFeature(ConfiguredStructureTags.EYE_OF_ENDER_LOCATED, entity.blockPosition(), 100, false);
//            if (stack.getTag() != null) {
//                if (blockpos != null) {
//                    stack.getTag().put("structurePos", NbtUtils.writeBlockPos(blockpos));
//                }
//            }
//        }
        //structurePos = world.locateStructure(ModTags.Structures.DECAYING_KINGDOM, center, 100, false);
    }

    public BlockPos getStructurePos(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("structurePos")) {
            return NbtUtils.readBlockPos(stack.getTag().getCompound("structurePos"));
        } else {
            return null;
        }
    }
}
