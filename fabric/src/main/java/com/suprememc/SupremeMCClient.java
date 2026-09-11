package com.suprememc;

import com.suprememc.client.AbyssaliteTridentRenderer;
import com.suprememc.client.AbyssaliteTridentSpecialRenderer;
import com.suprememc.client.BowPullProperty;
import com.suprememc.client.EnderSpiderRenderer;
import com.suprememc.client.FireCreeperRenderer;
import com.suprememc.client.GrizzlyBearRenderer;
import com.suprememc.client.SnowCreeperRenderer;
import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModEntities;
import com.suprememc.content.init.ModFluids;
import java.util.List;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.special.SpecialModelRenderers;

public class SupremeMCClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        var fluidModel = new FluidModel.Unbaked(
            new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block/milk_still")),
            new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block/milk_flow")),
            new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block/milk_overlay")),
            BlockTintSources.constant(0xFFFFFFFF));
        FluidRenderingRegistry.register(ModFluids.MILK, ModFluids.FLOWING_MILK, fluidModel);
        EntityRendererRegistry.register(ModEntities.ABYSSALITE_TRIDENT_ENTITY, AbyssaliteTridentRenderer::new);
        EntityRendererRegistry.register(ModEntities.GRIZZLY_BEAR_ENTITY, GrizzlyBearRenderer::new);
        EntityRendererRegistry.register(ModEntities.ENDER_SPIDER_ENTITY, EnderSpiderRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIRE_CREEPER_ENTITY, FireCreeperRenderer::new);
        EntityRendererRegistry.register(ModEntities.SNOW_CREEPER_ENTITY, SnowCreeperRenderer::new);
        EntityRendererRegistry.register(ModEntities.SNOW_TNT_ENTITY, TntRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIRE_TNT_ENTITY, TntRenderer::new);
        EntityRendererRegistry.register(ModEntities.PALM_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
        EntityRendererRegistry.register(ModEntities.PALM_CHEST_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
        EntityRendererRegistry.register(ModEntities.LAVENDER_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
        EntityRendererRegistry.register(ModEntities.LAVENDER_CHEST_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
        EntityRendererRegistry.register(ModEntities.CRIMSON_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
        EntityRendererRegistry.register(ModEntities.CRIMSON_CHEST_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
        EntityRendererRegistry.register(ModEntities.WARPED_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
        EntityRendererRegistry.register(ModEntities.WARPED_CHEST_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
        SpecialModelRenderers.ID_MAPPER.put(AbyssaliteTridentSpecialRenderer.ID,
                AbyssaliteTridentSpecialRenderer.Unbaked.MAP_CODEC);
        RangeSelectItemModelProperties.ID_MAPPER.put(BowPullProperty.ID, BowPullProperty.MAP_CODEC);
        // Grayscale palm leaves texture relies on foliage tinting, same as vanilla oak leaves.
        BlockColorRegistry.register(List.of(BlockTintSources.foliage()), ModContent.PALM_LEAVES);
    }
}