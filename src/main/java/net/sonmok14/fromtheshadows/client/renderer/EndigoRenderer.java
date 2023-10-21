package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.sonmok14.fromtheshadows.client.models.EndigoModel;
import net.sonmok14.fromtheshadows.client.renderer.layer.EndigoLayerRenderer;
import net.sonmok14.fromtheshadows.entity.EndigoEntity;
import net.sonmok14.fromtheshadows.entity.NehemothEntity;
import net.sonmok14.fromtheshadows.utils.registry.ParticleRegistry;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EndigoRenderer extends GeoEntityRenderer<EndigoEntity> {
    private final RandomSource random = RandomSource.create();
    public EndigoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EndigoModel());
        this.addRenderLayer(new EndigoLayerRenderer(this));
        shadowRadius = 1f;
    }

    @Override  public Vec3 getRenderOffset(EndigoEntity p_114336_, float p_114337_) {

        if(p_114336_.squeakyProgress < 45 && p_114336_.isAlive()) {
            return new Vec3(this.random.nextGaussian() * 0.02D, 0.0D, this.random.nextGaussian() * 0.02D);
        }
        return super.getRenderOffset(p_114336_, p_114337_);
    }

    @Override
    public void postRender(PoseStack poseStack, EndigoEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        }
    @Override
    public void render(EndigoEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.scale(1f, 1f, 1f);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }
}
