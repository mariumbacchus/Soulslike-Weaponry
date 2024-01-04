package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.entity.mobs.BossEntity;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.ParticleEvents;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DarkinScythePre extends SoulHarvestingItem {

    public final int MAX_SOULS = CommonConfig.DARKIN_SCYTHE_MAX_SOULS.get();
    private final float attackSpeed;

    public DarkinScythePre(Tier pTier, float attackSpeed, Properties pProperties) {
        super(pTier, CommonConfig.DARKIN_SCYTHE_DAMAGE.get(), attackSpeed, pProperties);
        this.attackSpeed = attackSpeed;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (p_43296_) -> p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        if (target.isDeadOrDying()) {
            int amount = (target instanceof BossEntity || target instanceof WitherBoss) ? 20 : 1;
            if (target.getHandSlots().iterator().next().getItem() instanceof ProjectileWeaponItem || target instanceof Animal) {
                this.addAmount(stack, amount, SoulType.BLUE);
            } else {
                this.addAmount(stack, amount, SoulType.RED);
            }
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean pIsSelected) {
        super.inventoryTick(stack, world, entity, slot, pIsSelected);
        if (entity instanceof Player player) {
            if (this.getSouls(stack) >= MAX_SOULS && slot == player.getInventory().selected) {
                if (!world.isClientSide) {
                    ParticleEvents.dawnbreakerEvent(world, player.getX(), player.getY(), player.getZ(), 1f);
                }
                world.playSound(null, entity.blockPosition(), SoundRegistry.DAWNBREAKER_EVENT.get(), SoundSource.HOSTILE, 0.8f, 1f);
                Item item = WeaponRegistry.DARKIN_SCYTHE_PRIME.get();
                switch (this.getDominantType(stack)) {
                    case RED -> item = WeaponRegistry.DARKIN_SCYTHE_PRIME.get();
                    case BLUE -> item = WeaponRegistry.SHADOW_ASSASSIN_SCYTHE.get();
                }
                ItemStack newStack = new ItemStack(item);
                Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
                for (Enchantment enchant : enchants.keySet()) {
                    newStack.enchant(enchant, enchants.get(enchant));
                }
                player.getInventory().removeItemNoUpdate(slot);
                player.getInventory().add(slot, newStack);
            }
        }
    }

    public void addAmount(ItemStack stack, int amount, SoulType soulType) {
        if (stack.hasTag()) {
            if (stack.getTag().contains(soulType.id)) {
                stack.getTag().putInt(soulType.id, stack.getTag().getInt(soulType.id) + amount);
            } else {
                stack.getTag().putInt(soulType.id, amount);
            }
        }
    }

    public SoulType getDominantType(ItemStack stack) {
        if (stack.hasTag()) {
            int blue = stack.getTag().contains(SoulType.BLUE.id) ? stack.getTag().getInt(SoulType.BLUE.id) : 0;
            int red = stack.getTag().contains(SoulType.RED.id) ? stack.getTag().getInt(SoulType.RED.id) : 0;
            if (blue > red) {
                return SoulType.BLUE;
            } else {
                return SoulType.RED;
            }
        }
        return SoulType.RED;
    }

    private float getBonusDamage(ItemStack stack) {
        float soulPercent = (float) this.getSouls(stack) / (float) MAX_SOULS;
        return (float) CommonConfig.DARKIN_SCYTHE_BONUS_DAMAGE.get() * soulPercent;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.getDamage() + this.getBonusDamage(stack), AttributeModifier.Operation.ADDITION));
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
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.TRANSFORMATION, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }

    public enum SoulType {
        RED("red_soul"), BLUE("blue_soul");

        final String id;
        SoulType(String id) {
            this.id = id;
        }
    }
}
