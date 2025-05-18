package net.soulsweaponry.items.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class EnhancedHallowheart extends Hallowheart {

    public EnhancedHallowheart(ArmorMaterial material, Type type, Settings settings) {
        super(material, type, settings);
        this.addTooltipAbility(TooltipAbilities.UNBURNABLE, TooltipAbilities.EXALT);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player && this.isSlotActive(player, EquipmentSlot.CHEST)) {
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

    @Override
    public boolean isSlotActive(PlayerEntity player, EquipmentSlot slot) {
        ItemStack stack = player.getEquippedStack(slot);
        return !stack.isEmpty() && !this.isDisabled(stack) && stack.isOf(ArmorRegistry.ENHANCED_WITHERED_CHEST);
    }

    @Override
    public Text[] getLoreTooltips() {
        return new Text[] {
                Text.translatable("tooltip.soulsweapons.withered_chest.lore.enhanced.1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.withered_chest.lore.enhanced.2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.withered_chest.lore.enhanced.3").formatted(Formatting.DARK_GRAY),
        };
    }

    @Override
    public PlayState soulsAnimation(AnimationState<?> event) {
        event.getController().setAnimation(RawAnimation.begin().thenPlay("soul_spin"));
        return PlayState.CONTINUE;
    }

    private PlayState heartAnimation(AnimationState<?> event) {
        //Note: maybe figure out how to use ISyncable and add different animations (already in .animations.json).
        event.getController().setAnimation(RawAnimation.begin().thenPlay("idle_heartbeat"));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        super.registerControllers(controllerRegistrar);
        controllerRegistrar.add(new AnimationController<>(this, "heart", 0, this::heartAnimation));
    }
}
