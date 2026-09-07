package com.suprememc.client;

import com.suprememc.Constants;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.CreeperPowerLayer;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.resources.Identifier;

public class SnowCreeperRenderer extends CreeperRenderer {
    private static final Identifier SNOW_CREEPER_LOCATION = Identifier.fromNamespaceAndPath(
        Constants.MOD_ID, "textures/entity/snow_creeper/snow_creeper.png");

    public SnowCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.layers.removeIf(layer -> layer instanceof CreeperPowerLayer);
        this.addLayer(new SnowCreeperPowerLayer(this, context.getModelSet()));
    }

    @Override
    public Identifier getTextureLocation(CreeperRenderState state) {
        return SNOW_CREEPER_LOCATION;
    }
}