package net.sonmok14.fromtheshadows.server.utils.registry;


import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
public class DamageRegistry {
    public static final DamageSource causeIncinerateDamage(LivingEntity attacker){
        return new DamageSourceRandomMessages("incinerate", attacker).setScalesWithDifficulty().bypassArmor().bypassEnchantments().setMagic();
    }
    public static final DamageSource causeBleedingDamage(LivingEntity attacker){
        return new DamageSourceRandomMessages("bleeding", attacker).setScalesWithDifficulty().bypassArmor().bypassEnchantments().setMagic();
    }

    private static class DamageSourceRandomMessages extends EntityDamageSource {

        public DamageSourceRandomMessages(String message, Entity entity) {
            super(message, entity);
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity attacked) {
            int type = attacked.getRandom().nextInt(3);
            LivingEntity livingentity = attacked.getKillCredit();
            String s = "death.attack." + this.msgId + "_" + type;
            String s1 = s + ".player";
            return livingentity != null ? Component.translatable(s1, attacked.getDisplayName(), livingentity.getDisplayName()) : Component.translatable(s, attacked.getDisplayName());
        }
    }
}
