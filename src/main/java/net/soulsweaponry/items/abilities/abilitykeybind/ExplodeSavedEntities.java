package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.ComponentRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spawns an explosion around the player and detonates all the entities saved to the item, spawning
 * a regular TNT explosion with power based on the item's level.
 * Entities to explode and be removed come from the {@link ComponentRegistry#SAVED_ENTITY_UUID_LIST} component.
 */
public record ExplodeSavedEntities(double userExplosionRadius, float baseDamage, float bonusDamagePerLvl,
                                   float bonusEnchantDmgMod, float knockup,
                                   float spearBaseExplosionPower, float spearBonusExplosionPowerPerLvl,
                                   float powerNeededToApplyWeakness, int weaknessDuration,
                                   int weaknessBaseAmp, float bonusWeaknessAmpPerPower,
                                   int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IKeybindAbility {

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        if (this.isCoolingDown(player, stack)) {
            return;
        }
        Box box = player.getBoundingBox().expand(this.userExplosionRadius);
        List<Entity> entities = world.getOtherEntities(player, box);
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        float damage = this.baseDamage + this.bonusDamagePerLvl * lvl;
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity living) {
                entity.damage(world.getDamageSources().mobAttack(player),
                        damage + this.bonusEnchantDmgMod * EnchantmentHelper.getDamage(world, stack, living, world.getDamageSources().playerAttack(player), 0));
                entity.addVelocity(0, this.knockup, 0);
            }
        }
        ParticleHandler.particleOutburstMap(world, 250, player.getX(), player.getY(), player.getZ(), ParticleEvents.DEFAULT_GRAND_SKYFALL_MAP, 0.5f);
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1f, 1f);
        this.applyItemCooldown(stack.getItem(), player, this.getScaledCooldownExplode(stack));
        this.explodeSavedEntities(world, stack, player);
    }

    private void explodeSavedEntities(ServerWorld world, ItemStack stack, PlayerEntity player) {
        List<UUID> uuids = stack.get(ComponentRegistry.SAVED_ENTITY_UUID_LIST);
        if (uuids != null) {
            for (UUID uuid : uuids) {
                Entity entity = world.getEntity(uuid);
                if (entity == null) {
                    continue;
                }
                float power = this.spearBaseExplosionPower + this.spearBonusExplosionPowerPerLvl * WeaponUtil.getUpgradeLevel(stack);
                world.createExplosion(player, entity.getX(), entity.getY(), entity.getZ(), power, false, World.ExplosionSourceType.TRIGGER);
                if (power >= this.powerNeededToApplyWeakness) {
                    for (Entity entity2 : world.getOtherEntities(player, entity.getBoundingBox().expand(power))) {
                        if (entity2 instanceof LivingEntity living) {
                            living.addStatusEffect(new StatusEffectInstance(
                                    StatusEffects.WEAKNESS, this.weaknessDuration,
                                    (int) (this.weaknessBaseAmp + this.bonusWeaknessAmpPerPower * power)));
                        }
                    }
                }
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
            stack.set(ComponentRegistry.SAVED_ENTITY_UUID_LIST, List.of());
        }
    }

    private int getScaledCooldownExplode(ItemStack stack) {
        return Math.max(this.minCooldown, this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl);
    }

    public static void saveEntityOnItem(ItemStack stack, Entity entity) {
        List<UUID> ids = Optional.ofNullable(stack.get(ComponentRegistry.SAVED_ENTITY_UUID_LIST)).orElse(new ArrayList<>());
        List<UUID> newList = new ArrayList<>(ids);
        newList.add(entity.getUuid());
        stack.set(ComponentRegistry.SAVED_ENTITY_UUID_LIST, newList);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        float power = this.spearBaseExplosionPower + this.spearBonusExplosionPowerPerLvl * WeaponUtil.getUpgradeLevel(stack);
        return List.of(
                Text.translatable("tooltip.soulsweapons.draupnirs_wail").formatted(Formatting.RED),
                Text.translatable("tooltip.soulsweapons.draupnirs_wail.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.draupnirs_wail.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.draupnirs_wail.3", String.format("%.1f", power)).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.draupnirs_wail.4").formatted(Formatting.GRAY)
        );
    }
}
