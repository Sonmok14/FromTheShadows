package net.sonmok14.fromtheshadows.client.models;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.entity.EndigoEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class EndigoModel extends GeoModel<EndigoEntity> {
    @Override
    public ResourceLocation getModelResource(EndigoEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/endigo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EndigoEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "textures/entity/endigo.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EndigoEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/endigo.animation.json");
    }

    @Override
    public void setCustomAnimations(EndigoEntity animatable, long instanceId, AnimationState<EndigoEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        head.setRotX(entityData.headPitch() * 0.01F);
        head.setRotY(entityData.netHeadYaw() * 0.01F);
    }
}
