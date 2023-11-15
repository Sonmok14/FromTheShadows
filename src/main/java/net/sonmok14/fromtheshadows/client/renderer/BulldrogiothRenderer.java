package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.sonmok14.fromtheshadows.client.models.BulldrogiothModel;
import net.sonmok14.fromtheshadows.client.renderer.layer.BulldrogiothLayerRenderer;
import net.sonmok14.fromtheshadows.server.entity.BulldrogiothEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BulldrogiothRenderer extends GeoEntityRenderer<BulldrogiothEntity> {

    public BulldrogiothRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BulldrogiothModel());
        this.addRenderLayer(new BulldrogiothLayerRenderer(this));
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
    public void postRender(PoseStack poseStack, BulldrogiothEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        BlockState block = animatable.level().getBlockState(animatable.blockPosition().below());
        if (model.getBone("left_arm_particlepivot").isPresent()) {
            if (animatable.attackID == 1 && animatable.attacktick == 32 && !animatable.isInWater()) {

                for(int i = 0; i < 5; ++i) {
                    animatable.getCommandSenderWorld().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), model.getBone("left_arm_particlepivot").get().getWorldPosition().x,
                            model.getBone("left_arm_particlepivot").get().getWorldPosition().y,
                            model.getBone("left_arm_particlepivot").get().getWorldPosition().z,  animatable.getRandom().nextGaussian() * 0.5D,  animatable.getRandom().nextGaussian() * 0.5D,  animatable.getRandom().nextGaussian() * 0.5D);
                }
            }
        }
        if (model.getBone("right_arm_particlepivot").isPresent()) {
            if (animatable.attackID == 1 && animatable.attacktick == 32) {

                for(int i = 0; i < 5; ++i) {
                    animatable.getCommandSenderWorld().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), model.getBone("right_arm_particlepivot").get().getWorldPosition().x,
                            model.getBone("right_arm_particlepivot").get().getWorldPosition().y,
                            model.getBone("right_arm_particlepivot").get().getWorldPosition().z,  animatable.getRandom().nextGaussian() * 0.5D,  animatable.getRandom().nextGaussian() * 0.5D,  animatable.getRandom().nextGaussian() * 0.5D);
                }
            }
        }
    }
}


