package net.sonmok14.fromtheshadows.client.models.items;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.items.PlagueDoctorMaskItem;
import software.bernie.geckolib.model.GeoModel;

public class PlagueDoctorMaskModel extends GeoModel<PlagueDoctorMaskItem> {
    @Override
    public ResourceLocation getModelResource(PlagueDoctorMaskItem object) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/plague.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PlagueDoctorMaskItem object) {
        return new ResourceLocation(Fromtheshadows.MODID, "textures/armor/plague.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PlagueDoctorMaskItem animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/diabolium_armor.animation.json");
    }
}

