package net.soulsweaponry.items.abilities.useonentity;

import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.soulsweaponry.entity.mobs.BigChungus;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;

public record TameChungus() implements IAbility {

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof BigChungus chungus && !chungus.isTamed()) {
            chungus.setTamed(true, false);
            chungus.setOwner(user);
            chungus.setTarget(null);
            chungus.getWorld().sendEntityStatus(chungus, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
            chungus.getNavigation().stop();
            return ActionResult.SUCCESS;
        }
        return ActionResult.FAIL;
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.chungus_whisperer").formatted(Formatting.GREEN).formatted(Formatting.ITALIC),
                Text.translatable("tooltip.soulsweapons.chungus_whisperer.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.chungus_whisperer.2").formatted(Formatting.GRAY)
        );
    }
}
