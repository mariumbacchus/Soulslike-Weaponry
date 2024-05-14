package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
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
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShadowAssassinScythe extends UmbralTrespassItem {

    private final float attackSpeed;
    private static final String HAS_EFFECT = "has_shadow_step";
    public static final int TICKS_FOR_BONUS = CommonConfig.SHADOW_ASSASSIN_SCYTHE_SHADOW_STEP_TICKS.get();

    public ShadowAssassinScythe(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, CommonConfig.DARKIN_SCYTHE_DAMAGE.get() + CommonConfig.DARKIN_SCYTHE_BONUS_DAMAGE.get(), attackSpeed, settings, CommonConfig.SHADOW_ASSASSIN_SCYTHE_TICKS_BEFORE_DISMOUNT.get());
        this.attackSpeed = attackSpeed;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        if (attacker instanceof PlayerEntity player) {
            var cooldownManager = player.getItemCooldownManager();
            if (!cooldownManager.isCoolingDown(this)) {
                attacker.addStatusEffect(new StatusEffectInstance(EffectRegistry.SHADOW_STEP.get(), TICKS_FOR_BONUS,
                        MathHelper.floor(WeaponUtil.getEnchantDamageBonus(stack)/2f)));
                cooldownManager.set(this, CommonConfig.SHADOW_ASSASSIN_SCYTHE_SHADOW_STEP_COOLDOWN.get());
            }
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (stack.hasNbt()) {
            stack.getNbt().putBoolean(HAS_EFFECT, stack.hasNbt() && entity instanceof LivingEntity living && living.hasStatusEffect(EffectRegistry.SHADOW_STEP.get()));
        }
    }

    private boolean canGetBonus(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(HAS_EFFECT)) {
            return stack.getNbt().getBoolean(HAS_EFFECT);
        }
        return false;
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.canGetBonus(stack) ? this.getAttackDamage() + ConfigConstructor.shadow_assassin_scythe_shadow_step_bonus_damage : this.getAttackDamage(), EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", this.attackSpeed, EntityAttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SHADOW_STEP, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.UMBRAL_TRESPASS, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
