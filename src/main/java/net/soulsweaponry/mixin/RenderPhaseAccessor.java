package net.soulsweaponry.mixin;

import net.minecraft.client.render.RenderPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RenderPhase.class)
public interface RenderPhaseAccessor {

    @Accessor("END_PORTAL_PROGRAM")
    static RenderPhase.ShaderProgram getEndPortalProgram() {
        throw new UnsupportedOperationException("Mixin accessor");
    }
}
