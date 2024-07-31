package net.soulsweaponry.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.ConfigConstructor;
import net.soulsweaponry.entitydata.ParryData;
import net.soulsweaponry.entitydata.posture.PostureData;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId)
public class ModEvents {

//    @SubscribeEvent
//    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
//        if (event.getObject() instanceof PlayerEntity player) {
//            if (!player.getCapability(PostureDataProvider.POSTURE_DATA).isPresent()) {
//                event.addCapability(new Identifier(SoulsWeaponry.ModId, "properties"), new PostureDataProvider());
//            }
//        }
//    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            PlayerEntity original = event.getOriginal();
            PlayerEntity player = event.getPlayer();
            //original.getCapability(PostureDataProvider.POSTURE_DATA).ifPresent(old -> player.getCapability(PostureDataProvider.POSTURE_DATA).ifPresent(up -> up.copyFrom(old)));
            PostureData.setPosture(player, PostureData.getPosture(original));
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
        }
    }

    @SubscribeEvent
    public static void onLivingEntityTicks(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        int posture = PostureData.getPosture(entity);
        if (posture >= ConfigConstructor.max_posture_loss) {
            if (!entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK.get())) {
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT.get(), SoundCategory.PLAYERS, .5f, 1f);
            }
            entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK.get(), 60, 1));
            PostureData.setPosture(entity, 0);
        } else if (posture > 0 && entity.age % 4 == 0) {
            PostureData.reducePosture(entity, 1);
        }
    }
}
