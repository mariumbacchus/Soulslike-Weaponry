package net.soulsweaponry.items.katana;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.item.BloodlustRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.items.abilities.abilitykeybind.BloodlustAbility;
import net.soulsweaponry.items.abilities.posthit.Bleed;
import net.soulsweaponry.items.abilities.targetdamaged.BloodlossInVicinity;
import net.soulsweaponry.registry.EffectRegistry;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Bloodlust extends ModdedSword implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    private static final Bleed BLEED = new Bleed((int) ConfigConstructor.bloodlust_bleed_post_hit, ConfigConstructor.bloodlust_bleed_post_hit_bonus_per_bloodthirsty_amp);
    private static final BloodlustAbility BLOODLUST_ABILITY = new BloodlustAbility(
            ConfigConstructor.bloodlust_ability_self_damage, (int) ConfigConstructor.bloodlust_ability_self_bleed,
            (int) ConfigConstructor.bloodlust_ability_bloodthirsty_duration, (int) ConfigConstructor.bloodlust_ability_bloodthirsty_amp,
            (int) ConfigConstructor.bloodlust_ability_strength_duration, (int) ConfigConstructor.bloodlust_ability_strength_amp
    );
    private static final BloodlossInVicinity BLOODLOSS_IN_VICINITY = new BloodlossInVicinity(
            new StatusEffectInstance(StatusEffects.STRENGTH, (int) ConfigConstructor.bloodlust_bloodloss_in_vicinity_gives_strength_duration, (int) ConfigConstructor.bloodlust_bloodloss_in_vicinity_gives_strength_amp)
    );

    public Bloodlust(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.bloodlust_damage, ConfigConstructor.bloodlust_attack_speed, settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.addAbility(BLOODLUST_ABILITY, BLEED, BLOODLOSS_IN_VICINITY);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player && world instanceof ServerWorld serverWorld && player.age % 10 == 0) {
            if (player.hasStatusEffect(EffectRegistry.BLOODTHIRSTY.get())) {
                this.triggerAnim(player, GeoItem.getOrAssignId(stack, serverWorld), "empowered", "spin");
            } else {
                this.triggerAnim(player, GeoItem.getOrAssignId(stack, serverWorld), "empowered", "idle");
            }
        }
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_bloodlust;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final BloodlustRenderer renderer = new BloodlustRenderer();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "empowered", state -> PlayState.STOP)
                .triggerableAnim("spin", RawAnimation.begin().thenPlay("spin"))
                .triggerableAnim("idle", RawAnimation.begin().thenPlay("idle"))
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_bloodlust;
    }
}