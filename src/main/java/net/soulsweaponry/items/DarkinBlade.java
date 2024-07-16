package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.DarkinBladeRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DarkinBlade extends UltraHeavyWeapon implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    
    public DarkinBlade(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, CommonConfig.DARKIN_BLADE_DAMAGE.get(), attackSpeed, settings, true);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled()) {
            stack.damage(1, attacker, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            this.notifyDisabled(attacker);
            return true;
        }
        if (attacker instanceof PlayerEntity player) {
            if (!player.getItemCooldownManager().isCoolingDown(stack.getItem()) && !(player.getHealth() >= player.getMaxHealth())) {
                if (!player.isCreative()) player.getItemCooldownManager().set(this, CommonConfig.LIFE_STEAL_COOLDOWN.get());
                float healing = CommonConfig.LIFE_STEAL_BASE_HEAL.get();
                if (CommonConfig.LIFE_STEAL_SCALES.get()) {
                    healing += MathHelper.ceil(((float)WeaponUtil.getEnchantDamageBonus(stack))/2);
                }
                attacker.heal(healing);
            }
        }
        this.gainStrength(attacker);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            int duration = CommonConfig.DARKIN_BLADE_ABILITY_COOLDOWN.get();
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                Vec3d rotation = player.getRotationVector().multiply(1f);
                player.addVelocity(rotation.getX(), 1, rotation.getZ());
                player.world.playSound(player, player.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1f, 1f);
                duration = MathHelper.floor(duration/1.5f);
                //NOTE: Ground Smash method is in parent class DetonateGroundItem
                user.addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL.get(), 600, CommonConfig.DARKIN_BLADE_ABILITY_DAMAGE.get().intValue()));
            } else {
                this.detonateGroundEffect(user, CommonConfig.DARKIN_BLADE_ABILITY_DAMAGE.get().intValue(), 0, world, stack);
            }
            stack.damage(3, user, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
            player.getItemCooldownManager().set(this, duration);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.OMNIVAMP, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SWORD_SLAM, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.HEAVY, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event){
        event.getController().setAnimation(new AnimationBuilder().addAnimation("heartbeat", EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));    
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public float getBaseExpansion() {
        return CommonConfig.DARKIN_BLADE_CALCULATED_FALL_BASE_RADIUS.get();
    }

    @Override
    public float getExpansionModifier() {
        return CommonConfig.DARKIN_BLADE_CALCULATED_FALL_HEIGHT_INCREASE_RADIUS_MODIFIER.get();
    }

    @Override
    public float getLaunchModifier() {
        return CommonConfig.DARKIN_BLADE_CALCULATED_FALL_TARGET_LAUNCH_MODIFIER.get();
    }

    @Override
    public float getMaxExpansion() {
        return CommonConfig.DARKIN_BLADE_CALCULATED_FALL_MAX_RADIUS.get();
    }

    @Override
    public float getMaxDetonationDamage() {
        return CommonConfig.DARKIN_BLADE_CALCULATED_FALL_MAX_DAMAGE.get();
    }

    @Override
    public float getFallDamageIncreaseModifier() {
        return CommonConfig.DARKIN_BLADE_CALCULATED_FALL_HEIGHT_INCREASE_DAMAGE_MODIFIER.get();
    }

    @Override
    public boolean shouldHeal() {
        return CommonConfig.DARKIN_BLADE_CALCULATED_FALL_SHOULD_HEAL.get();
    }

    @Override
    public float getHealFromDamageModifier() {
        return CommonConfig.DARKIN_BLADE_CALCULATED_FALL_HEAL_FROM_DAMAGE_MODIFIER.get();
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
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private DarkinBladeRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BuiltinModelItemRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new DarkinBladeRenderer();

                return renderer;
            }
        });
    }

    @Override
    public boolean isDisabled() {
        return CommonConfig.DISABLE_USE_DARKIN_BLADE.get();
    }
}
