package net.soulsweaponry.items.armor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.blocks.*;
import net.soulsweaponry.client.renderer.armor.ChaosArmorRenderer;
import net.soulsweaponry.client.renderer.armor.ChaosSetRenderer;
import net.soulsweaponry.client.renderer.armor.EChaosArmorRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ChaosSet extends ModdedArmor implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final HashMap<Block, WitheredBlock> turnableBlocks = new HashMap<>();
    private final HashMap<Block, WitheredGrass> turnableGrass = new HashMap<>();
    private final HashMap<Block, WitheredTallGrass> turnableTallPlant = new HashMap<>();
    /**
     * Will contain harmful effects as the key, and the opposite beneficial effect as value
     */
    private static final HashMap<StatusEffect, StatusEffect> FLIPPABLE_EFFECTS = new HashMap<>();

    public ChaosSet(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, settings);
        this.turnableBlocks.put(Blocks.GRASS_BLOCK, BlockRegistry.WITHERED_GRASS_BLOCK.get());
        this.turnableBlocks.put(Blocks.DIRT, BlockRegistry.WITHERED_DIRT.get());

        this.turnableGrass.put(Blocks.GRASS, BlockRegistry.WITHERED_GRASS.get());
        this.turnableGrass.put(Blocks.FERN, BlockRegistry.WITHERED_FERN.get());
        this.turnableGrass.put(Blocks.SWEET_BERRY_BUSH, BlockRegistry.WITHERED_BERRY_BUSH.get());

        this.turnableTallPlant.put(Blocks.TALL_GRASS, BlockRegistry.WITHERED_TALL_GRASS.get());
        this.turnableTallPlant.put(Blocks.LARGE_FERN, BlockRegistry.WITHERED_LARGE_FERN.get());
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        if (entity instanceof PlayerEntity player) {
            if (this.isHelmetEquipped(player)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.LUCK, 40, 0));
                if (!player.getItemCooldownManager().isCoolingDown(ItemRegistry.CHAOS_CROWN.get()) && !player.getItemCooldownManager().isCoolingDown(ItemRegistry.CHAOS_HELMET.get())) {
                    this.flipEffects(player);
                }
            }
            if (this.isRobesEquipped(player)) {
                this.turnBlocks(player, world, player.getBlockPos(), 0);
                if (player.age % 40 == 0) {
                    for (LivingEntity target : world.getNonSpectatingEntities(LivingEntity.class, player.getBoundingBox().expand(3D))) {
                        if (!(target instanceof PlayerEntity) && target != player) {
                            target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 80, 1));
                        }
                    }
                }
            }
            if (this.isChestActive(player)) {
                ItemStack chest = player.getInventory().getArmorStack(2);
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 40, 1));
                if (!world.isClient && !player.getItemCooldownManager().isCoolingDown(chest.getItem()) && player.getAttacker() != null) {
                    this.shockwave(world, player);
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
                        if (blockState2.isIn(BlockTags.SMALL_FLOWERS)) world.setBlockState(mutable, BlockRegistry.HYDRANGEA.get().getDefaultState().with(WitheredFlower.CANNOT_TURN, false));
                        for (Block turnGrass : this.turnableGrass.keySet()) if (blockState2.isOf(turnGrass)) world.setBlockState(mutable, this.turnableGrass.get(turnGrass).getDefaultState());
                        for (Block turnTallPlant : this.turnableTallPlant.keySet()) if (blockState2.isOf(turnTallPlant)) {
                            world.removeBlock(mutable, false);
                            TallPlantBlock.placeAt(world, this.turnableTallPlant.get(turnTallPlant).getDefaultState(), mutable, 2);
                        }
                        if (blockState2.isIn(BlockTags.TALL_FLOWERS)) {
                            world.removeBlock(mutable, false);
                            TallPlantBlock.placeAt(world, BlockRegistry.OLEANDER.get().getDefaultState().with(WitheredTallFlower.CANNOT_TURN, false), mutable, 2);
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

    static {
        FLIPPABLE_EFFECTS.put(StatusEffects.SLOWNESS, StatusEffects.SPEED);
        FLIPPABLE_EFFECTS.put(StatusEffects.MINING_FATIGUE, StatusEffects.HASTE);
        FLIPPABLE_EFFECTS.put(StatusEffects.WEAKNESS, StatusEffects.STRENGTH);
        FLIPPABLE_EFFECTS.put(StatusEffects.BLINDNESS, StatusEffects.NIGHT_VISION);
        FLIPPABLE_EFFECTS.put(StatusEffects.HUNGER, StatusEffects.SATURATION);
        FLIPPABLE_EFFECTS.put(StatusEffects.LEVITATION, StatusEffects.SLOW_FALLING);
    }

    private void flipEffects(PlayerEntity player) {
        List<StatusEffectInstance> statusEffectsCopy = new ArrayList<>(player.getStatusEffects());
        List<StatusEffect> effectsToRemove = new ArrayList<>();
        boolean triggered = false;
        for (StatusEffectInstance instance : statusEffectsCopy) {
            StatusEffect effect = instance.getEffectType();
            if (effect.getCategory() == StatusEffectCategory.HARMFUL) {
                int duration = (int) (instance.getDuration() / 3f);
                int amplifier = (int) (instance.getAmplifier() / 2f);
                StatusEffect newEffect = StatusEffects.REGENERATION;
                for (StatusEffect harmful : FLIPPABLE_EFFECTS.keySet()) {
                    if (effect.equals(harmful)) {
                        newEffect = FLIPPABLE_EFFECTS.get(harmful);
                        break;
                    }
                }
                effectsToRemove.add(effect);
                triggered = true;
                player.addStatusEffect(new StatusEffectInstance(newEffect, duration, amplifier));
            }
        }
        for (StatusEffect effectToRemove : effectsToRemove) {
            player.removeStatusEffect(effectToRemove);
        }
        if (triggered && !player.isCreative()) {
            player.getItemCooldownManager().set(ItemRegistry.CHAOS_CROWN.get(), ConfigConstructor.chaos_crown_flip_effect_cooldown);
            player.getItemCooldownManager().set(ItemRegistry.CHAOS_HELMET.get(), ConfigConstructor.chaos_crown_flip_effect_cooldown);
        }
    }

    private void shockwave(World world, PlayerEntity player) {
        float i = ConfigConstructor.arkenplate_shockwave_knockback;
        ItemStack stack = player.getInventory().getArmorStack(2);
        if (stack == null) return;
        i += EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
        if (!world.isClient) ((ServerWorld)world).spawnParticles(ParticleTypes.EXPLOSION_EMITTER, player.getX(), player.getBodyY(0.5D), player.getZ(), 1, 0, 0, 0, 0);
        for (Entity entity : world.getOtherEntities(player, player.getBoundingBox().expand(5D))) {
            if (entity instanceof LivingEntity target && !target.isTeammate(player)) {
                if (this.equals(ItemRegistry.ENHANCED_ARKENPLATE)) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 160, 2));
                }
                target.damage(player.getDamageSources().mobAttack(player), ConfigConstructor.arkenplate_shockwave_damage);
                double x = player.getX() - target.getX();
                double z = player.getZ() - target.getZ();
                target.takeKnockback(i * 0.5f, x, z);
            }
        }
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
        if (!player.isCreative()) {
            player.getItemCooldownManager().set(stack.getItem(), ConfigConstructor.arkenplate_shockwave_cooldown);
        }
    }

    private boolean isHelmetEquipped(PlayerEntity player) {
        ItemStack helmet = player.getInventory().getArmorStack(3);
        return !helmet.isEmpty() && !this.isDisabled(helmet) && (helmet.isOf(ItemRegistry.CHAOS_CROWN.get()) || helmet.isOf(ItemRegistry.CHAOS_HELMET.get()));
    }

    private boolean isRobesEquipped(PlayerEntity player) {
        ItemStack chest = player.getInventory().getArmorStack(2);
        return !chest.isEmpty() && !this.isDisabled(chest) && chest.isOf(ItemRegistry.CHAOS_ROBES.get());
    }

    private boolean isChestActive(PlayerEntity player) {
        ItemStack chest = player.getInventory().getArmorStack(2);
        return !chest.isEmpty() && !this.isDisabled(chest) && (chest.isOf(ItemRegistry.ARKENPLATE.get()) || chest.isOf(ItemRegistry.ENHANCED_ARKENPLATE.get())) && player.getHealth() < player.getMaxHealth()/2;
    }

    private PlayState predicate(AnimationState<?> event) {
        if (this == ItemRegistry.CHAOS_ROBES.get()) {
            event.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
        if (this.equals(ItemRegistry.ENHANCED_ARKENPLATE.get())) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("soul_spin"));
        } else if (this.equals(ItemRegistry.ARKENPLATE.get())) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("no_souls"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (Screen.hasShiftDown()) {
            if (stack.isOf(ItemRegistry.CHAOS_CROWN.get()) || stack.isOf(ItemRegistry.CHAOS_HELMET.get())) {
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_crown").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_crown_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_crown_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_crown_description_3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.reversal").formatted(Formatting.DARK_AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.reversal_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.reversal_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.reversal_3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.reversal_4").formatted(Formatting.DARK_GRAY));
                if (stack.isOf(ItemRegistry.CHAOS_CROWN.get())) {
                    if (Screen.hasControlDown()) {
                        for (int i = 1; i <= 4; i++) {
                            tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_crown_lore_" + i).formatted(Formatting.DARK_GRAY));
                        }
                    } else {
                        tooltip.add(Text.translatable("tooltip.soulsweapons.control"));
                    }
                } else {
                    if (Screen.hasControlDown()) {
                        for (int i = 1; i <= 4; i++) {
                            tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_helm_lore_" + i).formatted(Formatting.DARK_GRAY));
                        }
                    } else {
                        tooltip.add(Text.translatable("tooltip.soulsweapons.control"));
                    }
                }
            } else if (stack.isOf(ItemRegistry.CHAOS_ROBES.get())) {
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_robes").formatted(Formatting.WHITE));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_robes_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_robes_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_robes_description_3").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_robes_description_4").formatted(Formatting.GRAY));
                if (Screen.hasControlDown()) {
                    for (int i = 1; i <= 4; i++) {
                        tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_robes_lore_" + i).formatted(Formatting.DARK_GRAY));
                    }
                } else {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.control"));
                }
            } else if (stack.isOf(ItemRegistry.ARKENPLATE.get()) || stack.isOf(ItemRegistry.ENHANCED_ARKENPLATE.get())) {
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate").formatted(Formatting.AQUA));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate_description_1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate_description_2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.aftershock").formatted(Formatting.DARK_GREEN));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.aftershock.1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.aftershock.2").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.aftershock.3").formatted(Formatting.GRAY));
                if (stack.isOf(ItemRegistry.ENHANCED_ARKENPLATE.get())) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.aftershock.4").formatted(Formatting.GRAY));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.mirror").formatted(Formatting.DARK_PURPLE));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.mirror.1").formatted(Formatting.GRAY).append(Text.literal((int)(ConfigConstructor.arkenplate_mirror_trigger_percent * 100f) + "%").formatted(Formatting.RED)));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate.mirror.2").formatted(Formatting.GRAY));
                }
                if (!stack.isOf(ItemRegistry.ENHANCED_ARKENPLATE.get())) {
                    if (Screen.hasControlDown()) {
                        for (int i = 1; i <= 4; i++) {
                            tooltip.add(Text.translatable("tooltip.soulsweapons.arkenplate_lore_" + i).formatted(Formatting.DARK_GRAY));
                        }
                    } else {
                        tooltip.add(Text.translatable("tooltip.soulsweapons.control"));
                    }
                }
            }
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_chaos_set;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<?> original) {
                if (this.renderer == null) {
                    if (itemStack.isOf(ItemRegistry.CHAOS_HELMET.get()) || itemStack.isOf(ItemRegistry.ARKENPLATE.get())) {
                        this.renderer = new ChaosArmorRenderer();
                    } else if (itemStack.isOf(ItemRegistry.ENHANCED_ARKENPLATE.get())) {
                        this.renderer = new EChaosArmorRenderer();
                    } else {
                        this.renderer = new ChaosSetRenderer();
                    }
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        if (stack.isOf(ItemRegistry.CHAOS_CROWN.get()) || stack.isOf(ItemRegistry.CHAOS_HELMET.get())) {
            return ConfigConstructor.disable_use_chaos_crown;
        } else if (stack.isOf(ItemRegistry.ARKENPLATE.get()) || stack.isOf(ItemRegistry.ENHANCED_ARKENPLATE.get())) {
            return ConfigConstructor.disable_use_arkenplate;
        } else if (stack.isOf(ItemRegistry.CHAOS_ROBES.get())) {
            return ConfigConstructor.disable_use_chaos_robes;
        }
        return false;
    }
}