package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.sonmok14.fromtheshadows.client.models.UrnaxModel;
import net.sonmok14.fromtheshadows.client.renderer.layer.UrnaxLayerRenderer;
import net.sonmok14.fromtheshadows.entity.UrnaxEntity;
import net.sonmok14.fromtheshadows.utils.registry.ParticleRegistry;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class UrnaxRenderer extends GeoEntityRenderer<UrnaxEntity> {
    public UrnaxRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new UrnaxModel());
        this.addRenderLayer(new UrnaxLayerRenderer(this));
        shadowRadius = 1f;
    }
    @Override
    public void postRender(PoseStack poseStack, UrnaxEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if(animatable.isAlive()) {
            if (model.getBone("root").isPresent()) {
                if (animatable.growlProgress == 29 || (animatable.attacktick < 4 && animatable.attackID == 2)) {
                    int numClouds = (int) Math.floor(1 * 1);
                    for (int i = 0; i < numClouds; i++) {
                        animatable.getCommandSenderWorld().addParticle(ParticleRegistry.LIGHTNING.get(), model.getBone("root").get().getWorldPosition().x,
                                model.getBone("root").get().getWorldPosition().y,
                                model.getBone("root").get().getWorldPosition().z, animatable.getRandom().nextGaussian() * 2D, animatable.getRandom().nextGaussian() * 2D, animatable.getRandom().nextGaussian() * 2D);
                    }
                }
            }
            if (model.getBone("right_hand").isPresent()) {
                if (animatable.clenchProgress == 5 || (animatable.attacktick > 15 && animatable.attackID == 2)) {
                    int numClouds = (int) Math.floor(1 * 1);
                    for (int i = 0; i < numClouds; i++) {
                        animatable.getCommandSenderWorld().addParticle(ParticleRegistry.LIGHTNING.get(), model.getBone("right_hand").get().getWorldPosition().x,
                                model.getBone("right_hand").get().getWorldPosition().y,
                                model.getBone("right_hand").get().getWorldPosition().z, animatable.getRandom().nextGaussian() * 0.8D, animatable.getRandom().nextGaussian() * 0.8D, animatable.getRandom().nextGaussian() * 0.8D);
                    }
                }
            }
            if (model.getBone("left_hand").isPresent()) {
                if (animatable.clenchProgress == 5) {
                    int numClouds = (int) Math.floor(1 * 1);
                    for (int i = 0; i < numClouds; i++) {
                        animatable.getCommandSenderWorld().addParticle(ParticleRegistry.LIGHTNING.get(), model.getBone("left_hand").get().getWorldPosition().x,
                                model.getBone("left_hand").get().getWorldPosition().y,
                                model.getBone("left_hand").get().getWorldPosition().z, animatable.getRandom().nextGaussian() * 0.8D, animatable.getRandom().nextGaussian() * 0.8D, animatable.getRandom().nextGaussian() * 0.8D);
                    }
                }
            }
        }
        }
    @Override
    public void render(UrnaxEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.scale(1f, 1f, 1f);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }

    @Override
    protected int getBlockLightLevel(UrnaxEntity p_114496_, BlockPos p_114497_) {
        return 7;
    }

}
