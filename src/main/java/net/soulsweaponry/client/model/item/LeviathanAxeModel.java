package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.axe.LeviathanAxe;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class LeviathanAxeModel extends GeoModel<LeviathanAxe> {

    @Override
    public Identifier getModelResource(LeviathanAxe leviathanAxe, @Nullable GeoRenderer<LeviathanAxe> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/leviathan_axe.geo.json");
    }

    @Override
    public Identifier getTextureResource(LeviathanAxe leviathanAxe, @Nullable GeoRenderer<LeviathanAxe> geoRenderer) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/leviathan_axe.png");
    }

    @Override
    public Identifier getAnimationResource(LeviathanAxe animatable) {
        return null;
    }
}
