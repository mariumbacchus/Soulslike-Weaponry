package net.soulsweaponry.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.api.entitystats.EntityBleed;
import net.soulsweaponry.api.entitystats.EntityPosture;
import net.soulsweaponry.api.entitystats.EntityStatsUtil;
import net.soulsweaponry.api.trickweapon.TrickWeaponUtil;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.*;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.util.UUID;

import static net.soulsweaponry.registry.EffectRegistry.randomVibrantRGBA;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new EntityStatsUtil());
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        TrickWeaponUtil.loadMappings(event.getServer());
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            PlayerEntity oldPlayer = event.getOriginal();
            PlayerEntity newPlayer = event.getEntity();
            //oldPlayer.getCapability(PostureDataProvider.POSTURE_DATA).ifPresent(old -> newPlayer.getCapability(PostureDataProvider.POSTURE_DATA).ifPresent(up -> up.copyFrom(old)));
            PostureData.setPosture(newPlayer, PostureData.getPosture(oldPlayer));

            NbtCompound oldData = oldPlayer.getPersistentData();
            NbtCompound newData = newPlayer.getPersistentData();
            if (oldData.contains(ReturningProjectileData.PROJECTILE_ID) && newPlayer instanceof ServerPlayerEntity serverPlayerEntity) {
                UUID uuid = oldData.getUuid(ReturningProjectileData.PROJECTILE_ID);
                newData.putUuid(ReturningProjectileData.PROJECTILE_ID, uuid);
                ReturningProjectileData.syncData(uuid, serverPlayerEntity);
            }

            event.getOriginal().invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        // NOTE: In other versions, either annotate capability class with @AutoRegisterCapability (or @CapabilityInject)
        // instead of having this method.
        //event.register(PostureData.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.SERVER) {
            PlayerEntity player = event.player;
            int parryFrames = ParryData.getParryFrames(player);
            if (parryFrames >= 1) {
                ParryData.addParryFrames(player, 1);
                player.stopUsingItem();
            }
            if (player.getAttacking() != null) {
                TargetPostureData.updateTargetPosture(player, player.getAttacking());
            } else {
                TargetPostureData.resetValues(player);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingEntityTicks(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        int posture = PostureData.getPosture(entity);
        int bleed = BleedData.getBleed(entity);
        if (!EntityPosture.isPostureDisabled(entity) && posture >= EntityPosture.getMaxPostureLoss(entity) && EntityPosture.getMaxPostureLoss(entity) != 0) {
            if (!entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK.get())) {
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT.get(), SoundCategory.PLAYERS, .5f, 1f);
            }
            entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK.get(), 60, 1));
            PostureData.setPosture(entity, 0);
        }
        if (!EntityBleed.isBleedDisabled(entity) && bleed >= EntityBleed.getMaxBleed(entity)) {
            EntityBleed.triggerBloodLoss(entity);
        }
        if (!entity.getWorld().isClient) {
            if (entity.age % ((int) ConfigConstructor.posture_loss_reduction_interval) == 0 && posture > 0) {
                PostureData.reducePosture(entity, (int) ConfigConstructor.posture_loss_reduction_amount);
            }
            if (entity.age % ((int) ConfigConstructor.bleed_reduction_interval) == 0 && bleed > 0) {
                BleedData.reduceBleed(entity, (int) ConfigConstructor.bleed_reduction_amount);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem event) {
        PlayerEntity player = event.getEntity();
        World level = event.getLevel();
        Hand hand = event.getHand();
        ItemStack stack = event.getItemStack();

        if (!stack.isOf(Items.POTION)
                && !stack.isOf(Items.SPLASH_POTION)
                && !stack.isOf(Items.LINGERING_POTION)
                && !stack.isOf(Items.TIPPED_ARROW)) {
            return;
        }

        Potion potion = PotionUtil.getPotion(stack);
        if (potion == null || !potion.equals(EffectRegistry.CHUNGUS_TONIC_POTION.get())) {
            return;
        }

        if (!level.isClient) {
            ItemStack updated = stack.copy();
            updated.getOrCreateNbt().putInt("CustomPotionColor", randomVibrantRGBA());
            player.setStackInHand(hand, updated);
        }
    }
}
