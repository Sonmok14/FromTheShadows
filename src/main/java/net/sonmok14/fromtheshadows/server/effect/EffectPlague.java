package net.sonmok14.fromtheshadows.server.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.sonmok14.fromtheshadows.server.utils.registry.EffectRegistry;

public class EffectPlague extends MobEffect {
    private int lastDuration = -1;
    public EffectPlague(MobEffectCategory p_19451_, int p_19452_) {
        super(MobEffectCategory.HARMFUL, 0X534D50);
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if(p_19467_.getHealth() > 1) {
           p_19467_.hurt(p_19467_.damageSources().magic(), 1);
        }
        for (LivingEntity livingentity : p_19467_.level().getEntitiesOfClass(LivingEntity.class, p_19467_.getBoundingBox().inflate(2D))) {
            if (livingentity != p_19467_) {
                if(!livingentity.hasEffect(EffectRegistry.PLAGUE.get()))
                livingentity.addEffect(new MobEffectInstance(EffectRegistry.PLAGUE.get(), 200), p_19467_);
            }
        }
        super.applyEffectTick(p_19467_, p_19468_);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        lastDuration = duration;
        return duration > 0 && duration % 40 == 0;
    }
}
