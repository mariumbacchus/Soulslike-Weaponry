package net.soulsweaponry.api.entitystats;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.soulsweaponry.SoulsWeaponry;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class EntityStatsUtil implements IdentifiableResourceReloadListener {

    private static final String FOLDER = "entitystats";
    private static final Identifier ID = Identifier.of(SoulsWeaponry.ModId, "entity_attributes");
    private static final Gson GSON = new Gson();
    private static final Map<Identifier, EntityStats> statsMap = new HashMap<>();

    public static Optional<EntityStats> getStats(Identifier entityId) {
        return Optional.ofNullable(statsMap.get(entityId));
    }

    public static Identifier getEntityIdentifier(Entity entity) {
        return Registries.ENTITY_TYPE.getId(entity.getType());
    }

    public static Optional<EntityStats> getStats(Entity entity) {
        return getStats(getEntityIdentifier(entity));
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer,
                                          ResourceManager manager,
                                          Profiler prepareProfiler,
                                          Profiler applyProfiler,
                                          Executor prepareExecutor,
                                          Executor applyExecutor) {
        CompletableFuture<Map<Identifier, EntityStats>> loadFuture = CompletableFuture.supplyAsync(() -> {
            Map<Identifier, EntityStats> newStats = new HashMap<>();
            manager.findResources(FOLDER, id -> id.getPath().endsWith(".json")).forEach((resId, resource) -> {
                String path = resId.getPath(); // i.e "entitystats/returning_knight.json"
                String fileName = path.substring(FOLDER.length() + 1);
                String entityName = fileName.substring(0, fileName.length() - 5);
                Identifier entityId = Identifier.of(resId.getNamespace(), entityName);
                try (var stream = manager.getResource(resId).get().getInputStream(); var reader = new InputStreamReader(stream)) {
                    EntityStats stats = GSON.fromJson(JsonParser.parseReader(reader), EntityStats.class);
                    newStats.put(entityId, stats);
                } catch (IOException | JsonSyntaxException e) {
                    System.err.println("Error loading entitystats JSON '" + resId + "': " + e.getMessage());
                }
            });
            return newStats;
        }, prepareExecutor);

        // Wait for synchronizer then apply on the executor
        return loadFuture.thenComposeAsync(loaded ->
                        synchronizer.whenPrepared(getFabricId())
                                .thenRunAsync(() -> {
                                    statsMap.clear();
                                    statsMap.putAll(loaded);
                                }, applyExecutor)
                , applyExecutor);
    }

    public static void register() {
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new EntityStatsUtil());
    }

    /**
     * Calculates a reduced amount based on the resistance provided, likely coming from an entity.
     * <p> {@code result = amount * 2^(-resistance * 0.01)} </p>
     * Some results for clarity:
     * <p> resistance = -100 => 200% of amount </p>
     * <p> resistance = 0 => 100% </p>
     * <p> resistance = 10 => 93% </p>
     * <p> resistance = 25 => 84% </p>
     * <p> resistance = 50 => 71% </p>
     * <p> resistance = 75 => 59% </p>
     * <p> resistance = 100 => 50% </p>
     * <p> resistance = 150 => 35% </p>
     * <p> resistance = 300 => 12.5% </p>
     */
    public static float calculateResistance(float resistance, float amountToReduce) {
        return Math.round(amountToReduce * Math.pow(2, -resistance * 0.01));
    }
}
