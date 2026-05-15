package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.ArrayList;
import java.util.List;

public record Block(float magicDamageReductionWhenBlocking) implements IAbility {

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public float modifyUserDamageTaken(LivingEntity user, float damageTaken, DamageSource source, ItemStack stack, Hand hand) {
        if (user.isBlocking()) {
            damageTaken *= 1f - this.magicDamageReductionWhenBlocking;
        }
        return damageTaken;
    }

    @Override
    public UseAction getUseAction() {
        return UseAction.BLOCK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 7200;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        List<Text> tooltip = new ArrayList<>();
        tooltip.add(Text.translatable("tooltip.soulsweapons.block").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("tooltip.soulsweapons.block.1").formatted(Formatting.GRAY));
        float reduction = 1f - this.magicDamageReductionWhenBlocking;
        if (reduction > 0f) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.block.2", String.format("%.0f", this.magicDamageReductionWhenBlocking * 100) + "%").formatted(Formatting.GRAY));
        }
        return tooltip;
    }
}
