package net.sonmok14.fromtheshadows.client;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sonmok14.fromtheshadows.client.renderer.items.CrustArmorRenderer;
import net.sonmok14.fromtheshadows.client.renderer.items.DiaboliumArmorRenderer;
import net.sonmok14.fromtheshadows.client.renderer.items.PlagueDoctorMaskRenderer;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.client.particle.BloodParticle;
import net.sonmok14.fromtheshadows.client.renderer.*;
import net.sonmok14.fromtheshadows.server.CommonProxy;
import net.sonmok14.fromtheshadows.server.items.CrustArmorItem;
import net.sonmok14.fromtheshadows.server.items.DiaboliumArmorItem;
import net.sonmok14.fromtheshadows.server.items.PlagueDoctorMaskItem;
import net.sonmok14.fromtheshadows.server.utils.registry.EntityRegistry;
import net.sonmok14.fromtheshadows.server.utils.registry.ItemRegistry;
import net.sonmok14.fromtheshadows.server.utils.registry.ParticleRegistry;
import software.bernie.example.item.GeckoArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;


public class ClientProxy extends CommonProxy {

    public void commonInit() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::registry);
    }


        public void clientInit() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());

        GeoArmorRenderer.registerArmorRenderer(PlagueDoctorMaskItem.class, () -> new PlagueDoctorMaskRenderer());
        GeoArmorRenderer.registerArmorRenderer(DiaboliumArmorItem.class, () -> new DiaboliumArmorRenderer());
        GeoArmorRenderer.registerArmorRenderer(CrustArmorItem.class, () -> new CrustArmorRenderer());
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

}
