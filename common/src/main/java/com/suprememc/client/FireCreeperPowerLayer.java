package com.suprememc.client;

import com.suprememc.Constants;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.creeper.CreeperModel;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.client.renderer.entity.state.CreeperRenderState;
import net.minecraft.resources.Identifier;

/** Mirrors vanilla's CreeperPowerLayer, but points at the fire creeper's charged armor texture. */
public class FireCreeperPowerLayer extends EnergySwirlLayer<CreeperRenderState, CreeperModel> {
    private static final Identifier POWER_LOCATION = Identifier.fromNamespaceAndPath(
        Constants.MOD_ID, "textures/entity/fire_creeper/fire_creeper_armor.png");
    private final CreeperModel model;

    public FireCreeperPowerLayer(RenderLayerParent<CreeperRenderState, CreeperModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new CreeperModel(modelSet.bakeLayer(ModelLayers.CREEPER_ARMOR));
    }

    @Override
    protected boolean isPowered(CreeperRenderState state) {
        return state.isPowered;
    }

    @Override
    protected float xOffset(float t) {
        return t * 0.01F;
    }

    @Override
    protected Identifier getTextureLocation() {
        return POWER_LOCATION;
    }

    @Override
    protected CreeperModel model() {
        return this.model;
    }
}
