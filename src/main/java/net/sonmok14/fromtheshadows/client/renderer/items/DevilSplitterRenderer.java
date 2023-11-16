package net.sonmok14.fromtheshadows.client.renderer.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.sonmok14.fromtheshadows.client.models.items.DevilSplitterModel;
import net.sonmok14.fromtheshadows.server.items.CrustArmorItem;
import net.sonmok14.fromtheshadows.server.items.DevilSplitterItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class DevilSplitterRenderer extends GeoItemRenderer<DevilSplitterItem> {
    public DevilSplitterRenderer() {
        super(new DevilSplitterModel());
    }

    @Override
    public RenderType getRenderType(DevilSplitterItem animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
