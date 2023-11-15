package net.sonmok14.fromtheshadows.client.models.items;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.items.DiaboliumArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class DiaboliumArmorModel extends GeoModel<DiaboliumArmorItem> {
    @Override
    public ResourceLocation getModelResource(DiaboliumArmorItem object) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/diabolium_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DiaboliumArmorItem object) {
        return new ResourceLocation(Fromtheshadows.MODID, "textures/armor/diabolium_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DiaboliumArmorItem animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/diabolium_armor.animation.json");
    }
}

