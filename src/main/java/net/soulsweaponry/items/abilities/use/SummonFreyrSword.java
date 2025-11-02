package net.soulsweaponry.items.abilities.use;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import net.soulsweaponry.entitydata.FreyrSwordSummonData;
import net.soulsweaponry.items.ITooltipInfo;
import net.soulsweaponry.items.abilities.IAbility;

import java.util.List;
import java.util.UUID;

public class SummonFreyrSword implements IAbility {

    //TODO make FreyrSwordEntity more dynamic/open up for damage changes and even model changes
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        FreyrSwordEntity entity = new FreyrSwordEntity(world, user, stack);
        UUID uuid = entity.getUuid();
        UUID prevUuid = FreyrSwordSummonData.getSummonUuid(user);
        if (world instanceof ServerWorld serverWorld) {
            if (prevUuid == null) {
                FreyrSwordSummonData.setSummonUuid(user, uuid);
                prevUuid = uuid;
            }
            Entity sword = serverWorld.getEntity(prevUuid);
            if (sword instanceof FreyrSwordEntity) {
                return TypedActionResult.fail(stack);
            } else {
                user.getInventory().removeOne(stack);
                entity.setPos(user.getX(), user.getY(), user.getZ());
                entity.setStationaryPos(FreyrSwordEntity.NULLISH_POS);
                world.spawnEntity(entity);
            }
            FreyrSwordSummonData.setSummonUuid(user, uuid);
        }
        user.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1f, 1f);
        return TypedActionResult.success(stack);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.summon_weapon").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.summon_weapon.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.summon_weapon.2", ITooltipInfo.formatKeybindText(KeyBindRegistry.returnFreyrSword.getBoundKeyLocalizedText())).formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.summon_weapon.3", ITooltipInfo.formatKeybindText(KeyBindRegistry.stationaryFreyrSword.getBoundKeyLocalizedText())).formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.summon_weapon.4").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.summon_weapon.5").formatted(Formatting.DARK_GRAY)
        );
    }
}
