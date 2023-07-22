package net.sonmok14.fromtheshadows.client.models.items;

import net.minecraft.world.item.ItemStack;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.items.ThirstforBloodItem;


import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class ThirstforBloodModel extends GeoModel<ThirstforBloodItem> {
    @Override
    public ResourceLocation getModelResource(ThirstforBloodItem object) {
        return new ResourceLocation(Fromtheshadows.MODID, "geo/item/thirst_for_blood.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ThirstforBloodItem object) {
        return new ResourceLocation(Fromtheshadows.MODID, "textures/item/thirst_for_blood.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ThirstforBloodItem animatable) {
        return new ResourceLocation(Fromtheshadows.MODID, "animations/thirst_for_blood.animation.json");
    }



}
