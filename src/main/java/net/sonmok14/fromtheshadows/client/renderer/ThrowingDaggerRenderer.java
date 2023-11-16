package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sonmok14.fromtheshadows.client.models.ThrowingDaggerModel;
import net.sonmok14.fromtheshadows.server.entity.projectiles.ThrowingDaggerEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class ThrowingDaggerRenderer extends GeoProjectilesRenderer<ThrowingDaggerEntity> {
    public ThrowingDaggerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ThrowingDaggerModel());
    }

    @Override
    public RenderType getRenderType(ThrowingDaggerEntity entity, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(entity));
    }

    @Override
    public void render(ThrowingDaggerEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0F));
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}
