package net.soulsweaponry.items.spear;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.client.renderer.item.DraupnirSpearItemRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.DraupnirSpearEntity;
import net.soulsweaponry.items.ChargeToUseItem;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class DraupnirSpear extends ChargeToUseItem implements GeoItem, IKeybindAbility {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public DraupnirSpear(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.draupnir_spear_damage, ConfigConstructor.draupnir_spear_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.INFINITY, TooltipAbilities.DETONATE_SPEARS);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity && !playerEntity.getItemCooldownManager().isCoolingDown(this)) {
            int i = WeaponUtil.getChargeTime(stack, user, remainingUseTicks);
            if (i >= 10) {
                DraupnirSpearEntity entity = new DraupnirSpearEntity(world, playerEntity, stack);
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 5.0F, 1.0F);
                entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                world.spawnEntity(entity);
                world.playSoundFromEntity(null, entity, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                this.saveSpearData(stack, entity);
                this.applyItemCooldown(playerEntity, this.getScaledCooldownThrow(stack));
                stack.damage(1, playerEntity, WeaponUtil.getActiveHandSlot(playerEntity));
            }
        }
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.draupnir_spear_enchant_reduces_throw_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.draupnir_spear_enchant_reduces_throw_cooldown_ids;
    }

    protected int getReduceCooldownEnchantLevelAbility(ItemStack stack) {
        if (ConfigConstructor.draupnir_spear_enchant_reduces_ability_cooldown) {
            return this.getMaxLevel(ConfigConstructor.draupnir_spear_enchant_reduces_ability_cooldown_ids, stack);
        }
        return 0;
    }

    protected int getScaledCooldownThrow(ItemStack stack) {
        int base = (int) ConfigConstructor.draupnir_spear_throw_cooldown;
        return (int) Math.max(ConfigConstructor.draupnir_spear_throw_min_cooldown, base - this.getReduceCooldownEnchantLevel(stack) * 5);
    }

    protected int getScaledCooldownSummon(ItemStack stack) {
        int base = (int) ConfigConstructor.draupnir_spear_summon_spears_cooldown;
        return (int) Math.max(ConfigConstructor.draupnir_spear_summon_spears_min_cooldown, base - this.getReduceCooldownEnchantLevelAbility(stack) * 20);
    }

    protected int getScaledCooldownExplode(ItemStack stack) {
        int base = (int) ConfigConstructor.draupnir_spear_detonate_cooldown;
        return (int) Math.max(ConfigConstructor.draupnir_spear_detonate_min_cooldown, base - this.getReduceCooldownEnchantLevelAbility(stack) * 7);
    }

    private PlayState predicate(AnimationState<?> event){
        event.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private DraupnirSpearItemRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new DraupnirSpearItemRenderer();

                return this.renderer;
            }
        });
    }

    private void saveSpearData(ItemStack stack, DraupnirSpearEntity entity) {
        List<Integer> ids = Optional.ofNullable(stack.get(ComponentRegistry.INT_LIST)).orElse(new ArrayList<>());
        List<Integer> newList = new ArrayList<>(ids);
        newList.add(entity.getId());
        stack.set(ComponentRegistry.INT_LIST, newList);
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            if (player.isSneaking()) {
                if (!player.hasStatusEffect(EffectRegistry.COOLDOWN)) {
                    int r = 4;
                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.PLAYERS, 1f, 1f);
                    for (int theta = 0; theta < 360; theta += 45) {
                        double x0 = player.getX();
                        double z0 = player.getZ();
                        double x = x0 + r * Math.cos(theta * Math.PI / 180);
                        double z = z0 + r * Math.sin(theta * Math.PI / 180);
                        double x1 = Math.cos(theta * Math.PI / 180);
                        double z1 = Math.sin(theta * Math.PI / 180);
                        DraupnirSpearEntity entity = new DraupnirSpearEntity(world, player, stack);
                        entity.setPos(x, player.getY() + 5, z);
                        entity.setVelocity(x1, -3, z1);
                        entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                        world.spawnEntity(entity);
                        this.saveSpearData(stack, entity);
                        ParticleHandler.particleOutburst(world, 10, x, player.getY() + 5, z, ParticleTypes.CLOUD, new Vec3d(4, 4, 4), 0.5f);
                    }
                    if (!player.isCreative()) player.addStatusEffect(new StatusEffectInstance(EffectRegistry.COOLDOWN, this.getScaledCooldownSummon(stack), 0));
                } else if (ConfigConstructor.inform_player_about_cooldown_effect) {
                    player.sendMessage(Text.translatableWithFallback("soulsweapons.weapon.on_cooldown", "Can't cast this ability with the Cooldown effect!"), true);
                }
            } else {
                Box box = player.getBoundingBox().expand(3);
                List<Entity> entities = world.getOtherEntities(player, box);
                float power = ConfigConstructor.draupnir_spear_projectile_damage;
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity living) {
                        entity.damage(world.getDamageSources().mobAttack(player), power + EnchantmentHelper.getDamage(world, stack, living, world.getDamageSources().playerAttack(player), 0));
                        entity.addVelocity(0, .1f, 0);
                    }
                }
                ParticleHandler.particleOutburstMap(world, 250, player.getX(), player.getY(), player.getZ(), ParticleEvents.DEFAULT_GRAND_SKYFALL_MAP, 0.5f);
                world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1f, 1f);
                this.applyItemCooldown(player, this.getScaledCooldownExplode(stack));
                List<Integer> ids = stack.get(ComponentRegistry.INT_LIST);
                if (ids != null) {
                    for (int id : ids) {
                        Entity entity = world.getEntityById(id);
                        if (entity instanceof DraupnirSpearEntity spear) {
                            spear.detonate();
                        }
                    }
                    stack.set(ComponentRegistry.INT_LIST, List.of());
                }
            }
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_draupnir_spear;
    }
}