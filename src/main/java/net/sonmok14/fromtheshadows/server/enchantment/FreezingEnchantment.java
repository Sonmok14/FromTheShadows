package net.sonmok14.fromtheshadows.server.enchantment;


import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class FreezingEnchantment extends Enchantment {
    public FreezingEnchantment(Enchantment.Rarity p_44996_, EquipmentSlot... p_44997_) {
        super(p_44996_, EnchantmentCategory.WEAPON, p_44997_);
    }

    public int getMinCost(int p_45000_) {
        return 10 + 20 * (p_45000_ - 1);
    }

    public int getMaxCost(int p_45002_) {
        return super.getMinCost(p_45002_) + 50;
    }

    public int getMaxLevel() {
        return 3;
    }

    public boolean checkCompatibility(Enchantment p_45266_) {
        return super.checkCompatibility(p_45266_) && p_45266_ != Enchantments.FIRE_ASPECT;
    }

    public boolean isTradeable() {
        return true;
    }

    public boolean isDiscoverable() {
        return true;
    }
}
