package net.soulsweaponry.items;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.CometSpearEntity;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.util.CustomDamageSource;
import net.soulsweaponry.util.ParticleNetworking;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class CometSpear extends SwordItem implements IAnimatable{
    
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final String SMASH = "ground_slam";
    public static final String FALL_DISTANCE = "fall_distance";

    public CometSpear(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.comet_spear_damage, attackSpeed, settings);
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity)user;
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                float enchant = WeaponUtil.getEnchantDamageBonus(stack);
                if (stack == user.getOffHandStack()) {
                    float f = user.getYaw();
                    float g = user.getPitch();
                    float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
                    float k = -MathHelper.sin(g * 0.017453292F);
                    float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
                    float m = MathHelper.sqrt(h * h + k * k + l * l);
                    float n = 3.0F * ((5.0F + enchant) / 4.0F);
                    h *= n / m;
                    k *= n / m;
                    l *= n / m;
                    
                    user.addVelocity((double)h, (double)k, (double)l);
                    playerEntity.useRiptide(20);
                    world.playSoundFromEntity(null, playerEntity, SoundEvents.ITEM_TRIDENT_RIPTIDE_3, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    if (playerEntity.isOnGround()) {
                        playerEntity.move(MovementType.SELF, new Vec3d(0.0D, 1.1999999284744263D, 0.0D));
                    }
                    user.addStatusEffect(new StatusEffectInstance(EffectRegistry.CALCULATED_FALL, 600, 0));
                    playerEntity.getItemCooldownManager().set(this, (int) (ConfigConstructor.comet_spear_skyfall_ability_cooldown-(enchant*20)));
                    if (stack.hasNbt()) {
                        stack.getNbt().putBoolean(SMASH, true);
                    }
                    stack.damage(4, (LivingEntity)playerEntity, (p_220045_0_) -> {
                        p_220045_0_.sendToolBreakStatus(user.getActiveHand());
                    });
                } else {
                    stack.damage(2, (LivingEntity)playerEntity, (p_220045_0_) -> {
                        p_220045_0_.sendToolBreakStatus(user.getActiveHand());
                    });
                    playerEntity.getItemCooldownManager().set(this, (int) (ConfigConstructor.comet_spear_throw_ability_cooldown-(enchant*5)));
                    
                    CometSpearEntity entity = new CometSpearEntity(world, playerEntity, stack);
                    entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 5.0F, 1.0F);
                    entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    world.spawnEntity(entity);
                    world.playSoundFromEntity((PlayerEntity)null, entity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            float power = ConfigConstructor.comet_spear_ability_damage;
            CometSpear.detonateGround(player, power, 0, 2, stack, world, false, 35);
        }
    }

    public static void detonateGround(PlayerEntity player, float power, float baseExpansion, float expansionModifier, 
            ItemStack stack, World world, boolean shouldHeal, float launchDivisor) {
        if (!player.isOnGround() && stack.hasNbt() && stack.getNbt().contains(SMASH) && stack.getNbt().getBoolean(SMASH)) {
            stack.getNbt().putFloat(FALL_DISTANCE, player.fallDistance);
        }
        if (stack.hasNbt() && stack.getNbt().contains(SMASH) && stack.getNbt().contains(FALL_DISTANCE) && stack.getNbt().getBoolean(SMASH) && player.isOnGround()) {
            float expansion = baseExpansion + expansionModifier * (stack.getNbt().getFloat(FALL_DISTANCE)/10);
            power += stack.getNbt().getFloat(FALL_DISTANCE)/5;
            Box box = player.getBoundingBox().expand(expansion);
            List<Entity> entities = world.getOtherEntities(player, box);
            for (Entity targets : entities) {
                if (targets instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) targets;
                    livingEntity.damage(CustomDamageSource.obliterateDamageSource(player), power + EnchantmentHelper.getAttackDamage(stack, livingEntity.getGroup()));
                    livingEntity.addVelocity(0, stack.getNbt().getFloat(FALL_DISTANCE)/launchDivisor, 0);
                    if (shouldHeal) player.heal(ConfigConstructor.lifesteal_item_base_healing - 1 + (ConfigConstructor.lifesteal_item_heal_scales ? WeaponUtil.getEnchantDamageBonus(stack)/2 : 0));
                }
            }
            world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);

            double pDistance = stack.getNbt().getFloat(FALL_DISTANCE) >= 25 ? stack.getNbt().getFloat(FALL_DISTANCE)/25 : 1;
            if (!world.isClient) {
                ParticleNetworking.specificServerParticlePacket((ServerWorld) world, PacketRegistry.GRAND_SKYFALL_SMASH_ID, player.getBlockPos(), pDistance);
            }
            //Reset nbts
            stack.getNbt().putFloat(FALL_DISTANCE, 0);
            stack.getNbt().putBoolean(SMASH, false);
        }
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (itemStack.getDamage() >= itemStack.getMaxDamage() - 1) {
            return TypedActionResult.fail(itemStack);
        } 
         else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {        
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.SKYFALL, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.INFINITY, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.CRIT, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
