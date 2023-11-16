package net.sonmok14.fromtheshadows.server.utils.registry;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.items.*;
import net.sonmok14.fromtheshadows.server.items.ArmorMaterials;
import org.checkerframework.checker.units.qual.C;

public class ItemRegistry {

    public static ArmorMaterials CRUST_ARMOR_MATERIAL = new ArmorMaterials("crust", 40, new int[]{3, 5, 7, 3}, 12, SoundEvents.ARMOR_EQUIP_NETHERITE, 6, 0.4f);
    public static ArmorMaterials DIABOLIUM_ARMOR_MATERIAL = new ArmorMaterials("diabolium", 24, new int[]{3, 6, 8, 3}, 18, SoundEvents.ARMOR_EQUIP_NETHERITE, 3, 0.2f);
    public static ArmorMaterials PLAGUE_ARMOR_MATERIAL = new ArmorMaterials("plague", 8, new int[]{2, 3, 3, 2}, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0, 0f);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Fromtheshadows.MODID);



    public static final RegistryObject<DiaboliumArmorItem> DIABOLIUM_HEAD = ITEMS.register("diabolium_helmet",
            () -> new DiaboliumArmorItem(DIABOLIUM_ARMOR_MATERIAL, EquipmentSlot.HEAD, new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON).tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<DiaboliumArmorItem> DIABOLIUM_CHEST = ITEMS.register("diabolium_chest",
            () -> new DiaboliumArmorItem(DIABOLIUM_ARMOR_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON).tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<DiaboliumArmorItem> DIABOLIUM_LEGGINGS = ITEMS.register("diabolium_leggings",
            () -> new DiaboliumArmorItem(DIABOLIUM_ARMOR_MATERIAL, EquipmentSlot.LEGS, new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON).tab(CreativeModeTab.TAB_COMBAT)));


    public static final RegistryObject<CrustArmorItem> CRUST_HEAD = ITEMS.register("crust_helmet",
            () -> new CrustArmorItem(CRUST_ARMOR_MATERIAL, EquipmentSlot.HEAD, new Item.Properties().rarity(Rarity.UNCOMMON).tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<CrustArmorItem> CRUST_CHEST = ITEMS.register("crust_chest",
            () -> new CrustArmorItem(CRUST_ARMOR_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON).tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<CrustArmorItem> CRUST_LEGGINGS = ITEMS.register("crust_leggings",
            () -> new CrustArmorItem(CRUST_ARMOR_MATERIAL, EquipmentSlot.LEGS, new Item.Properties().rarity(Rarity.UNCOMMON).tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> DIABOLIUM_INGOT = ITEMS.register("diabolium_ingot",
            () -> new Item(new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> DEVIL_SPLITTER = ITEMS.register("devil_splitter", () -> new DevilSplitterItem(new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant().tab(CreativeModeTab.TAB_COMBAT)));

    public static final RegistryObject<Item> THIRST_FOR_BLOOD = ITEMS.register("thirst_for_blood", () -> new ThirstforBloodItem(new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant().tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> DIABOLIUM_NUGGET = ITEMS.register("diabolium_nugget",
            () -> new Item(new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> BOTTLE_OF_BLOOD = ITEMS.register("bottle_of_blood",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> SUSPICIOUS_CLOTH = ITEMS.register("suspicious_cloth",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<PlagueDoctorMaskItem> PLAGUE_DOCTOR_MASK = ITEMS.register("plague_doctor_mask",
            () -> new PlagueDoctorMaskItem(PLAGUE_ARMOR_MATERIAL, EquipmentSlot.HEAD, new Item.Properties().rarity(Rarity.UNCOMMON).tab(CreativeModeTab.TAB_COMBAT)));
    public static final RegistryObject<Item> CRYSTALLIZED_BLOOD = ITEMS.register("crystallized_blood",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> CRIMSON_SHELL = ITEMS.register("crimson_shell",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> FROGLIN_LEG = ITEMS.register("froglin_leg",
            () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> DIABOLIUM_BLOCK = ITEMS.register("diabolium_block", () -> new BlockItem(BlockRegistry.DIABOLIUM_BLOCK.get(), new Item.Properties().fireResistant().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
    public static final RegistryObject<Item> CLERIC_SPAWN_EGG = ITEMS.register("cleric_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityRegistry.CLERIC, 0x95A5A6,0xE74C3C, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> BULLDROGIOTH_SPAWN_EGG = ITEMS.register("bulldrogioth_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityRegistry.BULLDROGIOTH, 0xFC2C48,0xF7D7BA, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> FROGLIN_SPAWN_EGG = ITEMS.register("froglin_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityRegistry.FROGLIN, 0x5AB333,0xB1E83D, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> NEHEMOTH_SPAWN_EGG = ITEMS.register("nehemoth_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityRegistry.NEHEMOTH, 0x523647,0x674c59, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
}
