package net.soulsweaponry.items;

import java.util.List;
import java.util.Objects;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.util.ParticleNetworking;
import net.soulsweaponry.util.WeaponUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class Dawnbreaker extends SwordItem implements IAnimatable {

    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    
    public Dawnbreaker(ToolMaterial toolMaterial, float attackSpeed, Settings settings) {
        super(toolMaterial, ConfigConstructor.dawnbreaker_damage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        super.postHit(stack, target, attacker);
        target.setOnFireFor(4 + 3 * EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack));
        if (target.isUndead() || ConfigConstructor.dawnbreaker_affect_all_entities) {
            if (target.isDead()) {
                if (target.hasStatusEffect(EffectRegistry.RETRIBUTION)) {
                    double chance = ConfigConstructor.dawnbreaker_ability_chance_modifier + 1 - (Math.pow(.75, (double)target.getStatusEffect(EffectRegistry.RETRIBUTION).getAmplifier()));
                    double random = target.getRandom().nextDouble();
                    if (random < chance) {
                        if (!attacker.world.isClient) {
                            if (attacker instanceof ServerPlayerEntity) {
                                BlockPos pos = target.getBlockPos();
                                ParticleNetworking.sendServerParticlePacket((ServerWorld) attacker.world, PacketRegistry.DAWNBREAKER_PACKET_ID, pos);
                            }
                        }
                        target.world.playSound(
                            null,
                            target.getBlockPos(),
                            SoundRegistry.DAWNBREAKER_EVENT,
                            SoundCategory.HOSTILE,
                            2f,
                            1f
                        );
                        Box aoe = target.getBoundingBox().expand(10);
                        List<Entity> entities = attacker.getWorld().getOtherEntities(target, aoe);
                        boolean bl = ConfigConstructor.dawnbreaker_affect_all_entities;
                        for (Entity entity : entities) {
                            if (entity instanceof LivingEntity targetHit) {
                                if (targetHit.isUndead() || bl) {
                                    if (!targetHit.equals(attacker)) {
                                        targetHit.setOnFireFor(4 + EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack));
                                        targetHit.damage(DamageSource.explosion(attacker), ConfigConstructor.dawnbreaker_ability_damage + 5 * EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack));
                                        targetHit.addStatusEffect(new StatusEffectInstance(EffectRegistry.FEAR, 80, 0));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (target.hasStatusEffect(EffectRegistry.RETRIBUTION)) {
                int amplifier = Objects.requireNonNull(target.getStatusEffect(EffectRegistry.RETRIBUTION)).getAmplifier();
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.RETRIBUTION, 80, amplifier + 1));
            } else {
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.RETRIBUTION, 80, EnchantmentHelper.getLevel(Enchantments.FIRE_ASPECT, stack)));
            }
        }
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.DAWNBREAKER, stack, tooltip);
            WeaponUtil.addAbilityTooltip(WeaponUtil.TooltipAbilities.BLAZING_BLADE, stack, tooltip);
        } else {
            tooltip.add(Text.translatable("tooltip.soulsweapons.shift"));
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
