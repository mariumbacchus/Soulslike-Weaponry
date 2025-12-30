package net.soulsweaponry.client.model.entity.mobs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.soulsweaponry.client.renderer.entity.mobs.BigChungusRenderer;

@Environment(EnvType.CLIENT)
public class BigChungusModel extends EntityModel<BigChungusRenderer.BigChungusRenderState> {

    private final ModelPart root;

    public BigChungusModel(ModelPart root) {
        super(root);
        this.root = root;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        root.addChild(
                "earRight",
                ModelPartBuilder.create().uv(2, 0).cuboid(1.0F, 0F, -1.0F, 2.0F, 7.0F, 1.0F),
                ModelTransform.NONE
        );

        root.addChild(
                "earLeft",
                ModelPartBuilder.create().uv(3, 0).cuboid(-3.0F, 0F, -1.0F, 2.0F, 7.0F, 1.0F),
                ModelTransform.NONE
        );

        root.addChild(
                "feetRight",
                ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, 23F, -3.0F, 2.0F, 1.0F, 6.0F),
                ModelTransform.NONE
        );

        root.addChild(
                "feetLeft",
                ModelPartBuilder.create().uv(32, 0).cuboid(2.0F, 23F, -3.0F, 2.0F, 1.0F, 6.0F),
                ModelTransform.NONE
        );

        root.addChild(
                "body",
                ModelPartBuilder.create().uv(0, 19).cuboid(-6.0F, 15F, -6.0F, 12.0F, 8.0F, 12.0F),
                ModelTransform.NONE
        );

        root.addChild(
                "head",
                ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, 7F, -4.0F, 8.0F, 8.0F, 8.0F),
                ModelTransform.NONE
        );

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(BigChungusRenderer.BigChungusRenderState state) {
        // Yarn names vary slightly by build, but the idea is:
        // head yaw/pitch now come from the render state, not parameters.
        //
        // If LivingEntityRenderState in the mappings exposes headYaw/headPitch:
        // this.head.yaw = state.headYaw * ((float)Math.PI / 180F);
        // this.head.pitch = state.headPitch * ((float)Math.PI / 180F);

        // If the fields are named differently in your workspace,
        // just map them here (search for "yaw"/"pitch" on the state class in your IDE).
    }

    public ModelPart getRoot() {
        return this.root;
    }
}