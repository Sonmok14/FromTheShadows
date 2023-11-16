package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.sonmok14.fromtheshadows.client.models.ClericModel;
import net.sonmok14.fromtheshadows.client.renderer.layer.ClericLayerRenderer;
import net.sonmok14.fromtheshadows.server.entity.ClericEntity;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

public class ClericRenderer extends GeoEntityRenderer<ClericEntity> {
    ClericEntity golem;
    MultiBufferSource bufferIn;
    ResourceLocation text;
    public ClericRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ClericModel());
        shadowRadius = 0.5f;
        this.addLayer(new ClericLayerRenderer(this));
    }
    @Override
    public void renderEarly(ClericEntity animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, partialTicks);
        this.golem = animatable;
        this.bufferIn = bufferSource;
        this.text = this.getTextureLocation(animatable);
    }

    @Override
    public void render(ClericEntity animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        if (bone.getName().equals("item")) {
            poseStack.pushPose();
            RenderUtils.translateToPivotPoint(poseStack, bone);
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(-180f));
            poseStack.translate(0, 0, -0.1f);
            poseStack.scale(1f, 1f, -1f);
            ItemStack itemstack = animatable.getMainHandItem();
            if(!itemstack.isEmpty()) {
                Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, 0);
            }
            poseStack.popPose();
            buffer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(text));
        }
    }
}
