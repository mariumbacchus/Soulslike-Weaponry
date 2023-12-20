package net.soulsweaponry.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.FreyrSwordItemRenderer;
import net.soulsweaponry.config.CommonConfig;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class FreyrSword extends SwordItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final EntityDataAccessor<Optional<UUID>> SUMMON_UUID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.OPTIONAL_UUID);

    public FreyrSword(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.SWORD_OF_FREYR_DAMAGE.get(), pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
//        FreyrSwordEntity entity = new FreyrSwordEntity(world, user, stack);TODO add FreyrSwordEntity
//        Optional<UUID> uuid = Optional.of(entity.getUuid());
//        try {
//            if (user.getEntityData().get(SUMMON_UUID).isPresent() && world instanceof ServerLevel) {
//                Entity sword = ((ServerLevel)world).getEntity(user.getEntityData().get(SUMMON_UUID).get());
//                if (sword instanceof FreyrSwordEntity) {
//                    return InteractionResultHolder.fail(stack);
//                } else {
//                    user.getInventory().removeItem(stack);
//                    entity.setPos(user.getX(), user.getY(), user.getZ());
//                    user.playSound(SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.PLAYERS, 1f, 1f);
//                    entity.setStationaryPos(FreyrSwordEntity.NULLISH_POS);
//                    world.addFreshEntity(entity);
//                }
//            }
//            user.getEntityData().set(SUMMON_UUID, uuid);
//        } catch (Exception e) {
//            user.getEntityData().define(SUMMON_UUID, uuid);
//        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            //WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SUMMON_WEAPON, stack, tooltip);TODO weaponutil
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(pStack, pLevel, tooltip, pIsAdvanced);
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private FreyrSwordItemRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new FreyrSwordItemRenderer();

                return renderer;
            }
        });
    }
}
