package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.sonmok14.fromtheshadows.client.models.FroglinVomitModel;
import net.sonmok14.fromtheshadows.server.entity.projectiles.FrogVomit;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

public class FroglinVomitRenderer extends GeoProjectilesRenderer<FrogVomit> {

    public FroglinVomitRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new FroglinVomitModel());
    }
    @Override
    public void render(FrogVomit entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.scale(2, 2, 2);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }
}

