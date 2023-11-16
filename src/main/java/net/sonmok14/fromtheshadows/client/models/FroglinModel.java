package net.sonmok14.fromtheshadows.client.models;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.entity.FroglinEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class FroglinModel extends AnimatedGeoModel<FroglinEntity> {
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/frog.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/frog_red.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/frog_gray.png");
    @Override
    public ResourceLocation getModelResource(FroglinEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/frog.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FroglinEntity animatable) {
        return animatable.getVariant() == 0 ? TEXTURE_0 : animatable.getVariant() == 1 ? TEXTURE_1 : TEXTURE_2;
    }

    @Override
    public ResourceLocation getAnimationResource(FroglinEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/frog.animation.json");
    }

    @Override
    public void setCustomAnimations(FroglinEntity animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        IBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * Mth.DEG_TO_RAD);
        head.setRotationX(0.5F);
        if(animatable.attackID == 2) {
            head.setRotationX(1F);
        }
    }
}
