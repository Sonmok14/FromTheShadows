package net.sonmok14.fromtheshadows.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.client.renderer.FTSRenderType;
import net.sonmok14.fromtheshadows.server.entity.BulldrogiothEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class BulldrogiothLayerRenderer extends GeoRenderLayer<BulldrogiothEntity> {
    private static final ResourceLocation LAYER = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/bulldrogioth_eyes.png");


    @SuppressWarnings("unchecked")
    public BulldrogiothLayerRenderer(GeoRenderer<BulldrogiothEntity> entityRendererIn) {
        super(entityRendererIn);
    }



    @Override
    public void render(PoseStack poseStack, BulldrogiothEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType normal = FTSRenderType.eyes(LAYER);

                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, normal,
                            bufferSource.getBuffer(normal), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                            1, 1, 1, 1);

    }

}
