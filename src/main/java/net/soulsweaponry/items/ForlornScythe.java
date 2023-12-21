package net.soulsweaponry.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.ForlornScytheRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ForlornScythe extends SoulHarvestingItem implements IAnimatable {

    private static final String CRITICAL = "3rd_shot";
    private static final String PREV_UUID = "prev_projectile_uuid";
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    //TODO night prowler brukes no drag wither skulls, lag den og implement her med custom max age som du må lage som setter og getter
    public ForlornScythe(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.FORLORN_SCYTHE_DAMAGE.get(), pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack stack = user.getItemInHand(hand);
        if (!world.isClientSide) {
            this.detonatePrevEntity((ServerLevel) world, stack);
        }
        if (stack.hasTag()) {
            if (stack.getTag().contains(KILLS)) {
                int power = this.getSouls(stack);
                if (power > 0) {
                    WitherSkull entity = new WitherSkull(EntityType.WITHER_SKULL, world);
                    entity.setPos(user.getX(), user.getEyeY(), user.getZ());
                    entity.setOwner(user);
                    if (this.isCritical(stack)) {
                        entity.setDangerous(true);
                        stack.getTag().putInt(CRITICAL, 1);
                    } else {
                        stack.getTag().putInt(CRITICAL, stack.getTag().getInt(CRITICAL) + 1);
                    }
                    entity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 3f, 1.0F);
                    world.addFreshEntity(entity);
                    this.setPrevUuid(stack, entity);
                    this.addAmount(stack, -1);
                    user.getCooldowns().addCooldown(this, 10);
                    stack.hurtAndBreak(1, user, (p) -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                    return InteractionResultHolder.success(stack);
                }
            }
        }
        return InteractionResultHolder.fail(stack);
    }

    /**
     * Due to the skulls' drag at 0.95 (and 0.73 for the charged ones), the projectiles WILL get stuck in the air
     * at some point. Therefore, to avoid this, the previous projectile's UUID will be stored into NBT, then
     * checked in the world whether it exists or not, then removed accordingly.
     */
    private void detonatePrevEntity(ServerLevel world, ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(PREV_UUID)) {
            UUID uuid = stack.getTag().getUUID(PREV_UUID);
            Entity entity = world.getEntity(uuid);
            if (entity instanceof WitherSkull) {
                WitherSkull skull = (WitherSkull) entity;
                Explosion.BlockInteraction destructionType = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(world, skull.getOwner()) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
                world.explode(skull, skull.getX(), skull.getY(), skull.getZ(), skull.isDangerous() ? 2f : 1f, false, destructionType);
                skull.discard();
            }
        }
    }

    private void setPrevUuid(ItemStack stack, Entity entityToSet) {
        if (stack.hasTag()) {
            stack.getTag().putUUID(PREV_UUID, entityToSet.getUUID());
        }
    }

    private boolean isCritical(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(CRITICAL)) {
            return stack.getTag().getInt(CRITICAL) >= 3;
        } else {
            stack.getTag().putInt(CRITICAL, 1);
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SOUL_TRAP, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SOUL_RELEASE_WITHER, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.COLLECT, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
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
            private ForlornScytheRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new ForlornScytheRenderer();

                return renderer;
            }
        });
    }
}
