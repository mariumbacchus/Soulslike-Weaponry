package net.soulsweaponry.items;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.client.renderer.item.DarkinBladeRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DarkinBlade extends UltraHeavyWeapon implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public DarkinBlade(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.darkin_blade_damage, ConfigConstructor.darkin_blade_attack_speed, settings, true);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.OMNIVAMP, WeaponUtil.TooltipAbilities.SWORD_SLAM);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        if (attacker instanceof PlayerEntity player) {
            if (!player.getItemCooldownManager().isCoolingDown(stack.getItem()) && !(player.getHealth() >= player.getMaxHealth())) {
                if (!player.isCreative()) player.getItemCooldownManager().set(this, ConfigConstructor.lifesteal_item_cooldown);
                float healing = ConfigConstructor.lifesteal_item_base_healing;
                if (ConfigConstructor.lifesteal_item_heal_scales) {
                    healing += MathHelper.ceil(((float)WeaponUtil.getEnchantDamageBonus(stack))/2);
                }
                attacker.heal(healing);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            int duration = ConfigConstructor.darkin_blade_ability_cooldown;
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                Vec3d rotation = player.getRotationVector().multiply(1f);
                player.addVelocity(rotation.getX(), 1, rotation.getZ());
                world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1f, 1f);
                duration = MathHelper.floor(duration/1.5f);
                //NOTE: Ground Smash method is in parent class DetonateGroundItem
                user.addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL, 600, ConfigConstructor.darkin_blade_ability_damage));
            } else {
                this.detonateGroundEffect(user, ConfigConstructor.darkin_blade_ability_damage, 0, world, stack);
            }
            stack.damage(3, user, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
            player.getItemCooldownManager().set(this, duration);
        }
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    private PlayState predicate(AnimationState<?> event){
        event.getController().setAnimation(RawAnimation.begin().then("heartbeat", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "controller", 20, this::predicate));
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private final DarkinBladeRenderer renderer = new DarkinBladeRenderer();

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
    public float getBaseExpansion() {
        return ConfigConstructor.darkin_blade_calculated_fall_base_radius;
    }

    @Override
    public float getExpansionModifier() {
        return ConfigConstructor.darkin_blade_calculated_fall_height_increase_radius_modifier;
    }

    @Override
    public float getLaunchModifier() {
        return ConfigConstructor.darkin_blade_calculated_fall_target_launch_modifier;
    }

    @Override
    public float getMaxExpansion() {
        return ConfigConstructor.darkin_blade_calculated_fall_max_radius;
    }

    @Override
    public float getMaxDetonationDamage() {
        return ConfigConstructor.darkin_blade_calculated_fall_max_damage;
    }

    @Override
    public float getFallDamageIncreaseModifier() {
        return ConfigConstructor.darkin_blade_calculated_fall_height_increase_damage_modifier;
    }

    @Override
    public boolean shouldHeal() {
        return ConfigConstructor.darkin_blade_calculated_fall_should_heal;
    }

    @Override
    public float getHealFromDamageModifier() {
        return ConfigConstructor.darkin_blade_calculated_fall_heal_from_damage_modifier;
    }

    @Override
    public void doCustomEffects(LivingEntity target, LivingEntity user) {}

    @Override
    public Map<ParticleEffect, Vec3d> getParticles() {
        Map<ParticleEffect, Vec3d> map = new HashMap<>();
        map.put(ParticleTypes.FLAME, new Vec3d(1, 6, 1));
        return map;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_darkin_blade;
    }
}