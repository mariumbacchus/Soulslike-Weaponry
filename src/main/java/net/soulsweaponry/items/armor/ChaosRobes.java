package net.soulsweaponry.items.armor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.blocks.*;
import net.soulsweaponry.client.renderer.armor.ChaosSetRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.function.Consumer;

public class ChaosRobes extends ModdedArmor implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final HashMap<Block, WitheredBlock> turnableBlocks = new HashMap<>();
    private final HashMap<Block, WitheredGrass> turnableGrass = new HashMap<>();
    private final HashMap<Block, WitheredTallGrass> turnableTallPlant = new HashMap<>();

    public ChaosRobes(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.addTooltipAbility(TooltipAbilities.CORRUPT_GROUND);
        this.turnableBlocks.put(Blocks.GRASS_BLOCK, BlockRegistry.WITHERED_GRASS_BLOCK);
        this.turnableBlocks.put(Blocks.DIRT, BlockRegistry.WITHERED_DIRT);

        this.turnableGrass.put(Blocks.SHORT_GRASS, BlockRegistry.WITHERED_GRASS);
        this.turnableGrass.put(Blocks.FERN, BlockRegistry.WITHERED_FERN);
        this.turnableGrass.put(Blocks.SWEET_BERRY_BUSH, BlockRegistry.WITHERED_BERRY_BUSH);

        this.turnableTallPlant.put(Blocks.TALL_GRASS, BlockRegistry.WITHERED_TALL_GRASS);
        this.turnableTallPlant.put(Blocks.LARGE_FERN, BlockRegistry.WITHERED_LARGE_FERN);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player && this.isSlotActive(player, EquipmentSlot.CHEST)) {
            if (ConfigConstructor.chaos_cape_wither_ground) {
                this.turnBlocks(player, world, player.getBlockPos(), 0);
            }
            if (player.age % 40 == 0) {
                for (LivingEntity target : world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().expand(3D))) {
                    if (!(target instanceof PlayerEntity) && target != player) {
                        target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 1));
                    }
                }
            }
        }
    }

    public void turnBlocks(LivingEntity entity, World world, BlockPos blockPos, int bonusRadius) {
        if (!entity.isOnGround()) {
            return;
        }
        int f = Math.min(16, 3 + bonusRadius);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-f, -1, -f), blockPos.add(f, -1, f))) {
            if (!world.getBlockState(blockPos2).isAir()) {
                for (Block turnBlock : this.turnableBlocks.keySet()) {
                    if (world.getBlockState(blockPos2).getBlock() == turnBlock) {
                        BlockState blockState = this.turnableBlocks.get(turnBlock).getDefaultState();
                        if (!blockPos2.isWithinDistance(entity.getPos(), f)) continue;
                        mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                        BlockState blockState2 = world.getBlockState(mutable);
                        if (blockState2.isIn(BlockTags.SMALL_FLOWERS)) world.setBlockState(mutable, BlockRegistry.HYDRANGEA.getDefaultState().with(WitheredFlower.CANNOT_TURN, false));
                        for (Block turnGrass : this.turnableGrass.keySet()) if (blockState2.isOf(turnGrass)) world.setBlockState(mutable, this.turnableGrass.get(turnGrass).getDefaultState());
                        for (Block turnTallPlant : this.turnableTallPlant.keySet()) if (blockState2.isOf(turnTallPlant)) {
                            world.removeBlock(mutable, false);
                            TallPlantBlock.placeAt(world, this.turnableTallPlant.get(turnTallPlant).getDefaultState(), mutable, 2);
                        }
                        if (blockState2.isIn(BlockTags.TALL_FLOWERS)) {
                            world.removeBlock(mutable, false);
                            TallPlantBlock.placeAt(world, BlockRegistry.OLEANDER.getDefaultState().with(WitheredTallFlower.CANNOT_TURN, false), mutable, 2);
                        }
                        world.setBlockState(blockPos2, blockState);
                        world.scheduleBlockTick(blockPos2, this.turnableBlocks.get(turnBlock), MathHelper.nextInt(entity.getRandom(), 50, 90));
                    } else if (world.getBlockState(blockPos2).getBlock() == this.turnableBlocks.get(turnBlock)) {
                        WitheredBlock block = (WitheredBlock) world.getBlockState(blockPos2).getBlock();
                        block.resetAge(world.getBlockState(blockPos2), world, blockPos2);
                    }
                }
            }
        }
    }

    @Override
    public Text[] getLoreTooltips() {
        return new Text[]{
                Text.translatable("tooltip.soulsweapons.chaos_robes_lore_1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_robes_lore_2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_robes_lore_3").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.chaos_robes_lore_4").formatted(Formatting.DARK_GRAY),
        };
    }

    private PlayState predicate(AnimationState<?> event) {
        event.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public boolean isSlotActive(PlayerEntity player, EquipmentSlot slot) {
        ItemStack stack = player.getEquippedStack(slot);
        return !stack.isEmpty() && !this.isDisabled(stack) && stack.getItem() instanceof ChaosRobes;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_chaos_robes;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return new String[0];
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> BipedEntityModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable BipedEntityModel<T> original) {
                if (this.renderer == null) {
                    this.renderer = new ChaosSetRenderer<ChaosRobes>();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public float[] getBleedBuildupResistances() {
        return ConfigConstructor.chaos_set_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ConfigConstructor.chaos_set_bleed_damage_resistances;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ConfigConstructor.chaos_set_posture_buildup_resistances;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ConfigConstructor.chaos_set_base_posture_increase;
    }
}
