package net.sonmok14.fromtheshadows.client.models;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.entity.BulldrogiothEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BulldrogiothModel extends AnimatedGeoModel<BulldrogiothEntity> {

    private static final ResourceLocation TEXTURE_0 = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/bulldrogioth.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation(Fromtheshadows.MODID, "textures/entity/bulldrogioth_swamp.png");

    @Override
    public ResourceLocation getModelResource(BulldrogiothEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/bulldrogioth.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BulldrogiothEntity animatable) {
        if(animatable.getVariant() == 2)
        {
            return new ResourceLocation(Fromtheshadows.MODID, "textures/entity/bulldrogioth_swamp.png");
        }
        if(animatable.getVariant() == 1)
        {
            return new ResourceLocation(Fromtheshadows.MODID, "textures/entity/bulldrogioth_blue.png");
        }

        return new ResourceLocation(Fromtheshadows.MODID, "textures/entity/bulldrogioth.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BulldrogiothEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/bulldrogioth.animation.json");
    }

    @Override
    public void setCustomAnimations(BulldrogiothEntity animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);

        IBone root = this.getAnimationProcessor().getBone("root");
        IBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraData.netHeadYaw * Mth.DEG_TO_RAD);
    }
}
