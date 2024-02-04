package net.sonmok14.fromtheshadows.server.utils.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;

public class BiomeRegistry {
    public static final ResourceKey<Biome> BURNT_FOREST = register("burnt_forest");

    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(Fromtheshadows.MODID, name));
    }


}
