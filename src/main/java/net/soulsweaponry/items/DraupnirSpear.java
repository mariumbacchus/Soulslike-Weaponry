package net.soulsweaponry.items;

import net.minecraft.client.network.ClientPlayerEntity;
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
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DraupnirSpear extends ChargeToUseItem implements GeoItem, IKeybindAbility {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    public static final String SPEARS_ID = "thrown_spears_id";

    public DraupnirSpear(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.draupnir_spear_damage, ConfigConstructor.draupnir_spear_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.INFINITY, WeaponUtil.TooltipAbilities.DETONATE_SPEARS);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                DraupnirSpearEntity entity = new DraupnirSpearEntity(world, playerEntity, stack);
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 5.0F, 1.0F);
                entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                world.spawnEntity(entity);
                world.playSoundFromEntity(null, entity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                this.saveSpearData(stack, entity);
                this.applyCooldown(playerEntity, this.getScaledCooldownThrow(stack));
                stack.damage(1, (LivingEntity)playerEntity, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
            }
        }
    }

    @Override
    public int getReduceCooldownEnchantLevel(ItemStack stack) {
        if (ConfigConstructor.draupnir_spear_damage_enchant_reduces_throw_cooldown) {
            return WeaponUtil.getEnchantDamageBonus(stack);
        }
        return 0;
    }

    protected int getReduceCooldownEnchantLevelAbility(ItemStack stack) {
        if (ConfigConstructor.draupnir_spear_damage_enchant_reduces_ability_cooldown) {
            return WeaponUtil.getEnchantDamageBonus(stack);
        }
        return 0;
    }

    protected int getScaledCooldownThrow(ItemStack stack) {
        int base = ConfigConstructor.draupnir_spear_throw_cooldown;
        return Math.max(ConfigConstructor.draupnir_spear_throw_min_cooldown, base - this.getReduceCooldownEnchantLevel(stack) * 5);
    }

    protected int getScaledCooldownSummon(ItemStack stack) {
        int base = ConfigConstructor.draupnir_spear_summon_spears_cooldown;
        return Math.max(ConfigConstructor.draupnir_spear_summon_spears_min_cooldown, base - this.getReduceCooldownEnchantLevelAbility(stack) * 20);
    }

    protected int getScaledCooldownExplode(ItemStack stack) {
        int base = ConfigConstructor.draupnir_spear_detonate_cooldown;
        return Math.max(ConfigConstructor.draupnir_spear_detonate_min_cooldown, base - this.getReduceCooldownEnchantLevelAbility(stack) * 7);
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
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private final DraupnirSpearItemRenderer renderer = new DraupnirSpearItemRenderer();

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
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_draupnir_spear;
    }

    private void saveSpearData(ItemStack stack, DraupnirSpearEntity entity) {
        if (stack.hasNbt()) {
            List<Integer> ids = new ArrayList<>();
            if (stack.getNbt().contains(SPEARS_ID)) {
                int[] arr = stack.getNbt().getIntArray(SPEARS_ID);
                ids = WeaponUtil.arrayToList(arr);
            }
            ids.add(entity.getId());
            stack.getNbt().putIntArray(SPEARS_ID, ids);
        }
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
                    if (entity instanceof LivingEntity) {
                        entity.damage(world.getDamageSources().mobAttack(player), power + EnchantmentHelper.getAttackDamage(stack, ((LivingEntity) entity).getGroup()));
                        entity.addVelocity(0, .1f, 0);
                    }
                }
                ParticleHandler.particleOutburstMap(world, 250, player.getX(), player.getY(), player.getZ(), ParticleEvents.DEFAULT_GRAND_SKYFALL_MAP, 0.5f);
                world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
                this.applyCooldown(player, this.getScaledCooldownExplode(stack));
                if (stack.hasNbt() && stack.getNbt().contains(DraupnirSpear.SPEARS_ID)) {
                    int[] ids = stack.getNbt().getIntArray(DraupnirSpear.SPEARS_ID);
                    for (int id : ids) {
                        Entity entity = world.getEntityById(id);
                        if (entity instanceof DraupnirSpearEntity spear) {
                            spear.detonate();
                        }
                    }
                    stack.getNbt().putIntArray(DraupnirSpear.SPEARS_ID, new int[0]);
                }
            }
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_draupnir_spear;
    }
}