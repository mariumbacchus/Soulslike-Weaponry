package net.soulsweaponry.client.registry;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.soulsweaponry.client.hud.*;

public class HudRegistry {

    public static int OVERLAY_Y_OFFSET = 0;

    public static void init() {
        registerEffectOverlay(new PostureHudOverlay());
        registerEffectOverlay(new BleedHudOverlay());
        CustomBossBar.init();
        HudRenderCallback.EVENT.register(new TargetPostureHudOverlay());
    }

    public static void registerEffectOverlay(EffectHudOverlay overlay) {
        overlay.setYOffset(OVERLAY_Y_OFFSET);
        HudRenderCallback.EVENT.register(overlay);
        OVERLAY_Y_OFFSET += 30;
    }
}
