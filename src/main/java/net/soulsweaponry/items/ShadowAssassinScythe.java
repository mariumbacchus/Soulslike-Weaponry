package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

public class ShadowAssassinScythe extends UmbralTrespassItem {

    private static final String HAS_EFFECT = "has_shadow_step";
    public static final int TICKS_FOR_BONUS = ConfigConstructor.shadow_assassin_scythe_shadow_step_ticks;

    public ShadowAssassinScythe(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.darkin_scythe_damage + ConfigConstructor.darkin_scythe_bonus_damage, ConfigConstructor.shadow_assassin_scythe_attack_speed, settings, ConfigConstructor.shadow_assassin_scythe_ticks_before_dismount);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.SHADOW_STEP);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (attacker instanceof PlayerEntity player) {
            var cooldownManager = player.getItemCooldownManager();
            if (!cooldownManager.isCoolingDown(this)) {
                attacker.addStatusEffect(new StatusEffectInstance(EffectRegistry.SHADOW_STEP, TICKS_FOR_BONUS,
                        MathHelper.floor(WeaponUtil.getEnchantDamageBonus(stack)/2f)));
                cooldownManager.set(this, ConfigConstructor.shadow_assassin_scythe_shadow_step_cooldown);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (stack.hasNbt()) {
            stack.getNbt().putBoolean(HAS_EFFECT, stack.hasNbt() && entity instanceof LivingEntity living && living.hasStatusEffect(EffectRegistry.SHADOW_STEP));
        }
    }

    private boolean canGetBonus(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(HAS_EFFECT) && !this.isDisabled(stack)) {
            return stack.getNbt().getBoolean(HAS_EFFECT);
        }
        return false;
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.canGetBonus(stack) ? this.getAttackDamage() + ConfigConstructor.shadow_assassin_scythe_shadow_step_bonus_damage : this.getAttackDamage(), EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", this.getAttackSpeed(), EntityAttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot);
        }
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_shadow_assassin_scythe;
    }

    @Override
    public float getAbilityDamage() {
        return ConfigConstructor.shadow_assassin_scythe_ability_damage;
    }

    @Override
    public int getAbilityCooldown() {
        return ConfigConstructor.shadow_assassin_scythe_ability_cooldown;
    }

    @Override
    public boolean shouldAbilityHeal() {
        return false;
    }
}