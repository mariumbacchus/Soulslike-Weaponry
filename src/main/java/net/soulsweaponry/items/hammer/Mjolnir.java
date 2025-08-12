package net.soulsweaponry.items.hammer;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.client.renderer.item.MjolnirItemRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.MjolnirProjectile;
import net.soulsweaponry.entity.projectile.noclip.WarmupLightningEntity;
import net.soulsweaponry.items.ChargeToUseItem;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class Mjolnir extends ChargeToUseItem implements GeoItem {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Mjolnir(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.mjolnir_damage, ConfigConstructor.mjolnir_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.MJOLNIR_LIGHTNING, TooltipAbilities.THROW_LIGHTNING, TooltipAbilities.RETURNING, TooltipAbilities.WEATHERBORN, TooltipAbilities.OFF_HAND_FLIGHT);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (this.isDisabled(stack) || world.isClient) {
            return;
        }
        float damage = this.getAttackDamage() - 1;
        float attackSpeed = this.getAttackSpeed();
        if (world.isRaining()) {
            damage += ConfigConstructor.mjolnir_rain_bonus_damage;
        }
        WeaponUtil.modifyStackAttributes(stack, damage, attackSpeed);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = WeaponUtil.getChargeTime(stack, user, remainingUseTicks);
        if (user instanceof PlayerEntity player && i >= 10) {
            int cooldown = 0;
            stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
            if (player.isSneaking()) {
                if (world instanceof ServerWorld serverWorld) {
                    this.smashGround(stack, serverWorld, player);
                    this.lightningCall(player, world);
                }
                double d = player.getRandom().nextGaussian() * 0.05D;
                double e = player.getRandom().nextGaussian() * 0.05D;
                for (int j = 0; j < 200; ++j) {
                    double newX = player.getRandom().nextDouble() - 0.5D + player.getRandom().nextGaussian() * 0.15D + d;
                    double newZ = player.getRandom().nextDouble() - 0.5D + player.getRandom().nextGaussian() * 0.15D + e;
                    double newY = player.getRandom().nextDouble() - 0.5D + player.getRandom().nextDouble() * 0.5D;
                    world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, Items.STONE.getDefaultStack()), player.getX(), player.getY(), player.getZ(), newX, newY/2, newZ);
                    world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, Items.DIRT.getDefaultStack()), player.getX(), player.getY(), player.getZ(), newX, newY/2, newZ);
                    world.addParticle(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY(), player.getZ(), newX, newY/8, newZ);
                    world.addParticle(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY(), player.getZ(), newX*10, newY*2, newZ*10);
                }
                cooldown = (int) ConfigConstructor.mjolnir_lightning_smash_cooldown;
            } else if (player.getOffHandStack().isOf(this)) {
                this.riptide(player, world, stack);
                if (!world.isRaining()) cooldown = (int) ConfigConstructor.mjolnir_riptide_cooldown;
            } else {
                this.throwHammer(world, player, stack);
            }
            if (cooldown != 0) {
                cooldown = (int) Math.max(ConfigConstructor.mjolnir_ability_min_cooldown, cooldown - this.getReduceCooldownEnchantLevel(stack) * 30);
            }
            this.applyItemCooldown(player, cooldown);
        }
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.mjolnir_ability_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.mjolnir_ability_enchant_reduces_cooldown_ids;
    }

    private void throwHammer(World world, PlayerEntity player, ItemStack stack) {
        MjolnirProjectile projectile = new MjolnirProjectile(world, player, stack);
        projectile.saveOnPlayer(player);
        float speed = (float) WeaponUtil.getEnchantDamageBonus(stack)/5;
        projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 2.5f + speed, 1.0f);
        projectile.pickupType = PickupPermission.CREATIVE_ONLY;
        world.spawnEntity(projectile);
        world.playSoundFromEntity(null, projectile, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0f, 1.0f);
        if (!player.getAbilities().creativeMode) {
            player.getInventory().removeOne(stack);
        }
    }

    private void riptide(PlayerEntity player, World world, ItemStack stack) {
        float sharpness = WeaponUtil.getEnchantDamageBonus(stack);
        WeaponUtil.launchTarget(player, 5f + sharpness, false);
        player.useRiptide(20, 15f, stack);
        if (player.isOnGround()) {
            player.move(MovementType.SELF, new Vec3d(0.0, 1.1999999284744263, 0.0));
        }
        world.playSoundFromEntity(null, player, SoundEvents.ITEM_TRIDENT_RIPTIDE_3.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    private void smashGround(ItemStack stack, ServerWorld world, PlayerEntity player) {
        Box box = player.getBoundingBox().expand(3);
        List<Entity> entities = world.getOtherEntities(player, box);
        float power = ConfigConstructor.mjolnir_smash_damage;
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                entity.damage(world.getDamageSources().mobAttack(player),
                        power + 2 * EnchantmentHelper.getDamage(world, stack, living, world.getDamageSources().playerAttack(player), 0));
                entity.addVelocity(0, .25f, 0);
            }
        }
        world.playSoundFromEntity(null, player, SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, .75f, 1f);
    }

    private void lightningCall(PlayerEntity player, World world) {
        for (int i = 1; i < ConfigConstructor.mjolnir_lightning_circle_amount + 1; i++) {
            int r = 5 * i;
            for (int theta = 0; theta < 360; theta+=30) {
                double x0 = player.getX();
                double z0 = player.getZ();
                double x = x0 + r * Math.cos(theta * Math.PI / 180);
                double z = z0 + r * Math.sin(theta * Math.PI / 180);
                WarmupLightningEntity entity = new WarmupLightningEntity(EntityRegistry.WARMUP_LIGHTNING, world);
                entity.setPos(x, player.getY(), z);
                entity.setWarmup(2 + i * 8);
                entity.setOwner(player);
                world.spawnEntity(entity);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_mjolnir;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private MjolnirItemRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new MjolnirItemRenderer();

                return this.renderer;
            }
        });
    }
}