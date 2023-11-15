package net.sonmok14.fromtheshadows.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.client.renderer.FTSRenderType;
import net.sonmok14.fromtheshadows.server.entity.NehemothEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class NehemothLayerRenderer extends GeoRenderLayer<NehemothEntity> {
    private static final ResourceLocation RENDER_TYPE = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/nehemoth_stone.png");
    private static final ResourceLocation LAYER = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/nehemoth_eye.png");
    private static final ResourceLocation LAYER_SOUL = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/nehemoth_eye_2.png");

    private static final ResourceLocation LAYER_SOUL_EYES = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/nehemoth_eye_3.png");
    @SuppressWarnings("unchecked")
    public NehemothLayerRenderer(GeoRenderer<NehemothEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, NehemothEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType normal =  RenderType.eyes(LAYER);
        RenderType nether1 =  FTSRenderType.eyes(LAYER_SOUL);
        RenderType nether2 =  RenderType.eyes(LAYER_SOUL_EYES);
        RenderType stone =  RenderType.armorCutoutNoCull(RENDER_TYPE);


        if (animatable.isStone()) {
            getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, stone,
                    bufferSource.getBuffer(stone), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }

            if(!animatable.isStone()) {
                if (animatable.getVariant() == 0) {
                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, normal,
                            bufferSource.getBuffer(normal), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                            3, 1, 1, 1);
                }
            }
        if (animatable.getVariant() == 1) {
            getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, nether1,
                    bufferSource.getBuffer(nether1), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                    0.4f, 0.4f, 0.4f, 0.4f);
            getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, nether2,
                    bufferSource.getBuffer(nether2), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }

    }

}
