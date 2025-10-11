package net.soulsweaponry.items.abilities.abilitykeybind;

import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.util.WeaponUtil;

import java.util.List;

public record BloodlustAbility(float selfDamage, int selfBleed, int bloodthirstyDuration, int bloodthirstyAmp, int strengthDuration, int strengthAmp) implements IKeybindAbility {

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        player.damage(DamageSourceRegistry.create(world, DamageSourceRegistry.BLEED), this.selfDamage);
        BleedData.addBleed(player, this.selfBleed);
        stack.damage(1, player, WeaponUtil.getActiveHandSlot(player));
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY, this.bloodthirstyDuration, this.bloodthirstyAmp));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, this.strengthDuration, this.strengthAmp));
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, SoundCategory.PLAYERS, .75f, 1f);
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {
        for (int i = 0; i < 30; i++) {
            world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState()),
                    player.getParticleX(1), player.getBodyY(0.5) + player.getRandom().nextDouble() * 2 - 1D, player.getParticleZ(1), 0, 0, 0);
        }
    }

    @Override
    public List<Text> getTooltipAbilities(ItemStack stack) {
        return List.of(
                Text.translatable("tooltip.soulsweapons.bloodlust").formatted(Formatting.DARK_RED),
                Text.translatable("tooltip.soulsweapons.bloodlust.1").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.bloodlust.2").formatted(Formatting.GRAY),
                Text.translatable("tooltip.soulsweapons.bloodlust.3").formatted(Formatting.GRAY)
        );
    }
}
