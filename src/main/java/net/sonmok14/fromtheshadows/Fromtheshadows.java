package net.sonmok14.fromtheshadows;


import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.sonmok14.fromtheshadows.proxy.ClientProxy;
import net.sonmok14.fromtheshadows.proxy.CommonProxy;
import net.sonmok14.fromtheshadows.utils.event.ServerEvents;
import net.sonmok14.fromtheshadows.utils.registry.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.GeckoLib;

import java.util.Locale;


@Mod(Fromtheshadows.MODID)
public class Fromtheshadows
{
    public static Fromtheshadows instance;
    public static final String MODID = "fromtheshadows";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final ResourceLocation ADD_SPAWNS_TO_BIOMES = new ResourceLocation(MODID, "mobspawns");
    public static CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public Fromtheshadows()
    {
        FTSConfig.loadConfig(FTSConfig.SERVER_SPEC,
                FMLPaths.CONFIGDIR.get().resolve("fromtheshadows-config.toml").toString());
        instance = this;

        GeckoLib.initialize();
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        PROXY.init(modEventBus);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ServerEvents());
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        ParticleRegistry.PARTICLES.register(modEventBus);
        SoundRegistry.MOD_SOUNDS.register(modEventBus);
        EffectRegistry.EFFECT.register(modEventBus);
        EffectRegistry.POTION.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        ModBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
        FTSCreativeTabRegistry.TABS.register(modEventBus);
        final DeferredRegister<Codec<? extends BiomeModifier>> serializers = DeferredRegister
                .create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, MODID);
        serializers.register(modEventBus);
        serializers.register("mobspawns", EntitySpawnRegistry::makeCodec);


    }


    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }

    private void setup(final FMLCommonSetupEvent event) {
        EffectRegistry.init();
    }


    private void enqueueIMC(final InterModEnqueueEvent event)
    {
    }

    private void processIMC(final InterModProcessEvent event)
    {
    }
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }
}
