package net.sonmok14.fromtheshadows.client.models;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.entity.projectiles.ThrowingDaggerEntity;
import software.bernie.geckolib.model.GeoModel;

public class ThrowingDaggerModel extends GeoModel<ThrowingDaggerEntity> {
    @Override
    public ResourceLocation getModelResource(ThrowingDaggerEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/throwing_dagger.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ThrowingDaggerEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "textures/entity/throwing_dagger.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ThrowingDaggerEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/frogvomit.animation.json");
    }


}
