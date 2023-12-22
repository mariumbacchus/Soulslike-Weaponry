package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.IItemRenderProperties;
import net.soulsweaponry.client.renderer.item.MjolnirItemRenderer;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class Mjolnir extends SwordItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final String RAINING = "raining";
    public static final String LIGHTNING_STATUS = "lightning_status";
    public static final String SHOULD_UPDATE_LIGHTNING = "should_update_lightning";
    public static final String OWNERS_LAST_POS = "owners_last_pos";

    public Mjolnir(Tier pTier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, CommonConfig.MJOLNIR_DAMAGE.get(), pAttackSpeedModifier, pProperties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }//TODO use animation og use duration er lik overalt for mange av weaponsene, kanskje lage en abstract class med de

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
        int i = this.getUseDuration(stack) - remainingUseTicks;
        if (user instanceof Player player && i >= 10) {
            int cooldown = 0;
            stack.hurtAndBreak(3, player, (p_43296_) -> p_43296_.broadcastBreakEvent(user.getUsedItemHand()));
            if (player.isCrouching()) {
                this.smashGround(stack, world, player);
                this.startLightningCall(stack);
                cooldown = CommonConfig.MJOLNIR_LIGHTNING_SMASH_COOLDOWN.get();
            } else if (player.getOffhandItem().is(this)) {
                this.riptide(player, world, stack);
                if (!world.isRaining()) cooldown = CommonConfig.MJOLNIR_RIPTIDE_COOLDOWN.get();
            } else {
                this.throwHammer(world, player, stack);
            }
            if (!player.isCreative()) player.getCooldowns().addCooldown(this, cooldown);
        }
    }

    private void throwHammer(Level world, Player player, ItemStack stack) {
        if (stack.hasTag()) {
            stack.getTag().putIntArray(OWNERS_LAST_POS, new int[]{player.getBlockX(), player.getBlockY(), player.getBlockZ()});
        }
//        MjolnirProjectile projectile = new MjolnirProjectile(world, player, stack);
//        float speed = WeaponUtil.getEnchantDamageBonus(stack)/5;
//        projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 2.5f + speed, 1.0f);
//        projectile.pickupType = PickupPermission.CREATIVE_ONLY;
//        world.spawnEntity(projectile);TODO add projectile
        //world.playSound(null, projectile, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0f, 1.0f);
        if (!player.isCreative()) {
            player.getInventory().removeItem(stack);
        }
    }
    //TODO riptide greia brukes flere steder, vurder å flytte til weaponutil
    private void riptide(Player player, Level world, ItemStack stack) {
        float sharpness = WeaponUtil.getEnchantDamageBonus(stack);
        float f = player.getYRot();
        float g = player.getXRot();
        float h = -Mth.sin(f * 0.017453292F) * Mth.cos(g * 0.017453292F);
        float k = -Mth.sin(g * 0.017453292F);
        float l = Mth.cos(f * 0.017453292F) * Mth.cos(g * 0.017453292F);
        float m = Mth.sqrt(h * h + k * k + l * l);
        float n = 3.0F * ((5.0F + sharpness) / 4.0F);
        h *= n / m;
        k *= n / m;
        l *= n / m;
        player.setDeltaMovement(player.getDeltaMovement().add(h, k, l));
        player.startAutoSpinAttack(20);
        if (player.isOnGround()) {
            player.move(MoverType.SELF, new Vec3(0.0, 1.1999999284744263, 0.0));
        }
        world.playSound(null, player, SoundEvents.TRIDENT_RIPTIDE_3, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private void smashGround(ItemStack stack, Level world, Player player) {
        AABB box = player.getBoundingBox().inflate(3);
        List<Entity> entities = world.getEntities(player, box);
        float power = CommonConfig.MJOLNIR_SMASH_DAMAGE.get();
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                entity.hurt(DamageSource.mobAttack(player), power + 2 * EnchantmentHelper.getDamageBonus(stack, ((LivingEntity) entity).getMobType()));
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, .25f, 0));
            }
        }

        world.playSound(null, player, SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, .75f, 1f);
        double d = player.getRandom().nextGaussian() * 0.05D;
        double e = player.getRandom().nextGaussian() * 0.05D;
        for (int j = 0; j < 200; ++j) {
            double newX = player.getRandom().nextDouble() - 0.5D + player.getRandom().nextGaussian() * 0.15D + d;
            double newZ = player.getRandom().nextDouble() - 0.5D + player.getRandom().nextGaussian() * 0.15D + e;
            double newY = player.getRandom().nextDouble() - 0.5D + player.getRandom().nextDouble() * 0.5D;
            world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, Items.STONE.getDefaultInstance()), player.getX(), player.getY(), player.getZ(), newX, newY/2, newZ);
            world.addParticle(new ItemParticleOption(ParticleTypes.ITEM, Items.DIRT.getDefaultInstance()), player.getX(), player.getY(), player.getZ(), newX, newY/2, newZ);
            world.addParticle(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY(), player.getZ(), newX, newY/8, newZ);
            world.addParticle(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY(), player.getZ(), newX*10, newY*2, newZ*10);
        }
    }

    private void startLightningCall(ItemStack stack) {
        if (stack.hasTag()) {
            stack.getTag().putBoolean(SHOULD_UPDATE_LIGHTNING, true);
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
            return InteractionResultHolder.consume(itemStack);
        }
    }

    private void lightningCall(Player player, Level world, ItemStack stack) {
        if (stack.hasTag()) {
            int[] triggers = {10, 20, 30};
            for (int i = 1; i < triggers.length + 1; i++) {
                if (this.getLightningStatus(stack) == triggers[i - 1]) {
                    this.spawnLightning(i, player, world);
                }
            }
        }

    }

    private void spawnLightning(int multiplier, Player player, Level world) {
        int r = 5*multiplier;
        for (int theta = 0; theta < 360; theta+=30) {
            double x0 = player.getX();
            double z0 = player.getZ();
            double x = x0 + r * Math.cos(theta * Math.PI / 180);
            double z = z0 + r * Math.sin(theta * Math.PI / 180);
            BlockPos pos = new BlockPos(x, player.getY(), z);
            if (world.canSeeSky(new BlockPos(x, player.getY(), z))) {
                LightningBolt entity = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
                entity.setPos(pos.getX(), pos.getY(), pos.getZ());
                world.addFreshEntity(entity);
            }
        }
    }

    private int getLightningStatus(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(LIGHTNING_STATUS)) {
            return stack.getTag().getInt(LIGHTNING_STATUS);
        } else {
            return 0;
        }
    }
    //TODO kan erstattes med flame pillar-like entities som summoner lightning i stedet når trigges i stedet for å ticke player
    private void updateLightningStatus(ItemStack stack, Player player, Level world) {
        if (stack.hasTag()) {
            if (stack.getTag().contains(LIGHTNING_STATUS) && stack.getTag().contains(SHOULD_UPDATE_LIGHTNING)
                    && stack.getTag().getBoolean(SHOULD_UPDATE_LIGHTNING)) {
                this.lightningCall(player, world, stack);
                stack.getTag().putInt(LIGHTNING_STATUS, stack.getTag().getInt(LIGHTNING_STATUS) + 1);
                if (stack.getTag().getInt(LIGHTNING_STATUS) >= 40) {
                    stack.getTag().putInt(LIGHTNING_STATUS, 0);
                    stack.getTag().putBoolean(SHOULD_UPDATE_LIGHTNING, false);
                }
            } else {
                stack.getTag().putInt(LIGHTNING_STATUS, 0);
                stack.getTag().putBoolean(SHOULD_UPDATE_LIGHTNING, false);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(stack, world, entity, pSlotId, pIsSelected);
        this.refreshRaining(world, stack);
        if (entity instanceof Player) this.updateLightningStatus(stack, (Player) entity, world);
    }

    private void refreshRaining(Level world, ItemStack stack) {
        if (stack.hasTag()) {
            stack.getTag().putBoolean(RAINING, world.isRaining());
        }
    }

    private boolean isRaining(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains(RAINING)) {
            return stack.getTag().getBoolean(RAINING);
        } else {
            return false;
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.isRaining(stack) ? CommonConfig.MJOLNIR_RAIN_BONUS_DAMAGE.get() - 1 + CommonConfig.MJOLNIR_DAMAGE.get() : CommonConfig.MJOLNIR_DAMAGE.get() - 1, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.8D, AttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.MJOLNIR_LIGHTNING, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.THROW_LIGHTNING, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.RETURNING, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.WEATHERBORN, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.OFF_HAND_FLIGHT, stack, tooltip);
        } else {
            tooltip.add(new TranslatableComponent("tooltip.soulsweapons.shift"));
        }
        super.appendHoverText(stack, pLevel, tooltip, pIsAdvanced);
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private MjolnirItemRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new MjolnirItemRenderer();

                return renderer;
            }
        });
    }
}
