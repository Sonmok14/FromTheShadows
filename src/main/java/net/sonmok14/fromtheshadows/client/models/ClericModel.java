package net.sonmok14.fromtheshadows.client.models;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.entity.ClericEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ClericModel extends GeoModel<ClericEntity> {


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
    public void setCustomAnimations(ClericEntity animatable, long instanceId, AnimationState<ClericEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone arms = this.getAnimationProcessor().getBone("arms");
        CoreGeoBone right_arm = this.getAnimationProcessor().getBone("right_arm");
        CoreGeoBone left_arm = this.getAnimationProcessor().getBone("left_arm");
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
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        head.setRotX(entityData.headPitch() * 0.01F);
        head.setRotY(entityData.netHeadYaw() * 0.01F);
    }
}
