package net.sonmok14.fromtheshadows.utils;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.entity.*;
import net.sonmok14.fromtheshadows.utils.registry.EntityRegistry;

@Mod.EventBusSubscriber(modid = Fromtheshadows.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {

        event.put(EntityRegistry.ENDIGO.get(), EndigoEntity.createAttributes().build());

        event.put(EntityRegistry.URNAX.get(), UrnaxEntity.createAttributes().build());

        event.put(EntityRegistry.NEHEMOTH.get(), NehemothEntity.createAttributes().build());
        SpawnPlacements.register(EntityRegistry.NEHEMOTH.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NehemothEntity::canNehemothSpawn);

        event.put(EntityRegistry.FROGLIN.get(), FroglinEntity.createAttributes().build());
        SpawnPlacements.register(EntityRegistry.FROGLIN.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FroglinEntity::canFroglinSpawn);

        event.put(EntityRegistry.BULLDROGIOTH.get(), BulldrogiothEntity.createAttributes().build());
        SpawnPlacements.register(EntityRegistry.BULLDROGIOTH.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BulldrogiothEntity::canBulldrogiothSpawn);

        event.put(EntityRegistry.CLERIC.get(), ClericEntity.createAttributes().build());

    }
}
