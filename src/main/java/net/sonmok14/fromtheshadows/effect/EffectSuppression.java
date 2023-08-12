package net.sonmok14.fromtheshadows.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingSwapItemsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sonmok14.fromtheshadows.utils.registry.EffectRegistry;

public class EffectSuppression extends MobEffect {
    private int lastDuration = -1;
    public EffectSuppression(MobEffectCategory p_19451_, int p_19452_) {
        super(MobEffectCategory.HARMFUL, 0XA43CE7);
        MinecraftForge.EVENT_BUS.addListener(this::chill);
        MinecraftForge.EVENT_BUS.addListener(this::chill2);
    }

    @SubscribeEvent
    public void chill(LivingEntityUseItemEvent event) {
        LivingEntity e = event.getEntity();
        if (e.hasEffect(this) && !e.isUsingItem()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void chill2(LivingSwapItemsEvent event) {
        LivingEntity e = event.getEntity();
        if (e.hasEffect(this)) {
            event.setCanceled(true);
        }
    }
    @Override
    public void applyEffectTick(LivingEntity p_19467_, int p_19468_) {
        if(p_19467_.getHealth() > 1) {
            p_19467_.hurt(p_19467_.damageSources().magic(), 1);
        }
        super.applyEffectTick(p_19467_, p_19468_);
    }

    public boolean isDurationEffectTick(int duration, int amplifier) {
        lastDuration = duration;
        return duration > 0 && duration % 40 == 0;
    }
}
