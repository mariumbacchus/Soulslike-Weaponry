package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.ParticleRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DragonslayerSwordBerserk extends UltraHeavyWeapon implements IKeybindAbility {
    
    public DragonslayerSwordBerserk(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, CommonConfig.GUTS_SWORD_DAMAGE.get(), attackSpeed, settings, true);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.RAGE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.HEAVY, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (this.isDisabled()) {
            this.notifyDisabled(player);
            return;
        }
        if (!player.getItemCooldownManager().isCoolingDown(this)) {
            stack.damage(1, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));
            player.getItemCooldownManager().set(this, CommonConfig.GUTS_SWORD_ABILITY_COOLDOWN.get());
            int power = MathHelper.floor(WeaponUtil.getEnchantDamageBonus(stack));
            player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY.get(), 200, power));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 0));

            world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, .75f, 1f);
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
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
        return CommonConfig.GUTS_SWORD_CALCULATED_FALL_BASE_RADIUS.get();
    }

    @Override
    public float getExpansionModifier() {
        return CommonConfig.GUTS_SWORD_CALCULATED_FALL_HEIGHT_INCREASE_RADIUS_MODIFIER.get();
    }

    @Override
    public float getLaunchModifier() {
        return CommonConfig.GUTS_SWORD_CALCULATED_FALL_TARGET_LAUNCH_MODIFIER.get();
    }

    @Override
    public float getMaxExpansion() {
        return CommonConfig.GUTS_SWORD_CALCULATED_FALL_MAX_RADIUS.get();
    }

    @Override
    public float getMaxDetonationDamage() {
        return CommonConfig.GUTS_SWORD_CALCULATED_FALL_MAX_DAMAGE.get();
    }

    @Override
    public float getFallDamageIncreaseModifier() {
        return CommonConfig.GUTS_SWORD_CALCULATED_FALL_HEIGHT_INCREASE_DAMAGE_MODIFIER.get();
    }

    @Override
    public boolean shouldHeal() {
        return CommonConfig.GUTS_SWORD_CALCULATED_FALL_SHOULD_HEAL.get();
    }

    @Override
    public float getHealFromDamageModifier() {
        return CommonConfig.GUTS_SWORD_CALCULATED_FALL_HEAL_FROM_DAMAGE_MODIFIER.get();
    }

    @Override
    public void doCustomEffects(LivingEntity target, LivingEntity user) {
        StatusEffectInstance[] effects = new StatusEffectInstance[] {
                new StatusEffectInstance(StatusEffects.WITHER, 140, 1)
        };
        for (StatusEffectInstance effect : effects) {
            target.addStatusEffect(effect);
        }
    }

    @Override
    public Map<ParticleEffect, Vec3d> getParticles() {
        Map<ParticleEffect, Vec3d> map = new HashMap<>();
        map.put(ParticleRegistry.DARK_STAR.get(), new Vec3d(1, 6, 1));
        map.put(ParticleTypes.FLAME, new Vec3d(1, 6, 1));
        return map;
    }

    @Override
    public boolean isDisabled() {
        return CommonConfig.DISABLE_USE_HEAP_OF_RAW_IRON.get();
    }
}
