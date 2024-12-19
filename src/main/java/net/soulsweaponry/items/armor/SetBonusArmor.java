package net.soulsweaponry.items.armor;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.items.ITooltipInfo;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SetBonusArmor extends ModdedArmor implements ITooltipInfo {

    public SetBonusArmor(ArmorMaterial material, Type slot, Settings settings) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!this.isDisabled(stack) && entity instanceof PlayerEntity player) {
            if (this.hasFullSet(player)) {
                for (StatusEffectInstance instance : this.getFullSetEffects()) {
                    player.addStatusEffect(instance);
                }
                this.tickAdditionalSetEffects(stack, player);
            }
        }
    }

    protected boolean hasFullSet(PlayerEntity player) {
        ItemStack boots = player.getInventory().getArmorStack(0);
        ItemStack leggings = player.getInventory().getArmorStack(1);
        ItemStack chestplate = player.getInventory().getArmorStack(2);
        ItemStack helmet = player.getInventory().getArmorStack(3);
        boolean bootsSlot = !boots.isEmpty() && boots.getItem() == this.getMatchingBoots();
        boolean leggingsSlot = !leggings.isEmpty() && leggings.getItem() == this.getMatchingLegs();
        boolean chestSlot = !chestplate.isEmpty() && chestplate.getItem() == this.getMatchingChest();
        boolean helmetSlot = !helmet.isEmpty() && helmet.getItem() == this.getMatchingHead();
        return bootsSlot && leggingsSlot && chestSlot && helmetSlot;
    }

    protected abstract void tickAdditionalSetEffects(ItemStack stack, PlayerEntity player);
    protected abstract Item getMatchingBoots();
    protected abstract Item getMatchingLegs();
    protected abstract Item getMatchingChest();
    protected abstract Item getMatchingHead();
    protected abstract StatusEffectInstance[] getFullSetEffects();
    protected abstract Text[] getCustomTooltips();

    @Override
    public List<WeaponUtil.TooltipAbilities> getTooltipAbilities() {
        return List.of();
    }

    @Override
    public void addTooltipAbility(WeaponUtil.TooltipAbilities... abilities) {

    }

    //TODO can still make this cleaner by using ITooltipInfo and TooltipAbilities
    @Override
    public Text[] getAdditionalTooltips() {
        List<Text> tooltip = new ArrayList<>();
        tooltip.add(Text.translatable("tooltip.soulsweapons.armor.set_bonus").formatted(Formatting.AQUA));
        for (StatusEffectInstance effect : this.getFullSetEffects()) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.armor.set_bonus.gain_effects").append(effect.getEffectType().getName()).formatted(Formatting.GRAY));
        }
        tooltip.addAll(Arrays.asList(this.getCustomTooltips()));
        return tooltip.toArray(new Text[0]);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        this.appendTooltipAbilities(stack, world, tooltip, context);
    }
}