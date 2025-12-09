package net.soulsweaponry.items.armor;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.armor.EChaosArmorRenderer;
import net.soulsweaponry.client.renderer.armor.WitheredArmorRenderer;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.function.Consumer;

public class EnhancedArkenplate extends Arkenplate {

    public EnhancedArkenplate(ArmorMaterial material, Type type, Item.Settings settings) {
        super(material, type, settings);
        this.addTooltipAbility(TooltipAbilities.MIRROR);
    }

    @Override
    public boolean isSlotActive(PlayerEntity player, EquipmentSlot slot) {
        ItemStack stack = player.getEquippedStack(slot);
        return !stack.isEmpty() && !this.isDisabled(stack) && stack.isOf(ArmorRegistry.ENHANCED_ARKENPLATE.get());
    }

    @Override
    public Text[] getLoreTooltips() {
        return new Text[]{
                Text.translatable("tooltip.soulsweapons.arkenplate_lore_1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.arkenplate_lore_2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.arkenplate_lore_3").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.arkenplate_lore_4").formatted(Formatting.DARK_GRAY),
        };
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public @NotNull BipedEntityModel<LivingEntity> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, BipedEntityModel<?> original) {
                if (this.renderer == null) {
                    this.renderer = new EChaosArmorRenderer<>();
                }
                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    @Override
    public PlayState predicate(AnimationState<?> event) {
        event.getController().setAnimation(RawAnimation.begin().thenPlay("soul_spin"));
        return PlayState.CONTINUE;
    }
}