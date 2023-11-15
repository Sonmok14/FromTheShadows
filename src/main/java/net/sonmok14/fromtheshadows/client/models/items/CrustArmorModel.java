package net.sonmok14.fromtheshadows.client.models.items;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.items.CrustArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class CrustArmorModel extends GeoModel<CrustArmorItem> {
    @Override
    public ResourceLocation getModelResource(CrustArmorItem object) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/crust_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CrustArmorItem object) {
        return new ResourceLocation(Fromtheshadows.MODID, "textures/armor/crust_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CrustArmorItem animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/diabolium_armor.animation.json");
    }
}

