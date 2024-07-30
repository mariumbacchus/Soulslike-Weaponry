package net.soulsweaponry.events;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.soulsweaponry.SoulsWeaponry;

import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = SoulsWeaponry.ModId)
public class PlayerDataHandlerEvents {
    //TODO make all these nbt instead...
    public static final TrackedData<Optional<UUID>> SUMMON_UUID = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    public static final TrackedData<Boolean> SHOULD_DAMAGE_RIDING = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Integer> TICKS_BEFORE_DISMOUNT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    public static final TrackedData<Optional<UUID>> THROWN_WEAPON_OPT = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    public static void registerDataTracker(PlayerEntity player) {
        try {
            player.getDataTracker().startTracking(SUMMON_UUID, Optional.empty());
            player.getDataTracker().startTracking(SHOULD_DAMAGE_RIDING, Boolean.FALSE);
            player.getDataTracker().startTracking(TICKS_BEFORE_DISMOUNT, 40);
            player.getDataTracker().startTracking(THROWN_WEAPON_OPT, Optional.empty());
        } catch (Exception e) {
            SoulsWeaponry.LOGGER.error("Failed to register custom player data tracker values", e);
        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        SoulsWeaponry.LOGGER.info("Attempting to register custom data tracker values for: " + player.getUuid());
        registerDataTracker(player);
    }
}
