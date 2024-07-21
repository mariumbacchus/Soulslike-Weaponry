package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class Skofnung extends ModdedSword {

    public static final String EMPOWERED = "empowered_attacks_left";

    /**
     * The Skofnung sword will add a status effect that disables all healing on the target for a period of time.
     * This effect can be removed, however, by using the Skofnung Stone. Additionally, when the stone is used while 
     * Skofnung is in the other hand, it temporarily sharpens the Skofnung sword, empowering it for the next 8 hits.
     * The empowering of the sword is coded in the {@link SkofnungStone} class.
     */
    public Skofnung(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.skofnung_damage, ConfigConstructor.skofnung_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.DISABLE_HEAL, WeaponUtil.TooltipAbilities.SHARPEN, WeaponUtil.TooltipAbilities.IS_SHARPENED);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        int duration = ConfigConstructor.skofnung_disable_heal_duration + (WeaponUtil.getEnchantDamageBonus(stack) * 40);
        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.DISABLE_HEAL, duration, 0));
        if (isEmpowered(stack)) {
            target.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLEED, 80, 0));
            if (attacker instanceof PlayerEntity player) {
                if (!player.getItemCooldownManager().isCoolingDown(this)) {
                    this.reduceEmpowered(stack, player.getWorld(), attacker);
                    player.getItemCooldownManager().set(this, 5);
                }
            } else {
                this.reduceEmpowered(stack, attacker.getWorld(), attacker);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND && isEmpowered(stack)) {
            Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", ConfigConstructor.skofnung_damage + (ConfigConstructor.skofnung_bonus_damage - 1), EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", this.getAttackSpeed(), EntityAttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        }
        return super.getAttributeModifiers(stack, slot);
    }

    public static boolean isEmpowered(ItemStack stack) {
        return stack.hasNbt() && stack.getNbt().contains(EMPOWERED) && stack.getNbt().getInt(EMPOWERED) > 0 && !ConfigConstructor.disable_use_skofnung;
    }

    public static Integer empAttacksLeft(ItemStack stack) {
        if (isEmpowered(stack)) {
            return stack.getNbt().getInt(EMPOWERED);
        } else {
            return 0;
        }
    }

    private void reduceEmpowered(ItemStack stack, World world, LivingEntity attacker) {
        if (isEmpowered(stack)) {
            stack.getNbt().putInt(EMPOWERED, stack.getNbt().getInt(EMPOWERED) - 1);
            if (stack.getNbt().getInt(EMPOWERED) <= 0) {
                world.playSound(null, attacker.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, .75f, 1f);
            }
        }
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_skofnung;
    }
}