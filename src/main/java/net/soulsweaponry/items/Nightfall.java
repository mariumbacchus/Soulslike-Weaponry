package net.soulsweaponry.items;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
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
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.SummonsData;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.*;

public class Nightfall extends UltraHeavyWeapon implements IAnimatable, IKeybindAbility, ISummonAllies {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Nightfall(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.nightfall_damage, ConfigConstructor.nightfall_attack_speed, settings, true);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.SUMMON_GHOST, WeaponUtil.TooltipAbilities.SHIELD, WeaponUtil.TooltipAbilities.OBLITERATE);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                if (!player.isCreative()) player.getItemCooldownManager().set(this, ConfigConstructor.nightfall_smash_cooldown - EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) * 50);
                stack.damage(3, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));
                Vec3d vecBlocksAway = player.getRotationVector().multiply(3).add(player.getPos());
                BlockPos targetArea = new BlockPos(vecBlocksAway.x, user.getY(), vecBlocksAway.z);
                Box aoe = new Box(targetArea).expand(3);
                List<Entity> entities = world.getOtherEntities(player, aoe);
                float power = ConfigConstructor.nightfall_ability_damage;
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity target) {
                        entity.damage(CustomDamageSource.obliterateDamageSource(player), power + 2 * EnchantmentHelper.getAttackDamage(stack, ((LivingEntity) entity).getGroup()));
                        entity.setVelocity(entity.getVelocity().x, .5f, entity.getVelocity().z);
                        this.spawnRemnant(target, user);
                    }
                }
                player.world.playSound(player, targetArea, SoundRegistry.NIGHTFALL_BONK_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                if (!world.isClient) {
                    ParticleHandler.particleOutburstMap(world, 150, targetArea.getX(), targetArea.getY() + .1f, targetArea.getZ(), ParticleEvents.OBLITERATE_MAP, 1f);
                }
            }
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            this.spawnRemnant(target, attacker);
        }
        return super.postHit(stack, target, attacker);
    }

    public void spawnRemnant(LivingEntity target, LivingEntity attacker) {
        if (target.isUndead() && target.isDead() && attacker instanceof PlayerEntity && !this.isDisabled(attacker.getMainHandStack())) {
            double chance = new Random().nextDouble();
            World world = attacker.getEntityWorld();
            if (!world.isClient && this.canSummonEntity((ServerWorld) world, attacker, this.getSummonsListId()) && chance < ConfigConstructor.nightfall_summon_chance) {
                Remnant entity = new Remnant(EntityRegistry.REMNANT, world);
                entity.setPos(target.getX(), target.getY() + .1F, target.getZ());
                entity.setOwner((PlayerEntity) attacker);
                world.spawnEntity(entity);
                this.saveSummonUuid(attacker, entity.getUuid());
                world.playSound(null, target.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                if (!attacker.world.isClient) {
                    ParticleHandler.particleOutburstMap(attacker.getWorld(), 50, target.getX(), target.getY(), target.getZ(), ParticleEvents.SOUL_RUPTURE_MAP, 1f);
                }
            }
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public Text[] getAdditionalTooltips() {
        return new Text[] {
                new TranslatableText("tooltip.soulsweapons.nightfall.part_1").formatted(Formatting.DARK_GRAY),
                new TranslatableText("tooltip.soulsweapons.nightfall.part_2").formatted(Formatting.DARK_GRAY),
                new TranslatableText("tooltip.soulsweapons.nightfall.part_3").formatted(Formatting.DARK_GRAY)
        };
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(this)) {
            player.getItemCooldownManager().set(this, (ConfigConstructor.nightfall_shield_cooldown - EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) * 100));
            stack.damage(3, (LivingEntity)player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 200, ConfigConstructor.nightfall_ability_shield_power));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0));
            world.playSound(null, player.getBlockPos(), SoundRegistry.NIGHTFALL_SHIELD_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
    }

    @Override
    public float getBaseExpansion() {
        return ConfigConstructor.nightfall_calculated_fall_base_radius;
    }

    @Override
    public float getExpansionModifier() {
        return ConfigConstructor.nightfall_calculated_fall_height_increase_radius_modifier;
    }

    @Override
    public float getLaunchModifier() {
        return ConfigConstructor.nightfall_calculated_fall_target_launch_modifier;
    }

    @Override
    public float getMaxExpansion() {
        return ConfigConstructor.nightfall_calculated_fall_max_radius;
    }

    @Override
    public float getMaxDetonationDamage() {
        return ConfigConstructor.nightfall_calculated_fall_max_damage;
    }

    @Override
    public float getFallDamageIncreaseModifier() {
        return ConfigConstructor.nightfall_calculated_fall_height_increase_damage_modifier;
    }

    @Override
    public boolean shouldHeal() {
        return ConfigConstructor.nightfall_calculated_fall_should_heal;
    }

    @Override
    public float getHealFromDamageModifier() {
        return ConfigConstructor.nightfall_calculated_fall_heal_from_damage_modifier;
    }

    @Override
    public void doCustomEffects(LivingEntity target, LivingEntity user) {
        this.spawnRemnant(target, user);
    }

    @Override
    public Map<ParticleEffect, Vec3d> getParticles() {
        Map<ParticleEffect, Vec3d> map = new HashMap<>();
        map.put(ParticleTypes.SOUL_FIRE_FLAME, new Vec3d(1, 6, 1));
        return map;
    }

    @Override
    public int getMaxSummons() {
        return ConfigConstructor.nightfall_summoned_allies_cap;
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
}