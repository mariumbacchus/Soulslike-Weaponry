package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.Moonknight;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MoonknightModel extends DefaultedEntityGeoModel<Moonknight> {

    public MoonknightModel() {
        super(new Identifier(SoulsWeaponry.ModId, "moonknight"), true);
    }
}
