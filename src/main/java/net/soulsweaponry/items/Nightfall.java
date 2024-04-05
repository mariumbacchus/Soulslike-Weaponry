package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
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
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.*;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Nightfall extends UltraHeavyWeapon implements GeoItem, IKeybindAbility, ISummonAllies {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    
    public Nightfall(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.nightfall_damage, attackSpeed, settings, true);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                if (!player.isCreative()) player.getItemCooldownManager().set(this, ConfigConstructor.nightfall_smash_cooldown - EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) * 50);
                stack.damage(3, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));
                Vec3d vecBlocksAway = player.getRotationVector().multiply(3).add(player.getPos());
                BlockPos targetArea = new BlockPos((int)vecBlocksAway.x, (int) user.getY(), (int) vecBlocksAway.z);
                Box aoe = new Box(targetArea).expand(3);
                List<Entity> entities = world.getOtherEntities(player, aoe);
                float power = ConfigConstructor.nightfall_ability_damage;
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity target) {
                        entity.damage(CustomDamageSource.create(world, CustomDamageSource.OBLITERATED, player), power + 2 * EnchantmentHelper.getAttackDamage(stack, ((LivingEntity) entity).getGroup()));
                        entity.setVelocity(entity.getVelocity().x, .5f, entity.getVelocity().z);
                        this.spawnRemnant(target, user);
                    }
                }
                world.playSound(player, targetArea, SoundRegistry.NIGHTFALL_BONK_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                if (!world.isClient) {
                    ParticleHandler.particleOutburstMap(world, 150, targetArea.getX(), targetArea.getY() + .1f, targetArea.getZ(), ParticleEvents.OBLITERATE_MAP, 1f);
                }
            }
        }
    }

    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        this.spawnRemnant(target, attacker);
        this.gainStrength(attacker);
        return super.postHit(stack, target, attacker);
    }

    public void spawnRemnant(LivingEntity target, LivingEntity attacker) {
        if (target.isUndead() && target.isDead() && attacker instanceof PlayerEntity) {
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
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            private final NightfallRenderer renderer = new NightfallRenderer();

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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SUMMON_GHOST, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SHIELD, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.OBLITERATE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.HEAVY, stack, tooltip);
            for (int i = 1; i <= 3; i++) {
                tooltip.add(Text.translatable("tooltip.soulsweapons.nightfall.part_" + i).formatted(Formatting.DARK_GRAY));
            }
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
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
        return 3f;
    }

    @Override
    public float getExpansionModifier() {
        return 1.5f;
    }

    @Override
    public float getLaunchDivisor() {
        return 30;
    }

    @Override
    public boolean shouldHeal() {
        return false;
    }

    @Override
    public StatusEffectInstance[] applyEffects() {
        return new StatusEffectInstance[0];
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
}
