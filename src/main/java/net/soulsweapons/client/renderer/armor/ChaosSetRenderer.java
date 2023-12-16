package net.soulsweapons.client.renderer.armor;

import net.soulsweapons.client.model.armor.ChaosSetModel;
import net.soulsweapons.items.ChaosSet;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class ChaosSetRenderer extends GeoArmorRenderer<ChaosSet> {

    public ChaosSetRenderer() {
        super(new ChaosSetModel());

        this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorRightLeg";
        this.leftLegBone = "armorLeftLeg";
        this.rightBootBone = "armorRightBoot";
        this.leftBootBone = "armorLeftBoot";
    }
}
