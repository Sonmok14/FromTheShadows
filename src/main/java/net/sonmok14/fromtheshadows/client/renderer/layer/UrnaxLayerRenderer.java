package net.sonmok14.fromtheshadows.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.client.renderer.FTSRenderType;
import net.sonmok14.fromtheshadows.entity.EndigoEntity;
import net.sonmok14.fromtheshadows.entity.UrnaxEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class UrnaxLayerRenderer extends GeoRenderLayer<UrnaxEntity> {
    private static final ResourceLocation LAYER = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/urnax_layer.png");


    @SuppressWarnings("unchecked")
    public UrnaxLayerRenderer(GeoRenderer<UrnaxEntity> entityRendererIn) {
        super(entityRendererIn);
    }



    @Override
    public void render(PoseStack poseStack, UrnaxEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType eyes = FTSRenderType.eyes(LAYER);
                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, eyes,
                            bufferSource.getBuffer(eyes), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                            1, 1, 1, 1);

    }

}
