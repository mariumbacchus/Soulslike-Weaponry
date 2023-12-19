package net.soulsweaponry.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.soulsweaponry.registry.SoundRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SkofnungStone extends Item {

    public SkofnungStone(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stoneStack = user.getItemInHand(hand);
        boolean shouldDamage = false;
        for (InteractionHand offHand : InteractionHand.values()) {
            ItemStack swordStack = user.getItemInHand(offHand);
            if (/*swordStack.getItem() instanceof Skofnung && */swordStack.hasTag()) {//TODO make sword
                //swordStack.getTag().putInt(Skofnung.EMPOWERED, ConfigConstructor.skofnung_stone_additional_empowered_strikes);TODO make config and skofnung sword
                shouldDamage = true;
                world.playSound(user, user.getOnPos(), SoundRegistry.SHARPEN_EVENT.get(), SoundSource.PLAYERS, .5f, 1f);
                world.playSound(user, user.getOnPos(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, .5f, .5f);
            }
        }
        for (MobEffect effect : ForgeRegistries.MOB_EFFECTS) {
            if (effect.getCategory().equals(MobEffectCategory.HARMFUL) && user.hasEffect(effect)) {
                user.removeEffect(effect);
                world.playSound(user, user.getOnPos(), SoundRegistry.RESTORE_EVENT.get(), SoundSource.PLAYERS, 1f, 1f);
                shouldDamage = true;
            }
        }

        if (shouldDamage) {
            stoneStack.hurt(1, user.getRandom(), user instanceof ServerPlayer ? (ServerPlayer)user : null);
            return InteractionResultHolder.success(stoneStack);
        } else {
            return InteractionResultHolder.fail(stoneStack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            //WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.DISABLE_DEBUFS, stack, tooltip);TODDO make weapon util
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }
}
