package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.soulsweaponry.entity.projectile.noclip.WarmupLightningEntity;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record CircleLightningCall(
        float baseSmashDamage, float bonusSmashDamagePerLvl, float enchantBonusSmashDamageMultiplier,
        int lightningCircleAmount, float lightningPerCircle, float rangeBetweenCircles,
        int minCooldown, int cooldown, int reducedCooldownPerLvl
) implements IKeybindAbility {

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (this.isCoolingDown(player, stack)) {
            return;
        }
        int lvl = WeaponUtil.getUpgradeLevel(stack);
        Box box = player.getBoundingBox().expand(3);
        List<Entity> entities = world.getOtherEntities(player, box);
        for (Entity entity : entities) {
            float damage = this.baseSmashDamage + this.bonusSmashDamagePerLvl * lvl;
            if (entity instanceof LivingEntity living) {
                entity.damage(world, world.getDamageSources().mobAttack(player),
                         damage + this.enchantBonusSmashDamageMultiplier * EnchantmentHelper.getDamage(world, stack, living, world.getDamageSources().playerAttack(player), 0));
                entity.addVelocity(0, .25f, 0);
            }
        }
        world.playSoundFromEntity(null, player, SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.PLAYERS, .75f, 1f);
        float thetaStep = 360f / this.lightningPerCircle;
        for (int i = 1; i < this.lightningCircleAmount + 1; i++) {
            float r = this.rangeBetweenCircles * i;
            for (float theta = 0; theta < 360; theta += thetaStep) {
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
        this.applyItemCooldown(stack, player, Math.max(this.minCooldown, this.cooldown - lvl * this.reducedCooldownPerLvl));
        stack.damage(3, player, WeaponUtil.getActiveHandSlot(player));
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player, @Nullable Hand hand) {
        if (this.isCoolingDown(player, stack)) {
            return;
        }
        double d = player.getRandom().nextGaussian() * 0.05D;
        double e = player.getRandom().nextGaussian() * 0.05D;
        for (int j = 0; j < 200; ++j) {
            double newX = player.getRandom().nextDouble() - 0.5D + player.getRandom().nextGaussian() * 0.15D + d;
            double newZ = player.getRandom().nextDouble() - 0.5D + player.getRandom().nextGaussian() * 0.15D + e;
            double newY = player.getRandom().nextDouble() - 0.5D + player.getRandom().nextDouble() * 0.5D;
            world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, Items.STONE.getDefaultStack()), player.getX(), player.getY(), player.getZ(), newX, newY/2, newZ);
            world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, Items.DIRT.getDefaultStack()), player.getX(), player.getY(), player.getZ(), newX, newY/2, newZ);
            world.addParticle(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY(), player.getZ(), newX, newY/8, newZ);
            world.addParticle(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY(), player.getZ(), newX*10, newY*2, newZ*10);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.lightning_call").formatted(Formatting.GOLD),
                Text.translatable("tooltip.soulsweapons.mjolnir_lightning_call.description.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.mjolnir_lightning_call.description.2").formatted(Formatting.GRAY)
        );
    }
}
