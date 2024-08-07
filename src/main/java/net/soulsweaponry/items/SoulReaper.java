package net.soulsweaponry.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.Forlorn;
import net.soulsweaponry.entity.mobs.SoulReaperGhost;
import net.soulsweaponry.entity.mobs.Soulmass;
import net.soulsweaponry.entitydata.IEntityDataSaver;
import net.soulsweaponry.entitydata.SummonsData;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.UUID;

public class SoulReaper extends SoulHarvestingItem implements IAnimatable, ISummonAllies {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public SoulReaper(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.soul_reaper_damage, ConfigConstructor.soul_reaper_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.SOUL_RELEASE);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
        if (this.isDisabled(stack)) {
            this.notifyDisabled(player);
            return TypedActionResult.fail(stack);
        }
        if (stack.hasNbt() && stack.getNbt().contains(KILLS)) {
            int power = this.getSouls(stack);
            if (player.isCreative()) power = player.getRandom().nextBetween(5, 50);
            if (power >= 3 && !world.isClient && this.canSummonEntity((ServerWorld) world, player, this.getSummonsListId())) {
                Vec3d vecBlocksAway = player.getRotationVector().multiply(3).add(player.getPos());
                ParticleHandler.particleOutburstMap(world, 50, vecBlocksAway.getX(), vecBlocksAway.getY(), vecBlocksAway.getZ(), ParticleEvents.CONJURE_ENTITY_MAP, 1f);
                world.playSound(null, player.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.PLAYERS, 0.8f, 1f);
                if (power < 10) {
                    SoulReaperGhost entity = new SoulReaperGhost(EntityRegistry.SOUL_REAPER_GHOST, world);
                    entity.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
                    entity.setOwner(player);
                    world.spawnEntity(entity);
                    this.saveSummonUuid(player, entity.getUuid());
                    if (!player.isCreative()) this.addAmount(stack, -3);
                } else if (player.isSneaking() || power < 30) {
                    Forlorn entity = new Forlorn(EntityRegistry.FORLORN, world);
                    entity.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
                    entity.setOwner(player);
                    world.spawnEntity(entity);
                    this.saveSummonUuid(player, entity.getUuid());
                    if (!player.isCreative()) this.addAmount(stack, -10);
                } else {
                    Soulmass entity = new Soulmass(EntityRegistry.SOULMASS, world);
                    entity.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
                    entity.setOwner(player);
                    world.spawnEntity(entity);
                    this.saveSummonUuid(player, entity.getUuid());
                    if (!player.isCreative()) this.addAmount(stack, -30);
                }

                stack.damage(3, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(hand));
                return TypedActionResult.success(stack, true);
            } 
        }
        return TypedActionResult.fail(stack);
	}

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        //Figure out how to dynamically change the animation with ISyncable (problem now is that it can't override the prev. animation)
        /*ClientPlayerEntity player;
        if ((player = MinecraftClient.getInstance().player) != null) {
            for (Hand hand : Hand.values()) {
                ItemStack stack = player.getStackInHand(hand);
                if (stack.isOf(WeaponRegistry.SOUL_REAPER)) {
                    int souls = this.getSouls(stack);
                    if (souls >= 10) {
                        if (souls >= 30) {
                            event.getController().setAnimation(new AnimationBuilder().addAnimation("high_souls", EDefaultLoopTypes.LOOP));
                        } else {
                            event.getController().setAnimation(new AnimationBuilder().addAnimation("mid_souls", EDefaultLoopTypes.LOOP));
                        }
                        return PlayState.CONTINUE;
                    }
                }
            }
        }*/
        event.getController().setAnimation(new AnimationBuilder().addAnimation("low_souls", EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public int getMaxSummons() {
        return ConfigConstructor.soul_reaper_summoned_allies_cap;
    }

    @Override
    public String getSummonsListId() {
        return "SoulReaperSummons";
    }

    @Override
    public void saveSummonUuid(LivingEntity user, UUID summonUuid) {
        SummonsData.addSummonUUID((IEntityDataSaver) user, summonUuid, this.getSummonsListId());
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_soul_reaper;
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_soul_reaper;
    }
}