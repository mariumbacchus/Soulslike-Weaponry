package net.soulsweaponry.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;

@Mixin(InGameHud.class)
public interface InGameHudAccessor {
    @Invoker("renderOverlay")
    void invokeRenderOverlay(Identifier texture, float opacity);
}
