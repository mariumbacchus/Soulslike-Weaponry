package net.soulsweaponry.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.DraupnirSpearItemRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.IKeybindAbility;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DraupnirSpear extends SwordItem implements IAnimatable, IKeybindAbility {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final String SPEARS_ID = "thrown_spears_id";

    public DraupnirSpear(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.DRAUPNIR_DAMAGE.get(), pAttackSpeedModifier, pProperties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player playerEntity) {
            int i = this.getUseDuration(stack) - remainingUseTicks;
            if (i >= 10) {
                int enchant = WeaponUtil.getEnchantDamageBonus(stack);
//                DraupnirSpearEntity entity = new DraupnirSpearEntity(world, playerEntity, stack);TODO add draupnir spear entity
//                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 5.0F, 1.0F);
//                entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
//                world.spawnEntity(entity);
//                world.playSoundFromEntity(null, entity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (stack.hasTag()) {
                    List<Integer> ids = new ArrayList<>();
                    if (stack.getTag().contains(SPEARS_ID)) {
                        int[] arr = stack.getTag().getIntArray(SPEARS_ID);
                        ids = WeaponUtil.arrayToList(arr);
                    }
                    //ids.add(entity.getId());TODO draupnir entity
                    stack.getTag().putIntArray(SPEARS_ID, ids);
                }
                playerEntity.getCooldowns().addCooldown(this, CommonConfig.DRAUPNIR_THROW_COOLDOWN.get() - enchant * 5);
                stack.hurtAndBreak(1, playerEntity, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemStack);
        }
        else {
            user.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public void useKeybindAbilityServer(ServerLevel world, ItemStack stack, Player player) {
        if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
            AABB box = player.getBoundingBox().inflate(3);
            List<Entity> entities = world.getEntities(player, box);
            float power = CommonConfig.DRAUPNIR_PROJECTILE_DAMAGE.get();
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity) {
                    entity.hurt(DamageSource.mobAttack(player), power + EnchantmentHelper.getDamageBonus(stack, ((LivingEntity) entity).getMobType()));
                    entity.setDeltaMovement(entity.getDeltaMovement().add(0, .1f, 0));
                }
            }
            ParticleHandler.particleOutburstMap(world, 200, player.getX(), player.getY(), player.getZ(), ParticleEvents.GRAND_SKYFALL_MAP, 0.5f);
            world.playSound(null, player.getOnPos(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1f, 1f);
            player.getCooldowns().addCooldown(stack.getItem(), CommonConfig.DRAUPNIR_DETONATE_COOLDOWN.get());
            if (stack.hasTag() && stack.getTag().contains(DraupnirSpear.SPEARS_ID)) {
                int[] ids = stack.getTag().getIntArray(DraupnirSpear.SPEARS_ID);
                for (int id : ids) {
                    Entity entity = world.getEntity(id);
//                    if (entity instanceof DraupnirSpearEntity spear) {TODO make darpnir spear entity
//                        spear.detonate();
//                    }
                }
                stack.getTag().putIntArray(DraupnirSpear.SPEARS_ID, new int[0]);
            }
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientLevel world, ItemStack stack, LocalPlayer player) {

    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event){
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
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
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.INFINITY, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.DETONATE_SPEARS, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private DraupnirSpearItemRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new DraupnirSpearItemRenderer();

                return renderer;
            }
        });
    }
}
