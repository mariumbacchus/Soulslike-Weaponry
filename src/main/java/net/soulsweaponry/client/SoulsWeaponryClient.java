package net.soulsweaponry.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.model.entity.mobs.BigChungusModel;
import net.soulsweaponry.client.model.entity.mobs.SoulReaperGhostModel;
import net.soulsweaponry.client.model.entity.projectile.DragonslayerSwordspearModel;
import net.soulsweaponry.client.registry.EntityModelRegistry;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.client.registry.PacketsClient;
import net.soulsweaponry.client.registry.ParticleClientRegistry;
import net.soulsweaponry.client.registry.PredicateRegistry;
import net.soulsweaponry.client.renderer.entity.mobs.SoulReaperGhostRenderer;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.EntityRegistry;

public class SoulsWeaponryClient implements ClientModInitializer {
    public static final EntityModelLayer BIG_CHUNGUS_LAYER = new EntityModelLayer(new Identifier(SoulsWeaponry.ModId, "big_chungus"), "main");
    public static final EntityModelLayer DRAGONSLAYER_SWORDSPEAR_LAYER = new EntityModelLayer(new Identifier(SoulsWeaponry.ModId, "swordspear_entity"), "main");

    @Override
    public void onInitializeClient() {

        //Blocks
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BlockRegistry.WITHERED_GRASS);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BlockRegistry.WITHERED_TALL_GRASS);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BlockRegistry.WITHERED_FERN);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BlockRegistry.WITHERED_LARGE_FERN);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BlockRegistry.WITHERED_BERRY_BUSH);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BlockRegistry.HYDRANGEA);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BlockRegistry.OLEANDER);

        EntityRendererRegistry.register(EntityRegistry.SOUL_REAPER_GHOST, (context) -> {
            return new SoulReaperGhostRenderer(context, new SoulReaperGhostModel<>(context.getPart(EntityModelLayers.ZOMBIE)), 0.5f);
        });

        EntityModelLayerRegistry.registerModelLayer(DRAGONSLAYER_SWORDSPEAR_LAYER, DragonslayerSwordspearModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BIG_CHUNGUS_LAYER, BigChungusModel::getTexturedModelData);
        
        EntityModelRegistry.initClient();
        PredicateRegistry.initClient();
        PacketsClient.initClient();
        KeyBindRegistry.initClient();
        ParticleClientRegistry.initClient();        
    }
}
