package net.soulsweaponry.items.abilities.bonusdamage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.soulsweaponry.items.abilities.IAbility;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.ParticleRegistry;

import java.util.List;

/**
 * Overrides the {@link PlayerEntity#attack(Entity)} damage source to be magic damage, bypassing regular armor and shields.
 */
public class OnlyMagicDamage implements IAbility {

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.getWorld().isClient) {
            ParticleHandler.particleOutburst(attacker.getWorld(), 5, target.getX(), target.getBodyY(attacker.getRandom().nextBetween(6, 10) * 0.1f), target.getZ(),
                    ParticleRegistry.NIGHTFALL_PARTICLE, new Vec3d(0.5f, 2, 0.5f), 0.1f);
        }
    }

    @Override
    public DamageSource getMeleeDamageSource(ItemStack stack, DamageSource original, Entity target, PlayerEntity player) {
        return player.getDamageSources().indirectMagic(player, player);
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.moonlit").formatted(Formatting.AQUA),
                Text.translatable("tooltip.soulsweapons.moonlit.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.moonlit.2").formatted(Formatting.GRAY)
        );
    }
}
