package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.soulsweaponry.particles.ParticleEvents;
import net.soulsweaponry.particles.ParticleHandler;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Press ability keybind to summon lightning on mobs around you if the skies are clear, or
 * trigger "Storm Stomp" which damages and knocks targets away from you instead if they
 * had blocks above them.
 */
public record LightningCall(
        float blockRadius, int lightningAmount, float bonusLightningPerLvl,
        float stormStompDamage, float bonusStormStompDamagePerLvl, float stormStompKnockback,
        int minCooldown, int cooldown, int reducedCooldownPerLvl, float rainingCooldownMod
) implements IKeybindAbility {

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (this.isCoolingDown(player, stack)) {
            return;
        }
        Box chunkBox = new Box(player.getX() - this.blockRadius, player.getY() - this.blockRadius * 0.5f, player.getZ() - this.blockRadius,
                player.getX() + this.blockRadius, player.getY() + this.blockRadius * 0.5f, player.getZ() + this.blockRadius);
        List<Entity> nearbyEntities = world.getOtherEntities(player, chunkBox);
        if (nearbyEntities.isEmpty()) {
            return;
        }
        stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.STORMVEIL, 20, 0));
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        int lightning = (int) (this.lightningAmount + this.bonusLightningPerLvl * lvl);
        for (Entity nearbyEntity : nearbyEntities) {
            if (nearbyEntity instanceof LivingEntity target) {
                if (nearbyEntity instanceof TameableEntity tamed && tamed.isTamed()) {
                    continue;
                }
                if (world.isSkyVisible(target.getBlockPos())) {
                    for (int i = 0; i < lightning; i++) {
                        LightningEntity entity = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                        entity.setPos(target.getX(), target.getY(), target.getZ());
                        world.spawnEntity(entity);
                    }
                } else {
                    double x = target.getX() - player.getX();
                    double z = target.getX() - player.getX();
                    target.takeKnockback(this.stormStompKnockback, -x, -z);
                    target.damage(world.getDamageSources().mobAttack(player), this.stormStompDamage + this.bonusStormStompDamagePerLvl * lvl);
                    ParticleHandler.particleSphereList(world, 20, target.getX(), target.getY(), target.getZ(), ParticleEvents.DARK_EXPLOSION_LIST, 0.3f);
                }
                world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_GENERIC_EXPLODE.value(), SoundCategory.PLAYERS, 1f, 1f);
            }
        }
        this.applyItemCooldown(stack.getItem(), player, this.getScaledCooldownAbility(world, stack));
    }

    private int getScaledCooldownAbility(World world, ItemStack stack) {
        return (int) Math.max(this.minCooldown, (this.cooldown - WeaponUtil.getUpgradeLevel(stack) * this.reducedCooldownPerLvl) * (world.isRaining() ? this.rainingCooldownMod : 1f));
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.lightning_call").formatted(Formatting.YELLOW),
                Text.translatable("tooltip.soulsweapons.lightning_call.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.storm_stomp").formatted(Formatting.WHITE),
                Text.translatable("tooltip.soulsweapons.storm_stomp.1").formatted(Formatting.GRAY)
        );
    }
}
