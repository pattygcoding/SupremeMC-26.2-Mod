package com.suprememc.client;

import com.suprememc.Constants;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.CreeperPowerLayer;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.resources.Identifier;

public class FireCreeperRenderer extends CreeperRenderer {
    private static final Identifier FIRE_CREEPER_LOCATION = Identifier.fromNamespaceAndPath(
        Constants.MOD_ID, "textures/entity/fire_creeper/fire_creeper.png");

    public FireCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);
        // Swap the vanilla power layer (hardcoded creeper armor texture) for the fire creeper's own.
        this.layers.removeIf(layer -> layer instanceof CreeperPowerLayer);
        this.addLayer(new FireCreeperPowerLayer(this, context.getModelSet()));
    }

    @Override
    public Identifier getTextureLocation(CreeperRenderState state) {
        return FIRE_CREEPER_LOCATION;
    }
}
