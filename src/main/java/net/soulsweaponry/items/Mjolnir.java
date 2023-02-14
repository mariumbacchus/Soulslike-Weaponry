package net.soulsweaponry.items;

import java.util.List;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
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
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.MjolnirProjectile;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Mjolnir extends SwordItem implements IAnimatable {

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final String RAINING = "raining";
    public static final String LIGHTNING_STATUS = "lightning_status";
    public static final String SHOULD_UPDATE_LIGHTNING = "should_update_lightning";

    public Mjolnir(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.mjolnir_damage, attackSpeed, settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack) - remainingUseTicks;
        if (user instanceof PlayerEntity && i >= 10) {
            PlayerEntity player = (PlayerEntity) user;
            int cooldown = 0;
            stack.damage(3, player, p -> p.sendToolBreakStatus(user.getActiveHand()));
            if (player.isSneaking()) {
                this.smashGround(stack, world, player);
                this.startLightningCall(stack);
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
        MjolnirProjectile projectile = new MjolnirProjectile(world, player, stack);
        float speed = WeaponUtil.getEnchantDamageBonus(stack)/5;
        projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, 2.5f + speed, 1.0f);
        //if (player.getAbilities().creativeMode) {
        projectile.pickupType = PickupPermission.CREATIVE_ONLY;
        //}
        world.spawnEntity(projectile);
        world.playSoundFromEntity(null, projectile, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f);
        if (!player.getAbilities().creativeMode) {
            player.getInventory().removeOne(stack);
        }
    }

    private void riptide(PlayerEntity player, World world, ItemStack stack) {
        float sharpness = EnchantmentHelper.getAttackDamage(stack, player.getGroup());
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
        player.addVelocity((double)h, (double)k, (double)l);
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
            double newX = player.getRandom().nextDouble() - 1D * 0.5D + player.getRandom().nextGaussian() * 0.15D + d;
            double newZ = player.getRandom().nextDouble() - 1D * 0.5D + player.getRandom().nextGaussian() * 0.15D + e;
            double newY = player.getRandom().nextDouble() - 1D * 0.5D + player.getRandom().nextDouble() * 0.5D;
            world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, Items.STONE.getDefaultStack()), player.getX(), player.getY(), player.getZ(), newX, newY/2, newZ);
            world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, Items.DIRT.getDefaultStack()), player.getX(), player.getY(), player.getZ(), newX, newY/2, newZ);
            world.addParticle(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY(), player.getZ(), newX, newY/8, newZ);
            world.addParticle(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY(), player.getZ(), newX*10, newY*2, newZ*10);
        }
    }

    private void startLightningCall(ItemStack stack) {
        if (stack.hasNbt()) {
            stack.getNbt().putBoolean(SHOULD_UPDATE_LIGHTNING, true);
        }
    }

    @Override
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

    private void lightningCall(PlayerEntity player, World world, ItemStack stack) {
        if (stack.hasNbt()) {
            int[] triggers = {10, 20, 30};
            for (int i = 1; i < triggers.length + 1; i++) {
                if (this.getLightningStatus(stack) == triggers[i - 1]) {
                    this.spawnLightning(i, player, world);
                }
            }
        }
        
    }

    private void spawnLightning(int multiplier, PlayerEntity player, World world) {
        int r = 5*multiplier;
        for (int theta = 0; theta < 360; theta+=30) {
            double x0 = player.getX();
            double z0 = player.getZ();
            double x = x0 + r * Math.cos(theta * Math.PI / 180);
            double z = z0 + r * Math.sin(theta * Math.PI / 180);
            BlockPos pos = new BlockPos(x, player.getY(), z);
            if (world.isSkyVisible(new BlockPos(x, player.getY(), z))) {
                LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                entity.setPos(pos.getX(), pos.getY(), pos.getZ());
                world.spawnEntity(entity);
            }
        }
    }

    private int getLightningStatus(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains(LIGHTNING_STATUS)) {
            return stack.getNbt().getInt(LIGHTNING_STATUS);
        } else {
            return 0;
        }
    }

    private void updateLightningStatus(ItemStack stack, PlayerEntity player, World world) {
        if (stack.hasNbt()) {
            if (stack.getNbt().contains(LIGHTNING_STATUS) && stack.getNbt().contains(SHOULD_UPDATE_LIGHTNING)
              && stack.getNbt().getBoolean(SHOULD_UPDATE_LIGHTNING)) {
                this.lightningCall(player, world, stack);
                stack.getNbt().putInt(LIGHTNING_STATUS, stack.getNbt().getInt(LIGHTNING_STATUS) + 1);
                if (stack.getNbt().getInt(LIGHTNING_STATUS) >= 40) {
                    stack.getNbt().putInt(LIGHTNING_STATUS, 0);
                    stack.getNbt().putBoolean(SHOULD_UPDATE_LIGHTNING, false);
                }
            } else {
                stack.getNbt().putInt(LIGHTNING_STATUS, 0);
                stack.getNbt().putBoolean(SHOULD_UPDATE_LIGHTNING, false);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        this.refreshRaining(world, stack);
        if (entity instanceof PlayerEntity) this.updateLightningStatus(stack, (PlayerEntity)entity, world);
        
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
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableText("tooltip.soulsweapons.lightning").formatted(Formatting.GOLD));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.mjolnir_lightning_call_description_1").formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.mjolnir_lightning_call_description_2").formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.mjolnir_lightning_call_description_3").formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.mjolnir_throw").formatted(Formatting.LIGHT_PURPLE, Formatting.BOLD));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.mjolnir_throw_description_1").formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.mjolnir_throw_description_2").formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.returning").formatted(Formatting.DARK_PURPLE));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.returning_description").formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.weatherborn").formatted(Formatting.AQUA));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.weatherborn_description").formatted(Formatting.GRAY));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.off_hand_flight").formatted(Formatting.WHITE));
            tooltip.add(new TranslatableText("tooltip.soulsweapons.off_hand_flight_description").formatted(Formatting.GRAY));
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
