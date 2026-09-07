package com.suprememc.client;

import com.suprememc.Constants;
import com.suprememc.content.entity.EnderSpider;
import net.minecraft.client.model.monster.spider.SpiderModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SpiderRenderer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class EnderSpiderRenderer extends SpiderRenderer<EnderSpider> {
    private static final Identifier ENDER_SPIDER_LOCATION = Identifier.fromNamespaceAndPath(
        Constants.MOD_ID, "textures/entity/spider/ender_spider.png");
    private static final Identifier ENDER_SPIDER_EYES_LOCATION = Identifier.fromNamespaceAndPath(
        Constants.MOD_ID, "textures/entity/spider/ender_spider_eyes.png");

    public EnderSpiderRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.layers.clear();
        this.addLayer(new EnderSpiderEyesLayer(this));
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return ENDER_SPIDER_LOCATION;
    }

    private static final class EnderSpiderEyesLayer extends SpiderEyesLayer<SpiderModel> {
        private EnderSpiderEyesLayer(EnderSpiderRenderer renderer) {
            super(renderer);
        }

        @Override
        public RenderType renderType() {
            return RenderTypes.eyes(ENDER_SPIDER_EYES_LOCATION);
        }
    }
}