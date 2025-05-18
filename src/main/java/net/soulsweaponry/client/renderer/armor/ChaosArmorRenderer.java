package net.soulsweaponry.client.renderer.armor;

import net.minecraft.item.Item;
import net.soulsweaponry.client.model.armor.ChaosArmorModel;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ChaosArmorRenderer<T extends Item & GeoItem> extends GeoArmorRenderer<T> {

    public ChaosArmorRenderer() {
        super(new ChaosArmorModel<>());

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
