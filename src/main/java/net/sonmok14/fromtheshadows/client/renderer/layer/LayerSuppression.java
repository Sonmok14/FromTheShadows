package net.sonmok14.fromtheshadows.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.sonmok14.fromtheshadows.client.renderer.FTSRenderType;
import net.sonmok14.fromtheshadows.utils.registry.EffectRegistry;

public class LayerSuppression extends RenderLayer {
    private final RenderLayerParent parent;

    public LayerSuppression(RenderLayerParent parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(((LivingEntity) entity).hasEffect(EffectRegistry.SUPPRESSION.get()) && entity instanceof LivingEntity) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(getRenderType());
            float alpha = 0.5f;
            matrixStackIn.pushPose();
            this.getParentModel().renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords((LivingEntity)entity, 0), 1, 1, 1, alpha);
            matrixStackIn.popPose();
        }
    }


    private RenderType getRenderType() {
        return FTSRenderType.SUPPRESSION;
        }
}
