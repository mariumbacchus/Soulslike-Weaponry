package net.soulsweaponry.items.armor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.EffectRegistry;

public class SoulRobesItem extends ArmorItem {

    public SoulRobesItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
    }
    
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player) {
            if (this.hasFullSet(player)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 400, 0));
                player.addStatusEffect(new StatusEffectInstance(EffectRegistry.MAGIC_RESISTANCE.get(), 40, 1));
            }
        }
    }

    private boolean hasFullSet(PlayerEntity player) {
        ItemStack boots = player.getInventory().getArmorStack(0);
        ItemStack leggings = player.getInventory().getArmorStack(1);
        ItemStack chestplate = player.getInventory().getArmorStack(2);
        ItemStack helmet = player.getInventory().getArmorStack(3);
        boolean bootsSlot = !boots.isEmpty() && boots.getItem() == ArmorRegistry.SOUL_ROBES_BOOTS.get();
        boolean leggingsSlot = !leggings.isEmpty() && leggings.getItem() == ArmorRegistry.SOUL_ROBES_LEGGINGS.get();
        boolean chestSlot = !chestplate.isEmpty() && chestplate.getItem() == ArmorRegistry.SOUL_ROBES_CHESTPLATE.get();
        boolean helmetSlot = !helmet.isEmpty() && helmet.getItem() == ArmorRegistry.SOUL_ROBES_HELMET.get();
        return bootsSlot && leggingsSlot && chestSlot && helmetSlot;
    }
}
