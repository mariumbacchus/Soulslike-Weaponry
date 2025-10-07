package net.soulsweaponry.items.abilities.posthit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.soulsweaponry.api.entitystats.EntityFrost;
import net.soulsweaponry.entitydata.FrostData;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;
import java.util.UUID;

public record Permafrost(int frostApplied, int permafrostDuration, int permafrostBaseAmp, float permafrostAmpPerLvl) implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        int level = WeaponUtil.getUpgradeLevel(stack);
        FrostData.setFrostSource(target, attacker);
        FrostData.addFrost(target, this.frostApplied);
        target.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING, this.permafrostDuration, (int) (this.permafrostBaseAmp + this.permafrostAmpPerLvl * level)));
    }

    public static void iceExplosion(World world, BlockPos pos, Entity affectedEntity, float baseAoeDamage, int amplifier) {
        iceExplosion(world, pos, affectedEntity, baseAoeDamage, amplifier, 0f);
    }

    public static void iceExplosion(World world, BlockPos pos, Entity affectedEntity, float baseAoeDamage, int amplifier, float percentHealthDamage) {
        if (world instanceof ServerWorld serverWorld) {
            UUID uuid = FrostData.getFrostSource(affectedEntity);
            Entity attacker = serverWorld.getEntity(uuid);
            Box box = new Box(pos).expand(1.25D);
            List<Entity> entities = world.getOtherEntities(attacker, box);
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity) {
                    if (attacker != null) {
                        FrostData.setFrostSource(livingEntity, attacker);
                        if (livingEntity.isTeammate(attacker)) {
                            continue;
                        }
                    }
                    float damage = EntityFrost.getFrostTriggerDamage(livingEntity, baseAoeDamage + livingEntity.getMaxHealth() * percentHealthDamage);
                    livingEntity.damage(world.getDamageSources().freeze(), damage);
                    livingEntity.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZING, 200, amplifier));
                }
            }
            ParticleHandler.particleSphere(world, 300, pos.getX(), pos.getY() + .5f, pos.getZ(), ParticleEvents.ICE_PARTICLE, 1f);
        }
        world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 1f, 1f);
        world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 1f, .5f);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.freeze").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.freeze_description", this.frostApplied).formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.permafrost").formatted(Formatting.DARK_AQUA),
                Text.translatable("tooltip.soulsweapons.permafrost_description_1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.permafrost_description_2").formatted(Formatting.GRAY)
        );
    }
}
