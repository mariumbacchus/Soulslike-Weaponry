package net.soulsweaponry.items;

import com.google.common.collect.Maps;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.LeviathanAxeRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.ParticleEvents;
import net.soulsweaponry.util.ParticleHandler;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class LeviathanAxe extends AxeItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public LeviathanAxe(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.LEVIATHAN_AXE_DAMAGE.get(), pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int sharpness = Mth.floor(EnchantmentHelper.getDamageBonus(stack, target.getMobType()));
        target.addEffect(new MobEffectInstance(EffectRegistry.FREEZING.get(), 200, sharpness));
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        super.releaseUsing(stack, world, user, remainingUseTicks);
        if (user instanceof Player playerEntity) {
            int i = this.getUseDuration(stack) - remainingUseTicks;
            if (i >= 10) {
                stack.hurtAndBreak(3, playerEntity, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
                if (stack.hasTag()) {
                    //stack.getTag().putIntArray(Mjolnir.OWNERS_LAST_POS, new int[]{playerEntity.getBlockX(), playerEntity.getBlockY(), playerEntity.getBlockZ()});TODO add mjolnir
                }
//                LeviathanAxeEntity entity = new LeviathanAxeEntity(world, user, stack);
//                float speed = EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack)/5;
//                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F + speed, 1.0F);
//                entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
//                world.spawnEntity(entity);TODO create leviathan axe projectile
                world.playSound(playerEntity, playerEntity.getOnPos(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1f, .5f);
                if (!playerEntity.isCreative()) {
                    playerEntity.getInventory().removeItem(stack);
                }
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

    public static void iceExplosion(Level world, BlockPos pos, @Nullable Entity attacker, int amplifier) {
        AABB box = new AABB(pos).inflate(1D);
        List<Entity> entities = world.getEntities(attacker, box);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity && !(entity instanceof Player)) {
                livingEntity.hurt(DamageSource.FREEZE, (amplifier + 1) * 1.5f);
                livingEntity.addEffect(new MobEffectInstance(EffectRegistry.FREEZING.get(), 200, amplifier));
            }
        }
        if (!world.isClientSide) {
            ParticleHandler.particleSphere(world, 300, pos.getX(), pos.getY(), pos.getZ(), ParticleEvents.ICE_PARTICLE, 0.75f);
        }
        world.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.HOSTILE, 1f, 1f);
        world.playSound(null, pos, SoundEvents.GLASS_BREAK, SoundSource.HOSTILE, 1f, .5f);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.FREEZE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.PERMAFROST, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.HEAVY_THROW, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.RETURNING, stack, tooltip);
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
            private LeviathanAxeRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new LeviathanAxeRenderer();

                return renderer;
            }
        });
    }
}
