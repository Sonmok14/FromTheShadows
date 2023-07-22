package net.sonmok14.fromtheshadows.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class FTSRenderType extends RenderType {
    public FTSRenderType(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
    }

    public static RenderType getGlowingEffect(ResourceLocation locationIn) {
        RenderStateShard.TextureStateShard renderstate$texturestate = new RenderStateShard.TextureStateShard(locationIn, false, false);
        return create("glow_effect", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, RenderType
                .CompositeState.builder()
                .setTextureState(renderstate$texturestate)
                .setShaderState(RENDERTYPE_BEACON_BEAM_SHADER)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL).setOverlayState(OVERLAY)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false));
    }

    public static RenderType getghost(ResourceLocation texture) {
        CompositeState renderState = CompositeState.builder()
                .setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER)
                .setCullState(NO_CULL)
                .setTextureState(new TextureStateShard(texture, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .setWriteMaskState(COLOR_DEPTH_WRITE)
                .setDepthTestState(LEQUAL_DEPTH_TEST)
                .setLayeringState(NO_LAYERING)
                .createCompositeState(false);
        return create("ghost", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, renderState);
    }

}
