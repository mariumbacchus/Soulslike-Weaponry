package net.soulsweaponry.items;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.item.BloodthirsterRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Bloodthirster extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Bloodthirster(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.bloodthirster_damage, ConfigConstructor.bloodthirster_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.LIFE_STEAL, WeaponUtil.TooltipAbilities.OVERHEAL);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_bloodthirster;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (attacker instanceof PlayerEntity player) {
            if (!player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
                this.applyItemCooldown(player, Math.max(ConfigConstructor.lifesteal_item_min_cooldown, ConfigConstructor.lifesteal_item_cooldown - this.getReduceLifeStealCooldownEnchantLevel(stack) * 6));
                float healing = ConfigConstructor.lifesteal_item_base_healing;
                if (ConfigConstructor.lifesteal_item_heal_scales) {
                    healing += MathHelper.ceil(((float)WeaponUtil.getEnchantDamageBonus(stack))/2);
                }
                if (player.getHealth() == player.getMaxHealth() && ConfigConstructor.bloodthirster_overshields) {
                    healing = healing - 4 > 0 ? healing - 4 : 0;
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 60 + MathHelper.floor(healing*10), MathHelper.floor(healing)));
                } else if (player.getHealth() < player.getMaxHealth()) {
                    attacker.heal(healing);
                }
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BloodthirsterRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new BloodthirsterRenderer();

                return renderer;
            }
        });
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_bloodthirster;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.lifesteal_item_enchant_reduces_cooldown;
    }

    @Override
    public String getReduceCooldownEnchantId(ItemStack stack) {
        return ConfigConstructor.lifesteal_item_enchant_reduces_cooldown_id;
    }
}