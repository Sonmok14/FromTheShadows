package net.sonmok14.fromtheshadows.client.models;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.entity.projectiles.CoralThornEntity;
import software.bernie.geckolib.model.GeoModel;

public class CoralThornModel extends GeoModel<CoralThornEntity> {
    @Override
    public ResourceLocation getModelResource(CoralThornEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/coral_thorn.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CoralThornEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "textures/entity/coral_thorn_red.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CoralThornEntity animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/frogvomit.animation.json");
    }


}
