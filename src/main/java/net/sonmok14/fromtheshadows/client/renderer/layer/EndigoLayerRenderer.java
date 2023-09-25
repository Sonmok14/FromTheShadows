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
import net.sonmok14.fromtheshadows.entity.BulldrogiothEntity;
import net.sonmok14.fromtheshadows.entity.EndigoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class EndigoLayerRenderer extends GeoRenderLayer<EndigoEntity> {
    private static final ResourceLocation LAYER = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/endigo_layer.png");
    private static final ResourceLocation HAND = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/endigo_layer_hand.png");


    @SuppressWarnings("unchecked")
    public EndigoLayerRenderer(GeoRenderer<EndigoEntity> entityRendererIn) {
        super(entityRendererIn);
    }



    @Override
    public void render(PoseStack poseStack, EndigoEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType eyes = FTSRenderType.eyes(LAYER);
        RenderType hand =  FTSRenderType.entityTranslucentEmissive(HAND);

        float strength = 0.5F + Mth.clamp(((float) Math.cos((animatable.layerTicks + partialTick) * 0.1F)) - 0.5F, -0.5F, 0.5F);

        strength += Mth.lerp(partialTick, animatable.olayerBrightness, animatable.layerBrightness) * 1 * Mth.PI;
        strength = Mth.clamp(strength, 0.1f, 1);

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, hand,
                bufferSource.getBuffer(hand), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                strength, strength, strength, strength);

                    getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, eyes,
                            bufferSource.getBuffer(eyes), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                            1, 1, 1, 1);

    }

}
