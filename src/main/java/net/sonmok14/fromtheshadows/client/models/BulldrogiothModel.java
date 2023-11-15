package net.sonmok14.fromtheshadows.client.models;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.entity.BulldrogiothEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BulldrogiothModel extends GeoModel<BulldrogiothEntity> {

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
    public void setCustomAnimations(BulldrogiothEntity animatable, long instanceId, AnimationState<BulldrogiothEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone root = this.getAnimationProcessor().getBone("root");
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        head.setRotX(entityData.headPitch() * 0.01F);
        head.setRotY(entityData.netHeadYaw() * 0.01F);
    }
}
