package net.soulsweaponry.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.PackResourceMetadata;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.resource.PathResourcePack;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.config.CommonConfig;
import net.soulsweaponry.entitydata.parry.ParryData;
import net.soulsweaponry.entitydata.parry.ParryDataProvider;
import net.soulsweaponry.entitydata.posture.PostureData;
import net.soulsweaponry.entitydata.posture.PostureDataProvider;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.networking.packets.S2C.ParrySyncS2C;
import net.soulsweaponry.networking.packets.S2C.PostureSyncS2C;
import net.soulsweaponry.registry.EffectRegistry;
import net.soulsweaponry.registry.SoundRegistry;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity player) {
            if (!player.getCapability(ParryDataProvider.PARRY_DATA).isPresent()) {
                event.addCapability(new Identifier(SoulsWeaponry.ModId, "properties"), new ParryDataProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            PlayerEntity original = event.getOriginal();
            PlayerEntity player = event.getPlayer();

            original.getCapability(ParryDataProvider.PARRY_DATA).ifPresent(old -> player.getCapability(ParryDataProvider.PARRY_DATA).ifPresent(up -> up.copyFrom(old)));
            original.getCapability(PostureDataProvider.POSTURE_DATA).ifPresent(old -> player.getCapability(PostureDataProvider.POSTURE_DATA).ifPresent(up -> up.copyFrom(old)));
            //original.getCapability(SummonsDataProvider.SUMMONS_DATA).ifPresent(old -> player.getCapability(SummonsDataProvider.SUMMONS_DATA).ifPresent(up -> up.copyFrom(old)));

            event.getOriginal().invalidateCaps();
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        // NOTE: In other versions, either annotate capability class with @AutoRegisterCapability (or @CapabilityInject)
        // instead of having this method.
        event.register(ParryData.class);
        event.register(PostureData.class);
        //event.register(SummonsData.class);TODO gotta test if fabric implementation works or not, if it doesnt, fix up provider and uncomment registration etc.
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
        if (posture >= CommonConfig.MAX_POSTURE_LOSS.get()) {
            if (!entity.hasStatusEffect(EffectRegistry.POSTURE_BREAK.get())) {
                entity.getWorld().playSound(null, entity.getBlockPos(), SoundRegistry.POSTURE_BREAK_EVENT.get(), SoundCategory.PLAYERS, .5f, 1f);
            }
            entity.addStatusEffect(new StatusEffectInstance(EffectRegistry.POSTURE_BREAK.get(), 60, 1));
            PostureData.setPosture(entity, 0);
        } else if (posture > 0 && entity.age % 4 == 0) {
            PostureData.reducePosture(entity, 1);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isClient) {
            if (event.getEntity() instanceof ServerPlayerEntity player) {
                ModMessages.sendToPlayer(new ParrySyncS2C(ParryData.getParryFrames(player)), player);
                ModMessages.sendToPlayer(new PostureSyncS2C(PostureData.getPosture(player)), player);
            }
        }
    }

    @SubscribeEvent
    public static void addBuiltinPack(AddPackFindersEvent event) {
        // NOTE: Maybe done differently in other versions, if so see https://github.com/MinecraftForge/MinecraftForge/blob/1.18.x/src/test/java/net/minecraftforge/debug/AddPackFinderEventTest.java
        try {
            if (event.getPackType() == ResourceType.CLIENT_RESOURCES) {
                var resourcePath = ModList.get().getModFileById(SoulsWeaponry.ModId).getFile().findResource("2d_weapons");
                PathResourcePack pack = new PathResourcePack(ModList.get().getModFileById(SoulsWeaponry.ModId).getFile().getFileName() + ":" + resourcePath, resourcePath);
                var metadataSection = pack.parseMetadata(PackResourceMetadata.READER);
                if (metadataSection != null) {
                    event.addRepositorySource((packConsumer, packConstructor) ->
                            packConsumer.accept(packConstructor.create(
                                    "builtin/2d_weapons", new LiteralText("2D Weapon Models"), false,
                                    () -> pack, metadataSection, ResourcePackProfile.InsertionPosition.BOTTOM, ResourcePackSource.PACK_SOURCE_BUILTIN, false)));
                }
            }
        }
        catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
