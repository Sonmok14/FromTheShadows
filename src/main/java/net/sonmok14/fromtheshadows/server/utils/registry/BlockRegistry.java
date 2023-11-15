package net.sonmok14.fromtheshadows.server.utils.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.blocks.DiaboliumBlock;

public class BlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Fromtheshadows.MODID);


    public static final RegistryObject<Block> DIABOLIUM_BLOCK = BLOCKS.register("diabolium_block", () -> new DiaboliumBlock(BlockBehaviour.Properties.of().explosionResistance(30).strength(4.0F).sound(SoundType.METAL)));
}
