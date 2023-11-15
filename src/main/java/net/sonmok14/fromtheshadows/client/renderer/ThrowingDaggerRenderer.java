package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.sonmok14.fromtheshadows.client.models.ThrowingDaggerModel;
import net.sonmok14.fromtheshadows.server.entity.projectiles.ThrowingDaggerEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ThrowingDaggerRenderer extends GeoEntityRenderer<ThrowingDaggerEntity> {
    public ThrowingDaggerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ThrowingDaggerModel());
    }


    @Override
    public void render(ThrowingDaggerEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0F));
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();

    }

}
