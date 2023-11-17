package net.sonmok14.fromtheshadows.server.world.biome;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.sonmok14.fromtheshadows.server.FTSConfig;
import net.sonmok14.fromtheshadows.server.utils.registry.EntityRegistry;
import net.sonmok14.fromtheshadows.server.utils.registry.ModBiomeModifiers;

public class FTSBiomeModifier implements BiomeModifier {
    public static final FTSBiomeModifier INSTANCE = new FTSBiomeModifier();

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && biome.containsTag(BiomeTags.IS_OVERWORLD) && !biome.is(Biomes.DEEP_DARK) && !biome.is(Tags.Biomes.IS_VOID)) {
            if (FTSConfig.SERVER.bulldrogiothSpawnRate.get() > 0) {
                if (biome.is(BiomeTags.IS_BEACH) || biome.is(Biomes.RIVER) || biome.is(Tags.Biomes.IS_SWAMP)) {
                    builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(EntityRegistry.BULLDROGIOTH.get(), FTSConfig.SERVER.bulldrogiothSpawnRate.get(), 1, 1));
                }
            }
            if (FTSConfig.SERVER.nehemothSpawnRate.get() > 0) {
                if (biome.is(BiomeTags.IS_OVERWORLD) || biome.is(BiomeTags.IS_OCEAN) || biome.is(BiomeTags.IS_DEEP_OCEAN)) {
                    builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(EntityRegistry.NEHEMOTH.get(), FTSConfig.SERVER.nehemothSpawnRate.get(), 1, 1));
                }
            }
            if (FTSConfig.SERVER.froglinSpawnRate.get() > 0) {
                if (biome.is(Biomes.LUSH_CAVES) || biome.is(Tags.Biomes.IS_SWAMP)) {
                    builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(EntityRegistry.FROGLIN.get(), FTSConfig.SERVER.froglinSpawnRate.get(), 1, 3));
                }
            }
        }
        if (FTSConfig.SERVER.soulfirenehemothSpawnRate.get() > 0) {
            if (biome.is(Biomes.SOUL_SAND_VALLEY)) {
                builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(EntityRegistry.NEHEMOTH.get(), FTSConfig.SERVER.soulfirenehemothSpawnRate.get(), 1, 1));
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return ModBiomeModifiers.FTS_ENTITY_MODIFIER_TYPE.get();
    }
}
