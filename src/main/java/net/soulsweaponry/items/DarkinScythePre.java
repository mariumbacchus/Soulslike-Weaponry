package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
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
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.BossEntity;
import net.soulsweaponry.registry.PacketRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.ParticleNetworking;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DarkinScythePre extends SoulHarvestingItem {
    private final int MAX_SOULS = ConfigConstructor.darkin_scythe_max_souls;
    private float attackSpeed;

    public DarkinScythePre(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.darkin_scythe_damage, attackSpeed, settings);
        this.attackSpeed = attackSpeed;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, (e) -> {
            e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
        });
        if (target.isDead()) {
            int amount = (target instanceof BossEntity || target instanceof WitherEntity) ? 20 : 1;
            if (target.getItemsHand().iterator().next().getItem() instanceof RangedWeaponItem || target instanceof PassiveEntity) {
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
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (this.getSouls(stack) >= MAX_SOULS && slot == player.getInventory().selectedSlot) {
                if (!world.isClient) {
                    BlockPos pos = entity.getBlockPos();
                    ParticleNetworking.sendServerParticlePacket((ServerWorld) world, PacketRegistry.DAWNBREAKER_PACKET_ID, pos);
                }
                world.playSound(null, entity.getBlockPos(), SoundRegistry.DAWNBREAKER_EVENT, SoundCategory.HOSTILE, 0.8f, 1f);
                Item item = WeaponRegistry.DARKIN_SCYTHE_PRIME;
                System.out.println(this.getDominantType(stack));
                switch (this.getDominantType(stack)) {
                    case RED -> item = WeaponRegistry.DARKIN_SCYTHE_PRIME;
                    case BLUE -> item = WeaponRegistry.SHADOW_ASSASSIN_SCYTHE;
                }
                ItemStack newStack = new ItemStack(item);
                Map<Enchantment, Integer> enchants = EnchantmentHelper.get(stack);
                for (Enchantment enchant : enchants.keySet()) {
                    newStack.addEnchantment(enchant, enchants.get(enchant));
                }
                player.getInventory().removeOne(stack);
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

    private SoulType getDominantType(ItemStack stack) {
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
        float soulPercent = (float) this.getSouls(stack) / (float) MAX_SOULS;
        float bonusDamage = (float) ConfigConstructor.darkin_scythe_bonus_damage * soulPercent;
        return bonusDamage;
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

    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.getAttackDamage() + this.getBonusDamage(stack), EntityAttributeModifier.Operation.ADDITION));
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
            tooltip.add(new TranslatableText("tooltip.soulsweapons.transformation").formatted(Formatting.LIGHT_PURPLE));
            for (int i = 1; i <= 8; i++) {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.transformation_description_" + i).formatted(Formatting.GRAY));
            }
            tooltip.add(new LiteralText(MathHelper.floor(((float)this.getSouls(stack)/MAX_SOULS)*100) + "%").formatted(this.getDominantType(stack).equals(SoulType.BLUE) ? Formatting.AQUA : Formatting.RED));
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    enum SoulType {
        RED("red_soul"), BLUE("blue_soul");

        String id;
        SoulType(String id) {
            this.id = id;
        }
    }
}
