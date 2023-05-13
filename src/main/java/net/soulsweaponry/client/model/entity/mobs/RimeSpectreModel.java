package net.soulsweaponry.client.model.entity.mobs;

import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.entity.mobs.RimeSpectre;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class RimeSpectreModel extends DefaultedEntityGeoModel<RimeSpectre> {

    public RimeSpectreModel() {
        super(new Identifier(SoulsWeaponry.ModId, "rime_spectre"), true);
    }
}
