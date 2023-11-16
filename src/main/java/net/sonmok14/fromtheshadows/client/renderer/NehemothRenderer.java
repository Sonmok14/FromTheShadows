package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.client.models.NehemothModel;
import net.sonmok14.fromtheshadows.client.renderer.layer.NehemothLayerRenderer;
import net.sonmok14.fromtheshadows.server.entity.NehemothEntity;
import net.sonmok14.fromtheshadows.server.utils.registry.ParticleRegistry;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class NehemothRenderer extends GeoEntityRenderer<NehemothEntity> {
    public NehemothRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NehemothModel());
        this.addLayer(new NehemothLayerRenderer(this));
        shadowRadius = 1.5f;
    }
    @Override
    public RenderType getRenderType(NehemothEntity animatable, float partialTick, PoseStack poseStack,
                                    MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight,
                                    ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void render(NehemothEntity animatable, float entityYaw, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight) {
        stack.pushPose();
        stack.scale(1.2f, 1.2f, 1.2f);
        super.render(animatable, entityYaw, partialTick, stack, bufferSource, packedLight);
        stack.popPose();
    }
}

