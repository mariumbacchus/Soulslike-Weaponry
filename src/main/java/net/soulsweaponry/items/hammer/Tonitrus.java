package net.soulsweaponry.items.hammer;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.soulsweaponry.client.renderer.item.TonitrusRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.use.Stormveil;
import net.soulsweaponry.items.abilities.posthit.StormveilSurge;
import net.soulsweaponry.items.abilities.userdamaged.ElectricCherry;
import net.soulsweaponry.registry.EffectRegistry;
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

    private static final StormveilSurge STORMVEIL_SURGE = new StormveilSurge(
            ConfigConstructor.tonitrus_stormveil_chain_lightning_range_per_amp, ConfigConstructor.tonitrus_stormveil_chain_lightning_damage_per_amp,
            (int) ConfigConstructor.tonitrus_ability_min_cooldown, (int) ConfigConstructor.tonitrus_ability_cooldown, (int) ConfigConstructor.tonitrus_ability_reduced_cooldown_per_level
    );
    private static final Stormveil STORMVEIL = new Stormveil(
            (int) ConfigConstructor.tonitrus_stormveil_effect_base_amp,
            ConfigConstructor.tonitrus_stormveil_effect_amp_per_sharpness_enchant_ceiled,
            (int) ConfigConstructor.tonitrus_stormveil_effect_duration
    );

    public Tonitrus(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.tonitrus_damage, ConfigConstructor.tonitrus_attack_speed, settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.addAbility(STORMVEIL, STORMVEIL_SURGE, ElectricCherry.EFFECT_INSTANCE);
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
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_tonitrus;
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