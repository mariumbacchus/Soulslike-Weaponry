package net.soulsweaponry.events;

import net.minecraft.entity.EntityType;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.resource.PathPackResources;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.*;
import net.soulsweaponry.entity.projectile.NightsEdge;
import net.soulsweaponry.networking.ModMessages;
import net.soulsweaponry.registry.AttributeRegistry;
import net.soulsweaponry.registry.EntityRegistry;

import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvents {

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, AttributeRegistry.POSTURE_BUILDUP_RESISTANCE.get());
        event.add(EntityType.PLAYER, AttributeRegistry.BASE_POSTURE_INCREASE.get());
        event.add(EntityType.PLAYER, AttributeRegistry.BLEED_BUILDUP_RESISTANCE.get());
        event.add(EntityType.PLAYER, AttributeRegistry.BLEED_DAMAGE_RESISTANCE.get());
    }

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.WITHERED_DEMON.get(), WitheredDemon.createDemonAttributes().build());
        event.put(EntityRegistry.ACCURSED_LORD_BOSS.get(), AccursedLordBoss.createDemonAttributes().build());
        event.put(EntityRegistry.DRAUGR_BOSS.get(), DraugrBoss.createBossAttributes().build());
        event.put(EntityRegistry.NIGHT_SHADE.get(), NightShade.createBossAttributes().build());
        event.put(EntityRegistry.RETURNING_KNIGHT.get(), ReturningKnight.createBossAttributes().build());
        event.put(EntityRegistry.BIG_CHUNGUS.get(), BigChungus.createChungusAttributes().build());
        event.put(EntityRegistry.REMNANT.get(), Remnant.createRemnantAttributes().build());
        event.put(EntityRegistry.DARK_SORCERER.get(), DarkSorcerer.createSorcererAttributes().build());
        event.put(EntityRegistry.SOUL_REAPER_GHOST.get(), SoulReaperGhost.createGhostAttributes().build());
        event.put(EntityRegistry.FORLORN.get(), Forlorn.createForlornAttributes().build());
        event.put(EntityRegistry.EVIL_FORLORN.get(), EvilForlorn.createForlornAttributes().build());
        event.put(EntityRegistry.SOULMASS.get(), Soulmass.createSoulmassAttributes().build());
        event.put(EntityRegistry.CHAOS_MONARCH.get(), ChaosMonarch.createBossAttributes().build());
        event.put(EntityRegistry.FREYR_SWORD_ENTITY_TYPE.get(), FreyrSwordEntity.createEntityAttributes().build());
        event.put(EntityRegistry.MOONKNIGHT.get(), Moonknight.createBossAttributes().build());
        event.put(EntityRegistry.FROST_GIANT.get(), FrostGiant.createGiantAttributes().build());
        event.put(EntityRegistry.RIME_SPECTRE.get(), RimeSpectre.createSpectreAttributes().build());
        event.put(EntityRegistry.DAY_STALKER.get(), DayStalker.createBossAttributes().build());
        event.put(EntityRegistry.NIGHT_PROWLER.get(), NightProwler.createBossAttributes().build());
        event.put(EntityRegistry.WARMTH_ENTITY.get(), WarmthEntity.createEntityAttributes().build());
        event.put(EntityRegistry.NIGHTS_EDGE.get(), NightsEdge.createAttributes().build());
    }

    @SubscribeEvent
    public static void addBuiltinPack(AddPackFindersEvent event) {
        // NOTE: Maybe done differently in other versions, if so see https://github.com/MinecraftForge/MinecraftForge/blob/1.18.x/src/test/java/net/minecraftforge/debug/AddPackFinderEventTest.java
        if (event.getPackType() == ResourceType.CLIENT_RESOURCES) {
            var modFileInfo = ModList.get().getModFileById(SoulsWeaponry.ModId);
            if (modFileInfo == null) {
                SoulsWeaponry.LOGGER.error("Could not find mod file for {}", SoulsWeaponry.ModId);
                return;
            }
            var modFile = modFileInfo.getFile();

            addBuiltin(event,
                    "builtin/2d_weapons",
                    Text.literal("2D Weapon Models"),
                    modFile.findResource("resourcepacks/2d_weapons"));

            addBuiltin(event,
                    "builtin/legacy_2d",
                    Text.literal("Legacy 2D Models"),
                    modFile.findResource("resourcepacks/legacy_2d"));

            addBuiltin(event,
                    "builtin/legacy_3d",
                    Text.literal("Legacy 3D Models"),
                    modFile.findResource("resourcepacks/legacy_3d"));

            addBuiltin(event,
                    "builtin/fresh_animations_compat",
                    Text.literal("Fresh Animations Compat."),
                    modFile.findResource("resourcepacks/fresh_animations_compat"));

            addBuiltin(event,
                    "builtin/enhanced_gow",
                    Text.literal("Szombie's 3D GOW Weapons"),
                    modFile.findResource("resourcepacks/enhanced_gow"));
        }
    }

    private static void addBuiltin(AddPackFindersEvent event, String id, Text displayName, Path path) {
        var pack = ResourcePackProfile.create(
                id,
                displayName,
                false,
                name -> new PathPackResources(name, true, path),
                event.getPackType(),
                ResourcePackProfile.InsertionPosition.TOP,
                ResourcePackSource.BUILTIN
        );

        if (pack != null) {
            event.addRepositorySource(consumer -> consumer.accept(pack));
        } else {
            SoulsWeaponry.LOGGER.warn("Failed to create builtin resource pack '{}' from path {}", id, path);
        }
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModMessages.register();
        });
    }
}
