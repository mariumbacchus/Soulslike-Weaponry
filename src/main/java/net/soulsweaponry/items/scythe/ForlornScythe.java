package net.soulsweaponry.items.scythe;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.soulsweaponry.client.renderer.item.ForlornScytheRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.items.SoulHarvestingItem;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class ForlornScythe extends SoulHarvestingItem implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public ForlornScythe(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.forlorn_scythe_damage, ConfigConstructor.forlorn_scythe_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.SOUL_RELEASE_WITHER);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.isDisabled(stack)) {
            this.notifyDisabled(user);
            return TypedActionResult.fail(stack);
        }
        if (!world.isClient) {
            this.detonatePrevEntity((ServerWorld) world, stack);
        }
        Integer kills = stack.get(ComponentRegistry.KILLS);
        if (kills != null) {
            int power = this.getSouls(stack);
            if (power > 0 || user.isCreative()) {
                WitherSkullEntity entity = new WitherSkullEntity(EntityType.WITHER_SKULL, world);
                entity.setPos(user.getX(), user.getEyeY(), user.getZ());
                entity.setOwner(user);
                if (this.isCritical(stack)) {
                    entity.setCharged(true);
                    stack.set(ComponentRegistry.AMOUNT_USED, 1);
                } else {
                    stack.set(ComponentRegistry.AMOUNT_USED, 1 + Optional.ofNullable(stack.get(ComponentRegistry.AMOUNT_USED)).orElse(0));
                }
                entity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0F, 3f, 1.0F);
                world.spawnEntity(entity);
                this.setPrevUuid(stack, entity);
                if (!user.isCreative()) this.addAmount(stack, -1);
                user.getItemCooldownManager().set(this, 10);
                stack.damage(1, user, LivingEntity.getSlotForHand(hand));
                return TypedActionResult.success(stack, world.isClient());
            }
        }
        return TypedActionResult.fail(stack);
    }

    /**
     * Due to the skulls' drag at 0.95 (and 0.73 for the charged ones), the projectiles WILL get stuck in the air
     * at some point. Therefore, to avoid this, the previous projectile's UUID will be stored into NBT, then
     * checked in the world whether it exists or not, then removed accordingly.
     */
    private void detonatePrevEntity(ServerWorld world, ItemStack stack) {
        UUID prev = stack.get(ComponentRegistry.SAVED_UUID);
        if (prev != null) {
            Entity entity = world.getEntity(prev);
            if (entity instanceof WitherSkullEntity skull) {
                world.createExplosion(skull, skull.getX(), skull.getY(), skull.getZ(), skull.isCharged() ? 2f : 1f, false, World.ExplosionSourceType.MOB);
                skull.discard();
            }
        }
    }

    private void setPrevUuid(ItemStack stack, Entity entityToSet) {
        stack.set(ComponentRegistry.SAVED_UUID, entityToSet.getUuid());
    }

    private boolean isCritical(ItemStack stack) {
        return Optional.ofNullable(stack.get(ComponentRegistry.AMOUNT_USED)).orElseGet(() -> {
            stack.set(ComponentRegistry.AMOUNT_USED, 1);
            return 1;
        }) >= 3;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private ForlornScytheRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new ForlornScytheRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_forlorn_scythe;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return null;
    }
}