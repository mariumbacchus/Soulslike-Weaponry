package net.soulsweaponry.items;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.MathHelper;
import net.soulsweaponry.client.renderer.item.BloodthirsterRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Bloodthirster extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

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
                this.applyCooldown(player, Math.max(ConfigConstructor.lifesteal_item_min_cooldown, ConfigConstructor.lifesteal_item_cooldown - this.getReduceCooldownEnchantLevel(stack) * 6));
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
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private final BloodthirsterRenderer renderer = new BloodthirsterRenderer();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_bloodthirster;
    }

    @Override
    public int getReduceCooldownEnchantLevel(ItemStack stack) {
        if (ConfigConstructor.lifesteal_item_damage_enchant_reduces_cooldown) {
            return WeaponUtil.getEnchantDamageBonus(stack);
        }
        return 0;
    }
}