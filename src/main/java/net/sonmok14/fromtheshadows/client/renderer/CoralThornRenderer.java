package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.sonmok14.fromtheshadows.client.models.CoralThornModel;
import net.sonmok14.fromtheshadows.server.entity.projectiles.CoralThornEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CoralThornRenderer extends GeoEntityRenderer<CoralThornEntity> {
    public CoralThornRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new CoralThornModel());
    }


    @Override
    public void render(CoralThornEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0F));
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();

    }

}
