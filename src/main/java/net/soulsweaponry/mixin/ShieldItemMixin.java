package net.soulsweaponry.mixin;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.abilities.IHasAbilities;
import net.soulsweaponry.items.abilities.otherkeybind.Parry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ShieldItem.class)
public abstract class ShieldItemMixin implements IHasAbilities {

    @Unique
    private static final Parry PARRY = new Parry(
            (int) WeaponConfig.shield_parry_frames,
            WeaponConfig.shield_parry_bonus_frames_per_level,
            (int) WeaponConfig.shield_parry_max_animation_frames,
            (int) WeaponConfig.shield_parry_min_cooldown,
            (int) WeaponConfig.shield_parry_cooldown,
            (int) WeaponConfig.shield_parry_reduced_cooldown_per_level
    );

    @Inject(method = "<init>", at = @At("TAIL"))
    public void shield$init(Item.Settings settings, CallbackInfo info) {
        if (WeaponConfig.enable_shield_parry) {
            this.addAbility(PARRY);
        }
    }

    @Inject(method = "appendTooltip", at = @At("TAIL"))
    protected void interceptTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo info) {
        if (WeaponConfig.enable_shield_parry) {
            this.appendTooltipAbilities(stack, tooltip);
        }
    }
}
