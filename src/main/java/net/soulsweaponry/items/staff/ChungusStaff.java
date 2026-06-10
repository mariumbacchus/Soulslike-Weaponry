package net.soulsweaponry.items.staff;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.WeaponConfig;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.abilitykeybind.BasicKeybindAbility;
import net.soulsweaponry.items.abilities.inventorytick.BasicInventoryTickAbility;
import net.soulsweaponry.items.abilities.use.ShootChungusHeads;
import net.soulsweaponry.items.abilities.useonentity.TameChungus;
import net.soulsweaponry.registry.EffectRegistry;

import java.util.List;

public class ChungusStaff extends ModdedSword {

    private static final BasicInventoryTickAbility CHUNGUS_TONIC_EFFECT = new BasicInventoryTickAbility(
            (stack, world, entity, slot, equipped) -> entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.CHUNGUS_TONIC_EFFECT, 120, 0, true, false)),
            List.of(
                    Text.translatable("tooltip.soulsweapons.chungus_infused").formatted(Formatting.GREEN).formatted(Formatting.BOLD),
                    Text.translatable("tooltip.soulsweapons.chungus_infused.1").formatted(Formatting.GRAY),
                    Text.translatable("tooltip.soulsweapons.chungus_infused.2").formatted(Formatting.GRAY),
                    Text.translatable("tooltip.soulsweapons.chungus_infused.3").formatted(Formatting.GRAY)
            ), 100
    );
    private static final BasicKeybindAbility BUFF_USER = new BasicKeybindAbility(
            (serverWorld, stack, player) -> {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE,
                        (int) WeaponConfig.chungus_staff_buff_haste_duration,  (int) WeaponConfig.chungus_staff_buff_haste_amp));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED,
                        (int) WeaponConfig.chungus_staff_buff_speed_duration, (int) WeaponConfig.chungus_staff_buff_speed_amp));
            },
            (clientWorld, stack, player) -> player.playSound(SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0F, 1.0F),
            List.of(
                    Text.translatable("tooltip.soulsweapons.chungus_infused.4").formatted(Formatting.GRAY)
            ), 3,
            (int) WeaponConfig.chungus_staff_buff_ability_min_cooldown,
            (int) WeaponConfig.chungus_staff_buff_ability_cooldown,
            (int) WeaponConfig.chungus_staff_buff_ability_reduced_cooldown_per_level
    );
    private static final TameChungus TAME_CHUNGUS = new TameChungus();
    private static final ShootChungusHeads SHOOT_CHUNGUS_HEADS = new ShootChungusHeads(
            WeaponConfig.chungus_staff_chungsplosion_projectile_speed,
            (int) WeaponConfig.chungus_staff_chungsplosion_ticks_before_explosion,
            (int) WeaponConfig.chungus_staff_chungsplosion_cooldown
    );

    public ChungusStaff(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) WeaponConfig.chungus_staff_damage, WeaponConfig.chungus_staff_attack_speed, settings);
        this.addAbility(CHUNGUS_TONIC_EFFECT, BUFF_USER, TAME_CHUNGUS, SHOOT_CHUNGUS_HEADS);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return WeaponConfig.disable_use_chungus_staff;
    }

    @Override
    public boolean hasRecipeRemainder() {
        return true;
    }

    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return stack.copy();
    }

    @Override
    public List<Text> getAdditionalItemTooltips() {
        return List.of(
                Text.translatable("tooltip.soulsweapons.chungus_staff.1").formatted(Formatting.DARK_GRAY)
        );
    }
}
