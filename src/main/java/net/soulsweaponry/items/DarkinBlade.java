package net.soulsweaponry.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.DarkinBladeRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
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

public class DarkinBlade extends UltraHeavyWeapon implements IAnimatable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public DarkinBlade(Tier toolMaterial, float attackSpeed, Item.Properties settings) {
        super(toolMaterial, CommonConfig.DARKIN_BLADE_DAMAGE.get(), attackSpeed, settings, true);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
                if (!player.isCreative()) player.getCooldowns().addCooldown(this, CommonConfig.LIFE_STEAL_COOLDOWN.get());
                float healing = CommonConfig.LIFE_STEAL_BASE_HEAL.get();
                if (CommonConfig.LIFE_STEAL_SCALES.get()) {
                    //healing += Mth.ceil(((float)WeaponUtil.getEnchantDamageBonus(stack))/2);TODO add weapon util
                }
                attacker.heal(healing);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player player) {
            int duration = CommonConfig.DARKIN_BLADE_ABILITY_COOLDOWN.get();
            int i = this.getUseDuration(stack) - remainingUseTicks;
            if (i >= 10) {
                Vec3 rotation = player.getViewVector(1f).scale(1f);
                player.setDeltaMovement(player.getDeltaMovement().add(rotation.x(), 1, rotation.z()));
                world.playSound(player, player.getOnPos(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1f, 1f);
                duration = Mth.floor(duration/1.5);
                user.addEffect(new MobEffectInstance(EffectRegistry.CALCULATED_FALL.get(), 600, 0));
            }
            if (stack.hasTag()) {
                stack.getTag().putBoolean(CometSpear.SMASH, true);
            }
            stack.hurtAndBreak(3, player, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
            player.getCooldowns().addCooldown(this, duration);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(stack, world, entity, pSlotId, pIsSelected);
        if (entity instanceof Player player) { //TODO siden nbts legger til refreshes stacken veldig mye som ser rart ut, finn en fiks
            float power = CommonConfig.DARKIN_BLADE_ABILITY_DAMAGE.get();
            CometSpear.detonateGround(player, power, 3, 1.75f, stack, world, true, 25);
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
            return InteractionResultHolder.success(itemStack);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
//            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.OMNIVAMP, stack, tooltip);
//            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SWORD_SLAM, stack, tooltip);
//            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.HEAVY, stack, tooltip);TODO weaponutil
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(pStack, pLevel, tooltip, pIsAdvanced);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event){
        event.getController().setAnimation(new AnimationBuilder().addAnimation("heartbeat", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE; //TODO hvis du klarer å få til sou lreaper animasjon, gjør det også her med at hvis bruker er lav hp kan hjerte dunke fortere
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
            private DarkinBladeRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new DarkinBladeRenderer();

                return renderer;
            }
        });
    }
}
