package net.sonmok14.fromtheshadows.utils.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.entity.*;
import net.sonmok14.fromtheshadows.entity.projectiles.*;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
            Fromtheshadows.MODID);


    public static final RegistryObject<EntityType<EndigoEntity>> ENDIGO = ENTITY_TYPES.register("endigo",
            () -> EntityType.Builder.of(EndigoEntity::new, MobCategory.MONSTER).sized(0.6F, 3f)
                    .noSummon()
                    .clientTrackingRange(9).build(new ResourceLocation(Fromtheshadows.MODID, "endigo").toString()));
    public static final RegistryObject<EntityType<NehemothEntity>> NEHEMOTH = ENTITY_TYPES.register("nehemoth",
            () -> EntityType.Builder.of(NehemothEntity::new, MobCategory.MONSTER).sized(1.25f, 3.65f)
                    .fireImmune().clientTrackingRange(9).build(new ResourceLocation(Fromtheshadows.MODID, "nehemoth").toString()));
    public static final RegistryObject<EntityType<FroglinEntity>> FROGLIN = ENTITY_TYPES.register("froglin",
            () -> EntityType.Builder.of(FroglinEntity::new, MobCategory.MONSTER).sized(1f, 2f)
                   .clientTrackingRange(9).build(new ResourceLocation(Fromtheshadows.MODID, "froglin").toString()));
    public static final RegistryObject<EntityType<BulldrogiothEntity>> BULLDROGIOTH = ENTITY_TYPES.register("bulldrogioth",
            () -> EntityType.Builder.of(BulldrogiothEntity::new, MobCategory.MONSTER).sized(2.5f, 2.9f)
                    .clientTrackingRange(9).build(new ResourceLocation(Fromtheshadows.MODID, "bulldrogioth").toString()));
    public static final RegistryObject<EntityType<ClericEntity>> CLERIC = ENTITY_TYPES.register("cleric",
            () -> EntityType.Builder.of(ClericEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F)
                    .clientTrackingRange(9).build(new ResourceLocation(Fromtheshadows.MODID, "cleric").toString()));
    public static final RegistryObject<EntityType<ScreenShakeEntity>> SCREEN_SHAKE = ENTITY_TYPES.register("screen_shake", () -> EntityType.Builder.<ScreenShakeEntity>of(ScreenShakeEntity::new, MobCategory.MISC)
            .noSummon()
            .sized(1.0f, 1.0f)
            .setUpdateInterval(Integer.MAX_VALUE)
            .build(Fromtheshadows.MODID + ":screen_shake"));

    public static final RegistryObject<EntityType<FallingBlockEntity>> FALLING_BLOCK = ENTITY_TYPES.register("falling_block", () -> EntityType.Builder.<FallingBlockEntity>of(FallingBlockEntity::new, MobCategory.MISC)
            .noSummon()
            .sized(1.0f, 1.0f)
            .setUpdateInterval(Integer.MAX_VALUE)
            .build(Fromtheshadows.MODID + ":falling_block"));

    public static final RegistryObject<EntityType<DoomBreathEntity>> DOOM_BREATH = ENTITY_TYPES.register("doom_breath", () -> EntityType.Builder.<DoomBreathEntity>of(DoomBreathEntity::new, MobCategory.MISC)
            .noSummon()
            .sized(1.0f, 1.0f)
            .setUpdateInterval(Integer.MAX_VALUE)
            .build(Fromtheshadows.MODID + ":doom_breath"));

    public static final RegistryObject<EntityType<PlayerBreathEntity>> PLAYER_BREATH = ENTITY_TYPES.register("player_breath", () -> EntityType.Builder.<PlayerBreathEntity>of(PlayerBreathEntity::new, MobCategory.MISC)
            .noSummon()
            .sized(1.0f, 1.0f)
            .setUpdateInterval(Integer.MAX_VALUE)
            .build(Fromtheshadows.MODID + ":player_breath"));

    public static final RegistryObject<EntityType<CoralThornEntity>> CORAL_THORN = ENTITY_TYPES.register("coral_thorn", () -> EntityType.Builder.<CoralThornEntity>of(CoralThornEntity::new, MobCategory.MISC)
            .sized(1.0f, 1.0f)
            .noSummon()
            .setUpdateInterval(Integer.MAX_VALUE)
            .build(Fromtheshadows.MODID + ":coral_thorn"));
    public static final RegistryObject<EntityType<ThrowingDaggerEntity>> THROWING_DAGGER = ENTITY_TYPES.register("throwing_dagger", () -> EntityType.Builder.<ThrowingDaggerEntity>of(ThrowingDaggerEntity::new, MobCategory.MISC)
            .sized(1.0f, 1.0f)
            .noSummon()
            .setUpdateInterval(Integer.MAX_VALUE)
            .build(Fromtheshadows.MODID + ":throwing_dagger"));
    public static final RegistryObject<EntityType<FrogVomit>> FROGLIN_VOMIT = ENTITY_TYPES.register("froglin_vomit", () -> EntityType.Builder.<FrogVomit>of(FrogVomit::new, MobCategory.MISC)
            .noSummon()
            .sized(1.0f, 1.0f)
            .setUpdateInterval(Integer.MAX_VALUE)
            .build(Fromtheshadows.MODID + ":froglin_vomit"));
}
