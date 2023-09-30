package net.soulsweaponry.items;

import java.util.List;
import java.util.Random;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.ParticleNetworking;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Nightfall extends UltraHeavyWeapon implements IAnimatable, IKeybindAbility {
    
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    
    public Nightfall(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.nightfall_damage, attackSpeed, settings, true);
    }
    
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        }
        else {
            user.setCurrentHand(hand);
            return TypedActionResult.success(itemStack);
        }
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity player) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                if (!player.isCreative()) player.getItemCooldownManager().set(this, ConfigConstructor.nightfall_smash_cooldown - EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) * 50);
                stack.damage(3, player, (p_220045_0_) -> {
                    p_220045_0_.sendToolBreakStatus(player.getActiveHand());
                });
                Vec3d vecBlocksAway = player.getRotationVector().multiply(3).add(player.getPos());
                BlockPos targetArea = new BlockPos(vecBlocksAway.x, user.getY(), vecBlocksAway.z);
                Box aoe = new Box(targetArea).expand(3);
                List<Entity> entities = world.getOtherEntities(player, aoe);
                float power = ConfigConstructor.nightfall_ability_damage;
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity) {
                        entity.damage(CustomDamageSource.obliterateDamageSource(player), power + 2 * EnchantmentHelper.getAttackDamage(stack, ((LivingEntity) entity).getGroup()));
                        entity.setVelocity(entity.getVelocity().x, .5f, entity.getVelocity().z);
                    }
                }
                player.world.playSound(player, targetArea, SoundRegistry.NIGHTFALL_BONK_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                if (!world.isClient) {
                    ParticleNetworking.sendServerParticlePacket((ServerWorld) world, PacketRegistry.OBLITERATE_ID, targetArea, 200);
                }
            }
        }
    }

    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.isUndead() && target.isDead() && attacker instanceof PlayerEntity) {
            double chance = new Random().nextDouble();
            if (chance < ConfigConstructor.nightfall_summon_chance) {
                World world = attacker.getEntityWorld();
                Remnant entity = new Remnant(EntityRegistry.REMNANT, world);
                entity.setPos(target.getX(), target.getY() + .1F, target.getZ());
                entity.setOwner((PlayerEntity) attacker);
                world.spawnEntity(entity);
                world.playSound(null, target.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                if (!attacker.world.isClient) {
                    BlockPos pos = target.getBlockPos();
                    ParticleNetworking.sendServerParticlePacket((ServerWorld) attacker.world, PacketRegistry.SOUL_RUPTURE_PACKET_ID, pos, 50);
                }
            }
        }
        return super.postHit(stack, target, attacker);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
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
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SUMMON_GHOST, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SHIELD, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.OBLITERATE, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.HEAVY, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(this)) {
            player.getItemCooldownManager().set(this, (ConfigConstructor.nightfall_shield_cooldown - EnchantmentHelper.getLevel(Enchantments.UNBREAKING, stack) * 100));
            stack.damage(3, (LivingEntity)player, (p_220045_0_) -> {
                p_220045_0_.sendToolBreakStatus(player.getActiveHand());
            });
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 200, ConfigConstructor.nightfall_ability_shield_power));
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0));
            world.playSound(null, player.getBlockPos(), SoundRegistry.NIGHTFALL_SHIELD_EVENT, SoundCategory.PLAYERS, 1f, 1f);
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
    }
}
