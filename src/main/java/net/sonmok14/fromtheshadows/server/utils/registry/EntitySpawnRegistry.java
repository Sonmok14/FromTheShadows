package net.sonmok14.fromtheshadows.server.utils.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;

public record EntitySpawnRegistry(HolderSet<Biome> biomes, SpawnerData spawn) implements BiomeModifier {

    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject
            .create(Fromtheshadows.ADD_SPAWNS_TO_BIOMES, ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Fromtheshadows.MODID);

    @Override
    public void modify(Holder<Biome> biome, Phase phase, Builder builder) {
        if (phase == Phase.ADD && this.biomes.contains(biome)) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, this.spawn);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<EntitySpawnRegistry> makeCodec() {
        return RecordCodecBuilder.create(builder -> builder
                .group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(EntitySpawnRegistry::biomes),
                        SpawnerData.CODEC.fieldOf("spawn").forGetter(EntitySpawnRegistry::spawn))
                .apply(builder, EntitySpawnRegistry::new));
    }
}

