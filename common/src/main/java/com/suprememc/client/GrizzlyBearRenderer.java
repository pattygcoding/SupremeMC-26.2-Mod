package com.suprememc.client;

import com.suprememc.Constants;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PolarBearRenderer;
import net.minecraft.client.renderer.entity.state.PolarBearRenderState;
import net.minecraft.resources.Identifier;

public class GrizzlyBearRenderer extends PolarBearRenderer {
    private static final Identifier GRIZZLY_BEAR_LOCATION = Identifier.fromNamespaceAndPath(
        Constants.MOD_ID, "textures/entity/bear/grizzlybear.png");
    private static final Identifier GRIZZLY_BEAR_BABY_LOCATION = Identifier.fromNamespaceAndPath(
        Constants.MOD_ID, "textures/entity/bear/grizzlybear_baby.png");

    public GrizzlyBearRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public Identifier getTextureLocation(PolarBearRenderState state) {
        return state.isBaby ? GRIZZLY_BEAR_BABY_LOCATION : GRIZZLY_BEAR_LOCATION;
    }
}