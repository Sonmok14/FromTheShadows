package net.sonmok14.fromtheshadows.client.models;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.entity.ClericEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ClericModel extends AnimatedGeoModel<ClericEntity> {


    @Override
    public ResourceLocation getModelResource(ClericEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/cultist.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ClericEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "textures/entity/cultist.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ClericEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/cultist.animation.json");
    }
    @Override
    public void setCustomAnimations(ClericEntity animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        IBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);


        IBone arms = this.getAnimationProcessor().getBone("arms");
        IBone right_arm = this.getAnimationProcessor().getBone("right_arm");
        IBone left_arm = this.getAnimationProcessor().getBone("left_arm");
        if(animatable.isAggressive() || animatable.attackID != 0)
        {
            arms.setHidden(true);
            right_arm.setHidden(false);
            left_arm.setHidden(false);
        }
        else {
            arms.setHidden(false);
            right_arm.setHidden(true);
            left_arm.setHidden(true);
        }
    }
}
