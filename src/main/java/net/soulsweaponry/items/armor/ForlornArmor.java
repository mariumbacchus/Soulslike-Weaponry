package net.soulsweaponry.items.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.util.IAnimatedDeath;

public class ForlornArmor extends SetBonusArmor {

    public ForlornArmor(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    protected void tickAdditionalSetEffects(PlayerEntity player) {
        if (player.world.isClient) return;
        for (Entity entity : player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(ConfigConstructor.forlorn_set_bonus_range))) {
            if (entity instanceof LivingEntity living && living.isDead() && (entity instanceof IAnimatedDeath animatedDeath ? animatedDeath.getDeathTicks() == 1 : living.deathTime == 1)) {
                player.heal(ConfigConstructor.forlorn_set_bonus_heal / 4f); // Gets ticked 4 times by the time living.deathTime increases by 1
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
}