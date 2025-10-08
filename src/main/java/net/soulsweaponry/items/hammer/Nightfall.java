package net.soulsweaponry.items.hammer;

import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.client.renderer.item.NightfallRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.SummonsData;
import net.soulsweaponry.items.ISummonAllies;
import net.soulsweaponry.items.UltraHeavyWeapon;
import net.soulsweaponry.items.abilities.detonateground.DetonateGroundAttributes;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.*;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;
import java.util.function.Consumer;

public class Nightfall extends UltraHeavyWeapon implements GeoItem, IKeybindAbility, ISummonAllies {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final DetonateGroundAttributes attributes = new DetonateGroundAttributes(
            ConfigConstructor.nightfall_calculated_fall_base_radius,
            ConfigConstructor.nightfall_calculated_fall_height_increase_radius_modifier,
            ConfigConstructor.nightfall_calculated_fall_target_launch_modifier,
            ConfigConstructor.nightfall_calculated_fall_target_max_launch_power,
            ConfigConstructor.nightfall_calculated_fall_max_radius,
            ConfigConstructor.nightfall_calculated_fall_max_damage,
            ConfigConstructor.nightfall_calculated_fall_height_increase_damage_modifier,
            ConfigConstructor.nightfall_calculated_fall_heal_from_damage_modifier,
            Map.of(ParticleTypes.SOUL_FIRE_FLAME, new Vec3d(1, 6, 1)),
            (target, user, fallDistance) -> this.spawnRemnant(target, user),
            (user, fallDistance, stack) -> {}
    );

    public Nightfall(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.nightfall_damage, ConfigConstructor.nightfall_attack_speed, settings, (int) ConfigConstructor.nightfall_posture_loss);
        this.addTooltipAbility(TooltipAbilities.SUMMON_GHOST, TooltipAbilities.SHIELD, TooltipAbilities.OBLITERATE);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player && !player.getItemCooldownManager().isCoolingDown(this)) {
            int i = WeaponUtil.getChargeTime(stack, user, remainingUseTicks);
            if (i >= 10 && world instanceof ServerWorld serverWorld) {
                this.applyItemCooldown(player, this.getScaledCooldownSmash(stack));
                stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
                Vec3d vecBlocksAway = player.getRotationVector().multiply(3).add(player.getPos());
                BlockPos targetArea = new BlockPos((int)vecBlocksAway.x, (int) user.getY(), (int) vecBlocksAway.z);
                Box aoe = new Box(targetArea).expand(3);
                List<Entity> entities = world.getOtherEntities(player, aoe);
                float power = ConfigConstructor.nightfall_ability_damage;
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity target) {
                        entity.damage(DamageSourceRegistry.create(world, DamageSourceRegistry.OBLITERATED, player),
                                power + 2 * EnchantmentHelper.getDamage(serverWorld, stack, target, world.getDamageSources().playerAttack(player), 0));
                        entity.setVelocity(entity.getVelocity().x, .5f, entity.getVelocity().z);
                        this.spawnRemnant(target, user);
                    }
                }
                world.playSound(null, targetArea, SoundRegistry.NIGHTFALL_BONK_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                ParticleHandler.particleOutburstMap(world, 150, targetArea.getX(), targetArea.getY() + .1f, targetArea.getZ(), ParticleEvents.OBLITERATE_MAP, 1f);
            }
        }
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return ConfigConstructor.nightfall_enchant_reduces_cooldown;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return ConfigConstructor.nightfall_enchant_reduces_cooldown_ids;
    }

    protected int getScaledCooldownSmash(ItemStack stack) {
        float base = ConfigConstructor.nightfall_smash_cooldown;
        return (int) Math.max(ConfigConstructor.nightfall_smash_min_cooldown, base - this.getReduceCooldownEnchantLevel(stack) * 50);
    }

    protected int getScaledCooldownShield(ItemStack stack) {
        float base = ConfigConstructor.nightfall_shield_cooldown;
        return (int) Math.max(ConfigConstructor.nightfall_shield_min_cooldown, base - this.getReduceCooldownEnchantLevel(stack) * 100);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            this.spawnRemnant(target, attacker);
        }
        return super.postHit(stack, target, attacker);
    }

    public void spawnRemnant(LivingEntity target, LivingEntity attacker) {
        if (target.hasInvertedHealingAndHarm() && target.isDead() && attacker instanceof PlayerEntity && !this.isDisabled(attacker.getMainHandStack())) {
            double chance = new Random().nextDouble();
            World world = attacker.getEntityWorld();
            if (!world.isClient && this.canSummonEntity((ServerWorld) world, attacker, this.getSummonsListId()) && chance < ConfigConstructor.nightfall_summon_chance) {
                Remnant entity = new Remnant(EntityRegistry.REMNANT, world);
                entity.setPos(target.getX(), target.getY() + .1F, target.getZ());
                entity.setOwner((PlayerEntity) attacker);
                world.spawnEntity(entity);
                this.saveSummonUuid(attacker, entity.getUuid());
                world.playSound(null, target.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                if (!attacker.getWorld().isClient) {
                    ParticleHandler.particleOutburstMap(attacker.getWorld(), 50, target.getX(), target.getY(), target.getZ(), ParticleEvents.SOUL_RUPTURE_MAP, 1f);
                }
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
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private NightfallRenderer renderer;

            @Override
            public BuiltinModelItemRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new NightfallRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[] {
                Text.translatable("tooltip.soulsweapons.nightfall.part_1").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.nightfall.part_2").formatted(Formatting.DARK_GRAY),
                Text.translatable("tooltip.soulsweapons.nightfall.part_3").formatted(Formatting.DARK_GRAY)
        };
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(this)) {
            this.applyItemCooldown(player, this.getScaledCooldownShield(stack));
            stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 200, (int) ConfigConstructor.nightfall_ability_shield_power));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0));
            world.playSound(null, player.getBlockPos(), SoundRegistry.NIGHTFALL_SHIELD_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {
    }

    @Override
    public int getMaxSummons() {
        return (int) ConfigConstructor.nightfall_summoned_allies_cap;
    }

    @Override
    public String getSummonsListId() {
        return "NightfallSummons";
    }

    @Override
    public void saveSummonUuid(LivingEntity user, UUID summonUuid) {
        SummonsData.addSummonUUID((IEntityDataSaver) user, summonUuid, this.getSummonsListId());
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_nightfall;
    }

    @Override
    public DetonateGroundAttributes getDetonateGroundAttributes() {
        return this.attributes;
    }
}