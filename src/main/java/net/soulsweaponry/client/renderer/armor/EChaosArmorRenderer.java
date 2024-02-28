package net.soulsweaponry.client.renderer.armor;

import net.soulsweaponry.client.model.armor.EChaosArmorModel;
import net.soulsweaponry.items.armor.ChaosSet;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class EChaosArmorRenderer extends GeoArmorRenderer<ChaosSet> {

    public EChaosArmorRenderer() {
        super(new EChaosArmorModel());

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
