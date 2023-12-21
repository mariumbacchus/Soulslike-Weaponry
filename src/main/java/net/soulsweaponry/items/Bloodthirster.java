package net.soulsweaponry.items;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.BloodthirsterRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class Bloodthirster extends SwordItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Bloodthirster(Tier pTier, float attackSpeed, Properties pProperties) {
        super(pTier, CommonConfig.BLOODTHIRSTER_DAMAGE.get(), attackSpeed, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
                if (!player.isCreative()) player.getCooldowns().addCooldown(this, CommonConfig.LIFE_STEAL_COOLDOWN.get());
                float healing = CommonConfig.LIFE_STEAL_BASE_HEAL.get();
                if (CommonConfig.LIFE_STEAL_SCALES.get()) {
                    healing += Mth.ceil(((float) WeaponUtil.getEnchantDamageBonus(stack))/2);
                }
                if (player.getHealth() == player.getMaxHealth() && CommonConfig.BLOODTHIRSTER_OVERSHIELD.get()) {
                    healing = healing - 4 > 0 ? healing - 4 : 0;
                    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 60 + Mth.floor(healing*10), Mth.floor(healing)));
                } else if (player.getHealth() < player.getMaxHealth()) {
                    attacker.heal(healing);
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.LIFE_STEAL, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.OVERHEAL, stack, tooltip);
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
            private BloodthirsterRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new BloodthirsterRenderer();

                return renderer;
            }
        });
    }
}
