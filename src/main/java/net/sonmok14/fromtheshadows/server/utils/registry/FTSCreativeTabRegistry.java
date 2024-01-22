package net.sonmok14.fromtheshadows.server.utils.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;

public class FTSCreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Fromtheshadows.MODID);



public static final RegistryObject<CreativeModeTab> ITEM = TABS.register("items", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Fromtheshadows.MODID + ".item")).icon(() -> new ItemStack(ItemRegistry.BOTTLE_OF_BLOOD.get())).displayItems((enabledFeatures, entries) -> {
        entries.accept(ItemRegistry.NEHEMOTH_SPAWN_EGG.get());
    entries.accept(ItemRegistry.FROGLIN_SPAWN_EGG.get());
    entries.accept(ItemRegistry.BULLDROGIOTH_SPAWN_EGG.get());
    entries.accept(ItemRegistry.CLERIC_SPAWN_EGG.get());
        entries.accept(ItemRegistry.DIABOLIUM_HEAD.get());
        entries.accept(ItemRegistry.DIABOLIUM_CHEST.get());
        entries.accept(ItemRegistry.DIABOLIUM_LEGGINGS.get());
    entries.accept(ItemRegistry.CRUST_HEAD.get());
    entries.accept(ItemRegistry.CRUST_CHEST.get());
    entries.accept(ItemRegistry.CRUST_LEGGINGS.get());

    entries.accept(ItemRegistry.PLAGUE_DOCTOR_MASK.get());

    entries.accept(ItemRegistry.DEVIL_SPLITTER.get());
    entries.accept(ItemRegistry.THIRST_FOR_BLOOD.get());

    entries.accept(ItemRegistry.DIABOLIUM_NUGGET.get());
        entries.accept(ItemRegistry.DIABOLIUM_INGOT.get());
    entries.accept(ItemRegistry.DIABOLIUM_BLOCK.get());

    entries.accept(ItemRegistry.SUSPICIOUS_CLOTH.get());
    entries.accept(ItemRegistry.CRIMSON_SHELL.get());
        entries.accept(ItemRegistry.BOTTLE_OF_BLOOD.get());
    entries.accept(ItemRegistry.CRYSTALLIZED_BLOOD.get());
    entries.accept(ItemRegistry.FROGLIN_LEG.get());
    }).build());
}