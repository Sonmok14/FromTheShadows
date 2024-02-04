package net.sonmok14.fromtheshadows.client.models.items;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.items.DiaboliumArmorItem;
import net.sonmok14.fromtheshadows.server.items.PolarExplorerArmorItem;
import software.bernie.geckolib.model.GeoModel;

public class PolarExplorerArmorModel extends GeoModel<PolarExplorerArmorItem> {
    @Override
    public ResourceLocation getModelResource(PolarExplorerArmorItem object) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/polar_explorer_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PolarExplorerArmorItem object) {
        return new ResourceLocation(Fromtheshadows.MODID, "textures/armor/polar_explorer_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PolarExplorerArmorItem animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/diabolium_armor.animation.json");
    }
}

