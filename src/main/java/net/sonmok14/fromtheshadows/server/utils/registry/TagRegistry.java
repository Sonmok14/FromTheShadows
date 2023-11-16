package net.sonmok14.fromtheshadows.server.utils.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;

public class TagRegistry {
    public static final TagKey<Biome> NEHEMOTH_SPAWN = registerBiomeTag("nehemoth_spawn.json");
    public static final TagKey<Biome> BULLDROGIOTH_SPAWN = registerBiomeTag("bulldrogioth_spawn");
    public static final TagKey<Biome> FROGLIN_SPAWN = registerBiomeTag("froglin_spawn");



    private static TagKey<EntityType<?>> registerEntityTag(String name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Fromtheshadows.MODID, name));
    }
    private static TagKey<Item> registerItemTag(String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Fromtheshadows.MODID, name));
    }

    private static TagKey<Block> registerBlockTag(String name) {
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Fromtheshadows.MODID, name));
    }

    private static TagKey<Biome> registerBiomeTag(String name) {
        return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Fromtheshadows.MODID, name));
    }

    private static TagKey<Structure> registerStructureTag(String name) {
        return TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Fromtheshadows.MODID, name));
    }
}
