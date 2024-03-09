package net.soulsweaponry.entity.mobs;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.soulsweaponry.registry.ArmorRegistry;
import net.soulsweaponry.registry.WeaponRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Forlorn extends Remnant {
    
    public Forlorn(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        Forlorn.initEquip(this, Collections.emptyMap());
    }

    @Override
    protected void initGoals() {
        super.initGoals();
    }

    public static DefaultAttributeContainer.Builder createForlornAttributes() {
        return MobEntity.createMobAttributes()
        .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 25D)
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 15D)
        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3000000003D)
        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public int getSoulAmount() {
        return 10;
    }

    public static void initEquip(LivingEntity entity, Map<Enchantment, Integer> enchants) {
        HashMap<EquipmentSlot, ItemStack> equip = new HashMap<>();
        if (entity.getRandom().nextBoolean()) {
            equip.put(EquipmentSlot.MAINHAND, new ItemStack(WeaponRegistry.FORLORN_SCYTHE));
        } else {
            equip.put(EquipmentSlot.MAINHAND, new ItemStack(WeaponRegistry.GUTS_SWORD));
        }
        equip.put(EquipmentSlot.HEAD, new ItemStack(ArmorRegistry.FORLORN_HELMET));
        equip.put(EquipmentSlot.CHEST, new ItemStack(ArmorRegistry.FORLORN_CHESTPLATE));
        equip.put(EquipmentSlot.LEGS, new ItemStack(ArmorRegistry.FORLORN_LEGGINGS));
        equip.put(EquipmentSlot.FEET, new ItemStack(ArmorRegistry.FORLORN_BOOTS));
        for (EquipmentSlot slot : equip.keySet()) {
            ItemStack item = equip.get(slot);
            for (Enchantment enchant : enchants.keySet()) {
                if (enchant.isAcceptableItem(item)) {
                    item.addEnchantment(enchant, enchants.get(enchant));
                }
            }
            entity.equipStack(slot, item);
        }
    }
}
