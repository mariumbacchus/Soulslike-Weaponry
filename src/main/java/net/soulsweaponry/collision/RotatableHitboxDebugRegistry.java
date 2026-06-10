package net.soulsweaponry.collision;

import net.minecraft.world.World;

import java.util.*;

public class RotatableHitboxDebugRegistry {

    private static final Map<String, Entry> HITBOXES = new LinkedHashMap<>();

    public static void put(World world, String id, RotatableHitbox hitbox) {
        put(world, id, hitbox, 2);
    }

    public static void put(World world, String id, RotatableHitbox hitbox, int lifetimeTicks) {
        if (world == null || !world.isClient()) {
            return;
        }
        HITBOXES.put(id, new Entry(hitbox.copy(), world.getTime() + lifetimeTicks));
    }

    public static List<RotatableHitbox> getActive(World world) {
        List<RotatableHitbox> result = new ArrayList<>();
        if (world == null) {
            return result;
        }
        long time = world.getTime();
        Iterator<Map.Entry<String, Entry>> iterator = HITBOXES.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry entry = iterator.next().getValue();
            if (entry.expireTime < time) {
                iterator.remove();
                continue;
            }
            result.add(entry.hitbox.copy());
        }
        return result;
    }

    public static void clear() {
        HITBOXES.clear();
    }

    private record Entry(RotatableHitbox hitbox, long expireTime) {}
}