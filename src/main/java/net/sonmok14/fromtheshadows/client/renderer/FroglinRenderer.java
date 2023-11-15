package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.sonmok14.fromtheshadows.client.models.FroglinModel;
import net.sonmok14.fromtheshadows.server.entity.FroglinEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FroglinRenderer extends GeoEntityRenderer<FroglinEntity> {

    public FroglinRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new FroglinModel());
        shadowRadius = 0.8f;
    }

    @Override
    public void postRender(PoseStack poseStack, FroglinEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (animatable.isFull()) {

            if (model.getBone("cuberotate").isPresent()) {
                if (animatable.tickCount % 70 == 0) {
                    animatable.getCommandSenderWorld().addParticle(ParticleTypes.FALLING_WATER,
                            model.getBone("cuberotate").get().getWorldPosition().x,
                            model.getBone("cuberotate").get().getWorldPosition().y,
                            model.getBone("cuberotate").get().getWorldPosition().z, 0,
                            0, 0);
                }


                if (animatable.tickCount % 120 == 0) {
                    animatable.getCommandSenderWorld().addParticle(ParticleTypes.FALLING_DRIPSTONE_WATER,
                            model.getBone("cuberotate").get().getWorldPosition().x,
                            model.getBone("cuberotate").get().getWorldPosition().y,
                            model.getBone("cuberotate").get().getWorldPosition().z, 0,
                            0, 0);
                }
                if (animatable.tickCount % 140 == 0) {
                    animatable.getCommandSenderWorld().addParticle(ParticleTypes.RAIN,
                            model.getBone("cuberotate").get().getWorldPosition().x,
                            model.getBone("cuberotate").get().getWorldPosition().y,
                            model.getBone("cuberotate").get().getWorldPosition().z, 0,
                            0, 0);
                }
            }
        }

        }
    }


