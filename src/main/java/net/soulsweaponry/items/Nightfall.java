package net.soulsweaponry.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.NightfallRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.ParticleHandler;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class Nightfall extends UltraHeavyWeapon implements IAnimatable, IKeybindAbility {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Nightfall(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.NIGHTFALL_DAMAGE.get(), pAttackSpeedModifier, pProperties, true);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemStack);
        }
        else {
            user.startUsingItem(hand);
            return InteractionResultHolder.success(itemStack);
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player player) {
            int i = this.getUseDuration(stack) - remainingUseTicks;
            if (i >= 10) {
                if (!player.isCreative()) player.getCooldowns().addCooldown(this, CommonConfig.NIGHTFALL_SMASH_COOLDOWN.get() - EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) * 50);
                stack.hurtAndBreak(3, player, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
                Vec3 vecBlocksAway = player.getViewVector(1f).scale(3).add(player.position());
                BlockPos targetArea = new BlockPos(vecBlocksAway.x, user.getY(), vecBlocksAway.z);
                AABB aoe = new AABB(targetArea).inflate(3);
                List<Entity> entities = world.getEntities(player, aoe);
                float power = CommonConfig.NIGHTFALL_ABILITY_DAMAGE.get();
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity) {
                        entity.hurt(/*CustomDamageSource.obliterateDamageSource(player)*/DamageSource.ANVIL, power + 2 * EnchantmentHelper.getDamageBonus(stack, ((LivingEntity) entity).getMobType()));//TODO weaponutil
                        entity.setDeltaMovement(entity.getDeltaMovement().add(0, .5f, 0));
                    }
                }
                world.playSound(player, targetArea, SoundRegistry.NIGHTFALL_BONK_EVENT.get(), SoundSource.PLAYERS, 1f, 1f);
                if (!world.isClientSide) {
                    //ParticleNetworking.sendServerParticlePacket((ServerWorld) world, PacketRegistry.OBLITERATE_ID, targetArea, 200);TODO better particle handling
                    HashMap<ParticleOptions, Vec3> map = new HashMap<>();
                    map.put(ParticleTypes.LARGE_SMOKE, new Vec3(1, 8, 1));
                    map.put(ParticleTypes.SOUL_FIRE_FLAME, new Vec3(2, 8, 2));
                    map.put(ParticleTypes.SOUL, new Vec3(2, 8, 2));
                    map.put(new ItemParticleOption(ParticleTypes.ITEM, Items.DIRT.getDefaultInstance()), new Vec3(1, 2, 1));
                    map.put(new ItemParticleOption(ParticleTypes.ITEM, Items.STONE.getDefaultInstance()), new Vec3(1, 2, 1));
                    ParticleHandler.particleOutburstMap(world, 150, targetArea.getX(), targetArea.getY() + .1f, targetArea.getZ(), map, 1f);
                }
            }
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (WeaponUtil.isUndead(target) && target.isDeadOrDying() && attacker instanceof Player) {
            double chance = new Random().nextDouble();
            if (chance < CommonConfig.NIGHTFALL_SUMMON_CHANCE.get()) {
                Level world = attacker.getLevel();
//                Remnant entity = new Remnant(EntityRegistry.REMNANT, world);
//                entity.setPos(target.getX(), target.getY() + .1F, target.getZ());
//                entity.setOwner((PlayerEntity) attacker);
//                world.spawnEntity(entity);TODO make entity
                world.playSound(null, target.getOnPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT.get(), SoundSource.PLAYERS, 1f, 1f);
                if (!world.isClientSide) {
//                    BlockPos pos = target.getBlockPos();TODO particle handling
//                    ParticleNetworking.sendServerParticlePacket((ServerWorld) attacker.world, PacketRegistry.SOUL_RUPTURE_PACKET_ID, pos, 50);
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SUMMON_GHOST, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SHIELD, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.OBLITERATE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.HEAVY, stack, tooltip);
            for (int i = 1; i <= 3; i++) {
                tooltip.add(new TranslatableComponent("tooltip.soulsweapons.nightfall.part_" + i).withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }

    @Override
    public void useKeybindAbilityServer(ServerLevel world, ItemStack stack, Player player) {
        if (!player.getCooldowns().isOnCooldown(this)) {
            player.getCooldowns().addCooldown(this, (CommonConfig.NIGHTFALL_SHIELD_COOLDOWN.get() - EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) * 100));
            stack.hurtAndBreak(3, player, (p_43296_) -> p_43296_.broadcastBreakEvent(player.getUsedItemHand()));
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 200, CommonConfig.NIGHTFALL_ABILITY_SHIELD_POWER.get()));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 0));
            world.playSound(null, player.getOnPos(), SoundRegistry.NIGHTFALL_SHIELD_EVENT.get(), SoundSource.PLAYERS, 1f, 1f);
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientLevel world, ItemStack stack, LocalPlayer player) {
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private NightfallRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new NightfallRenderer();

                return renderer;
            }
        });
    }
}
