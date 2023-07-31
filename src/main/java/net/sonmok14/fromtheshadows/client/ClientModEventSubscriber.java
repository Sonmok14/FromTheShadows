package net.sonmok14.fromtheshadows.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.client.renderer.*;
import net.sonmok14.fromtheshadows.client.renderer.BulldrogiothRenderer;
import net.sonmok14.fromtheshadows.utils.registry.EntityRegistry;
import net.sonmok14.fromtheshadows.utils.registry.ItemRegistry;

@Mod.EventBusSubscriber(modid = Fromtheshadows.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventSubscriber {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(EntityRegistry.THROWING_DAGGER.get(), ThrowingDaggerRenderer::new);
        event.registerEntityRenderer(EntityRegistry.CORAL_THORN.get(), CoralThornRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BULLDROGIOTH.get(), BulldrogiothRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FROGLIN_VOMIT.get(), FroglinVomitRenderer::new);
        event.registerEntityRenderer(EntityRegistry.FROGLIN.get(), FroglinRenderer::new);
        event.registerEntityRenderer(EntityRegistry.DOOM_BREATH.get(), DoomBreathRenderer::new);
        event.registerEntityRenderer(EntityRegistry.PLAYER_BREATH.get(), PlayerBreathRenderer::new);
        event.registerEntityRenderer(EntityRegistry.CLERIC.get(), ClericRenderer::new);
        event.registerEntityRenderer(EntityRegistry.NEHEMOTH.get(), NehemothRenderer::new);
        event.registerEntityRenderer(EntityRegistry.SCREEN_SHAKE.get(), RendererNull::new);
        event.registerEntityRenderer(EntityRegistry.FALLING_BLOCK.get(), FallingBlockRenderer::new);
        try {

            ItemProperties.register(ItemRegistry.THIRST_FOR_BLOOD.get(), new ResourceLocation("using"), (stack, p_2394211, p_2394212, j) -> p_2394212 != null && p_2394212.isUsingItem() && p_2394212.getUseItem() == stack ? 1.0F : 0.0F);
        } catch (Exception e) {
            Fromtheshadows.LOGGER.warn("Could not load item models for weapons");

        }
    }



    }

