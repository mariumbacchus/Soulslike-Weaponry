package net.soulsweaponry.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity.PickupPermission;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.MjolnirProjectile;
import net.soulsweaponry.entity.projectile.invisible.WarmupLightningEntity;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class Mjolnir extends ChargeToUseItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final String RAINING = "raining";
    public static final String OWNERS_LAST_POS = "owners_last_pos";

    public Mjolnir(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.mjolnir_damage, attackSpeed, settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack) - remainingUseTicks;
        if (user instanceof PlayerEntity player && i >= 10) {
            int cooldown = 0;
            stack.damage(3, player, p -> p.sendToolBreakStatus(user.getActiveHand()));
            if (player.isSneaking()) {
                this.smashGround(stack, world, player);
                this.lightningCall(player, world);
                cooldown = ConfigConstructor.mjolnir_lightning_smash_cooldown;
            } else if (player.getOffHandStack().isOf(this)) {
                this.riptide(player, world, stack);
                if (!world.isRaining()) cooldown = ConfigConstructor.mjolnir_riptide_cooldown;
            } else {
                this.throwHammer(world, player, stack);
            }
            if (!player.isCreative()) player.getItemCooldownManager().set(this, cooldown);
        }
    }

    private void throwHammer(World world, PlayerEntity player, ItemStack stack) {
        if (stack.hasNbt()) {
            stack.getNbt().putIntArray(OWNERS_LAST_POS, new int[]{player.getBlockX(), player.getBlockY(), player.getBlockZ()});
        }
        MjolnirProjectile projectile = new MjolnirProjectile(world, player, stack);
        float speed = WeaponUtil.getEnchantDamageBonus(stack)/5f;
        projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 2.5f + speed, 1.0f);
        projectile.pickupType = PickupPermission.CREATIVE_ONLY;
        world.spawnEntity(projectile);
        world.playSoundFromEntity(null, projectile, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f);
        if (!player.getAbilities().creativeMode) {
            player.getInventory().removeOne(stack);
        }
    }

    private void riptide(PlayerEntity player, World world, ItemStack stack) {
        float sharpness = WeaponUtil.getEnchantDamageBonus(stack);
        float f = player.getYaw();
        float g = player.getPitch();
        float h = -MathHelper.sin(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
        float k = -MathHelper.sin(g * 0.017453292F);
        float l = MathHelper.cos(f * 0.017453292F) * MathHelper.cos(g * 0.017453292F);
        float m = MathHelper.sqrt(h * h + k * k + l * l);
        float n = 3.0F * ((5.0F + sharpness) / 4.0F);
        h *= n / m;
        k *= n / m;
        l *= n / m;
        player.addVelocity(h, k, l);
        player.useRiptide(20);
        if (player.isOnGround()) {
            player.move(MovementType.SELF, new Vec3d(0.0, 1.1999999284744263, 0.0));
        }
        world.playSoundFromEntity(null, player, SoundEvents.ITEM_TRIDENT_RIPTIDE_3, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    private void smashGround(ItemStack stack, World world, PlayerEntity player) {
        Box box = player.getBoundingBox().expand(3);
        List<Entity> entities = world.getOtherEntities(player, box);
        float power = ConfigConstructor.mjolnir_smash_damage;
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                entity.damage(DamageSource.mob(player), power + 2*EnchantmentHelper.getAttackDamage(stack, ((LivingEntity) entity).getGroup()));
                entity.addVelocity(0, .25f, 0);
            }
        }

        world.playSoundFromEntity(null, player, SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, .75f, 1f);
        double d = player.getRandom().nextGaussian() * 0.05D;
        double e = player.getRandom().nextGaussian() * 0.05D;
        for(int j = 0; j < 200; ++j) {
            double newX = player.getRandom().nextDouble() - 0.5D + player.getRandom().nextGaussian() * 0.15D + d;
            double newZ = player.getRandom().nextDouble() - 0.5D + player.getRandom().nextGaussian() * 0.15D + e;
            double newY = player.getRandom().nextDouble() - 0.5D + player.getRandom().nextDouble() * 0.5D;
            world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, Items.STONE.getDefaultStack()), player.getX(), player.getY(), player.getZ(), newX, newY/2, newZ);
            world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, Items.DIRT.getDefaultStack()), player.getX(), player.getY(), player.getZ(), newX, newY/2, newZ);
            world.addParticle(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY(), player.getZ(), newX, newY/8, newZ);
            world.addParticle(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY(), player.getZ(), newX*10, newY*2, newZ*10);
        }
    }

    private void lightningCall(PlayerEntity player, World world) {
        for (int i = 1; i < ConfigConstructor.mjolnir_lightning_circle_amount + 1; i++) {
            int r = 5 * i;
            for (int theta = 0; theta < 360; theta+=30) {
                double x0 = player.getX();
                double z0 = player.getZ();
                double x = x0 + r * Math.cos(theta * Math.PI / 180);
                double z = z0 + r * Math.sin(theta * Math.PI / 180);
                WarmupLightningEntity entity = new WarmupLightningEntity(EntityRegistry.WARMUP_LIGHTNING, world);
                entity.setPos(x, player.getY(), z);
                entity.setWarmup(2 + i * 8);
                entity.setOwner(player);
                world.spawnEntity(entity);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        this.refreshRaining(world, stack);
    }

    private void refreshRaining(World world, ItemStack stack) {
        if (stack.hasNbt()) {
            stack.getNbt().putBoolean(RAINING, world.isRaining());
        }
    }

    private boolean isRaining(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(RAINING)) {
            return stack.getNbt().getBoolean(RAINING);
        } else {
            return false;
        }
    }
    
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
        if (slot == EquipmentSlot.MAINHAND) {
            Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", this.isRaining(stack) ? ConfigConstructor.mjolnir_rain_bonus_damage - 1 + ConfigConstructor.mjolnir_damage : ConfigConstructor.mjolnir_damage - 1, EntityAttributeModifier.Operation.ADDITION));
            builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -2.8D, EntityAttributeModifier.Operation.ADDITION));
            attributeModifiers = builder.build();
            return attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.MJOLNIR_LIGHTNING, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.THROW_LIGHTNING, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.RETURNING, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.WEATHERBORN, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.OFF_HAND_FLIGHT, stack, tooltip);
        } else {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.shift"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void registerControllers(AnimationData data) {        
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
