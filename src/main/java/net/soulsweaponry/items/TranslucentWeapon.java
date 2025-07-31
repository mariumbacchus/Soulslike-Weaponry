package net.soulsweaponry.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.TooltipAbilities;

import java.util.Optional;

public class TranslucentWeapon extends ModdedSword {

    public TranslucentWeapon(ToolMaterial toolMaterial, int attackDamage, float ingameAttackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, ingameAttackSpeed, settings);
        this.tooltipAbilities.add(TooltipAbilities.TRANSPARENT);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_translucent_weapons;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        Boolean invisible = stack.get(ComponentRegistry.INVISIBLE);
        if (invisible != null) {
            stack.set(ComponentRegistry.INVISIBLE, !invisible);
        } else {
            stack.set(ComponentRegistry.INVISIBLE, true);
        }
        user.getItemCooldownManager().set(this, 20);
        user.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.8f, 0.75f);
        return TypedActionResult.success(stack);
    }

    public static boolean isInvisible(ItemStack stack) {
        return Optional.ofNullable(stack.get(ComponentRegistry.INVISIBLE)).orElse(false);
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return null;
    }
}
