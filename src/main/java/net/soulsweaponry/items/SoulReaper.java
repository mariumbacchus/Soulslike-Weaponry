package net.soulsweaponry.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.SoulReaperRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleEvents;
import net.soulsweaponry.util.ParticleHandler;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class SoulReaper extends SoulHarvestingItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    //TODO sjekk geckolib wiki om ISyncable, kan gi soul reaper custom animasjon som ideen var tidlegere, se også pistol example class geckolib lagde
    public SoulReaper(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.SOULREAPER_DAMAGE.get(), pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.hasTag() && stack.getTag().contains(KILLS)) {
            int power = this.getSouls(stack);
            if (power >= 3) {
                Vec3 vecBlocksAway = player.getViewVector(1f).scale(3).add(player.position());
                ParticleHandler.particleOutburstMap(world, 50, vecBlocksAway.x(), vecBlocksAway.y(), vecBlocksAway.z(), ParticleEvents.CONJURE_ENTITY_MAP, 1f);
                world.playSound(player, player.getOnPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT.get(), SoundSource.PLAYERS, 0.8f, 1f);
//                if (power < 10) {
//                    SoulReaperGhost entity = new SoulReaperGhost(EntityRegistry.SOUL_REAPER_GHOST, world);
//                    entity.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
//                    entity.setOwner(player);
//                    world.spawnEntity(entity);
//                    this.addAmount(stack, -3);
//                } else if (player.isSneaking() || power < 30) {
//                    Forlorn entity = new Forlorn(EntityRegistry.FORLORN, world);
//                    entity.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
//                    entity.setOwner(player);
//                    world.spawnEntity(entity);
//                    this.addAmount(stack, -10);
//                } else {
//                    Soulmass entity = new Soulmass(EntityRegistry.SOULMASS, world);
//                    entity.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
//                    entity.setOwner(player);
//                    world.spawnEntity(entity);
//                    this.addAmount(stack, -30);
//                }TODO add entities

                stack.hurtAndBreak(3, player, (p_43296_) -> p_43296_.broadcastBreakEvent(player.getUsedItemHand()));
                return InteractionResultHolder.success(stack);
            }
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SOUL_TRAP, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SOUL_RELEASE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.COLLECT, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("low_souls", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private SoulReaperRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new SoulReaperRenderer();

                return renderer;
            }
        });
    }
}
