package net.soulsweaponry.items.spear;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entity.projectile.DragonslayerSwordspearEntity;
import net.soulsweaponry.items.ChargeToUseItem;
import net.soulsweaponry.items.IDragonBonus;
import net.soulsweaponry.items.abilities.inventorytick.RainBoostsStats;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.util.TooltipAbilities;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public class DragonslayerSwordspear extends ChargeToUseItem implements IDragonBonus {

    private static final RainBoostsStats RAIN_BOOSTS_STATS = new RainBoostsStats(ConfigConstructor.dragonslayer_swordspear_rain_bonus_damage, ConfigConstructor.dragonslayer_swordspear_rain_bonus_attack_speed);

    public DragonslayerSwordspear(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.dragonslayer_swordspear_damage, ConfigConstructor.dragonslayer_swordspear_attack_speed, settings);
        this.addTooltipAbility(TooltipAbilities.LIGHTNING_CALL /*TooltipAbilities.THROW_LIGHTNING*/, TooltipAbilities.STORM_STOMP, TooltipAbilities.DRAGONS_SCOURGE);
        this.addAbility(RAIN_BOOSTS_STATS);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity playerEntity) {
            int i = WeaponUtil.getChargeTime(stack, user, remainingUseTicks);
            if (i >= 10) {
                if (stack != user.getOffHandStack()) {
                    stack.damage(1, playerEntity, WeaponUtil.getActiveHandSlot(playerEntity));
                    DragonslayerSwordspearEntity entity = new DragonslayerSwordspearEntity(world, playerEntity, stack);
                    entity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 5.0F, 1.0F);
                    entity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                    world.spawnEntity(entity);
                    world.playSoundFromEntity(null, entity, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                    this.applyItemCooldown(playerEntity, this.getScaledCooldownThrow(world, stack));
                } else {
                    stack.damage(3, playerEntity, WeaponUtil.getActiveHandSlot(playerEntity));
                    user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 5));
                    user.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 100, 0));
                    Box chunkBox = new Box(user.getX() - 10, user.getY() - 5, user.getZ() - 10, user.getX() + 10, user.getY() + 5, user.getZ() + 10);
                    List<Entity> nearbyEntities = world.getOtherEntities(user, chunkBox);
                    //Entity["EntityKey"/number?, l = "ClientLevel", x, y, z] and so on... Includes items too!
                    for (Entity nearbyEntity : nearbyEntities) {
                        if (nearbyEntity instanceof LivingEntity target) {
                            if (nearbyEntity instanceof TameableEntity tamed && tamed.isTamed()) {
                                continue;
                            }
                            if (world.isSkyVisible(target.getBlockPos())) {
                                for (i = 0; i < ConfigConstructor.dragonslayer_swordspear_lightning_amount; i++) {
                                    LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                                    entity.setPos(target.getX(), target.getY(), target.getZ());
                                    world.spawnEntity(entity);
                                }
                            } else {
                                double x = target.getX() - user.getX();
                                double z = target.getX() - user.getX();
                                target.takeKnockback(5F, -x, -z);
                                target.damage(world.getDamageSources().mobAttack(user), ConfigConstructor.dragonslayer_swordspear_ability_damage);
                                if (!world.isClient) {
                                    ParticleHandler.particleSphereList(world, 20, target.getX(), target.getY(), target.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
                                }
                            }
                            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1f, 1f);
                        }
                    }
                    this.applyItemCooldown(playerEntity, this.getScaledCooldownAbility(world, stack));
                }
            }
        }
    }

    protected int getScaledCooldownAbility(World world, ItemStack stack) {
        float base = ConfigConstructor.dragonslayer_swordspear_ability_cooldown;
        return (int) Math.max(ConfigConstructor.dragonslayer_swordspear_ability_min_cooldown, base - this.getReduceCooldownEnchantLevel(stack) * 20 / (world.isRaining() ? 2f : 1f));
    }

    protected int getScaledCooldownThrow(World world, ItemStack stack) {
        float base = ConfigConstructor.dragonslayer_swordspear_throw_cooldown;
        return (int) Math.max(ConfigConstructor.dragonslayer_swordspear_throw_min_cooldown, base - this.getReduceCooldownEnchantLevel(stack) * 10 / (world.isRaining() ? 2f : 1f));
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_dragonslayer_swordspear;
    }

    @Override
    public float getBaseDragonBonus(ItemStack stack) {
        return ConfigConstructor.dragonslayer_swordspear_dragons_scourge_bonus;
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return this.getDragonBonus(target, baseAttackDamage, damageSource);
    }
}