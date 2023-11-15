package net.sonmok14.fromtheshadows.client.renderer.items;

import net.sonmok14.fromtheshadows.client.models.items.ThirstforBloodModel;
import net.sonmok14.fromtheshadows.server.items.ThirstforBloodItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ThirstforBloodRenderer extends GeoItemRenderer<ThirstforBloodItem> {
    public ThirstforBloodRenderer() {
        super(new ThirstforBloodModel());
    }


}
