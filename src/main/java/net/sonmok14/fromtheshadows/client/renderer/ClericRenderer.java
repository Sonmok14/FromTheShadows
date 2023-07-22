package net.sonmok14.fromtheshadows.client.renderer;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.sonmok14.fromtheshadows.client.models.ClericModel;
import net.sonmok14.fromtheshadows.entity.ClericEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ClericRenderer extends GeoEntityRenderer<ClericEntity> {
    public ClericRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ClericModel());
        shadowRadius = 1f;
    }
}
