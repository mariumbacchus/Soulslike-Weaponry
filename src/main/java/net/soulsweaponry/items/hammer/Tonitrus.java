package net.soulsweaponry.items.hammer;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.soulsweaponry.client.renderer.item.TonitrusRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.ChainLightning;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Tonitrus extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public Tonitrus(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.tonitrus_damage, ConfigConstructor.tonitrus_attack_speed, settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.addTooltipAbility(TooltipAbilities.STORMVEIL);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack) && attacker.hasStatusEffect(EffectRegistry.STORMVEIL)) {
            StatusEffectInstance instance = attacker.getStatusEffect(EffectRegistry.STORMVEIL);
            int amp = instance.getAmplifier();
            float radius = ConfigConstructor.tonitrus_stormveil_chain_lightning_range_per_amp * (amp + 1);
            float damage = ConfigConstructor.tonitrus_stormveil_chain_lightning_damage_per_amp * (amp + 1);
            ChainLightning.trigger(attacker.getWorld(), target, attacker, true, damage, radius);
            if (stack.hasNbt() && stack.getNbt().contains("Empowered") && stack.getNbt().getBoolean("Empowered")) {
                LightningEntity lightningEntity = new LightningEntity(EntityType.LIGHTNING_BOLT, attacker.getWorld());
                lightningEntity.setPos(target.getX(), target.getY(), target.getZ());
                attacker.getWorld().spawnEntity(lightningEntity);
                stack.getOrCreateNbt().putBoolean("Empowered", false);
                attacker.removeStatusEffect(EffectRegistry.STORMVEIL);
                // Reduce Stormveil to 20 ticks (originally remove, but want to keep immunity to lightning for a second still)
                attacker.addStatusEffect(new StatusEffectInstance(EffectRegistry.STORMVEIL, 20));
                if (target instanceof PlayerEntity player) {
                    this.applyEffectCooldown(player, (int) Math.max(ConfigConstructor.tonitrus_ability_max_cooldown, ConfigConstructor.tonitrus_ability_cooldown - this.getReduceCooldownEnchantLevel(stack) * 10));
                }
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!user.hasStatusEffect(EffectRegistry.COOLDOWN)) {
            if (user.hasStatusEffect(EffectRegistry.STORMVEIL)) {
                if (stack.hasNbt() && stack.getNbt().contains("Empowered") && !stack.getNbt().getBoolean("Empowered")) {
                    stack.getOrCreateNbt().putBoolean("Empowered", true);
                    return TypedActionResult.consume(stack);
                }
                stack.getOrCreateNbt().putBoolean("Empowered", true);
                return TypedActionResult.pass(stack);
            } else {
                stack.getOrCreateNbt().putBoolean("Empowered", false);
                stack.damage(1, user, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                int amp = MathHelper.ceil( ConfigConstructor.tonitrus_stormveil_effect_base_amp + WeaponUtil.getEnchantDamageBonus(stack) * ConfigConstructor.tonitrus_stormveil_effect_amp_per_sharpness_enchant_ceiled);
                user.addStatusEffect(new StatusEffectInstance(EffectRegistry.STORMVEIL, (int) ConfigConstructor.tonitrus_stormveil_effect_duration, amp));
                world.playSound(null, user.getBlockPos(), SoundRegistry.STORMVEIL_TRIGGER, SoundCategory.PLAYERS, 1f, 1f);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.fail(stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player && world instanceof ServerWorld serverWorld && player.age % 10 == 0) {
            if (player.hasStatusEffect(EffectRegistry.STORMVEIL)) {
                this.triggerAnim(player, GeoItem.getOrAssignId(stack, serverWorld), "sparks", "charged_bounce");
            } else {
                this.triggerAnim(player, GeoItem.getOrAssignId(stack, serverWorld), "sparks", "idle");
            }
        }
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_tonitrus;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_tonitrus;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.tonitrus_ability_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.tonitrus_ability_enchant_reduces_cooldown_ids;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private final TonitrusRenderer renderer = new TonitrusRenderer();

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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "sparks", state -> PlayState.STOP)
                .triggerableAnim("charged_bounce", RawAnimation.begin().thenPlay("charged_bounce"))
                .triggerableAnim("idle", RawAnimation.begin().thenPlay("idle"))
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }
}
