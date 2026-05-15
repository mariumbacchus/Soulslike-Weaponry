package net.soulsweaponry.client.registry;

import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;

public class ShaderRegistry {

    public static ShaderProgram MOONLIGHT_BLADE_SHADER;

    public static void init() {
        CoreShaderRegistrationCallback.EVENT.register(context -> {
            context.register(
                    Identifier.of(SoulsWeaponry.ModId, "rendertype_moonlight_blade_shader"),
                    VertexFormats.POSITION,
                    program -> MOONLIGHT_BLADE_SHADER = program
            );
        });
    }
}