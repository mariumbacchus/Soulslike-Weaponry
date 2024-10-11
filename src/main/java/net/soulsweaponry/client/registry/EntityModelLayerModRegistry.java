package net.soulsweaponry.client.registry;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.model.entity.mobs.BigChungusModel;
import net.soulsweaponry.client.model.entity.projectile.DragonslayerSwordspearModel;

public class EntityModelLayerModRegistry {

    private static final Dilation HAT_DILATION = new Dilation(0.5F);
    private static final Dilation ARMOR_DILATION = new Dilation(1.0F);

    public static final EntityModelLayer BIG_CHUNGUS_LAYER = createMain("big_chungus");
    public static final EntityModelLayer DRAGONSLAYER_SWORDSPEAR_LAYER = createMain("swordspear_entity");
    public static final EntityModelLayer DARK_SORCERER_LAYER = createMain("dark_sorcerer");
    public static final EntityModelLayer FORLORN_LAYER = createMain("forlorn");
    public static final EntityModelLayer REMNANT_LAYER = createMain("remnant");
    public static final EntityModelLayer SOUL_REAPER_GHOST_LAYER = createMain("soul_reaper_ghost");

    public static final EntityModelLayer DARK_SORCERER_INNER_ARMOR = createInnerArmor("dark_sorcerer");
    public static final EntityModelLayer FORLORN_INNER_ARMOR = createInnerArmor("forlorn");
    public static final EntityModelLayer REMNANT_INNER_ARMOR = createInnerArmor("remnant");
    public static final EntityModelLayer SOUL_REAPER_GHOST_INNER_ARMOR = createInnerArmor("soul_reaper_ghost");

    public static final EntityModelLayer DARK_SORCERER_OUTER_ARMOR = createOuterArmor("dark_sorcerer");
    public static final EntityModelLayer FORLORN_OUTER_ARMOR = createOuterArmor("forlorn");
    public static final EntityModelLayer REMNANT_OUTER_ARMOR = createOuterArmor("remnant");
    public static final EntityModelLayer SOUL_REAPER_GHOST_OUTER_ARMOR = createOuterArmor("soul_reaper_ghost");

    public static void initClient(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(DRAGONSLAYER_SWORDSPEAR_LAYER, DragonslayerSwordspearModel::getTexturedModelData);
        event.registerLayerDefinition(BIG_CHUNGUS_LAYER, BigChungusModel::getTexturedModelData);
        registerBiped(event, DARK_SORCERER_LAYER);
        registerBiped(event, FORLORN_LAYER);
        registerBiped(event, REMNANT_LAYER);
        registerBiped(event, SOUL_REAPER_GHOST_LAYER);

        registerBipedInnerArmor(event, DARK_SORCERER_INNER_ARMOR);
        registerBipedInnerArmor(event, FORLORN_INNER_ARMOR);
        registerBipedInnerArmor(event, REMNANT_INNER_ARMOR);
        registerBipedInnerArmor(event, SOUL_REAPER_GHOST_INNER_ARMOR);

        registerBipedOuterArmor(event, DARK_SORCERER_OUTER_ARMOR);
        registerBipedOuterArmor(event, FORLORN_OUTER_ARMOR);
        registerBipedOuterArmor(event, REMNANT_OUTER_ARMOR);
        registerBipedOuterArmor(event, SOUL_REAPER_GHOST_OUTER_ARMOR);
    }

    private static EntityModelLayer createOuterArmor(String id) {
        return create(id, "outer_armor");
    }

    private static EntityModelLayer createInnerArmor(String id) {
        return create(id, "inner_armor");
    }

    private static EntityModelLayer createMain(String id) {
        return create(id, "main");
    }

    private static EntityModelLayer create(String id, String layer) {
        return new EntityModelLayer(new Identifier(SoulsWeaponry.ModId, id), layer);
    }

    private static void registerBiped(EntityRenderersEvent.RegisterLayerDefinitions event, EntityModelLayer layer) {
        event.registerLayerDefinition(layer, () -> TexturedModelData.of(BipedEntityModel.getModelData(Dilation.NONE, 0.0F), 64, 64));
    }

    private static void registerBipedInnerArmor(EntityRenderersEvent.RegisterLayerDefinitions event, EntityModelLayer layer) {
        event.registerLayerDefinition(layer, () -> TexturedModelData.of(BipedEntityModel.getModelData(HAT_DILATION, 0.0f), 64, 32));
    }

    private static void registerBipedOuterArmor(EntityRenderersEvent.RegisterLayerDefinitions event, EntityModelLayer layer) {
        event.registerLayerDefinition(layer, () -> TexturedModelData.of(BipedEntityModel.getModelData(ARMOR_DILATION, 0.0f), 64, 32));
    }
}