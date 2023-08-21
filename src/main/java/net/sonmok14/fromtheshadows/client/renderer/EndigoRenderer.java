package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.sonmok14.fromtheshadows.client.models.EndigoModel;
import net.sonmok14.fromtheshadows.entity.EndigoEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EndigoRenderer extends GeoEntityRenderer<EndigoEntity> {
    public EndigoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EndigoModel());
        shadowRadius = 1f;
    }

    @Override
    public void render(EndigoEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.scale(1.2f, 1.2f, 1.2f);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }
}
