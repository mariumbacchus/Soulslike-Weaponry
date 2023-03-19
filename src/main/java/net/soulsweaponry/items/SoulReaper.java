package net.soulsweaponry.items;

import java.util.List;
import java.util.Random;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.Forlorn;
import net.soulsweaponry.entity.mobs.SoulReaperGhost;
import net.soulsweaponry.entity.mobs.Soulmass;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.PacketRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleNetworking;
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

public class SoulReaper extends SoulHarvestingItem implements IAnimatable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    
    public SoulReaper(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.soulreaper_damage, attackSpeed, settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
        if (stack.hasNbt() && stack.getNbt().contains(KILLS)) {
            int power = this.getSouls(stack);
            if (power >= 3) {
                Vec3d vecBlocksAway = player.getRotationVector().multiply(3).add(player.getPos());
                if (!world.isClient) {
                    ParticleNetworking.sendServerParticlePacket((ServerWorld) world, PacketRegistry.CONJURE_ENTITY_PACKET_ID, new BlockPos(vecBlocksAway), 50);
                } else {
                    this.spawnParticles(world, vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
                }

                world.playSound(player, player.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.PLAYERS, 0.8f, 1f);
                if (power < 10) {
                    SoulReaperGhost entity = new SoulReaperGhost(EntityRegistry.SOUL_REAPER_GHOST, world);
                    entity.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
                    entity.setOwner(player);
                    world.spawnEntity(entity);
                    this.addAmount(stack, -3);
                } else if (power >= 10 && power < 30) {
                    Forlorn entity = new Forlorn(EntityRegistry.FORLORN, world);
                    entity.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
                    entity.setOwner(player);
                    world.spawnEntity(entity);
                    this.addAmount(stack, -10);
                } else if (power >= 30) {
                    Soulmass entity = new Soulmass(EntityRegistry.SOULMASS, world);
                    entity.setPos(vecBlocksAway.x, player.getY() + .1f, vecBlocksAway.z);
                    entity.setOwner(player);
                    world.spawnEntity(entity);
                    this.addAmount(stack, -30);
                }

                stack.damage(3, player, (p_220045_0_) -> {
                    p_220045_0_.sendToolBreakStatus(hand);
                });
                return TypedActionResult.success(stack, world.isClient());
            } 
        }
        return TypedActionResult.fail(stack); 
	}

    private void spawnParticles(World world, double x, double y, double z) {
        Random random = new Random();
        double d = random.nextGaussian() * 0.05D;
        double e = random.nextGaussian() * 0.05D;
        for(int i = 0; i < 50; ++i) {
            double newX = random.nextDouble() - 1D * 0.5D + random.nextGaussian() * 0.15D + d;
            double newZ = random.nextDouble() - 1D * 0.5D + random.nextGaussian() * 0.15D + e;
            double newY = random.nextDouble() - 1D * 0.5D + random.nextDouble() * 0.5D;
            world.addParticle(ParticleTypes.SOUL, x, y, z, newX/8, newY/2, newZ/8);
            world.addParticle(ParticleTypes.DRAGON_BREATH, x, y, z, newX/8, newY/2, newZ/8);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SOUL_TRAP, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SOUL_RELEASE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.COLLECT, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("low_souls", EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));    
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}