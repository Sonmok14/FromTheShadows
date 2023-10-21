package net.sonmok14.fromtheshadows.client.models.items;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.entity.UrnaxEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class UrnaxModel extends GeoModel<UrnaxEntity> {
    @Override
    public ResourceLocation getModelResource(UrnaxEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/urnax.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(UrnaxEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "textures/entity/urnax.png");
    }

    @Override
    public ResourceLocation getAnimationResource(UrnaxEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/urnax.animation.json");
    }
    @Override
    public void setCustomAnimations(UrnaxEntity animatable, long instanceId, AnimationState<UrnaxEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = this.getAnimationProcessor().getBone("neck");
        if (animationState == null)
            return;
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if (head != null) {
            head.setRotX(entityData.headPitch() * 0.010453292F);
            head.setRotX(-0.5f);
            head.setRotY(entityData.netHeadYaw() * 0.015453292F);
            head.updateScale(0.85f,0.85f,0.85f);
        }
    }
}
