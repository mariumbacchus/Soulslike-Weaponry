package net.soulsweaponry.items;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.DraupnirSpearEntity;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.WeaponUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public class DraupnirSpear extends ChargeToUseItem implements IAnimatable, IKeybindAbility {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public static final String SPEARS_ID = "thrown_spears_id";

    public DraupnirSpear(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, ConfigConstructor.draupnir_spear_damage, ConfigConstructor.draupnir_spear_attack_speed, settings);
        this.addTooltipAbility(WeaponUtil.TooltipAbilities.INFINITY, WeaponUtil.TooltipAbilities.DETONATE_SPEARS);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            if (i >= 10) {
                int enchant = WeaponUtil.getEnchantDamageBonus(stack);
                DraupnirSpearEntity entity = new DraupnirSpearEntity(world, playerEntity, stack);
                entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 5.0F, 1.0F);
                entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                world.spawnEntity(entity);
                world.playSoundFromEntity(null, entity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
                this.saveSpearData(stack, entity);
                playerEntity.getItemCooldownManager().set(this, ConfigConstructor.draupnir_spear_throw_cooldown - enchant * 5);
                stack.damage(1, (LivingEntity)playerEntity, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(user.getActiveHand()));
            }
        }
    }

    private <P extends Item & IAnimatable> PlayState predicate(AnimationEvent<P> event){
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
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
    public Text[] getAdditionalTooltips() {
        return new Text[0];
    }

    private void saveSpearData(ItemStack stack, DraupnirSpearEntity entity) {
        if (stack.hasNbt()) {
            List<Integer> ids = new ArrayList<>();
            if (stack.getNbt().contains(SPEARS_ID)) {
                int[] arr = stack.getNbt().getIntArray(SPEARS_ID);
                ids = WeaponUtil.arrayToList(arr);
            }
            ids.add(entity.getId());
            stack.getNbt().putIntArray(SPEARS_ID, ids);
        }
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (!player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
            if (player.isSneaking()) {
                if (!player.hasStatusEffect(EffectRegistry.COOLDOWN)) {
                    int r = 4;
                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.PLAYERS, 1f, 1f);
                    for (int theta = 0; theta < 360; theta += 45) {
                        double x0 = player.getX();
                        double z0 = player.getZ();
                        double x = x0 + r * Math.cos(theta * Math.PI / 180);
                        double z = z0 + r * Math.sin(theta * Math.PI / 180);
                        double x1 = Math.cos(theta * Math.PI / 180);
                        double z1 = Math.sin(theta * Math.PI / 180);
                        DraupnirSpearEntity entity = new DraupnirSpearEntity(world, player, stack);
                        entity.setPos(x, player.getY() + 5, z);
                        entity.setVelocity(x1, -3, z1);
                        entity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED;
                        world.spawnEntity(entity);
                        this.saveSpearData(stack, entity);
                        ParticleHandler.particleOutburst(world, 10, x, player.getY() + 5, z, ParticleTypes.CLOUD, new Vec3d(4, 4, 4), 0.5f);
                    }
                    if (!player.isCreative())
                        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.COOLDOWN, ConfigConstructor.draupnir_spear_summon_spears_cooldown, 0));
                } else {
                    player.sendMessage(Text.translatable("soulsweapons.weapon.on_cooldown"), true);
                }
            } else {
                Box box = player.getBoundingBox().expand(3);
                List<Entity> entities = world.getOtherEntities(player, box);
                float power = ConfigConstructor.draupnir_spear_projectile_damage;
                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity) {
                        entity.damage(DamageSource.mob(player), power + EnchantmentHelper.getAttackDamage(stack, ((LivingEntity) entity).getGroup()));
                        entity.addVelocity(0, .1f, 0);
                    }
                }
                ParticleHandler.particleOutburstMap(world, 250, player.getX(), player.getY(), player.getZ(), ParticleEvents.DEFAULT_GRAND_SKYFALL_MAP, 0.5f);
                world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1f, 1f);
                player.getItemCooldownManager().set(stack.getItem(), ConfigConstructor.draupnir_spear_detonate_cooldown);
                if (stack.hasNbt() && stack.getNbt().contains(DraupnirSpear.SPEARS_ID)) {
                    int[] ids = stack.getNbt().getIntArray(DraupnirSpear.SPEARS_ID);
                    for (int id : ids) {
                        Entity entity = world.getEntityById(id);
                        if (entity instanceof DraupnirSpearEntity spear) {
                            spear.detonate();
                        }
                    }
                    stack.getNbt().putIntArray(DraupnirSpear.SPEARS_ID, new int[0]);
                }
            }
        }
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, ClientPlayerEntity player) {
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_draupnir_spear;
    }
}