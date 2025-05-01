package net.soulsweaponry.client.hud;

import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.List;

public record CustomBossBar(EntityType<?> entityType, Identifier emptyBar, Identifier filledBar) {

    public static final List<CustomBossBar> CUSTOM_BOSS_BARS = new ArrayList<>();

    public static void init() {
        CUSTOM_BOSS_BARS.add(new CustomBossBar(EntityRegistry.RETURNING_KNIGHT, new Identifier(SoulsWeaponry.ModId, "textures/gui/posture/empty.png"), new Identifier(SoulsWeaponry.ModId, "textures/gui/posture/full.png")));
    }
}