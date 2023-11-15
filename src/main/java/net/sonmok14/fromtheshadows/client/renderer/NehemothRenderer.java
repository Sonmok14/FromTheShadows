package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.sonmok14.fromtheshadows.client.models.NehemothModel;
import net.sonmok14.fromtheshadows.client.renderer.layer.NehemothLayerRenderer;
import net.sonmok14.fromtheshadows.server.entity.NehemothEntity;
import net.sonmok14.fromtheshadows.server.utils.registry.ParticleRegistry;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NehemothRenderer extends GeoEntityRenderer<NehemothEntity> {
    public NehemothRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NehemothModel());
        this.addRenderLayer(new NehemothLayerRenderer(this));

        shadowRadius = 1.5f;
    }
    @Override
    protected float getDeathMaxRotation(NehemothEntity entityLivingBaseIn) {
        return 90;
    }

 @Override
   public void postRender(PoseStack poseStack, NehemothEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
       super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
      if(!animatable.isStone())
     {
         if (model.getBone("rotate").isPresent()) {
             if (animatable.attackID == 3 && animatable.attacktick == 17) {

                 for(int i = 0; i < 4; ++i) {
                     animatable.getCommandSenderWorld().addParticle(ParticleRegistry.BLOOD.get(), model.getBone("rotate").get().getWorldPosition().x,
                             model.getBone("rotate").get().getWorldPosition().y,
                             model.getBone("rotate").get().getWorldPosition().z,  animatable.getRandom().nextGaussian() * 3D,  animatable.getRandom().nextGaussian() * 3D,  animatable.getRandom().nextGaussian() * 3D);

                 }

             }

         }
        if (model.getBone("rotate3").isPresent() && animatable.getVariant() == 0) {
             if (animatable.tickCount % 70 == 0) {
               animatable.getCommandSenderWorld().addParticle(ParticleTypes.FALLING_WATER,
                      model.getBone("rotate3").get().getWorldPosition().x,
                   model.getBone("rotate3").get().getWorldPosition().y,
                       model.getBone("rotate3").get().getWorldPosition().z, 0,
                      0, 0);
          }

           if (animatable.tickCount % 120 == 0) {
            animatable.getCommandSenderWorld().addParticle(ParticleTypes.FALLING_DRIPSTONE_WATER,
                      model.getBone("rotate3").get().getWorldPosition().x,
                      model.getBone("rotate3").get().getWorldPosition().y,
                       model.getBone("rotate3").get().getWorldPosition().z, 0,
                   0, 0);
     }
     if (animatable.tickCount % 140 == 0) {
              animatable.getCommandSenderWorld().addParticle(ParticleTypes.RAIN,
                  model.getBone("rotate3").get().getWorldPosition().x,
                  model.getBone("rotate3").get().getWorldPosition().y,
                   model.getBone("rotate3").get().getWorldPosition().z, 0,
                    0, 0);
         }
       }
     }
     if (animatable.attackID == 1 || (animatable.attackID == 5 && !animatable.onGround())) {
      if (model.getBone("right_finger_part").isPresent()) {
          int flameDensity = 1;
       float flameRandomness = 0.15f;
           int numClouds = (int) Math.floor(1 * 1);
      for (int i = 0; i < numClouds; i++) {
         double x = model.getBone("right_finger_part").get().getWorldPosition().x + i * (model.getBone("right_finger_part").get().getWorldPosition().x) / numClouds;
             double y = model.getBone("right_finger_part").get().getWorldPosition().y + i * (model.getBone("right_finger_part").get().getWorldPosition().y) / numClouds;
            double z = model.getBone("right_finger_part").get().getWorldPosition().z + i * (model.getBone("right_finger_part").get().getWorldPosition().z) / numClouds;
           for (int j = 0; j < flameDensity; j++) {
            float xOffset = flameRandomness * (0.2f * animatable.getRandom().nextFloat() - 1);
           float yOffset = flameRandomness * (0.2f * animatable.getRandom().nextFloat() - 1);
          float zOffset = flameRandomness * (0.2f * animatable.getRandom().nextFloat() - 1);
           if(animatable.getVariant() == 1) {
                animatable.getCommandSenderWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME, x + xOffset, y + yOffset, z + zOffset, 0, 0, 0);
           }else
            {
                     animatable.getCommandSenderWorld().addParticle(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0, 0, 0);
                  }
            }
            }
   }
      if (model.getBone("left_finger_part").isPresent()) {
       int flameDensity = 1;
           float flameRandomness = 0.15f;
          int numClouds = (int) Math.floor(1 * 1);
        for (int i = 0; i < numClouds; i++) {
           double x = model.getBone("left_finger_part").get().getWorldPosition().x + i * (model.getBone("left_finger_part").get().getWorldPosition().x) / numClouds;
            double y = model.getBone("left_finger_part").get().getWorldPosition().y + i * (model.getBone("left_finger_part").get().getWorldPosition().y) / numClouds;
            double z = model.getBone("left_finger_part").get().getWorldPosition().z + i * (model.getBone("left_finger_part").get().getWorldPosition().z) / numClouds;
        for (int j = 0; j < flameDensity; j++) {
               float xOffset = flameRandomness * (0.2f * animatable.getRandom().nextFloat() - 0.6f);
              float yOffset = flameRandomness * (0.5f * animatable.getRandom().nextFloat() - 0.6f);
       float zOffset = flameRandomness * (0.2f * animatable.getRandom().nextFloat() - 0.6f);
               if(animatable.getVariant() == 1) {
                    animatable.getCommandSenderWorld().addParticle(ParticleTypes.SOUL_FIRE_FLAME, x + xOffset, y + yOffset, z + zOffset, 0, 0, 0);
                }else
                 {
                      animatable.getCommandSenderWorld().addParticle(ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset, 0, 0, 0);
                    }
              }
         }
           }
     }
   }

    @Override
    public void render(NehemothEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.scale(1.2f, 1.2f, 1.2f);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }
}

