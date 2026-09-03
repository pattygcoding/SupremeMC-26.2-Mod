package com.suprememc.client;

import com.mojang.serialization.MapCodec;
import com.suprememc.Constants;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.projectile.TridentModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import org.joml.Vector3fc;

import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;

/** In-hand/held rendering for the Abyssalite Trident; mirrors vanilla's {@code minecraft:trident} special model. */
public class AbyssaliteTridentSpecialRenderer implements NoDataSpecialModelRenderer {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "abyssalite_trident");
    public static final Identifier TEXTURE =
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/trident/abyssalite_trident.png");

    private final TridentModel model;

    private AbyssaliteTridentSpecialRenderer(TridentModel model) {
        this.model = model;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords,
            boolean hasFoil, int outlineColor) {
        collector.order(0).submitModel(this.model, Unit.INSTANCE, poseStack, TEXTURE, lightCoords, overlayCoords,
                outlineColor, (ModelFeatureRenderer.CrumblingOverlay) null);
        if (hasFoil) {
            collector.order(1).submitModel(this.model, Unit.INSTANCE, poseStack, RenderTypes.entityGlint(),
                    lightCoords, overlayCoords, outlineColor, (ModelFeatureRenderer.CrumblingOverlay) null);
        }
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        this.model.root().getExtentsForGui(new PoseStack(), consumer);
    }

    public record Unbaked() implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

        @Override
        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<Void> bake(SpecialModelRenderer.BakingContext context) {
            return new AbyssaliteTridentSpecialRenderer(new TridentModel(context.entityModelSet().bakeLayer(ModelLayers.TRIDENT)));
        }
    }
}
