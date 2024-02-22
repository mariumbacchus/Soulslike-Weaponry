package net.soulsweaponry.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;
import net.soulsweaponry.client.hud.PostureHudOverlay;
import net.soulsweaponry.client.model.entity.mobs.BigChungusModel;
import net.soulsweaponry.client.model.entity.mobs.SoulReaperGhostModel;
import net.soulsweaponry.client.model.entity.projectile.DragonslayerSwordspearModel;
import net.soulsweaponry.client.registry.EntityModelRegistry;
import net.soulsweaponry.client.registry.KeyBindRegistry;
import net.soulsweaponry.client.renderer.armor.EnhancedChaosArmorRenderer;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.client.registry.ParticleClientRegistry;
import net.soulsweaponry.client.registry.PredicateRegistry;
import net.soulsweaponry.client.renderer.armor.ChaosArmorRenderer;
import net.soulsweaponry.client.renderer.armor.ChaosSetRenderer;
import net.soulsweaponry.client.renderer.entity.mobs.SoulReaperGhostRenderer;
import net.soulsweaponry.client.renderer.item.*;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

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
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), BlockRegistry.VERGLAS_BLOCK);

        //Items
        GeoItemRenderer.registerItemRenderer(WeaponRegistry.NIGHTFALL, new NightfallRenderer());
        GeoItemRenderer.registerItemRenderer(WeaponRegistry.COMET_SPEAR, new CometSpearItemRenderer());
        GeoItemRenderer.registerItemRenderer(WeaponRegistry.BLOODTHIRSTER, new BloodthirsterRenderer());
        GeoItemRenderer.registerItemRenderer(WeaponRegistry.DARKIN_BLADE, new DarkinBladeRenderer());
        GeoItemRenderer.registerItemRenderer(WeaponRegistry.DAWNBREAKER, new DawnbreakerRenderer());
        GeoItemRenderer.registerItemRenderer(WeaponRegistry.SOUL_REAPER, new SoulReaperRenderer());
        GeoItemRenderer.registerItemRenderer(WeaponRegistry.FORLORN_SCYTHE, new ForlornScytheRenderer());
        GeoItemRenderer.registerItemRenderer(WeaponRegistry.LEVIATHAN_AXE, new LeviathanAxeRenderer());
        GeoItemRenderer.registerItemRenderer(WeaponRegistry.MJOLNIR, new MjolnirItemRenderer());
        GeoItemRenderer.registerItemRenderer(WeaponRegistry.FREYR_SWORD, new FreyrSwordItemRenderer());
        GeoItemRenderer.registerItemRenderer(WeaponRegistry.DRAUPNIR_SPEAR, new DraupnirSpearItemRenderer());
        GeoItemRenderer.registerItemRenderer(WeaponRegistry.EMPOWERED_DAWNBREAKER, new EmpoweredDawnbreakerRenderer());

        //Armor
        GeoArmorRenderer.registerArmorRenderer(new ChaosSetRenderer(), ItemRegistry.CHAOS_CROWN);
        GeoArmorRenderer.registerArmorRenderer(new ChaosSetRenderer(), ItemRegistry.CHAOS_ROBES);
        GeoArmorRenderer.registerArmorRenderer(new ChaosArmorRenderer(), ItemRegistry.CHAOS_HELMET);
        GeoArmorRenderer.registerArmorRenderer(new ChaosArmorRenderer(), ItemRegistry.ARKENPLATE);
        GeoArmorRenderer.registerArmorRenderer(new EnhancedChaosArmorRenderer(), ItemRegistry.ENHANCED_ARKENPLATE);

        EntityRendererRegistry.register(EntityRegistry.SOUL_REAPER_GHOST, (context) -> new SoulReaperGhostRenderer(context, new SoulReaperGhostModel<>(context.getPart(EntityModelLayers.ZOMBIE)), 0.5f));

        EntityModelLayerRegistry.registerModelLayer(DRAGONSLAYER_SWORDSPEAR_LAYER, DragonslayerSwordspearModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(BIG_CHUNGUS_LAYER, BigChungusModel::getTexturedModelData);

        HudRenderCallback.EVENT.register(new PostureHudOverlay());
        
        EntityModelRegistry.initClient();
        PredicateRegistry.initClient();
        PacketRegistry.registerS2CPackets();
        KeyBindRegistry.initClient();
        ParticleClientRegistry.initClient();
    }
}
