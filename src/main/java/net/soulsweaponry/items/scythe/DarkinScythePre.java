package net.soulsweaponry.items.scythe;

import net.minecraft.component.ComponentType;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.SoulHarvestingItem;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import net.soulsweaponry.util.ModTags;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.Optional;

public class DarkinScythePre extends SoulHarvestingItem {

    public final int MAX_SOULS = (int) ConfigConstructor.darkin_scythe_max_souls;

    public DarkinScythePre(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.darkin_scythe_damage, ConfigConstructor.darkin_scythe_attack_speed, settings);
        this.getTooltipAbilities().clear();
        this.addTooltipAbility(TooltipAbilities.TRANSFORMATION);
    }

    @Override
    public void handleKill(LivingEntity target, ItemStack stack) {
        int amount = target.getType().isIn(ModTags.Entities.BOSSES) ? 20 : 1;
        if (target.getType().isIn(ModTags.Entities.RANGED_MOBS) || target.getMainHandStack().getItem() instanceof RangedWeaponItem || target instanceof PassiveEntity) {
            this.addAmount(stack, amount, ComponentRegistry.BLUE_SOULS);
        } else {
            this.addAmount(stack, amount, ComponentRegistry.RED_SOULS);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!this.isDisabled(stack) && entity instanceof PlayerEntity player && entity.age % 20 == 0) {
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
                ItemEnchantmentsComponent enchantComponent = stack.getEnchantments();
                var enchants = enchantComponent.getEnchantments();
                for (RegistryEntry<Enchantment> enchant : enchants) {
                    newStack.addEnchantment(enchant, enchantComponent.getLevel(enchant));
                }
                player.getInventory().removeStack(slot);
                player.getInventory().insertStack(slot, newStack);
            }
            WeaponUtil.modifyStackAttributes(stack, this.getAttackDamage() + this.getBonusDamage(stack), this.getAttackSpeed());
        }
    }

    public void addAmount(ItemStack stack, int amount, ComponentType<Integer> soulType) {
        amount += Optional.ofNullable(stack.get(soulType)).orElse(0);
        stack.set(soulType, amount);
    }

    public SoulType getDominantType(ItemStack stack) {
        int blue = Optional.ofNullable(stack.get(ComponentRegistry.BLUE_SOULS)).orElse(0);
        int red = Optional.ofNullable(stack.get(ComponentRegistry.RED_SOULS)).orElse(0);
        if (blue > red) {
            return SoulType.BLUE;
        } else {
            return SoulType.RED;
        }
    }

    private float getBonusDamage(ItemStack stack) {
        if (this.isDisabled(stack)) return 0;
        float soulPercent = (float) this.getSouls(stack) / (float) MAX_SOULS;
        return ConfigConstructor.darkin_scythe_bonus_damage * soulPercent;
    }

    @Override
    public int getSouls(ItemStack stack) {
        int amount = 0;
        amount += Optional.ofNullable(stack.get(ComponentRegistry.BLUE_SOULS)).orElse(0);
        amount += Optional.ofNullable(stack.get(ComponentRegistry.RED_SOULS)).orElse(0);
        return amount;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_darkin_scythe;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return null;
    }

    public enum SoulType {
        BLUE, RED
    }
}