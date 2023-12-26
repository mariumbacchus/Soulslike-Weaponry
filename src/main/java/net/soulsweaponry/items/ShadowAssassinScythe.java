package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShadowAssassinScythe extends UmbralTrespassItem {

    private final float attackSpeed;
    private static final String IN_COMBAT = "in_combat";
    private static final String JUST_ATTACKED = "just_attacked";
    private static final String COMBAT_TICKS = "combat_ticks";
    public static final int TICKS_FOR_BONUS = CommonConfig.SHADOW_ASSASSIN_SCYTHE_SHADOW_STEP_TICKS.get();

    public ShadowAssassinScythe(Tier pTier, float attackSpeed, Properties pProperties) {
        super(pTier, CommonConfig.DARKIN_SCYTHE_DAMAGE.get() + CommonConfig.DARKIN_SCYTHE_BONUS_DAMAGE.get(), attackSpeed, pProperties, CommonConfig.SHADOW_ASSASSIN_SCYTHE_TICKS_BEFORE_DISMOUNT.get());
        this.attackSpeed = attackSpeed;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (p_43296_) -> p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        if (attacker instanceof Player) {//TODO turn this into an effect instead, granting more attack damage and speed post hit when having
            var cooldownManager = ((Player) attacker).getCooldowns();
            if (!cooldownManager.isOnCooldown(this) && this.getCombatTicks(stack) > 0) {
                attacker.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60,
                        Mth.floor(WeaponUtil.getEnchantDamageBonus(stack)/2f)));
                this.setJustAttacked(stack, true);
            }
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(stack, world, entity, pSlotId, pIsSelected);
        if (entity instanceof Player player) {
            ItemCooldowns cooldownManager = player.getCooldowns();
            if (this.justAttacked(stack) && entity.tickCount % 10 == 0 && this.getCombatTicks(stack) > 0) {
                this.addCombatTicks(stack, -10);
            }
            if (this.justAttacked(stack) && this.getCombatTicks(stack) <= 0) {
                cooldownManager.addCooldown(this, CommonConfig.SHADOW_ASSASSIN_SCYTHE_SHADOW_STEP_COOLDOWN.get());
            }
            if (!cooldownManager.isOnCooldown(this)) {
                this.setCanGetBonus(stack, true);
            } else {
                this.setCanGetBonus(stack, false);
                this.setJustAttacked(stack, false);
                this.setCombatTicks(stack, TICKS_FOR_BONUS);
            }
        }
    }

    private int getCombatTicks(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(COMBAT_TICKS)) {
            return stack.getTag().getInt(COMBAT_TICKS);
        }
        return TICKS_FOR_BONUS;
    }

    private void addCombatTicks(ItemStack stack, int amount) {
        if (stack.hasTag()) {
            if (stack.getTag().contains(COMBAT_TICKS)) {
                stack.getTag().putInt(COMBAT_TICKS, stack.getTag().getInt(COMBAT_TICKS) + amount);
            } else {
                stack.getTag().putInt(COMBAT_TICKS, TICKS_FOR_BONUS);
            }
        }
    }

    private void setCombatTicks(ItemStack stack, int amount) {
        if (stack.hasTag()) {
            stack.getTag().putInt(COMBAT_TICKS, amount);
        }
    }
    private void setJustAttacked(ItemStack stack, boolean bl) {
        if (stack.hasTag()) {
            stack.getTag().putBoolean(JUST_ATTACKED, bl);
        }
    }

    private boolean justAttacked(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(JUST_ATTACKED)) {
            return stack.getTag().getBoolean(JUST_ATTACKED);
        }
        return false;
    }

    private void setCanGetBonus(ItemStack stack, boolean bl) {
        if (stack.hasTag()) {
            stack.getTag().putBoolean(IN_COMBAT, bl);
        }
    }

    private boolean canGetBonus(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(IN_COMBAT)) {
            return stack.getTag().getBoolean(IN_COMBAT);
        }
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.canGetBonus(stack) ? this.getDamage() + CommonConfig.SHADOW_ASSASSIN_SCYTHE_SHADOW_STEP_BONUS_DAMAGE.get() : this.getDamage(), AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SHADOW_STEP, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.UMBRAL_TRESPASS, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }
}
