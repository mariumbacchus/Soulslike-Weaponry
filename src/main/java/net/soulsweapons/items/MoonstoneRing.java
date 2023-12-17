package net.soulsweapons.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.soulsweapons.registry.EffectRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MoonstoneRing extends Item {

    public MoonstoneRing(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (!user.hasEffect(EffectRegistry.MOON_HERALD.get()) && !world.isClientSide) {
            user.addEffect(new MobEffectInstance(EffectRegistry.MOON_HERALD.get(), 600, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack)));
            stack.hurt(1, user.getRandom(), user instanceof ServerPlayer ? (ServerPlayer)user : null);
            world.playSound(null, user.getOnPos(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.PLAYERS, 1f, 1f);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);//TODO add functionality to moon herald effect via moonlight shortsword
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            //WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.LUNAR_HERALD, stack, tooltip); TODO add weaponutil
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }
}
