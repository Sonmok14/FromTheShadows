package net.sonmok14.fromtheshadows.server.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EffectHealblock extends MobEffect {

    public EffectHealblock(MobEffectCategory p_19451_, int p_19452_) {
        super(MobEffectCategory.HARMFUL, 0X78828E);
        MinecraftForge.EVENT_BUS.addListener(this::chill);
    }

    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        super.applyEffectTick(p_19467_, p_19468_);
    }


    @SubscribeEvent
    public void chill(LivingHealEvent event) {
        LivingEntity e = event.getEntity();
        if (e.hasEffect(this)) {
            event.setCanceled(true);
        }
    }


    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }
}
