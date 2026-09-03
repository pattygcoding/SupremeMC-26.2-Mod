package com.suprememc.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.phys.AABB;

// Reimplements ThrownTridentRenderer instead of extending it so the vanilla trident texture isn't drawn on top.
public class AbyssaliteTridentRenderer extends EntityRenderer<ThrownTrident, ThrownTridentRenderState> {

    private final TridentModel model;

    public AbyssaliteTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new TridentModel(context.bakeLayer(ModelLayers.TRIDENT));
    }

    @Override
    public ThrownTridentRenderState createRenderState() {
        return new ThrownTridentRenderState();
    }

    @Override
    public void extractRenderState(ThrownTrident trident, ThrownTridentRenderState state, float partialTick) {
        super.extractRenderState(trident, state, partialTick);
        state.yRot = trident.getYRot(partialTick);
        state.xRot = trident.getXRot(partialTick);
        state.isFoil = trident.isFoil();
    }

    @Override
    protected AABB getBoundingBoxForCulling(ThrownTrident trident) {
        return super.getBoundingBoxForCulling(trident).inflate(1.5D);
    }

    @Override
    public void submit(ThrownTridentRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
            CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot + 90.0F));
        collector.order(0).submitModel(this.model, Unit.INSTANCE, poseStack,
                AbyssaliteTridentSpecialRenderer.TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY,
                state.outlineColor, (ModelFeatureRenderer.CrumblingOverlay) null);
        if (state.isFoil) {
            collector.order(1).submitModel(this.model, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(),
                    state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor,
                    (ModelFeatureRenderer.CrumblingOverlay) null);
        }
        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}