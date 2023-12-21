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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.CometSpearItemRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class CometSpear extends SwordItem implements IAnimatable {

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final String SMASH = "ground_slam";
    public static final String FALL_DISTANCE = "fall_distance";

    public CometSpear(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.COMET_SPEAR_DAMAGE.get(), pAttackSpeedModifier, pProperties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player playerEntity) {
            int i = this.getUseDuration(stack) - remainingUseTicks;
            if (i >= 10) {
                float enchant = WeaponUtil.getEnchantDamageBonus(stack);
                if (stack == user.getOffhandItem()) {
                    float f = user.getYRot();
                    float g = user.getXRot();
                    float h = -Mth.sin(f * 0.017453292F) * Mth.cos(g * 0.017453292F);
                    float k = -Mth.sin(g * 0.017453292F);
                    float l = Mth.cos(f * 0.017453292F) * Mth.cos(g * 0.017453292F);
                    float m = Mth.sqrt(h * h + k * k + l * l);
                    float n = 3.0F * ((5.0F + enchant) / 4.0F);
                    h *= n / m;
                    k *= n / m;
                    l *= n / m;

                    user.setDeltaMovement(user.getDeltaMovement().add(h, k, l));
                    playerEntity.startAutoSpinAttack(20);
                    world.playSound(null, playerEntity, SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (playerEntity.isOnGround()) {
                        playerEntity.move(MoverType.SELF, new Vec3(0.0D, 1.1999999F, 0.0D));
                    }
                    user.addEffect(new MobEffectInstance(EffectRegistry.CALCULATED_FALL.get(), 600, 0));
                    playerEntity.getCooldowns().addCooldown(this, (int) (CommonConfig.COMET_SPEAR_SKYFALL_COOLDOWN.get() - enchant*20));
                    if (stack.hasTag()) {
                        stack.getTag().putBoolean(SMASH, true);
                    }
                    stack.hurtAndBreak(4, playerEntity, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
                } else {
                    stack.hurtAndBreak(2, playerEntity, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
                    playerEntity.getCooldowns().addCooldown(this, (int) (CommonConfig.COMET_SPEAR_THROW_COOLDOWN.get() - enchant * 5));

//                    CometSpearEntity entity = new CometSpearEntity(world, playerEntity, stack);
//                    entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 5.0F, 1.0F);
//                    entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
//                    world.spawnEntity(entity); TODO add projectile
//                    world.playSoundFromEntity((PlayerEntity)null, entity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(stack, world, entity, pSlotId, pIsSelected);
        if (entity instanceof Player player) {
            float power = CommonConfig.COMET_SPEAR_ABILITY_DAMAGE.get();
            CometSpear.detonateGround(player, power, 0, 2, stack, world, false, 35);
        }
    }

    public static void detonateGround(Player player, float power, float baseExpansion, float expansionModifier, ItemStack stack, Level world, boolean shouldHeal, float launchDivisor) {
        if (!player.isOnGround() && stack.hasTag() && stack.getTag().contains(SMASH) && stack.getTag().getBoolean(SMASH)) {
            stack.getTag().putFloat(FALL_DISTANCE, player.fallDistance);
        }
        if (stack.hasTag() && stack.getTag().contains(SMASH) && stack.getTag().contains(FALL_DISTANCE) && stack.getTag().getBoolean(SMASH) && player.isOnGround()) {
            float expansion = baseExpansion + expansionModifier * (stack.getTag().getFloat(FALL_DISTANCE)/10);
            power += stack.getTag().getFloat(FALL_DISTANCE)/5;
            AABB box = player.getBoundingBox().inflate(expansion);
            List<Entity> entities = world.getEntities(player, box);
            for (Entity targets : entities) {
                if (targets instanceof LivingEntity livingEntity) {
                    livingEntity.hurt(DamageSource.ANVIL/*CustomDamageSource.obliterateDamageSource(player)*/, power + EnchantmentHelper.getDamageBonus(stack, livingEntity.getMobType()));//TODO add custom damage source
                    livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(0, stack.getTag().getFloat(FALL_DISTANCE)/launchDivisor, 0));
                    if (shouldHeal) player.heal(CommonConfig.LIFE_STEAL_BASE_HEAL.get() - 1 + (CommonConfig.LIFE_STEAL_SCALES.get() ? WeaponUtil.getEnchantDamageBonus(stack)/2 : 0));
                }
            }
            world.playSound(null, player.getOnPos(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1f, 1f);

            double pDistance = stack.getTag().getFloat(FALL_DISTANCE) >= 25 ? stack.getTag().getFloat(FALL_DISTANCE)/25 : 1;
            if (!world.isClientSide) {
                //ParticleNetworking.specificServerParticlePacket((ServerWorld) world, PacketRegistry.GRAND_SKYFALL_SMASH_ID, player.getBlockPos(), pDistance);TODO figure out particle managing
            }
            //Reset nbts
            stack.getTag().putFloat(FALL_DISTANCE, 0);
            stack.getTag().putBoolean(SMASH, false);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
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
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SKYFALL, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.INFINITY, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.CRIT, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
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
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private CometSpearItemRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new CometSpearItemRenderer();

                return renderer;
            }
        });
    }
}
