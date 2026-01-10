package net.soulsweaponry.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.numeric.NumericProperties;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NumericProperties.class)
public class NumericPropertiesAccessor {

    @Accessor("ID_MAPPER")
    static Codecs.IdMapper<Identifier, MapCodec<? extends NumericProperty>> sw_idMapper() {
        throw new AssertionError();
    }
}
