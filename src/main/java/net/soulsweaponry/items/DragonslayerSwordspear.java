package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.ParticleEvents;
import net.soulsweaponry.util.ParticleHandler;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DragonslayerSwordspear extends SwordItem {

    private static final String RAINING = "raining_id";

    public DragonslayerSwordspear(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.DRAGON_SWORDSPEAR_DAMAGE.get(), pAttackSpeedModifier, pProperties);
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
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof Player playerEntity) {
            int i = this.getUseDuration(stack) - remainingUseTicks;
            if (i >= 10) {
                //float j = EnchantmentHelper.getAttackDamage(stack, user.getGroup());
                if (stack != user.getOffhandItem()) {
                    stack.hurtAndBreak(1, playerEntity, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
                    /*DragonslayerSwordspearEntity entity = new DragonslayerSwordspearEntity(world, playerEntity, stack);
                    entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 5.0F, 1.0F);
                    entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    world.spawnEntity(entity);*///TODO add projectile
                    //world.playSoundFromEntity(null, entity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    playerEntity.getCooldowns().addCooldown(this, (int) (CommonConfig.DRAGON_SWORDSPEAR_THROW_COOLDOWN.get() - (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack)*20)) / (world.isRaining() ? 2 : 1));
                } else if (!world.isClientSide) {
                    stack.hurtAndBreak(3, playerEntity, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
                    user.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 5));
                    user.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0));
                    AABB chunkBox = new AABB(user.getX() - 10, user.getY() - 5, user.getZ() - 10, user.getX() + 10, user.getY() + 5, user.getZ() + 10);
                    List<Entity> nearbyEntities = world.getEntities(user, chunkBox);
                    //Entity["EntityKey"/number?, l = "ClientLevel", x, y, z] and so on... Includes items aswell!
                    for (Entity nearbyEntity : nearbyEntities) {
                        if (nearbyEntity instanceof LivingEntity target && !(nearbyEntity instanceof TamableAnimal)) {
                            if (world.canSeeSky(target.blockPosition())) {
                                for (i = 0; i < CommonConfig.DRAGON_SWORDSPEAR_LIGHTNING_AMOUNT.get(); i++) {
                                    LightningBolt entity = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
                                    entity.setPos(target.getX(), target.getY(), target.getZ());
                                    world.addFreshEntity(entity);
                                }
                            } else {
                                double x = target.getX() - user.getX();
                                double z = target.getX() - user.getX();
                                target.knockback(5F, -x, -z);
                                target.hurt(DamageSource.mobAttack(user), CommonConfig.DRAGON_SWORDSPEAR_ABILITY_DAMAGE.get());
                                ParticleHandler.particleSphereList(world, 20, target.getX(), target.getY(), target.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.25f);
                            }
                            world.playSound(null, user.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1f, 1f);
                        }
                    }

                    int sharpness = WeaponUtil.getEnchantDamageBonus(stack);
                    if (!playerEntity.isCreative()) playerEntity.getCooldowns().addCooldown(this, (CommonConfig.DRAGON_SWORDSPEAR_ABILITY_COOLDOWN.get() - (sharpness*20)) / (world.isRaining() ? 2 : 1));
                }
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        this.updateRaining(pLevel, pStack);
    }

    private void updateRaining(Level world, ItemStack stack) {
        if (stack.hasTag()) {
            stack.getTag().putBoolean(RAINING, world.isRaining());
        }
    }

    private boolean getRaining(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(RAINING)) {
            return stack.getTag().getBoolean(RAINING);
        } else {
            return false;
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND && this.getRaining(stack)) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", CommonConfig.DRAGON_SWORDSPEAR_DAMAGE.get(), AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.2D, AttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
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
        }//TODO use greia kan forenkles inn i en annen class kanskje sammen med charge animation etc.
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.LIGHTNING_CALL, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.INFINITY, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.THROW_LIGHTNING, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.STORM_STOMP, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.WEATHERBORN, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }
}
