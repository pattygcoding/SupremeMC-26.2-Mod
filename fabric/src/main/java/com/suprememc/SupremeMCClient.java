package com.suprememc;

import com.suprememc.client.AbyssaliteTridentRenderer;
import com.suprememc.client.AbyssaliteTridentSpecialRenderer;
import com.suprememc.content.ModContent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

public class SupremeMCClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModContent.ABYSSALITE_TRIDENT_ENTITY, AbyssaliteTridentRenderer::new);
        EntityRendererRegistry.register(ModContent.PALM_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
        EntityRendererRegistry.register(ModContent.PALM_CHEST_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
        SpecialModelRenderers.ID_MAPPER.put(AbyssaliteTridentSpecialRenderer.ID,
                AbyssaliteTridentSpecialRenderer.Unbaked.MAP_CODEC);
    }
}