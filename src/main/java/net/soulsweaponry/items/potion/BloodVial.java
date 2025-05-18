package net.soulsweaponry.items.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ItemRegistry;

public class BloodVial extends Item {

    public BloodVial(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user.getHealth() == user.getMaxHealth()) {
            return TypedActionResult.fail(stack);
        }
        user.heal(this.getHeal());
        user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, this.getRegenDuration(), this.getRegenAmp()));
        if (!user.isCreative()) {
            user.getInventory().insertStack(new ItemStack(ItemRegistry.GLASS_VIAL));
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            stack.decrement(1);
        }
        world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.PLAYERS, 1f, 1f);
        return TypedActionResult.success(stack);
    }

    public float getHeal() {
        return ConfigConstructor.blood_vial_heal;
    }

    public int getRegenDuration() {
        return (int) ConfigConstructor.blood_vial_regen_duration_ticks;
    }

    public int getRegenAmp() {
        return (int) ConfigConstructor.blood_vial_regen_amp;
    }
}
