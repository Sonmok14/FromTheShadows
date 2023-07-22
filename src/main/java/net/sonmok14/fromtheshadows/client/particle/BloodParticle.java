package net.sonmok14.fromtheshadows.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BloodParticle extends TextureSheetParticle {
    @SuppressWarnings("unused")
    private boolean reachedGround;
    private final SpriteSet spriteProvider;
    BloodParticle(ClientLevel p_108484_, double p_108485_, double p_108486_, double p_108487_ , SpriteSet spriteProvider) {
        super(p_108484_, p_108485_, p_108486_, p_108487_, 0.0D, 0.0D, 0.0D);
        this.gravity = 0.06F;
        this.friction = 0.999F;
        this.xd *= (double)0.8F;
        this.yd = Math.random() * (double)0.2F + (double)0.1F;
        this.zd *= (double)0.8F;
        var red = 170 / 255.0f;
        var green = 0 / 255.0f;
        var blue = 0 / 255.0f;
        var colorRed = Mth.nextFloat(random, red - 0.05f, red + 0.05f);
        setColor(colorRed, green, blue);
        this.yd = (double)(this.random.nextFloat() * 0.4F + 0.05F);
        this.quadSize *= this.random.nextFloat() * 2.0F + 0.2F;
        this.lifetime = (int)(50.0D / (Math.random() * 0.8D + 0.2D));
        hasPhysics = true;
        this.spriteProvider = spriteProvider;
        setSpriteFromAge(spriteProvider);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public float getQuadSize(float p_107089_) {
        float f = ((float)this.age + p_107089_) / (float)this.lifetime;
        return this.quadSize * (1.0F - f * f);
    }


    public void tick() {
        xo = x;
        yo = y;
        zo = z;

        if (age++ >= lifetime)
            remove();
        else {
            this.yd -= (double)this.gravity;
            setSpriteFromAge(spriteProvider);
            if (onGround) {
                yd = 0.0;
                reachedGround = true;
            }
            this.move(xd, yd, zd);
            if (y == yo) {
                xd *= 1.1;
                zd *= 1.1;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet p_108492_) {
            this.sprite = p_108492_;
        }

        public Particle createParticle(SimpleParticleType p_108503_, ClientLevel p_108504_, double p_108505_, double p_108506_, double p_108507_, double p_108508_, double p_108509_, double p_108510_) {
            BloodParticle waterdropparticle = new BloodParticle(p_108504_, p_108505_, p_108506_, p_108507_, sprite);
            waterdropparticle.pickSprite(this.sprite);
            return waterdropparticle;
        }
    }
    }
