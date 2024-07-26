package net.soulsweaponry.items;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
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
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.CometSpearItemRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.entity.projectile.CometSpearEntity;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CometSpear extends DetonateGroundItem implements IAnimatable{

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public CometSpear(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, CommonConfig.COMET_SPEAR_DAMAGE.get(), CommonConfig.COMET_SPEAR_ATTACK_SPEED.get(), settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.SKYFALL, WeaponUtil.TooltipAbilities.INFINITY, WeaponUtil.TooltipAbilities.CRIT);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                float enchant = WeaponUtil.getEnchantDamageBonus(stack);
                if (stack == user.getOffHandStack()) {
                    float f = user.getYaw();
                    float g = user.getPitch();
                    float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
                    float k = -MathHelper.sin(g * 0.017453292F);
                    float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
                    float m = MathHelper.sqrt(h * h + k * k + l * l);
                    float n = 3.0F * ((5.0F + enchant) / 4.0F);
                    h *= n / m;
                    k *= n / m;
                    l *= n / m;
                    
                    user.addVelocity(h, k, l);
                    playerEntity.useRiptide(20);
                    world.playSoundFromEntity(null, playerEntity, SoundEvents.ITEM_TRIDENT_RIPTIDE_3, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    if (playerEntity.isOnGround()) {
                        playerEntity.move(MovementType.SELF, new Vec3d(0.0D, 1.1999999284744263D, 0.0D));
                    }
                    //NOTE: Ground Smash method is in parent class DetonateGroundItem
                    user.addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL.get(), 600, CommonConfig.COMET_SPEAR_ABILITY_DAMAGE.get().intValue()));
                    playerEntity.getItemCooldownManager().set(this, (int) (CommonConfig.COMET_SPEAR_SKYFALL_COOLDOWN.get() - (enchant*20)));
                    stack.damage(4, (LivingEntity)playerEntity, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                } else {
                    stack.damage(2, (LivingEntity)playerEntity, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                    playerEntity.getItemCooldownManager().set(this, (int) (CommonConfig.COMET_SPEAR_THROW_COOLDOWN.get() - (enchant*5)));

                    CometSpearEntity entity = new CometSpearEntity(world, playerEntity, stack);
                    entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 5.0F, 1.0F);
                    entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    world.spawnEntity(entity);
                    world.playSoundFromEntity(null, entity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    @Override
    public float getBaseExpansion() {
        return CommonConfig.COMET_SPEAR_CALCULATED_FALL_BASE_RADIUS.get();
    }

    @Override
    public float getExpansionModifier() {
        return CommonConfig.COMET_SPEAR_CALCULATED_FALL_HEIGHT_INCREASE_RADIUS_MODIFIER.get();
    }

    @Override
    public float getLaunchModifier() {
        return CommonConfig.COMET_SPEAR_CALCULATED_FALL_TARGET_LAUNCH_MODIFIER.get();
    }

    @Override
    public float getMaxExpansion() {
        return CommonConfig.COMET_SPEAR_CALCULATED_FALL_MAX_RADIUS.get();
    }

    @Override
    public float getMaxDetonationDamage() {
        return CommonConfig.COMET_SPEAR_CALCULATED_FALL_MAX_DAMAGE.get();
    }

    @Override
    public float getFallDamageIncreaseModifier() {
        return CommonConfig.COMET_SPEAR_CALCULATED_FALL_HEIGHT_INCREASE_DAMAGE_MODIFIER.get();
    }

    @Override
    public boolean shouldHeal() {
        return CommonConfig.COMET_SPEAR_CALCULATED_FALL_SHOULD_HEAL.get();
    }

    @Override
    public float getHealFromDamageModifier() {
        return CommonConfig.COMET_SPEAR_CALCULATED_FALL_HEAL_FROM_DAMAGE_MODIFIER.get();
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
            private CometSpearItemRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BuiltinModelItemRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new CometSpearItemRenderer();

                return renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return CommonConfig.DISABLE_USE_COMET_SPEAR.get();
    }
}
