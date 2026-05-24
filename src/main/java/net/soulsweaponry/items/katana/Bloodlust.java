package net.soulsweaponry.items.katana;

import net.minecraft.block.Blocks;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.soulsweaponry.client.renderer.item.BloodlustRenderer;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.BleedData;
import net.soulsweaponry.items.ModdedSword;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;
import net.soulsweaponry.registry.DamageSourceRegistry;
import net.soulsweaponry.util.IKeybindAbility;
import net.soulsweaponry.util.TooltipAbilities;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Bloodlust extends ModdedSword implements IBleed, GeoItem, IKeybindAbility {

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Bloodlust(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, (int) ConfigConstructor.bloodlust_damage, ConfigConstructor.bloodlust_attack_speed, settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.addTooltipAbility(TooltipAbilities.BLOODLUST, TooltipAbilities.BLEED, TooltipAbilities.SCENT_OF_BLOOD);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!this.isDisabled(stack)) {
            this.applyBleed(attacker, target);
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (entity instanceof PlayerEntity player && world instanceof ServerWorld serverWorld && player.age % 10 == 0) {
            if (player.hasStatusEffect(EffectRegistry.BLOODTHIRSTY.get())) {
                this.triggerAnim(player, GeoItem.getOrAssignId(stack, serverWorld), "empowered", "spin");
            } else {
                this.triggerAnim(player, GeoItem.getOrAssignId(stack, serverWorld), "empowered", "idle");
            }
        }
    }

    @Override
    public boolean isFireproof() {
        return ConfigConstructor.is_fireproof_bloodlust;
    }

    @Override
    public boolean isDisabled(ItemStack stack) {
        return ConfigConstructor.disable_use_bloodlust;
    }

    @Override
    public boolean canEnchantReduceCooldown(ItemStack stack) {
        return false;
    }

    @Override
    public String[] getReduceCooldownEnchantIds(ItemStack stack) {
        return new String[0];
    }

    @Override
    public int getBleedAmount() {
        return (int) ConfigConstructor.bloodlust_bleed_post_hit;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BloodlustRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override public BuiltinModelItemRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new BloodlustRenderer();

                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "empowered", state -> PlayState.STOP)
                .triggerableAnim("spin", RawAnimation.begin().thenPlay("spin"))
                .triggerableAnim("idle", RawAnimation.begin().thenPlay("idle"))
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    @Override
    public void useKeybindAbilityServer(ServerWorld world, ItemStack stack, PlayerEntity player) {
        player.damage(DamageSourceRegistry.create(world, DamageSourceRegistry.BLEED), ConfigConstructor.bloodlust_ability_self_damage);
        BleedData.addBleed(player, (int) ConfigConstructor.bloodlust_ability_self_bleed);
        stack.damage(1, player, (p_220045_0_) -> p_220045_0_.sendToolBreakStatus(player.getActiveHand()));
        player.addStatusEffect(new StatusEffectInstance(EffectRegistry.BLOODTHIRSTY.get(), 300, (int) ConfigConstructor.bloodlust_ability_bloodthirsty_amp));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 400, (int) ConfigConstructor.bloodlust_ability_strength_amp));
        world.playSound(null, player.getBlockPos(), SoundRegistry.BLOOD_LOSS.get(), SoundCategory.PLAYERS, .75f, 1f);
    }

    @Override
    public void useKeybindAbilityClient(ClientWorld world, ItemStack stack, PlayerEntity player) {
        for (int i = 0; i < 30; i++) {
            world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.getDefaultState()),
                    player.getParticleX(1), player.getBodyY(0.5) + player.getRandom().nextDouble() * 2 - 1D, player.getParticleZ(1), 0, 0, 0);
        }
    }
}