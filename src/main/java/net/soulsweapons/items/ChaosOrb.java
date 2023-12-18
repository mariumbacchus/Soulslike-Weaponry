package net.soulsweapons.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChaosOrb extends Item {

    public ChaosOrb(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (!world.isClientSide) {
//            ChaosOrbEntity orb = new ChaosOrbEntity(EntityRegistry.CHAOS_ORB_ENTITY, world);
//            orb.setPosition(user.getX(), user.getEyeY(), user.getZ());
//            world.emitGameEvent(user, GameEvent.PROJECTILE_SHOOT, orb.getBlockPos());
//            world.spawnEntity(orb);TODO make chaos orb entity
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!user.isCreative()) {
                stack.shrink(1);
            }
            user.awardStat(Stats.ITEM_USED.get(this));
            user.swing(hand, true);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, tooltip, pIsAdvanced);
        for (int i = 1; i < 3 + 1; i++) {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.chaos_orb." + i).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
