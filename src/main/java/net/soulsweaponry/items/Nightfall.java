package net.soulsweaponry.items;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
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
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.NightfallRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.entitydata.SummonsData;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.*;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class Nightfall extends UltraHeavyWeapon implements IAnimatable, IKeybindAbility, ISummonAllies {
    
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    
    public Nightfall(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, CommonConfig.NIGHTFALL_DAMAGE.get(), attackSpeed, settings, true);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                if (!player.isCreative()) player.getItemCooldownManager().set(this, CommonConfig.NIGHTFALL_SMASH_COOLDOWN.get() - EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) * 50);
                stack.damage(3, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));
                Vec3d vecBlocksAway = player.getRotationVector().multiply(3).add(player.getPos());
                BlockPos targetArea = new BlockPos(vecBlocksAway.x, user.getY(), vecBlocksAway.z);
                Box aoe = new Box(targetArea).expand(3);
                List<Entity> entities = world.getOtherEntities(player, aoe);
                float power = CommonConfig.NIGHTFALL_ABILITY_DAMAGE.get();
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity target) {
                        entity.damage(CustomDamageSource.obliterateDamageSource(player), power + 2 * EnchantmentHelper.getAttackDamage(stack, ((LivingEntity) entity).getGroup()));
                        entity.setVelocity(entity.getVelocity().x, .5f, entity.getVelocity().z);
                        this.spawnRemnant(target, user);
                    }
                }
                player.world.playSound(player, targetArea, SoundRegistry.NIGHTFALL_BONK_EVENT.get(), SoundCategory.PLAYERS, 1f, 1f);
                if (!world.isClient) {
                    ParticleHandler.particleOutburstMap(world, 150, targetArea.getX(), targetArea.getY() + .1f, targetArea.getZ(), ParticleEvents.OBLITERATE_MAP, 1f);
                }
            }
        }
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.isDisabled()) {
            stack.damage(1, attacker, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
            this.notifyDisabled(attacker);
            return true;
        }
        this.spawnRemnant(target, attacker);
        return super.postHit(stack, target, attacker);
    }

    public void spawnRemnant(LivingEntity target, LivingEntity attacker) {
        if (target.isUndead() && target.isDead() && attacker instanceof PlayerEntity player && !this.isDisabled()) {
            double chance = new Random().nextDouble();
            World world = attacker.getEntityWorld();
            if (!world.isClient && this.canSummonEntity((ServerWorld) world, attacker, this.getSummonsListId()) && chance < CommonConfig.NIGHTFALL_SUMMON_CHANCE.get()) {
                Remnant entity = new Remnant(EntityRegistry.REMNANT.get(), world);
                entity.setPos(target.getX(), target.getY() + .1F, target.getZ());
                entity.setOwner(player);
                world.spawnEntity(entity);
                this.saveSummonUuid(attacker, entity.getUuid());
                world.playSound(null, target.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT.get(), SoundCategory.PLAYERS, 1f, 1f);
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
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SUMMON_GHOST, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SHIELD, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.OBLITERATE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.HEAVY, stack, tooltip);
            for (int i = 1; i <= 3; i++) {
                tooltip.add(new TranslatableText("tooltip.soulsweapons.nightfall.part_" + i).formatted(Formatting.DARK_GRAY));
            }
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (this.isDisabled()) {
            this.notifyDisabled(player);
            return;
        }
        if (!player.getItemCooldownManager().isCoolingDown(this)) {
            player.getItemCooldownManager().set(this, (CommonConfig.NIGHTFALL_SHIELD_COOLDOWN.get() - EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) * 100));
            stack.damage(3, (LivingEntity)player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 200, CommonConfig.NIGHTFALL_ABILITY_SHIELD_POWER.get()));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0));
            world.playSound(null, player.getBlockPos(), SoundRegistry.NIGHTFALL_SHIELD_EVENT.get(), SoundCategory.PLAYERS, 1f, 1f);
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
    }

    @Override
    public float getBaseExpansion() {
        return CommonConfig.NIGHTFALL_CALCULATED_FALL_BASE_RADIUS.get();
    }

    @Override
    public float getExpansionModifier() {
        return CommonConfig.NIGHTFALL_CALCULATED_FALL_HEIGHT_INCREASE_RADIUS_MODIFIER.get();
    }

    @Override
    public float getLaunchModifier() {
        return CommonConfig.NIGHTFALL_CALCULATED_FALL_TARGET_LAUNCH_MODIFIER.get();
    }

    @Override
    public float getMaxExpansion() {
        return CommonConfig.NIGHTFALL_CALCULATED_FALL_MAX_RADIUS.get();
    }

    @Override
    public float getMaxDetonationDamage() {
        return CommonConfig.NIGHTFALL_CALCULATED_FALL_MAX_DAMAGE.get();
    }

    @Override
    public float getFallDamageIncreaseModifier() {
        return CommonConfig.NIGHTFALL_CALCULATED_FALL_HEIGHT_INCREASE_DAMAGE_MODIFIER.get();
    }

    @Override
    public boolean shouldHeal() {
        return CommonConfig.NIGHTFALL_CALCULATED_FALL_SHOULD_HEAL.get();
    }

    @Override
    public float getHealFromDamageModifier() {
        return CommonConfig.NIGHTFALL_CALCULATED_FALL_HEAL_FROM_DAMAGE_MODIFIER.get();
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
        return CommonConfig.NIGHTFALL_ALLIES_CAP.get();
    }

    @Override
    public String getSummonsListId() {
        return "NightfallSummons";
    }

    @Override
    public void saveSummonUuid(LivingEntity user, UUID summonUuid) {
        SummonsData.addSummonUUID(user, summonUuid, this.getSummonsListId());
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private NightfallRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BuiltinModelItemRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new NightfallRenderer();

                return renderer;
            }
        });
    }

    @Override
    public boolean isDisabled() {
        return CommonConfig.DISABLE_USE_NIGHTFALL.get();
    }
}
