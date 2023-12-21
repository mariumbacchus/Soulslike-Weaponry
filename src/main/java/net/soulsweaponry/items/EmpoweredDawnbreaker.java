package net.soulsweaponry.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.EmpoweredDawnbreakerRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EmpoweredDawnbreaker extends AbstractDawnbreaker implements IKeybindAbility {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public EmpoweredDawnbreaker(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.EMPOWERED_DAWNBREAKER_DAMAGE.get(), pAttackSpeedModifier, pProperties);
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
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player player && !player.getCooldowns().isOnCooldown(this)) {
            int i = this.getUseDuration(stack) - remainingUseTicks;
            if (i >= 10) {
                stack.hurtAndBreak(1, player, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
                this.summonFlamePillars(world, stack, user);
                if (!player.isCreative()) {
                    player.getCooldowns().addCooldown(this, CommonConfig.EMPOWERED_DAWNBREAKER_ABILITY_COOLDOWN.get());
                }
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0));
            }
        }
    }

    private void summonFlamePillars(Level world, ItemStack stack, LivingEntity user) {
        if (!world.isClientSide) {
            int i = 0;
            List<BlockPos> list = new ArrayList<>();
            list.add(new BlockPos(0, 0, 0));
//            while (i < 20 + 3 * WeaponUtil.getEnchantDamageBonus(stack)) {TODO add flame pillar
//                int x = user.getBlockX() + user.getRandom().nextInt(12) - 6;
//                int y = user.getBlockY();
//                int z = user.getBlockZ() + user.getRandom().nextInt(12) - 6;
//                BlockPos pos = new BlockPos(x, y, z);
//                for (BlockPos listPos : list) {
//                    if (listPos != pos) {
//                        FlamePillar pillar = new FlamePillar(EntityRegistry.FLAME_PILLAR, world);
//                        pillar.setDamage(ConfigConstructor.empowered_dawnbreaker_ability_damage + WeaponUtil.getEnchantDamageBonus(stack) * 2);
//                        pillar.setPos(x, y, z);
//                        pillar.setOwner(user);
//                        pillar.setWarmup(i * 2);
//                        world.spawnEntity(pillar);
//                        i++;
//                    }
//                }
//            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.DAWNBREAKER, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.BLAZING_BLADE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.CHAOS_STORM, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.VEIL_OF_FIRE, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
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
        if (!player.getCooldowns().isOnCooldown(this)) {
            AbstractDawnbreaker.dawnbreakerEvent(player, player, stack);
            player.addEffect(new MobEffectInstance(EffectRegistry.VEIL_OF_FIRE.get(), 200, Mth.floor(WeaponUtil.getEnchantDamageBonus(stack)/2f)));
            player.getCooldowns().addCooldown(this, CommonConfig.EMPOWERED_DAWNBREAKER_ABILITY_COOLDOWN.get());
        }
        /*
        NOTE: Used to summon an orb of fireballs that shoots outwards from the player, but was a little
        too laggy with the particles from the explosion.

        double phi = Math.PI * (3. - Math.sqrt(5.));
        float points = 90;
        for (int i = 0; i < points; i++) {
            double y = 1 - (i/(points - 1)) * 2;
            double radius = Math.sqrt(1 - y*y);
            double theta = phi * i;
            double x = Math.cos(theta) * radius;
            double z = Math.sin(theta) * radius;
            AgingSmallFireball entity = new AgingSmallFireball(world, player, x, y, z);
            entity.setPos(player.getX(), player.getEyeY(), player.getZ());
            world.spawnEntity(entity);
        }
         */
    }

    @Override
    public void useKeybindAbilityClient(ClientLevel world, ItemStack stack, LocalPlayer player) {
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private EmpoweredDawnbreakerRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new EmpoweredDawnbreakerRenderer();

                return renderer;
            }
        });
    }
}
