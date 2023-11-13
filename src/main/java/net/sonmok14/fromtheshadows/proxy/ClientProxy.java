package net.sonmok14.fromtheshadows.proxy;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.client.ClientEvent;
import net.sonmok14.fromtheshadows.client.particle.BloodParticle;
import net.sonmok14.fromtheshadows.client.particle.LightningParticle;
import net.sonmok14.fromtheshadows.client.renderer.*;
import net.sonmok14.fromtheshadows.utils.registry.EntityRegistry;
import net.sonmok14.fromtheshadows.utils.registry.ItemRegistry;
import net.sonmok14.fromtheshadows.utils.registry.ParticleRegistry;


public class ClientProxy extends CommonProxy {

    public void commonInit() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::registry);
    }


        public void clientInit() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EntityRenderers.register(EntityRegistry.URNAX.get(), UrnaxRenderer::new);
        EntityRenderers.register(EntityRegistry.ENDIGO.get(), EndigoRenderer::new);
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
        event.registerSpecial(ParticleRegistry.LIGHTNING.get(), new LightningParticle.Factory());
        Minecraft.getInstance().particleEngine.register(ParticleRegistry.BLOOD.get(), BloodParticle.Provider::new);
    }

}
