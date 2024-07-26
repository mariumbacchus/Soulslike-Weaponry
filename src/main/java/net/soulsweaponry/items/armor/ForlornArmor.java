package net.soulsweaponry.items.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.util.IAnimatedDeath;

public class ForlornArmor extends SetBonusArmor {

    public ForlornArmor(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    protected void tickAdditionalSetEffects(PlayerEntity player) {
        if (player.world.isClient) return;
        for (Entity entity : player.getWorld().getOtherEntities(player, player.getBoundingBox().expand(CommonConfig.FORLORN_SET_BONUS_RANGE.get()))) {
            if (!player.getItemCooldownManager().isCoolingDown(ArmorRegistry.FORLORN_HELMET.get()) && entity instanceof LivingEntity living && living.isDead() && (entity instanceof IAnimatedDeath animatedDeath ? animatedDeath.getDeathTicks() == 1 : living.deathTime == 1)) {
                player.heal(CommonConfig.FORLORN_SET_BONUS_HEAL.get());
                // The method gets ticked multiple times during the server tick it seems, so this prevents it from healing continuously.
                player.getItemCooldownManager().set(ArmorRegistry.FORLORN_HELMET.get(), 1);
            }
        }
    }

    @Override
    protected Item getMatchingBoots() {
        return ArmorRegistry.FORLORN_BOOTS.get();
    }

    @Override
    protected Item getMatchingLegs() {
        return ArmorRegistry.FORLORN_LEGGINGS.get();
    }

    @Override
    protected Item getMatchingChest() {
        return ArmorRegistry.FORLORN_CHESTPLATE.get();
    }

    @Override
    protected Item getMatchingHead() {
        return ArmorRegistry.FORLORN_HELMET.get();
    }

    @Override
    protected StatusEffectInstance[] getFullSetEffects() {
        return new StatusEffectInstance[0];
    }

    @Override
    protected Text[] getCustomTooltips() {
        return new Text[] {
                new TranslatableText("tooltip.soulsweapons.armor.set_bonus.forlorn_armor_heal").formatted(Formatting.GRAY)
        };
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return CommonConfig.DISABLE_USE_FORLORN_ARMOR.get();
    }
}
