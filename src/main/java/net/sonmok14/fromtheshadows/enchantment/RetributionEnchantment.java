package net.sonmok14.fromtheshadows.enchantment;


import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class RetributionEnchantment extends Enchantment {

    public RetributionEnchantment(Rarity p_44996_, EquipmentSlot... p_44997_) {
        super(p_44996_, EnchantmentCategory.WEAPON, p_44997_);
    }
    public int getMinCost(int p_45233_) {
        return 1 + (p_45233_ - 1) * 8;
    }

    public int getMaxCost(int p_45238_) {
        return this.getMinCost(p_45238_) + 20;
    }


    public int getMaxLevel() {
        return 5;
    }
    public boolean checkCompatibility(Enchantment p_44644_) {
        return !(p_44644_ instanceof DamageEnchantment);
    }
    public float getDamageBonus(int p_44635_, MobType p_44636_) {
        if (p_44636_ == MobType.ILLAGER) {
            return (float) p_44635_ * 2.5F;
        }
        return super.getDamageBonus(p_44635_, p_44636_);
    }
}