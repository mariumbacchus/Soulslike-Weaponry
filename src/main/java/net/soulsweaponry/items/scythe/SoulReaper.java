package net.soulsweaponry.items.scythe;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.item.SoulReaperRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.SoulHarvestingItem;
import net.soulsweaponry.items.abilities.use.SoulReleasePowerBased;
import net.soulsweaponry.registry.EntityRegistry;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
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
        THRESHOLDS.put(3, EntityRegistry.SOUL_REAPER_GHOST.get());
        THRESHOLDS.put(10, EntityRegistry.FORLORN.get());
        THRESHOLDS.put(30, EntityRegistry.SOULMASS.get());
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
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final SoulReaperRenderer renderer = new SoulReaperRenderer();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_soul_reaper;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_soul_reaper;
    }
}