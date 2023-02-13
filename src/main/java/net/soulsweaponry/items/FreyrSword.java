package net.soulsweaponry.items;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.data.DataTracker.Entry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.FreyrSwordEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.network.ISyncable;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class FreyrSword extends SwordItem implements IAnimatable, ISyncable {

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final TrackedData<Optional<UUID>> SUMMON_UUID = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    public FreyrSword(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.sword_of_freyr_damage, attackSpeed, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        FreyrSwordEntity entity = new FreyrSwordEntity(world, user, stack);
        Optional<UUID> uuid = Optional.of(entity.getUuid());
        boolean isAlreadyTracking = false;
        for (Entry<?> entry : user.getDataTracker().getAllEntries()) {
            if (entry.getData() == SUMMON_UUID) {
                isAlreadyTracking = true;
                if (user.getDataTracker().get(SUMMON_UUID).isPresent() && world instanceof ServerWorld) {
                    Entity sword = ((ServerWorld)world).getEntity(user.getDataTracker().get(SUMMON_UUID).get());
                    if (sword != null && sword instanceof FreyrSwordEntity) {
                        return TypedActionResult.fail(stack);
                    } else {
                        user.getInventory().removeOne(stack);
                        entity.setPos(user.getX(), user.getY(), user.getZ());
                        user.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, SoundCategory.PLAYERS, 1f, 1f);
                        entity.setStationaryPos(FreyrSwordEntity.NULLISH_POS);
                        world.spawnEntity(entity);
                    }
                }
            }
        }
        if (!isAlreadyTracking) {
            user.getDataTracker().startTracking(SUMMON_UUID, uuid);
        } else {
            user.getDataTracker().set(SUMMON_UUID, uuid);
        }
        
        return TypedActionResult.success(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword").formatted(Formatting.AQUA));
            tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_1").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_2").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_3").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_4").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_description_5").formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_1").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
            tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_2").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
            tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_3").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
            tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_4").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
            tooltip.add(Text.translatable("tooltip.soulsweapons.freyr_sword_note_5").formatted(Formatting.DARK_GRAY, Formatting.ITALIC));
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void registerControllers(AnimationData data) {        
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onAnimationSync(int id, int state) {        
    }
}
