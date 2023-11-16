package net.sonmok14.fromtheshadows.client.renderer.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.client.models.items.CrustArmorModel;
import net.sonmok14.fromtheshadows.server.entity.projectiles.CoralThornEntity;
import net.sonmok14.fromtheshadows.server.items.CrustArmorItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class CrustArmorRenderer extends GeoArmorRenderer<CrustArmorItem> {

	public CrustArmorRenderer() {
		super(new CrustArmorModel());
		this.headBone = "armorHead";
		this.bodyBone = "armorBody";
		this.rightArmBone = "armorRightArm";
		this.leftArmBone = "armorLeftArm";
		this.rightLegBone = "armorRightLeg";
		this.leftLegBone = "armorLeftLeg";
		this.rightBootBone = "armorRightBoot";
		this.leftBootBone = "armorLeftBoot";
	}


}
