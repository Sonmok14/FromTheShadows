package net.sonmok14.fromtheshadows.server.world.biome;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.world.ModifiableStructureInfo;
import net.minecraftforge.common.world.StructureModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.utils.registry.WorldRegistry;


public class FTSStructureModifier implements StructureModifier {
    private static final RegistryObject<Codec<? extends StructureModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation(Fromtheshadows.MODID, "fts_structure_spawns"), ForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS, Fromtheshadows.MODID);

    public FTSStructureModifier() {
    }

    @Override
    public void modify(Holder<Structure> structure, Phase phase, ModifiableStructureInfo.StructureInfo.Builder builder) {
        if (phase == StructureModifier.Phase.ADD) {
            WorldRegistry.modifyStructure(structure, builder);

        }
    }

    public Codec<? extends StructureModifier> codec() {
        return (Codec)SERIALIZER.get();
    }

    public static Codec<FTSStructureModifier> makeCodec() {
        return Codec.unit(FTSStructureModifier::new);
    }
}