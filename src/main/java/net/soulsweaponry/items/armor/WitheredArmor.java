package net.soulsweaponry.items.armor;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.client.renderer.armor.WitheredArmorRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ICooldownItem;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WitheredArmor extends ModdedArmor implements GeoItem, IKeybindAbility, ICooldownItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public WitheredArmor(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player) {
            if (this.isChestActive(player)) {
                if (player.getHealth() < player.getMaxHealth() / 2f) {
                    if (player.hasStatusEffect(StatusEffects.WITHER)) {
                        player.removeStatusEffect(StatusEffects.WITHER);
                    }
                }
                if (this.isChestEnhanced(player)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 100, 0));
                    if (player.isOnFire() && player.age % 30 == 0) {
                        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0));
                    }
                    if (player.getHealth() < player.getMaxHealth() * ConfigConstructor.withered_chest_strength_trigger_percent_1) {
                        if (player.getHealth() < player.getMaxHealth() * ConfigConstructor.withered_chest_strength_trigger_percent_2) {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 40, 1));
                        } else {
                            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 40, 0));
                        }
                    }
                }
            }
        }
    }

    private boolean isChestActive(PlayerEntity player) {
        ItemStack chest = player.getInventory().getArmorStack(2);
        return !chest.isEmpty() && !this.isDisabled(chest) && (chest.isOf(ItemRegistry.WITHERED_CHEST) || chest.isOf(ItemRegistry.ENHANCED_WITHERED_CHEST));
    }

    private boolean isChestEnhanced(PlayerEntity player) {
        ItemStack chest = player.getInventory().getArmorStack(2);
        return !chest.isEmpty() && !this.isDisabled(chest) && chest.isOf(ItemRegistry.ENHANCED_WITHERED_CHEST);
    }

    private PlayState souls(AnimationState<?> event) {
        if (this.equals(ItemRegistry.ENHANCED_WITHERED_CHEST)) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("soul_spin"));
        } else {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("no_souls"));
        }
        return PlayState.CONTINUE;
    }

    private PlayState heart(AnimationState<?> event) {
        //Note: maybe figure out how to use ISyncable and add different animations (already in .animations.json).
        if (this.equals(ItemRegistry.ENHANCED_WITHERED_CHEST)) {
            event.getController().setAnimation(RawAnimation.begin().thenPlay("idle_heartbeat"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "souls", 0, this::souls));
        controllers.add(new AnimationController<>(this, "heart", 0, this::heart));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.LIFE_LEACH, ConfigConstructor.withered_chest_life_leach_duration, ConfigConstructor.withered_chest_life_leach_amplifier));
            player.getItemCooldownManager().set(stack.getItem(), Math.max(ConfigConstructor.withered_chest_ability_min_cooldown,
                    ConfigConstructor.withered_chest_ability_cooldown - this.getReduceCooldownEnchantLevel(stack) * 60));
            world.playSound(null, player.getBlockPos(), SoundRegistry.DEMON_BOSS_IDLE_EVENT, SoundCategory.PLAYERS, 0.75f, 1f);
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (Screen.hasShiftDown()) {
            if (stack.isOf(ItemRegistry.WITHERED_CHEST) || stack.isOf(ItemRegistry.ENHANCED_WITHERED_CHEST)) {
                boolean bl = stack.isOf(ItemRegistry.ENHANCED_WITHERED_CHEST);
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.unceasing").formatted(Formatting.DARK_PURPLE));
                for (int i = 1; i <= 4; i++) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.unceasing." + i).formatted(Formatting.GRAY));
                }
                WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.KEYBIND_ABILITY, stack, tooltip);
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.infectious").formatted(Formatting.DARK_RED));
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.infectious.1").formatted(Formatting.GRAY));
                tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.infectious.2").formatted(Formatting.GRAY));
                if (bl) {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.infectious.3").formatted(Formatting.GRAY));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.fire_immune").formatted(Formatting.GOLD));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.fire_immune.1").formatted(Formatting.GRAY));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.exalt").formatted(Formatting.RED));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.exalt.1").formatted(Formatting.GRAY));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.exalt.2").formatted(Formatting.GRAY));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.exalt.3").formatted(Formatting.GRAY));
                    tooltip.add(Text.translatable("tooltip.soulsweapons.withered_armor.exalt.4").formatted(Formatting.DARK_GRAY));
                }
                if (Screen.hasControlDown()) {
                    if (bl) {
                        for (int i = 1; i <= 3; i++) {
                            tooltip.add(Text.translatable("tooltip.soulsweapons.withered_chest.lore.enhanced." + i).formatted(Formatting.DARK_GRAY));
                        }
                    } else {
                        for (int i = 1; i <= 4; i++) {
                            tooltip.add(Text.translatable("tooltip.soulsweapons.withered_chest.lore." + i).formatted(Formatting.DARK_GRAY));
                        }
                    }
                } else {
                    tooltip.add(Text.translatable("tooltip.soulsweapons.control"));
                }
            }
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<LivingEntity> original) {
                if(this.renderer == null) {
                    if (itemStack.isOf(ItemRegistry.WITHERED_CHEST) || itemStack.isOf(ItemRegistry.ENHANCED_WITHERED_CHEST)) {
                        this.renderer = new WitheredArmorRenderer();
                    }
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);

                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_withered_chest;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_withered_set;
    }

    @Override
    public int getReduceCooldownEnchantLevel(ItemStack stack) {
        if ((stack.isOf(ItemRegistry.WITHERED_CHEST) || stack.isOf(ItemRegistry.ENHANCED_WITHERED_CHEST)) && ConfigConstructor.withered_chest_ability_unbreaking_reduces_cooldown) {
            return EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack);
        }
        return 0;
    }
}