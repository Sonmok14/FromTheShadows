package net.sonmok14.fromtheshadows.utils.registry;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.client.particle.BloodParticle;

@Mod.EventBusSubscriber(modid = Fromtheshadows.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister
            .create(ForgeRegistries.PARTICLE_TYPES, Fromtheshadows.MODID);

    public static final RegistryObject<SimpleParticleType> BLOOD = PARTICLES.register("blood",
            () -> new SimpleParticleType(false));
    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void registry(RegisterParticleProvidersEvent event) {
        Minecraft.getInstance().particleEngine.register(BLOOD.get(), BloodParticle.Provider::new);
    }
}
