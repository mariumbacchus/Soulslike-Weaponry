package net.soulsweaponry.items;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.ChaosOrbEntity;
import net.soulsweaponry.registry.EntityRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChaosOrb extends Item implements IConfigDisable {

    public ChaosOrb(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.isDisabled(stack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(stack);
        }
        if (!world.isClient) {
            ChaosOrbEntity orb = new ChaosOrbEntity(EntityRegistry.CHAOS_ORB_ENTITY.get(), world);
            orb.setPosition(user.getX(), user.getEyeY(), user.getZ());
            world.emitGameEvent(user, GameEvent.PROJECTILE_SHOOT, orb.getBlockPos());
            world.spawnEntity(orb);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_EYE_LAUNCH, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!user.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            user.swingHand(hand, true);
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.consume(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (this.isDisabled(stack)) {
            tooltip.add(Text.translatableWithFallback("tooltip.soulsweapons.disabled","Disabled"));
        }
        super.appendTooltip(stack, world, tooltip, context);
        for (int i = 1; i < 3 + 1; i++) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.chaos_orb." + i).formatted(Formatting.DARK_GRAY));
        }
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_chaos_orb;
    }
}