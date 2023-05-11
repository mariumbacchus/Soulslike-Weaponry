package net.soulsweaponry.entity.mobs;

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

public class Forlorn extends Remnant {
    
    public Forlorn(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        Forlorn.initEquip(this);
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

    public static void initEquip(LivingEntity entity) {
        if (entity.getRandom().nextBoolean()) {
            entity.equipStack(EquipmentSlot.MAINHAND,new ItemStack(WeaponRegistry.FORLORN_SCYTHE));
        } else {
            entity.equipStack(EquipmentSlot.MAINHAND,new ItemStack(WeaponRegistry.GUTS_SWORD));
        }
        entity.equipStack(EquipmentSlot.HEAD,new ItemStack(ArmorRegistry.FORLORN_HELMET));
        entity.equipStack(EquipmentSlot.CHEST,new ItemStack(ArmorRegistry.FORLORN_CHESTPLATE));
        entity.equipStack(EquipmentSlot.LEGS,new ItemStack(ArmorRegistry.FORLORN_LEGGINGS));
        entity.equipStack(EquipmentSlot.FEET,new ItemStack(ArmorRegistry.FORLORN_BOOTS));
    }
}
