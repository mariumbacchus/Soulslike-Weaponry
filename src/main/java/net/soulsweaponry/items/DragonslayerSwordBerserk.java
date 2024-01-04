package net.soulsweaponry.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DragonslayerSwordBerserk extends UltraHeavyWeapon implements IKeybindAbility {

    public DragonslayerSwordBerserk(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.GUTS_SWORD_DAMAGE.get(), pAttackSpeedModifier, pProperties, true);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.RAGE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.HEAVY, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }

    @Override
    public void useKeybindAbilityServer(ServerLevel world, ItemStack stack, Player player) {
        if (!player.getCooldowns().isOnCooldown(this)) {
            stack.hurtAndBreak(1, player, (p_43296_) -> p_43296_.broadcastBreakEvent(player.getUsedItemHand()));
            player.getCooldowns().addCooldown(this, CommonConfig.GUTS_SWORD_ABILITY_COOLDOWN.get());
            int power = Mth.floor(WeaponUtil.getEnchantDamageBonus(stack));
            player.addEffect(new MobEffectInstance(EffectRegistry.BLOODTHIRSTY.get(), 200, power));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0));

            world.playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, .75f, 1f);
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientLevel world, ItemStack stack, LocalPlayer player) {
    }
}
