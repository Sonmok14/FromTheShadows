package net.sonmok14.fromtheshadows.client.models;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.entity.projectiles.FrogVomit;
import software.bernie.geckolib.model.GeoModel;

public class FroglinVomitModel extends GeoModel<FrogVomit> {
    @Override
    public ResourceLocation getModelResource(FrogVomit animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/frogvomit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FrogVomit animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "textures/entity/vomit.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FrogVomit animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/frogvomit.animation.json");
    }


}
