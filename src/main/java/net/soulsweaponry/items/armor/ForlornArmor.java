package net.soulsweaponry.items.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.util.IAnimatedDeath;

public class ForlornArmor extends SetBonusArmor {

    public ForlornArmor(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    protected void tickAdditionalSetEffects(ItemStack stack, PlayerEntity player) {
        if (player.getWorld().isClient) return;
        for (Entity entity : player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(ConfigConstructor.forlorn_set_bonus_range))) {
            if (stack.getItem() == this.getMatchingHead() && entity instanceof LivingEntity living && living.isDead() && (entity instanceof IAnimatedDeath animatedDeath ? animatedDeath.getDeathTicks() == 1 : living.deathTime == 1)) {
                player.heal(ConfigConstructor.forlorn_set_bonus_heal);
            }
        }
    }

    @Override
    protected Item getMatchingBoots() {
        return ArmorRegistry.FORLORN_BOOTS;
    }

    @Override
    protected Item getMatchingLegs() {
        return ArmorRegistry.FORLORN_LEGGINGS;
    }

    @Override
    protected Item getMatchingChest() {
        return ArmorRegistry.FORLORN_CHESTPLATE;
    }

    @Override
    protected Item getMatchingHead() {
        return ArmorRegistry.FORLORN_HELMET;
    }

    @Override
    protected StatusEffectInstance[] getFullSetEffects() {
        return new StatusEffectInstance[0];
    }

    @Override
    protected Text[] getCustomTooltips() {
        return new Text[] {
                Text.translatable("tooltip.soulsweapons.armor.set_bonus.forlorn_armor_heal").formatted(Formatting.GRAY)
        };
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_forlorn_armor;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_forlorn_set;
    }
}