package net.soulsweaponry.client.renderer.armor;

import net.soulsweaponry.client.model.armor.EnhancedChaosArmorModel;
import net.soulsweaponry.items.ChaosSet;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class EnhancedChaosArmorRenderer extends GeoArmorRenderer<ChaosSet> {

    public EnhancedChaosArmorRenderer() {
        super(new EnhancedChaosArmorModel());

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
