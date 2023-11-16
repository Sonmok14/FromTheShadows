package net.sonmok14.fromtheshadows.client.renderer.items;

import net.sonmok14.fromtheshadows.client.models.items.DevilSplitterModel;
import net.sonmok14.fromtheshadows.server.items.DevilSplitterItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class DevilSplitterRenderer extends GeoItemRenderer<DevilSplitterItem> {
    public DevilSplitterRenderer() {
        super(new DevilSplitterModel());
    }


}
