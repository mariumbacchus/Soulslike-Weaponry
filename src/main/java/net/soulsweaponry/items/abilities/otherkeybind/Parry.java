package net.soulsweaponry.items.abilities.otherkeybind;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.ParryData;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

/**
 * Triggered by pressing R.
 */
public record Parry(int parryFrames, float parryFramesPerLvl, int maxFrames,
                    int minCooldown, int cooldown, int reducedCooldownPerLvl) implements IAbility {

    public void parry(PlayerEntity player, ItemStack stack) {
        if (!this.isCoolingDown(player, stack)) {
            int lvl = WeaponUtil.getUpgradeLevel(stack);
            int frames = (int) (this.parryFrames + this.parryFramesPerLvl * lvl);
            ParryData.setParryTicks((IEntityDataSaver) player, 1, frames, this.maxFrames);
            this.applyItemCooldownNoCheck(stack, player, player.isCreative() ? 10
                    : Math.max(this.minCooldown, this.cooldown - this.reducedCooldownPerLvl * lvl));
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.parry").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.parry_description_1",
                        IHasAbilities.formatKeybindText(KeyBindRegistry.parry.getBoundKeyLocalizedText())).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.parry_description_2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.parry_description_3").formatted(Formatting.GRAY)
        );
    }
}
