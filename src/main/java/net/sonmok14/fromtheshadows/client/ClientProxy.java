package net.sonmok14.fromtheshadows.client;


import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sonmok14.fromtheshadows.client.sound.NehemothSound;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.client.particle.BloodParticle;
import net.sonmok14.fromtheshadows.client.renderer.*;
import net.sonmok14.fromtheshadows.server.CommonProxy;
import net.sonmok14.fromtheshadows.server.entity.NehemothEntity;
import net.sonmok14.fromtheshadows.server.utils.registry.EntityRegistry;
import net.sonmok14.fromtheshadows.server.utils.registry.ItemRegistry;
import net.sonmok14.fromtheshadows.server.utils.registry.ParticleRegistry;

import javax.annotation.Nullable;


public class ClientProxy extends CommonProxy {
    public static final Int2ObjectMap<AbstractTickableSoundInstance> ENTITY_SOUND_INSTANCE_MAP = new Int2ObjectOpenHashMap<>();
    public void commonInit() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::registry);
    }


    public void clientInit() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        EntityRenderers.register(EntityRegistry.THROWING_DAGGER.get(), ThrowingDaggerRenderer::new);
        EntityRenderers.register(EntityRegistry.CORAL_THORN.get(), CoralThornRenderer::new);
        EntityRenderers.register(EntityRegistry.BULLDROGIOTH.get(), BulldrogiothRenderer::new);
        EntityRenderers.register(EntityRegistry.FROGLIN_VOMIT.get(), FroglinVomitRenderer::new);
        EntityRenderers.register(EntityRegistry.FROGLIN.get(), FroglinRenderer::new);
        EntityRenderers.register(EntityRegistry.DOOM_BREATH.get(), DoomBreathRenderer::new);
        EntityRenderers.register(EntityRegistry.PLAYER_BREATH.get(), PlayerBreathRenderer::new);
        EntityRenderers.register(EntityRegistry.CLERIC.get(), ClericRenderer::new);
        EntityRenderers.register(EntityRegistry.NEHEMOTH.get(), NehemothRenderer::new);
        EntityRenderers.register(EntityRegistry.SCREEN_SHAKE.get(), RendererNull::new);
        EntityRenderers.register(EntityRegistry.FALLING_BLOCK.get(), FallingBlockRenderer::new);
        try {
            ItemProperties.register(ItemRegistry.THIRST_FOR_BLOOD.get(), new ResourceLocation("using"), (stack, p_2394211, p_2394212, j) -> p_2394212 != null && p_2394212.isUsingItem() && p_2394212.getUseItem() == stack ? 1.0F : 0.0F);
        } catch (Exception e) {
            Fromtheshadows.LOGGER.warn("Could not load item models for weapons");

        }
    }

    public void registry(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.BLOOD.get(), BloodParticle.Provider::new);
    }

    @Override
    public void playWorldSound(@Nullable Object soundEmitter, byte type) {
        if (soundEmitter instanceof Entity entity && !entity.level().isClientSide) {
            return;
        }
        switch (type) {
            case 1:
                if (soundEmitter instanceof NehemothEntity nehemoth) {
                    NehemothSound sound;
                    AbstractTickableSoundInstance old = ENTITY_SOUND_INSTANCE_MAP.get(nehemoth.getId());
                    if (old == null || !(old instanceof NehemothSound nehemothSound && nehemothSound.isSameEntity(nehemoth))) {
                        sound = new NehemothSound(nehemoth);
                        ENTITY_SOUND_INSTANCE_MAP.put(nehemoth.getId(), sound);
                    } else {
                        sound = (NehemothSound) old;
                    }
                    if (!Minecraft.getInstance().getSoundManager().isActive(sound) && sound.canPlaySound()) {
                        Minecraft.getInstance().getSoundManager().queueTickingSound(sound);
                    }
                }
                break;
        }
    }


    public void clearSoundCacheFor(Entity entity) {
        ENTITY_SOUND_INSTANCE_MAP.remove(entity.getId());
    }
}
