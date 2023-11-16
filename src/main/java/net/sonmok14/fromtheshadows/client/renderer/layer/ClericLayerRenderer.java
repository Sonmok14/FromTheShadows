package net.sonmok14.fromtheshadows.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.entity.ClericEntity;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class ClericLayerRenderer extends GeoLayerRenderer<ClericEntity> {

    private static final ResourceLocation MODEL = new ResourceLocation(Fromtheshadows.MODID, "geo/cultist.geo.json");
    private static final ResourceLocation LAYER = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/cultist_layer.png");
    @SuppressWarnings("unchecked")

    public ClericLayerRenderer(IGeoRenderer<ClericEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, ClericEntity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType normal =  RenderType.eyes(LAYER);
        matrixStackIn.pushPose();
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, normal, matrixStackIn, bufferIn,
                bufferIn.getBuffer(normal), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
        matrixStackIn.popPose();
    }
    }
