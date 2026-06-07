package net.soulsweaponry.client.model.item;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.items.katana.Bloodlust;
import net.soulsweaponry.items.katana.DragonHuntersGreatKatana;
import software.bernie.geckolib.model.GeoModel;

public class DragonHuntersGreatKatanaModel extends GeoModel<DragonHuntersGreatKatana> {

    @Override
    public Identifier getAnimationResource(DragonHuntersGreatKatana animatable) {
        return null;
    }

    @Override
    public Identifier getModelResource(DragonHuntersGreatKatana object) {
        return Identifier.of(SoulsWeaponry.ModId, "geo/dragon_hunters_great_katana.geo.json");
    }

    @Override
    public Identifier getTextureResource(DragonHuntersGreatKatana object) {
        return Identifier.of(SoulsWeaponry.ModId, "textures/item/dragon_hunters_great_katana.png");
    }
    
}
