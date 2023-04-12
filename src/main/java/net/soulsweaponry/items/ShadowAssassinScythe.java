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
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShadowAssassinScythe extends UmbralTrespassItem {

    private final float attackSpeed;
    private static final String IN_COMBAT = "in_combat";
    private static final String JUST_ATTACKED = "just_attacked";
    private static final String COMBAT_TICKS = "combat_ticks";
    public static final int TICKS_FOR_BONUS = ConfigConstructor.shadow_assassin_scythe_shadow_step_ticks;

    public ShadowAssassinScythe(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.darkin_scythe_damage + ConfigConstructor.darkin_scythe_bonus_damage, attackSpeed, settings, ConfigConstructor.shadow_assassin_scythe_ticks_before_dismount);
        this.attackSpeed = attackSpeed;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, (e) -> {
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        if (attacker instanceof PlayerEntity) {
            var cooldownManager = ((PlayerEntity) attacker).getItemCooldownManager();
            if (!cooldownManager.isCoolingDown(this) && this.getCombatTicks(stack) > 0) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 60,
                        MathHelper.floor((float)WeaponUtil.getEnchantDamageBonus(stack)/2)));
                this.setJustAttacked(stack, true);
            }
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player) {
            ItemCooldownManager cooldownManager = player.getItemCooldownManager();
            if (this.justAttacked(stack) && entity.age % 10 == 0 && this.getCombatTicks(stack) > 0) {
                this.addCombatTicks(stack, -10);
            }
            if (this.justAttacked(stack) && this.getCombatTicks(stack) <= 0) {
                cooldownManager.set(this, ConfigConstructor.shadow_assassin_scythe_shadow_step_cooldown);
            }
            if (!cooldownManager.isCoolingDown(this)) {
                this.setCanGetBonus(stack, true);
            } else {
                this.setCanGetBonus(stack, false);
                this.setJustAttacked(stack, false);
                this.setCombatTicks(stack, TICKS_FOR_BONUS);
            }
        }
    }

    private int getCombatTicks(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(COMBAT_TICKS)) {
            return stack.getNbt().getInt(COMBAT_TICKS);
        }
        return TICKS_FOR_BONUS;
    }

    private void addCombatTicks(ItemStack stack, int amount) {
        if (stack.hasNbt()) {
            if (stack.getNbt().contains(COMBAT_TICKS)) {
                stack.getNbt().putInt(COMBAT_TICKS, stack.getNbt().getInt(COMBAT_TICKS) + amount);
            } else {
                stack.getNbt().putInt(COMBAT_TICKS, TICKS_FOR_BONUS);
            }
        }
    }

    private void setCombatTicks(ItemStack stack, int amount) {
        if (stack.hasNbt()) {
            stack.getNbt().putInt(COMBAT_TICKS, amount);
        }
    }
    private void setJustAttacked(ItemStack stack, boolean bl) {
        if (stack.hasNbt()) {
            stack.getNbt().putBoolean(JUST_ATTACKED, bl);
        }
    }

    private boolean justAttacked(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(JUST_ATTACKED)) {
            return stack.getNbt().getBoolean(JUST_ATTACKED);
        }
        return false;
    }

    private void setCanGetBonus(ItemStack stack, boolean bl) {
        if (stack.hasNbt()) {
            stack.getNbt().putBoolean(IN_COMBAT, bl);
        }
    }

    private boolean canGetBonus(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(IN_COMBAT)) {
            return stack.getNbt().getBoolean(IN_COMBAT);
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
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
