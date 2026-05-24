package net.soulsweaponry.items.abilities.targetdeath;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.soulsweaponry.entity.mobs.Remnant;
import net.soulsweaponry.items.abilities.ISummonAlliesAbility;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.List;
import java.util.Random;

public record SummonRemnant(boolean allowNonUndeadChance, double summonChance, int maxSummons, String summonListId) implements ISummonAlliesAbility {

    @Override
    public void onTargetDeath(DamageSource damageSource, ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.isUndead() || this.allowNonUndeadChance) {
            double chance = new Random().nextDouble();
            World world = attacker.getEntityWorld();
            if (!world.isClient && this.canSummonEntity((ServerWorld) world, attacker, this.getSummonsListId()) && chance < this.summonChance) {
                Remnant entity = new Remnant(EntityRegistry.REMNANT, world);
                entity.setPos(target.getX(), target.getY() + .1F, target.getZ());
                entity.setOwner((PlayerEntity) attacker);
                world.spawnEntity(entity);
                this.saveSummonUuid(attacker, entity.getUuid());
                world.playSound(null, target.getBlockPos(), SoundRegistry.NIGHTFALL_SPAWN_EVENT, SoundCategory.PLAYERS, 1f, 1f);
                if (!attacker.getWorld().isClient) {
                    ParticleHandler.particleOutburstMap(attacker.getWorld(), 50, target.getX(), target.getY(), target.getZ(), ParticleEvents.SOUL_RUPTURE_MAP, 1f);
                }
            }
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.ghost_summoner").formatted(Formatting.DARK_AQUA),
                Text.translatable("tooltip.soulsweapons.ghost_summoner.description.1").formatted(Formatting.GRAY)
        );
    }

    @Override
    public int getMaxSummons() {
        return this.maxSummons;
    }

    @Override
    public String getSummonsListId() {
        return this.summonListId;
    }
}
