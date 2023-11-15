package net.sonmok14.fromtheshadows.server.utils.registry;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.world.biome.FTSBiomeModifier;

public class ModBiomeModifiers {
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Fromtheshadows.MODID);
    public static final RegistryObject<Codec<FTSBiomeModifier>> FTS_ENTITY_MODIFIER_TYPE = BIOME_MODIFIER_SERIALIZERS.register("fts_entity_modifier", () -> Codec.unit(FTSBiomeModifier.INSTANCE));
}
