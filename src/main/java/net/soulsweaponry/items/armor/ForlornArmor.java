package net.soulsweaponry.items.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.AttributeRegistry;
import net.soulsweaponry.util.IAnimatedDeath;

import java.util.UUID;

public class ForlornArmor extends SetBonusArmor {

    public ForlornArmor(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_forlorn_set;
    }

    @Override
    public boolean isSlotActive(PlayerEntity player, EquipmentSlot slot) {
        return false;
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
    public Text[] getFullSetAbilities() {
        return new Text[] {
                Text.translatable("tooltip.soulsweapons.armor.set_bonus.forlorn_armor_heal").formatted(Formatting.GRAY)
        };
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_forlorn_armor;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return new String[0];
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> vanilla = super.getAttributeModifiers(slot);
        if (slot == this.type.getEquipmentSlot()) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(vanilla);

            UUID uuid = UUID.fromString("42703875-5257-402d-ae5e-f650be2fcb72"); // // 765a5b93-ad48-4057-96ce-44d9102fcb08 backup
            EntityAttributeModifier bleedMod = new EntityAttributeModifier(
                    uuid,
                    "Armor bleed buildup resistance",
                    200,                // e.g. 0.2 for +20%
                    EntityAttributeModifier.Operation.ADDITION
            );
            builder.put(AttributeRegistry.BLEED_BUILDUP_RESISTANCE, bleedMod);

            return builder.build();
        }
        return vanilla;//TODO go over this and give other armor this too
    }
}