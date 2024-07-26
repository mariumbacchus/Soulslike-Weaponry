package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.HashMap;
import java.util.Map;

public class Featherlight extends UltraHeavyWeapon {

    public Featherlight(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, CommonConfig.FEATHERLIGHT_DAMAGE.get(), CommonConfig.DISABLE_USE_FEATHERLIGHT.get() ? 1f : CommonConfig.FEATHERLIGHT_ATTACK_SPEED.get(), settings, true);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.FEATHERLIGHT);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    @Override
    public float getBaseExpansion() {
        return CommonConfig.FEATHERLIGHT_CALCULATED_FALL_BASE_RADIUS.get();
    }

    @Override
    public float getExpansionModifier() {
        return CommonConfig.FEATHERLIGHT_CALCULATED_FALL_HEIGHT_INCREASE_RADIUS_MODIFIER.get();
    }

    @Override
    public float getLaunchModifier() {
        return CommonConfig.FEATHERLIGHT_CALCULATED_FALL_TARGET_LAUNCH_MODIFIER.get();
    }

    @Override
    public float getMaxExpansion() {
        return CommonConfig.FEATHERLIGHT_CALCULATED_FALL_MAX_RADIUS.get();
    }

    @Override
    public float getMaxDetonationDamage() {
        return CommonConfig.FEATHERLIGHT_CALCULATED_FALL_MAX_DAMAGE.get();
    }

    @Override
    public float getFallDamageIncreaseModifier() {
        return CommonConfig.FEATHERLIGHT_CALCULATED_FALL_HEIGHT_INCREASE_DAMAGE_MODIFIER.get();
    }

    @Override
    public boolean shouldHeal() {
        return CommonConfig.FEATHERLIGHT_CALCULATED_FALL_SHOULD_HEAL.get();
    }

    @Override
    public float getHealFromDamageModifier() {
        return CommonConfig.FEATHERLIGHT_CALCULATED_FALL_HEAL_FROM_DAMAGE_MODIFIER.get();
    }

    @Override
    public void doCustomEffects(LivingEntity target, LivingEntity user) {
        StatusEffectInstance[] effects = new StatusEffectInstance[] {
                new StatusEffectInstance(EffectRegistry.BLIGHT.get(), 200, 4),
                new StatusEffectInstance(StatusEffects.SLOWNESS, 80, 2)
        };
        for (StatusEffectInstance effect : effects) {
            target.addStatusEffect(effect);
        }
    }

    @Override
    public Map<ParticleEffect, Vec3d> getParticles() {
        Map<ParticleEffect, Vec3d> map = new HashMap<>();
        map.put(ParticleRegistry.PURPLE_FLAME.get(), new Vec3d(1, 6, 1));
        return map;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return CommonConfig.DISABLE_USE_FEATHERLIGHT.get();
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }
}
