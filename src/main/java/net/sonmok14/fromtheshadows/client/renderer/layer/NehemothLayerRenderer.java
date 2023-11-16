package net.sonmok14.fromtheshadows.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.client.renderer.FTSRenderType;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.entity.NehemothEntity;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class NehemothLayerRenderer extends GeoLayerRenderer<NehemothEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(Fromtheshadows.MODID, "geo/nehemoth.geo.json");
    private static final ResourceLocation RENDER_TYPE = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/nehemoth_stone.png");
    private static final ResourceLocation LAYER = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/nehemoth_eye.png");
    private static final ResourceLocation LAYER_SOUL = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/nehemoth_eye_2.png");
    private static final ResourceLocation LAYER_SOUL_EYES = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/nehemoth_eye_3.png");
    @SuppressWarnings("unchecked")
    public NehemothLayerRenderer(IGeoRenderer<NehemothEntity> entityRendererIn) {
        super(entityRendererIn);
    }
    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLight, NehemothEntity animatable, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType normal =  RenderType.eyes(LAYER);
        RenderType nether1 =  FTSRenderType.eyes(LAYER_SOUL);
        RenderType nether2 =  RenderType.eyes(LAYER_SOUL_EYES);
        RenderType stone =  RenderType.armorCutoutNoCull(RENDER_TYPE);
        matrixStackIn.pushPose();
        if (animatable.isStone()) {
            this.getRenderer().render(this.getEntityModel().getModel(MODEL), animatable, partialTicks, stone, matrixStackIn, bufferIn,
                    bufferIn.getBuffer(stone), packedLight, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }if(!animatable.isStone()) {
            if (animatable.getVariant() == 0) {
                this.getRenderer().render(this.getEntityModel().getModel(MODEL), animatable, partialTicks, normal, matrixStackIn, bufferIn,
                        bufferIn.getBuffer(normal), packedLight, OverlayTexture.NO_OVERLAY,
                        3, 1, 1, 1);
            }
        }
        if (animatable.getVariant() == 1) {
            this.getRenderer().render(this.getEntityModel().getModel(MODEL), animatable, partialTicks, nether1, matrixStackIn, bufferIn,
                    bufferIn.getBuffer(nether1), packedLight, OverlayTexture.NO_OVERLAY,
                    0.4f, 0.4f, 0.4f, 0.4f);
            this.getRenderer().render(this.getEntityModel().getModel(MODEL), animatable, partialTicks, nether2, matrixStackIn, bufferIn,
                    bufferIn.getBuffer(nether2), packedLight, OverlayTexture.NO_OVERLAY,
                    1, 1, 1, 1);
        }
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), animatable, partialTicks, normal, matrixStackIn, bufferIn,
                bufferIn.getBuffer(normal), packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        matrixStackIn.popPose();
    }
}
