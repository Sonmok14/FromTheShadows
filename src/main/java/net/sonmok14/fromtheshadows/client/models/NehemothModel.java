package net.sonmok14.fromtheshadows.client.models;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.entity.NehemothEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class NehemothModel extends AnimatedGeoModel<NehemothEntity> {

    @Override
    public ResourceLocation getAnimationResource(NehemothEntity entity) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/nehemoth.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(NehemothEntity entity) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/nehemoth.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NehemothEntity entity) {
        if(entity.isStone())
        {
            return new ResourceLocation(Fromtheshadows.MODID, "textures/entity/nehemoth_stone.png");
        }
        if(entity.getVariant() == 1)
        {
            return new ResourceLocation(Fromtheshadows.MODID, "textures/entity/soul_retexture.png");
        }

        return new ResourceLocation(Fromtheshadows.MODID, "textures/entity/nehemoth_retexture.png");
    }

    @Override
    public void setCustomAnimations(NehemothEntity animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        IBone head = this.getAnimationProcessor().getBone("headrotate");
        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
        head.setRotationX(-1F);
    }
}
