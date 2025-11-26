package net.soulsweaponry.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
@Environment(EnvType.CLIENT)
public class ItemStackTooltipMixin {

    /**
     * Makes this mod's custom attributes a different color. This is because a new row of attributes would
     * be added instead of adding to existing lines, like instead of +2 and +3 resulting in a single +5, it would be 2 separate
     * rows of +2 and +3 with the same generic name, so to make the distinction clearer the color is different.
     */
    @Inject(method = "appendAttributeModifierTooltip", at = @At("HEAD"), cancellable = true)
    private void sw$colorMine(Consumer<Text> sink, @Nullable PlayerEntity player, RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier mod, CallbackInfo ci) {
        Identifier modId = mod.id();
        boolean changeColor = modId != null && SoulsWeaponry.ModId.equals(modId.getNamespace()) && modId.getPath().contains("upgrade");
        if (!changeColor) return;
        double d = mod.value();
        if (player != null) {
            if (mod.idMatches(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID)) {
                d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            } else if (mod.idMatches(Item.BASE_ATTACK_SPEED_MODIFIER_ID)) {
                d += player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_SPEED);
            }
        }
        double e;
        if (mod.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                || mod.operation() == EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
            e = d * 100.0;
        } else if (attribute.matches(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) {
            e = d * 10.0;
        } else {
            e = d;
        }
        if (d > 0.0) {
            sink.accept(
                    Text.translatable(
                                    "attribute.modifier.plus." + mod.operation().getId(),
                                    AttributeModifiersComponent.DECIMAL_FORMAT.format(e),
                                    Text.translatable(attribute.value().getTranslationKey())
                            )
                            .formatted(attribute.value().getFormatting(true))
                            .formatted(Formatting.AQUA)
            );
        }
        ci.cancel();
    }
}
