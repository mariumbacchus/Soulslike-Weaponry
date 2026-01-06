package net.soulsweaponry.items.scythe;

import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.soulsweaponry.client.renderer.item.SoulReaperRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.SoulHarvestingItem;
import net.soulsweaponry.items.abilities.use.SoulReleasePowerBased;
import net.soulsweaponry.registry.EntityRegistry;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.Consumer;

public class SoulReaper extends SoulHarvestingItem implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private static final NavigableMap<Integer, EntityType<?>> THRESHOLDS = new TreeMap<>();
    private static final SoulReleasePowerBased SOUL_RELEASE = new SoulReleasePowerBased(
            (int) ConfigConstructor.soul_reaper_summoned_allies_cap,
            "SoulReaperSummons",
            THRESHOLDS,
            ConfigConstructor.soul_reaper_summon_bonus_health_per_soul, ConfigConstructor.soul_reaper_summon_bonus_health_per_soul_addition_per_level,
            ConfigConstructor.soul_reaper_summon_max_bonus_health,
            ConfigConstructor.soul_reaper_summon_bonus_attack_damage_per_soul, ConfigConstructor.soul_reaper_summon_bonus_attack_damage_per_soul_addition_per_level,
            ConfigConstructor.soul_reaper_summon_max_bonus_attack_damage
    );

    static {
        THRESHOLDS.put(3, EntityRegistry.SOUL_REAPER_GHOST);
        THRESHOLDS.put(10, EntityRegistry.FORLORN);
        THRESHOLDS.put(30, EntityRegistry.SOULMASS);
    }

    public SoulReaper(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.soul_reaper_damage, ConfigConstructor.soul_reaper_attack_speed, settings);
        this.addAbility(SOUL_RELEASE);
    }

    private PlayState predicate(AnimationState<?> event){
        //Figure out how to dynamically change the animation with ISyncable (problem now is that it can't override the prev. animation)
        /*ClientPlayerEntity player;
        if ((player = MinecraftClient.getInstance().player) != null) {
            for (Hand hand : Hand.values()) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack.isOf(WeaponRegistry.SOUL_REAPER)) {
                    int souls = this.getSouls(stack);
                    if (souls >= 10) {
                        if (souls >= 30) {
                            event.getController().setAnimation(new AnimationBuilder().addAnimation("high_souls", EDefaultLoopTypes.LOOP));
                        } else {
                            event.getController().setAnimation(new AnimationBuilder().addAnimation("mid_souls", EDefaultLoopTypes.LOOP));
                        }
                        return PlayState.CONTINUE;
                    }
                }
            }
        }*/
        event.getController().setAnimation(RawAnimation.begin().then("low_souls", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private SoulReaperRenderer renderer;

            @Override
            public GeoItemRenderer<?> getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new SoulReaperRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_soul_reaper;
    }
}