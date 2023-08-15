package net.sonmok14.fromtheshadows.utils.registry;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.client.renderer.layer.LayerFreezing;
import net.sonmok14.fromtheshadows.client.renderer.layer.LayerSuppression;

import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class ClientLayerRegistry {

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        List<EntityType<? extends LivingEntity>> entityTypes = ImmutableList.copyOf(
                ForgeRegistries.ENTITY_TYPES.getValues().stream()
                        .filter(DefaultAttributes::hasSupplier)
                        .map(entityType -> (EntityType<? extends LivingEntity>) entityType)
                        .collect(Collectors.toList()));
        entityTypes.forEach((entityType -> {
            addLayerIfApplicable(entityType, event);
        }));
        for (String skinType : event.getSkins()){
            event.getSkin(skinType).addLayer(new LayerFreezing(event.getSkin(skinType)));
            event.getSkin(skinType).addLayer(new LayerSuppression(event.getSkin(skinType)));
        }
    }

    private static void addLayerIfApplicable(EntityType<? extends LivingEntity> entityType, EntityRenderersEvent.AddLayers event) {
        LivingEntityRenderer renderer = null;
        if(entityType != EntityType.ENDER_DRAGON){
            try{
                renderer = event.getRenderer(entityType);
            }catch (Exception e){
                Fromtheshadows.LOGGER.warn("Could not apply freezing layer to " + ForgeRegistries.ENTITY_TYPES.getKey(entityType) + ", has custom renderer that is not LivingEntityRenderer.");
            }
            if(renderer != null){
                renderer.addLayer(new LayerFreezing(renderer));
                renderer.addLayer(new LayerSuppression(renderer));
            }
        }
    }
}