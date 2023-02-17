package net.soulsweaponry.client.renderer.armor;

import net.soulsweaponry.client.model.armor.ChaosArmorModel;
import net.soulsweaponry.items.ChaosSet;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ChaosArmorRenderer extends GeoArmorRenderer<ChaosSet> {

    public ChaosArmorRenderer() {
        super(new ChaosArmorModel());

        /*this.headBone = "armorHead";
        this.bodyBone = "armorBody";
        this.rightArmBone = "armorRightArm";
        this.leftArmBone = "armorLeftArm";
        this.rightLegBone = "armorRightLeg";
        this.leftLegBone = "armorLeftLeg";
        this.rightBootBone = "armorRightBoot";
        this.leftBootBone = "armorLeftBoot";*/
    }
    
}
