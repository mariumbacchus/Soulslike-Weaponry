package net.soulsweaponry.mixin;

import net.minecraft.data.client.Model;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Models.class)
public interface ModelInvoker {

    @Invoker("item")
    static Model invokeItemModelBuilder(String parent, TextureKey... requiredTextureKeys) {
        throw new AssertionError();
    }
}