package net.sonmok14.fromtheshadows.client.models;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.entity.FroglinEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FroglinModel extends GeoModel<FroglinEntity> {
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
    public void setCustomAnimations(FroglinEntity animatable, long instanceId, AnimationState<FroglinEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        head.setRotX(entityData.headPitch() * 0.01F);
        head.setRotY(entityData.netHeadYaw() * 0.01F);
        head.setRotX(0.5F);
        if(animatable.attackID == 2) {
            head.setRotX(1F);
        }
    }
}
