package com.suprememc;

import com.suprememc.client.AbyssaliteTridentRenderer;
import com.suprememc.client.AbyssaliteTridentSpecialRenderer;
import com.suprememc.client.FireCreeperRenderer;
import com.suprememc.client.GrizzlyBearRenderer;
import com.suprememc.content.ModContent;
import java.util.List;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.BoatRenderer;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public final class SupremeMCClient {
    private SupremeMCClient() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModContent.ABYSSALITE_TRIDENT_ENTITY, AbyssaliteTridentRenderer::new);
        event.registerEntityRenderer(ModContent.GRIZZLY_BEAR_ENTITY, GrizzlyBearRenderer::new);
        event.registerEntityRenderer(ModContent.FIRE_CREEPER_ENTITY, FireCreeperRenderer::new);
        event.registerEntityRenderer(ModContent.PALM_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_BOAT));
        event.registerEntityRenderer(ModContent.PALM_CHEST_BOAT_ENTITY,
            context -> new BoatRenderer(context, ModelLayers.OAK_CHEST_BOAT));
    }

    @SubscribeEvent
    public static void registerSpecialModelRenderers(RegisterSpecialModelRendererEvent event) {
        event.register(AbyssaliteTridentSpecialRenderer.ID, AbyssaliteTridentSpecialRenderer.Unbaked.MAP_CODEC);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        // Grayscale palm leaves texture relies on foliage tinting, same as vanilla oak leaves.
        event.register(List.of(BlockTintSources.foliage()), ModContent.PALM_LEAVES);
    }
}