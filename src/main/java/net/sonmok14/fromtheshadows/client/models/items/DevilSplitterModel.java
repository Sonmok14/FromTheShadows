package net.sonmok14.fromtheshadows.client.models.items;

import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.items.DevilSplitterItem;
import net.sonmok14.fromtheshadows.server.items.ThirstforBloodItem;
import software.bernie.geckolib.model.GeoModel;

public class DevilSplitterModel extends GeoModel<DevilSplitterItem> {
    @Override
    public ResourceLocation getModelResource(DevilSplitterItem object) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/item/devil_splitter.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DevilSplitterItem object) {
        return new ResourceLocation(Fromtheshadows.MODID, "textures/item/devil_splitter.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DevilSplitterItem animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/thirst_for_blood.animation.json");
    }



}
