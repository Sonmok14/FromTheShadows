package net.sonmok14.fromtheshadows.client.renderer.items;

import net.sonmok14.fromtheshadows.client.models.items.CrustArmorModel;
import net.sonmok14.fromtheshadows.client.models.items.DiaboliumArmorModel;
import net.sonmok14.fromtheshadows.items.CrustArmorItem;
import net.sonmok14.fromtheshadows.items.DiaboliumArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class CrustArmorRenderer extends GeoArmorRenderer<CrustArmorItem> {

	public CrustArmorRenderer() {
		super(new CrustArmorModel());
	}



}
