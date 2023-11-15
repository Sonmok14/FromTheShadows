package net.sonmok14.fromtheshadows.client.renderer.items;

import net.sonmok14.fromtheshadows.client.models.items.CrustArmorModel;
import net.sonmok14.fromtheshadows.server.items.CrustArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class CrustArmorRenderer extends GeoArmorRenderer<CrustArmorItem> {

	public CrustArmorRenderer() {
		super(new CrustArmorModel());
	}



}
