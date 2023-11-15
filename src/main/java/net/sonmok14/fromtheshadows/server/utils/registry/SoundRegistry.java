package net.sonmok14.fromtheshadows.server.utils.registry;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;

@Mod.EventBusSubscriber(modid = Fromtheshadows.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundRegistry{

    public static final DeferredRegister<SoundEvent> MOD_SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Fromtheshadows.MODID);
    public static final RegistryObject<SoundEvent> BULLDROGIOTH_HURT = createSoundEvent("bulldrogioth_hurt");
    public static final RegistryObject<SoundEvent> BULLDROGIOTH_IDLE = createSoundEvent("bulldrogioth_idle");
    public static final RegistryObject<SoundEvent> BULLDROGIOTH_ATTACK = createSoundEvent("bulldrogioth_attack");
    public static final RegistryObject<SoundEvent> SOUL_LASER = createSoundEvent("soul_laser");
    public static final RegistryObject<SoundEvent> SOUL_LASER_READY = createSoundEvent("soul_laser_ready");
    public static final RegistryObject<SoundEvent> NEHEMOTH_IDLE = createSoundEvent("nehemoth_idle");
    public static final RegistryObject<SoundEvent> NEHEMOTH_DEATH = createSoundEvent("nehemoth_death");
    public static final RegistryObject<SoundEvent> NEHEMOTH_HURT = createSoundEvent("nehemoth_hurt");
    public static final RegistryObject<SoundEvent> NEHEMOTH_ROAR = createSoundEvent("nehemoth_roar");
    public static final RegistryObject<SoundEvent> NEHEMOTH_GORE_SOUND = createSoundEvent("goresound");
    public static final RegistryObject<SoundEvent> CULTIST_IDLE = createSoundEvent("cultist_idle");
    public static final RegistryObject<SoundEvent> CULTIST_HURT = createSoundEvent("cultist_hurt");
    public static final RegistryObject<SoundEvent> CULTIST_DEATH = createSoundEvent("cultist_death");
    public static final RegistryObject<SoundEvent> CULTIST_PREATTACK = createSoundEvent("cultist_preattack");
    public static final RegistryObject<SoundEvent> CULTIST_ATTACK = createSoundEvent("cultist_attack");
    public static final RegistryObject<SoundEvent> STOMP = createSoundEvent("nehemoth_stomp");

    private static RegistryObject<SoundEvent> createSoundEvent(final String soundName) {
        return MOD_SOUNDS.register(soundName, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Fromtheshadows.MODID, soundName)));
    }
}
