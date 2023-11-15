package net.sonmok14.fromtheshadows.server.utils.registry;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sonmok14.fromtheshadows.server.Fromtheshadows;
import net.sonmok14.fromtheshadows.server.enchantment.DivinityEnchantment;
import net.sonmok14.fromtheshadows.server.enchantment.FreezingEnchantment;
import net.sonmok14.fromtheshadows.server.enchantment.RetributionEnchantment;

public class EnchantmentRegistry {
    public static final DeferredRegister<Enchantment> ENCHANTMENT = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Fromtheshadows.MODID);
    public static final RegistryObject<Enchantment> DIVINITY = ENCHANTMENT.register("divinity", () -> new DivinityEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> RETRIBUTION = ENCHANTMENT.register("retribution", () -> new RetributionEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
    public static final RegistryObject<Enchantment> FREEZING = ENCHANTMENT.register("freezing", () -> new FreezingEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.MAINHAND));
}
