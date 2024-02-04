package net.sonmok14.fromtheshadows.client.renderer.items;

import net.sonmok14.fromtheshadows.client.models.items.PolarExplorerArmorModel;
import net.sonmok14.fromtheshadows.server.items.PolarExplorerArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class PolarExplorerArmorRenderer extends GeoArmorRenderer<PolarExplorerArmorItem> {

	public PolarExplorerArmorRenderer() {
		super(new PolarExplorerArmorModel());
	}



}
