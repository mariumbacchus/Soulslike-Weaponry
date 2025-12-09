package net.soulsweaponry.items.spear;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.item.CometSpearItemRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.CometSpearEntity;
import net.soulsweaponry.items.ChargeToUseItem;
import net.soulsweaponry.items.IDetonateGround;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.DetonateGroundAttributes;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.function.Consumer;

public class CometSpear extends ChargeToUseItem implements GeoItem, IDetonateGround {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final DetonateGroundAttributes attributes = new DetonateGroundAttributes(
            ConfigConstructor.comet_spear_calculated_fall_base_radius,
            ConfigConstructor.comet_spear_calculated_fall_height_increase_radius_modifier,
            ConfigConstructor.comet_spear_calculated_fall_target_launch_modifier,
            ConfigConstructor.comet_spear_calculated_fall_target_max_launch_power,
            ConfigConstructor.comet_spear_calculated_fall_max_radius,
            ConfigConstructor.comet_spear_calculated_fall_max_damage,
            ConfigConstructor.comet_spear_calculated_fall_height_increase_damage_modifier,
            ConfigConstructor.comet_spear_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleTypes.FLAME, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> {},
            (user, fallDistance, stack) -> {}
    );

    public CometSpear(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.comet_spear_damage, ConfigConstructor.comet_spear_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.SKYFALL, TooltipAbilities.INFINITY, TooltipAbilities.CRIT);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = WeaponUtil.getChargeTime(stack, remainingUseTicks);
            if (i >= 10) {
                float enchant = WeaponUtil.getEnchantDamageBonus(stack);
                if (user.isSneaking()) {
                    WeaponUtil.launchTarget(user, 5f + enchant, false);
                    playerEntity.useRiptide(20);
                    world.playSoundFromEntity(null, playerEntity, SoundEvents.ITEM_TRIDENT_RIPTIDE_3, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    if (playerEntity.isOnGround()) {
                        playerEntity.move(MovementType.SELF, new Vec3d(0.0D, 1.1999999284744263D, 0.0D));
                    }
                    //NOTE: Ground Smash method is in parent class DetonateGroundItem
                    user.addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL.get(), 600, (int) ConfigConstructor.comet_spear_ability_damage));
                    this.applyItemCooldown(playerEntity, this.getScaledCooldownSkyfall(stack));
                    stack.damage(4, (LivingEntity)playerEntity, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                } else {
                    stack.damage(2, (LivingEntity)playerEntity, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                    this.applyItemCooldown(playerEntity, this.getScaledCooldownThrow(stack));

                    CometSpearEntity entity = new CometSpearEntity(world, playerEntity, stack);
                    entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 5.0F, 1.0F);
                    entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    world.spawnEntity(entity);
                    world.playSoundFromEntity(null, entity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    protected int getScaledCooldownSkyfall(ItemStack stack) {
        int base = (int) ConfigConstructor.comet_spear_skyfall_ability_cooldown;
        return (int) Math.max(ConfigConstructor.comet_spear_skyfall_ability_min_cooldown, base - this.getReduceCooldownEnchantLevel(stack) * 20);
    }

    protected int getScaledCooldownThrow(ItemStack stack) {
        int base = (int) ConfigConstructor.comet_spear_throw_ability_cooldown;
        return (int) Math.max(ConfigConstructor.comet_spear_throw_ability_min_cooldown, base - this.getReduceCooldownEnchantLevel(stack) * 5);
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.comet_spear_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.comet_spear_enchant_reduces_cooldown_ids;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private CometSpearItemRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new CometSpearItemRenderer();

                return renderer;
            }
        });
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_comet_spear;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_comet_spear;
    }

    @Override
    public DetonateGroundAttributes getDetonationAttributes() {
        return this.attributes;
    }
}