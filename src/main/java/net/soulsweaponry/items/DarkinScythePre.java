package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.BossEntity;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Map;

public class DarkinScythePre extends SoulHarvestingItem {

    public final int MAX_SOULS = ConfigConstructor.darkin_scythe_max_souls;

    public DarkinScythePre(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.darkin_scythe_damage, ConfigConstructor.darkin_scythe_attack_speed, settings);
        this.getTooltipAbilities().clear();
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.TRANSFORMATION);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (target.isDead()) {
            int amount = (target instanceof BossEntity || target instanceof WitherEntity) ? 20 : 1;
            if (target.getHandItems().iterator().next().getItem() instanceof RangedWeaponItem || target instanceof PassiveEntity) {
                this.addAmount(stack, amount, SoulType.BLUE);
            } else {
                this.addAmount(stack, amount, SoulType.RED);
            }
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!this.isDisabled(stack) && entity instanceof PlayerEntity player) {
            if (this.getSouls(stack) >= MAX_SOULS && slot == player.getInventory().selectedSlot) {
                if (!world.isClient) {
                    ParticleHandler.particleSphere(world, 1000, entity.getX(), entity.getY() + .1f, entity.getZ(), ParticleTypes.FLAME, 1f);
                    ParticleHandler.particleOutburstMap(world, 200, entity.getX(), entity.getY() + .1f, entity.getZ(), ParticleEvents.DAWNBREAKER_MAP, 1f);
                }
                world.playSound(null, entity.getBlockPos(), SoundRegistry.DAWNBREAKER_EVENT, SoundCategory.HOSTILE, 0.8f, 1f);
                Item item = WeaponRegistry.DARKIN_SCYTHE_PRIME;
                switch (this.getDominantType(stack)) {
                    case RED -> item = WeaponRegistry.DARKIN_SCYTHE_PRIME;
                    case BLUE -> item = WeaponRegistry.SHADOW_ASSASSIN_SCYTHE;
                }
                ItemStack newStack = new ItemStack(item);
                Map<Enchantment, Integer> enchants = EnchantmentHelper.get(stack);
                for (Enchantment enchant : enchants.keySet()) {
                    newStack.addEnchantment(enchant, enchants.get(enchant));
                }
                player.getInventory().removeStack(slot);
                player.getInventory().insertStack(slot, newStack);
            }
        }
    }

    public void addAmount(ItemStack stack, int amount, SoulType soulType) {
        if (stack.hasNbt()) {
            if (stack.getNbt().contains(soulType.id)) {
                stack.getNbt().putInt(soulType.id, stack.getNbt().getInt(soulType.id) + amount);
            } else {
                stack.getNbt().putInt(soulType.id, amount);
            }
        }
    }

    public SoulType getDominantType(ItemStack stack) {
        if (stack.hasNbt()) {
            int blue = stack.getNbt().contains(SoulType.BLUE.id) ? stack.getNbt().getInt(SoulType.BLUE.id) : 0;
            int red = stack.getNbt().contains(SoulType.RED.id) ? stack.getNbt().getInt(SoulType.RED.id) : 0;
            if (blue > red) {
                return SoulType.BLUE;
            } else {
                return SoulType.RED;
            }
        }
        return SoulType.RED;
    }

    private float getBonusDamage(ItemStack stack) {
        if (this.isDisabled(stack)) return 0;
        float soulPercent = (float) this.getSouls(stack) / (float) MAX_SOULS;
        return (float) ConfigConstructor.darkin_scythe_bonus_damage * soulPercent;
    }

    @Override
    public int getSouls(ItemStack stack) {
        int amount = 0;
        if (stack.hasNbt()) {
            if (stack.getNbt().contains(SoulType.BLUE.id)) amount += stack.getNbt().getInt(SoulType.BLUE.id);
            if (stack.getNbt().contains(SoulType.RED.id)) amount += stack.getNbt().getInt(SoulType.RED.id);
        }
        return amount;
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.getAttackDamage() + this.getBonusDamage(stack), EntityAttributeModifier.Operation.ADDITION));
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
        return ConfigConstructor.disable_use_darkin_scythe;
    }

    public enum SoulType {
        RED("red_soul"), BLUE("blue_soul");

        final String id;
        SoulType(String id) {
            this.id = id;
        }
    }
}