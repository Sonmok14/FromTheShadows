package net.sonmok14.fromtheshadows.server.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.sonmok14.fromtheshadows.server.utils.registry.DamageRegistry;
import net.sonmok14.fromtheshadows.server.utils.registry.ParticleRegistry;

public class EffectHemorrhage extends MobEffect {
    private int lastDuration = -1;
    public EffectHemorrhage(MobEffectCategory p_19451_, int p_19452_) {
        super(MobEffectCategory.HARMFUL, 0XCA2D2D);
    }



    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if(p_19467_.isSprinting() || p_19467_.isUsingItem() || p_19467_.getSpeed() >= 0.25 || p_19467_.swinging) {
            p_19467_.hurt(DamageRegistry.causeBleedingDamage(p_19467_), Math.min(p_19468_ + 1, Math.round(lastDuration / 20F)));
            Vec3 vec3 = p_19467_.getBoundingBox().getCenter();
            for(int i = 0; i < 40; ++i) {
                double d0 = p_19467_.getRandom().nextGaussian() * 3D;
                double d1 = p_19467_.getRandom().nextGaussian() * 3D;
                double d2 = p_19467_.getRandom().nextGaussian() * 3D;
                p_19467_.level().addParticle(ParticleRegistry.BLOOD.get(), vec3.x, vec3.y, vec3.z, d0, d1, d2);
            }

        }
        super.applyEffectTick(p_19467_, p_19468_);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        lastDuration = duration;
        return duration > 0 && duration % 40 == 0;
    }

}
