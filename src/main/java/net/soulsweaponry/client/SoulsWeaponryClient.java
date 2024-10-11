package net.soulsweaponry.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.soulsweaponry.SoulsWeaponry;
import net.soulsweaponry.client.hud.PostureHudOverlay;
import net.soulsweaponry.client.model.entity.mobs.BigChungusModel;
import net.soulsweaponry.client.model.entity.projectile.DragonslayerSwordspearModel;
import net.soulsweaponry.client.registry.*;
import net.soulsweaponry.client.renderer.armor.ChaosArmorRenderer;
import net.soulsweaponry.client.renderer.armor.ChaosSetRenderer;
import net.soulsweaponry.client.renderer.armor.EChaosArmorRenderer;
import net.soulsweaponry.client.renderer.armor.WitheredArmorRenderer;
import net.soulsweaponry.client.renderer.entity.mobs.SoulReaperGhostRenderer;
import net.soulsweaponry.client.renderer.item.*;
import net.soulsweaponry.networking.PacketRegistry;
import net.soulsweaponry.registry.BlockRegistry;
import net.soulsweaponry.registry.EntityRegistry;
import net.soulsweaponry.registry.ItemRegistry;
import net.soulsweaponry.registry.WeaponRegistry;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class SoulsWeaponryClient implements ClientModInitializer {

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
        GeoArmorRenderer.registerArmorRenderer(new EChaosArmorRenderer(), ItemRegistry.ENHANCED_ARKENPLATE);
        GeoArmorRenderer.registerArmorRenderer(new WitheredArmorRenderer(), ItemRegistry.WITHERED_CHEST);
        GeoArmorRenderer.registerArmorRenderer(new WitheredArmorRenderer(), ItemRegistry.ENHANCED_WITHERED_CHEST);


        HudRenderCallback.EVENT.register(new PostureHudOverlay());

        EntityModelLayerModRegistry.initClient();
        EntityModelRegistry.initClient();
        PredicateRegistry.initClient();
        PacketRegistry.registerS2CPackets();
        KeyBindRegistry.initClient();
        ParticleClientRegistry.initClient();
    }
}
