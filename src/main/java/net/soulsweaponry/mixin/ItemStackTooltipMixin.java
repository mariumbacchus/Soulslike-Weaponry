package net.soulsweaponry.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.soulsweaponry.util.UpgradeUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(ItemStack.class)
@OnlyIn(Dist.CLIENT)
public abstract class ItemStackTooltipMixin {

    @Inject(method = "getTooltip", at = @At("RETURN"), cancellable = true)
    private void soulsweapons$recolorUpgradeAttributes(
            PlayerEntity player,
            TooltipContext context,
            CallbackInfoReturnable<List<Text>> cir
    ) {
        ItemStack stack = (ItemStack) (Object) this;
        List<Text> tooltip = cir.getReturnValue();

        Map<String, Integer> upgradeLineCounts = new HashMap<>();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            Multimap<EntityAttribute, EntityAttributeModifier> map = stack.getAttributeModifiers(slot);

            for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : map.entries()) {
                EntityAttribute attribute = entry.getKey();
                EntityAttributeModifier modifier = entry.getValue();

                if (!soulsweapons$isUpgradeModifier(modifier.getId())) {
                    continue;
                }

                double d = modifier.getValue();
                double e;

                if (modifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE
                        || modifier.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
                    e = d * 100.0;
                } else if (attribute.equals(net.minecraft.entity.attribute.EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE)) {
                    e = d * 10.0;
                } else {
                    e = d;
                }

                if (d > 0.0) {
                    String line = Text.translatable(
                            "attribute.modifier.plus." + modifier.getOperation().getId(),
                            ItemStack.MODIFIER_FORMAT.format(e),
                            Text.translatable(attribute.getTranslationKey())
                    ).getString();

                    upgradeLineCounts.merge(line, 1, Integer::sum);
                } else if (d < 0.0) {
                    e *= -1.0;

                    String line = Text.translatable(
                            "attribute.modifier.take." + modifier.getOperation().getId(),
                            ItemStack.MODIFIER_FORMAT.format(e),
                            Text.translatable(attribute.getTranslationKey())
                    ).getString();

                    upgradeLineCounts.merge(line, 1, Integer::sum);
                }
            }
        }

        for (int i = tooltip.size() - 1; i >= 0; i--) {
            Text line = tooltip.get(i);
            String key = line.getString();
            Integer remaining = upgradeLineCounts.get(key);

            if (remaining != null && remaining > 0) {
                tooltip.set(i, line.copy().formatted(Formatting.AQUA));

                if (remaining == 1) {
                    upgradeLineCounts.remove(key);
                } else {
                    upgradeLineCounts.put(key, remaining - 1);
                }
            }
        }

        cir.setReturnValue(tooltip);
    }

    @Unique
    private static boolean soulsweapons$isUpgradeModifier(UUID id) {
        return id.equals(UpgradeUtil.UPGRADE_DAMAGE)
                || id.equals(UpgradeUtil.UPGRADE_ATTACK_SPEED)
                || UpgradeUtil.UPGRADE_ARMOR_UUIDS.containsValue(id)
                || UpgradeUtil.UPGRADE_ARMOR_TOUGHNESS_UUIDS.containsValue(id)
                || id.equals(UpgradeUtil.UPGRADE_RANGED_DAMAGE)
                || id.equals(UpgradeUtil.UPGRADE_RANGED_HASTE);
    }
}