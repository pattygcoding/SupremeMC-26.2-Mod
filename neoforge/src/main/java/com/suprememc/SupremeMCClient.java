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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class SupremeMCClient {
    private SupremeMCClient() {
    }

    @SubscribeEvent
    public static void registerFluidModels(RegisterFluidModelsEvent event) {
        event.register(new FluidModel.Unbaked(
            new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block/milk_still")),
            new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block/milk_flow")),
            new Material(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "block/milk_overlay")),
            net.minecraft.client.color.block.BlockTintSources.constant(0xFFFFFFFF)),
            ModFluids.MILK, ModFluids.FLOWING_MILK);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.ABYSSALITE_TRIDENT_ENTITY, AbyssaliteTridentRenderer::new);
        event.registerEntityRenderer(ModEntities.GRIZZLY_BEAR_ENTITY, GrizzlyBearRenderer::new);
        event.registerEntityRenderer(ModEntities.ENDER_SPIDER_ENTITY, EnderSpiderRenderer::new);
        event.registerEntityRenderer(ModEntities.FIRE_CREEPER_ENTITY, FireCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.SNOW_CREEPER_ENTITY, SnowCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.SNOW_TNT_ENTITY, TntRenderer::new);
        event.registerEntityRenderer(ModEntities.FIRE_TNT_ENTITY, TntRenderer::new);
        event.registerEntityRenderer(ModEntities.PALM_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
        event.registerEntityRenderer(ModEntities.PALM_CHEST_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
        event.registerEntityRenderer(ModEntities.LAVENDER_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
        event.registerEntityRenderer(ModEntities.LAVENDER_CHEST_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
        event.registerEntityRenderer(ModEntities.CRIMSON_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
        event.registerEntityRenderer(ModEntities.CRIMSON_CHEST_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
        event.registerEntityRenderer(ModEntities.WARPED_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
        event.registerEntityRenderer(ModEntities.WARPED_CHEST_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
    }

    @SubscribeEvent
    public static void registerSpecialModelRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(AbyssaliteTridentSpecialRenderer.ID, AbyssaliteTridentSpecialRenderer.Unbaked.MAP_CODEC);
    }

    @SubscribeEvent
    public static void registerRangeSelectItemModelProperties(RegisterRangeSelectItemModelPropertyEvent event) {
        event.register(BowPullProperty.ID, BowPullProperty.MAP_CODEC);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        // Grayscale palm leaves texture relies on foliage tinting, same as vanilla oak leaves.
        event.register(List.of(BlockTintSources.foliage()), ModContent.PALM_LEAVES);
    }
}