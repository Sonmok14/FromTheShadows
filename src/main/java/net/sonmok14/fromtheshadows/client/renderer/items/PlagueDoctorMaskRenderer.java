package net.sonmok14.fromtheshadows.client.renderer.items;

import net.sonmok14.fromtheshadows.client.models.items.PlagueDoctorMaskModel;
import net.sonmok14.fromtheshadows.server.items.PlagueDoctorMaskItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class PlagueDoctorMaskRenderer extends GeoArmorRenderer<PlagueDoctorMaskItem> {

	public PlagueDoctorMaskRenderer() {
		super(new PlagueDoctorMaskModel());

		this.headBone = "armorHead";
		this.bodyBone = null;
		this.rightArmBone = null;
		this.leftArmBone = null;
		this.rightLegBone = null;
		this.leftLegBone = null;
		this.rightBootBone = null;
		this.leftBootBone = null;
	}



}
