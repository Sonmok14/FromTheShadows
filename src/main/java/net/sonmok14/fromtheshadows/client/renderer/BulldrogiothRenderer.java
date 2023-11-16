package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.sonmok14.fromtheshadows.client.models.BulldrogiothModel;
import net.sonmok14.fromtheshadows.client.renderer.layer.BulldrogiothLayerRenderer;
import net.sonmok14.fromtheshadows.server.entity.BulldrogiothEntity;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class BulldrogiothRenderer extends GeoEntityRenderer<BulldrogiothEntity> {

    public BulldrogiothRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BulldrogiothModel());
        this.addLayer(new BulldrogiothLayerRenderer(this));
        shadowRadius = 1.7f;
    }

    @Override
    public void render(BulldrogiothEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.scale(1.7f, 1.7f, 1.7f);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }

    @Override
    protected float getDeathMaxRotation(BulldrogiothEntity animatable) {
        return 0;
    }

    @Override
    public void render(GeoModel model, BulldrogiothEntity animatable, float partialTick, RenderType type,
                       PoseStack poseStack, MultiBufferSource bufferSource, VertexConsumer buffer,
                       int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}


