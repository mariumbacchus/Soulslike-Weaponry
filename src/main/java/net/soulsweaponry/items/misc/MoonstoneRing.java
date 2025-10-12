package net.soulsweaponry.items.misc;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

public class MoonstoneRing extends ModdedItem {

    public MoonstoneRing(Settings settings) {
        super(settings);
        this.addTooltipAbility(TooltipAbilities.LUNAR_HERALD);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.isDisabled(stack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(stack);
        }
        if (!user.hasStatusEffect(EffectRegistry.MOON_HERALD)) {
            user.addStatusEffect(new StatusEffectInstance(EffectRegistry.MOON_HERALD, (int) ConfigConstructor.moonstone_ring_lunar_herald_duration, (int) (ConfigConstructor.moonstone_ring_lunar_herald_base_amplifier + WeaponUtil.getLevel(stack, Enchantments.UNBREAKING))));
            stack.damage(1, user, LivingEntity.getSlotForHand(hand));
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, 1f, 1f);
            return TypedActionResult.success(stack, world.isClient());
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_moonstone_ring;
    }
}
