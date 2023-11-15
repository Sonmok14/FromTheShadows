package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.sonmok14.fromtheshadows.client.models.ClericModel;
import net.sonmok14.fromtheshadows.client.renderer.layer.ClericLayerRenderer;
import net.sonmok14.fromtheshadows.server.entity.ClericEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class ClericRenderer extends GeoEntityRenderer<ClericEntity> {
    ClericEntity golem;
    MultiBufferSource bufferIn;
    ResourceLocation text;
    public ClericRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ClericModel());
        shadowRadius = 0.5f;
        this.addRenderLayer(new ClericLayerRenderer(this));
    }

    @Override
    public void preRender(PoseStack poseStack, ClericEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.golem = animatable;
        this.bufferIn = bufferSource;
        this.text = this.getTextureLocation(animatable);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }


    @Override
    public void renderRecursively(PoseStack stack, ClericEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource multiBufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(stack, animatable, bone, renderType, bufferIn, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (bone.getName().equals("item")) {
            stack.pushPose();
            RenderUtils.translateToPivotPoint(stack, bone);
            stack.mulPose(Axis.XP.rotationDegrees(-180f));
            stack.translate(0, 0, -0.1f);
            stack.scale(1f, 1f, 1f);
            ItemStack itemstack = animatable.getMainHandItem();
            if(!itemstack.isEmpty()) {
                Minecraft.getInstance().getItemRenderer().renderStatic(itemstack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, stack, bufferIn, animatable.level(), 0);
            }
            stack.popPose();
            buffer = bufferIn.getBuffer(RenderType.entityCutoutNoCull(text));
        }
    }
}
