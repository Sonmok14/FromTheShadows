package net.sonmok14.fromtheshadows.server.level.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BurntForestBiomeDecorator {

    @SuppressWarnings("unused")
    private static Biome biome(Biome.Precipitation precipitation, float temperature, float downfall, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder biomeBuilder)
    {
        return biome(precipitation, temperature, downfall, spawnBuilder, biomeBuilder);
    }

    private static Biome biome(boolean precipitation, float temperature, float downfall, MobSpawnSettings.Builder spawnBuilder, BiomeGenerationSettings.Builder biomeBuilder) {
        return (new Biome.BiomeBuilder())
                .hasPrecipitation(precipitation)
                .temperature(temperature)
                .downfall(downfall)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(5129273)
                        .waterFogColor(2959393)
                        .fogColor(1117448)
                        .skyColor(2577989)
                        .foliageColorOverride(8242525)
                        .grassColorOverride(8242525)
                        .ambientParticle(new AmbientParticleSettings(ParticleTypes.WHITE_ASH, 0.118093334F))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();
    }


    public static Biome decorateBurntForest(HolderGetter<PlacedFeature> placedFeatureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        BiomeGenerationSettings.Builder biomeFeatures = new BiomeGenerationSettings.Builder(placedFeatureGetter, carverGetter);
        BiomeDefaultFeatures.commonSpawns(spawnSettings);
        BiomeDefaultFeatures.addFossilDecoration(biomeFeatures);
        globalOverworldGeneration(biomeFeatures);
        BiomeDefaultFeatures.addDefaultOres(biomeFeatures);
        addBurntForestVegetation(biomeFeatures);
        return biome(false, 0.9F, 0.0F, spawnSettings, biomeFeatures);
    }



    public static void addBurntForestVegetation(BiomeGenerationSettings.Builder builder) {
    }

    private static void globalOverworldGeneration(BiomeGenerationSettings.Builder p_194870_) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(p_194870_);
        BiomeDefaultFeatures.addDefaultCrystalFormations(p_194870_);
        BiomeDefaultFeatures.addDefaultMonsterRoom(p_194870_);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(p_194870_);
        p_194870_.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, MiscOverworldPlacements.SPRING_LAVA);
        BiomeDefaultFeatures.addSurfaceFreezing(p_194870_);
    }

}
