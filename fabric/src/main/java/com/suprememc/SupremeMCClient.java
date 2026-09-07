package com.suprememc;

import com.suprememc.client.AbyssaliteTridentRenderer;
import com.suprememc.client.AbyssaliteTridentSpecialRenderer;
import com.suprememc.client.BowPullProperty;
import com.suprememc.client.EnderSpiderRenderer;
import com.suprememc.client.FireCreeperRenderer;
import com.suprememc.client.GrizzlyBearRenderer;
import com.suprememc.client.SnowCreeperRenderer;
import com.suprememc.content.ModContent;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

public class SupremeMCClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModContent.ABYSSALITE_TRIDENT_ENTITY, AbyssaliteTridentRenderer::new);
        EntityRendererRegistry.register(ModContent.GRIZZLY_BEAR_ENTITY, GrizzlyBearRenderer::new);
        EntityRendererRegistry.register(ModContent.ENDER_SPIDER_ENTITY, EnderSpiderRenderer::new);
        EntityRendererRegistry.register(ModContent.FIRE_CREEPER_ENTITY, FireCreeperRenderer::new);
        EntityRendererRegistry.register(ModContent.SNOW_CREEPER_ENTITY, SnowCreeperRenderer::new);
        EntityRendererRegistry.register(ModContent.PALM_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
        EntityRendererRegistry.register(ModContent.PALM_CHEST_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
        SpecialModelRenderers.ID_MAPPER.put(AbyssaliteTridentSpecialRenderer.ID,
                AbyssaliteTridentSpecialRenderer.Unbaked.MAP_CODEC);
        RangeSelectItemModelProperties.ID_MAPPER.put(BowPullProperty.ID, BowPullProperty.MAP_CODEC);
        // Grayscale palm leaves texture relies on foliage tinting, same as vanilla oak leaves.
        BlockColorRegistry.register(List.of(BlockTintSources.foliage()), ModContent.PALM_LEAVES);
    }
}