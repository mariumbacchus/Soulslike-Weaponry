package net.soulsweaponry.items;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.item.LeviathanAxeRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.LeviathanAxeEntity;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class LeviathanAxe extends ModdedAxe implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public LeviathanAxe(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.leviathan_axe_damage, ConfigConstructor.leviathan_axe_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.FREEZE, WeaponUtil.TooltipAbilities.PERMAFROST, WeaponUtil.TooltipAbilities.HEAVY_THROW, WeaponUtil.TooltipAbilities.RETURNING);
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_leviathan_axe;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled(stack)) {
            return super.postHit(stack, target, attacker);
        }
        int sharpness = MathHelper.floor(EnchantmentHelper.getAttackDamage(stack, target.getGroup()));
        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING.get(), 200, sharpness));
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getChargeTime(stack, remainingUseTicks);
            if (i >= 10) {
                stack.damage(3, (LivingEntity)playerEntity, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
                if (stack.hasNbt()) {
                    stack.getNbt().putIntArray(Mjolnir.OWNERS_LAST_POS, new int[]{playerEntity.getBlockX(), playerEntity.getBlockY(), playerEntity.getBlockZ()});
                }
                LeviathanAxeEntity entity = new LeviathanAxeEntity(world, user, stack);
                entity.saveOnPlayer(playerEntity);
                float speed = (float)EnchantmentHelper.getLevel(Enchantments.SHARPNESS, stack)/5;
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 2.5F + speed, 1.0F);
                entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                world.spawnEntity(entity);
                world.playSound(playerEntity, playerEntity.getBlockPos(), SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1f, .5f);
                if (!playerEntity.getAbilities().creativeMode) {
                    playerEntity.getInventory().removeOne(stack);
                }
            }
        }
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (this.isDisabled(itemStack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    public static void iceExplosion(World world, BlockPos pos, @Nullable Entity attacker, int amplifier) {
        Box box = new Box(pos).expand(1D);
        List<Entity> entities = world.getOtherEntities(attacker, box);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity && !(entity instanceof PlayerEntity)) {
                livingEntity.damage(world.getDamageSources().freeze(), (amplifier + 1) * 1.5f);
                livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING.get(), 200, amplifier));
            }
        }
        if (!world.isClient) {
            ParticleHandler.particleSphere(world, 300, pos.getX(), pos.getY() + .5f, pos.getZ(), ParticleEvents.ICE_PARTICLE, 1f);
        }
        world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 1f, 1f);
        world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 1f, .5f);
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_leviathan_axe;
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
            private LeviathanAxeRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new LeviathanAxeRenderer();

                return renderer;
            }
        });
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String getReduceCooldownEnchantId(ItemStack stack) {
        return null;
    }
}