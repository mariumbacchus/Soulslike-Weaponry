package net.soulsweaponry.items.armor;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.client.renderer.armor.WitheredArmorRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.TooltipAbilities;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Hallowheart extends ModdedArmor implements GeoItem, IKeybindAbility {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Hallowheart(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
        this.addTooltipAbility(TooltipAbilities.UNCEASING, TooltipAbilities.INFECTIOUS);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player && this.isSlotActive(player, EquipmentSlot.CHEST)) {
            if (player.hasStatusEffect(StatusEffects.WITHER)) {
                player.removeStatusEffect(StatusEffects.WITHER);
            }
        }
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.LIFE_LEACH, (int) ConfigConstructor.withered_chest_life_leach_duration, (int) ConfigConstructor.withered_chest_life_leach_amplifier, false, false));
            player.getItemCooldownManager().set(stack.getItem(), (int) Math.max(ConfigConstructor.withered_chest_ability_min_cooldown,
                    ConfigConstructor.withered_chest_ability_cooldown - this.getReduceCooldownEnchantLevel(stack) * 60));
            world.playSound(null, player.getBlockPos(), SoundRegistry.DEMON_BOSS_IDLE_EVENT, SoundCategory.PLAYERS, 0.75f, 1f);
        }
    }

    @Override
    public Text[] getLoreTooltips() {
        return new Text[] {
            Text.translatable("tooltip.soulsweapons.withered_chest.lore.1").formatted(Formatting.DARK_GRAY),
            Text.translatable("tooltip.soulsweapons.withered_chest.lore.2").formatted(Formatting.DARK_GRAY),
            Text.translatable("tooltip.soulsweapons.withered_chest.lore.3").formatted(Formatting.DARK_GRAY),
            Text.translatable("tooltip.soulsweapons.withered_chest.lore.4").formatted(Formatting.DARK_GRAY),
        };
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {}

    public PlayState soulsAnimation(AnimationState<?> event) {
        event.getController().setAnimation(RawAnimation.begin().thenPlay("no_souls"));
        return PlayState.CONTINUE;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_hallowheart;
    }//TODO move

    @Override
    public boolean isSlotActive(PlayerEntity player, EquipmentSlot slot) {
        ItemStack stack = player.getEquippedStack(slot);
        return !stack.isEmpty() && !this.isDisabled(stack) && stack.getItem() instanceof Hallowheart;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_hallowheart;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.withered_chest_ability_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.withered_chest_ability_enchant_reduces_cooldown_ids;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> BipedEntityModel<?> getGeoArmorRenderer(@Nullable T livingEntity, ItemStack itemStack, @Nullable EquipmentSlot equipmentSlot, @Nullable BipedEntityModel<T> original) {
                if (this.renderer == null) {
                    this.renderer = new WitheredArmorRenderer<Hallowheart>();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "souls", 0, this::soulsAnimation));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public float[] getBasePostureIncrease() {
        return ConfigConstructor.withered_armor_base_posture_increase;
    }

    @Override
    public float[] getPostureBuildupResistances() {
        return ConfigConstructor.withered_armor_posture_buildup_resistances;
    }

    @Override
    public float[] getBleedBuildupResistances() {
        return ConfigConstructor.withered_armor_bleed_buildup_resistances;
    }

    @Override
    public float[] getBleedDamageResistances() {
        return ConfigConstructor.withered_armor_bleed_damage_resistances;
    }
}
