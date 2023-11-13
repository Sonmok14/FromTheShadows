package net.sonmok14.fromtheshadows.client.particle;


import com.github.alexthe666.citadel.client.render.LightningBoltData;
import com.github.alexthe666.citadel.client.render.LightningRender;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector4f;

import java.util.Random;

public class LightningParticle extends Particle {
    private float toX;
    private float toY;
    private float toZ;
    private LightningRender lightningRender = new LightningRender();

    public LightningParticle(ClientLevel world, double x, double y, double z, float xSpeed, float ySpeed, float zSpeed) {
        super(world, x, y, z);
        this.setSize(1, 3);
        this.gravity = 0.0F;
        this.lifetime = 4 + new Random().nextInt(2);
        this.toX = xSpeed;
        this.toY = ySpeed;
        this.toZ = zSpeed;

    }
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        PoseStack posestack = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        float f = (float)(Mth.lerp((double)partialTick, this.xo, this.x) - vec3.x());
        float f1 = (float)(Mth.lerp((double)partialTick, this.yo, this.y) - vec3.y());
        float f2 = (float)(Mth.lerp((double)partialTick, this.zo, this.z) - vec3.z());
        float lerpAge = this.age + partialTick;
        float ageProgress = lerpAge / (float) this.lifetime;
        float scale = 5F;
        float scale1 = 0.35F;
        posestack.pushPose();
        posestack.translate(f, f1, f2);
        posestack.scale(scale1, scale1, scale1);
        LightningBoltData.BoltRenderInfo lightningBoltData = new LightningBoltData.BoltRenderInfo(0.1F, 0.8F, 0.2F, 0.85F, new Vector4f(0.49F, 0.47F, 0.85F, (1.0F - ageProgress) * 0.7F), 0.1F);
        LightningBoltData bolt = new LightningBoltData(lightningBoltData, Vec3.ZERO, new Vec3(toX, toY, toZ), 12)
                .size(0.03F + random.nextFloat() * 0.2F)
                .lifespan(this.lifetime)
                .spawn(LightningBoltData.SpawnFunction.CONSECUTIVE);
        lightningRender.update(this, bolt, partialTick);
        lightningRender.render(partialTick, posestack, multibuffersource$buffersource);

        multibuffersource$buffersource.endBatch();
        posestack.popPose();
    }
    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new LightningParticle(worldIn, x, y, z, (float)xSpeed, (float)ySpeed, (float)zSpeed);
        }
    }
}
