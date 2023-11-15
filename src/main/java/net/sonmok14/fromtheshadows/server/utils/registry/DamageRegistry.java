package net.sonmok14.fromtheshadows.server.utils.registry;


import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
public class DamageRegistry {
    public static final ResourceKey<DamageType> BLEEDING = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("fromtheshadows:bleeding"));
    public static final ResourceKey<DamageType> INCINERATE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("fromtheshadows:incinerate"));


    public static DamageSource causeBleedingDamage(LivingEntity target){
        return new DamageSourceRandomMessages(target.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(BLEEDING), target);
    }

    public static DamageSource causeIncinerateDamage(LivingEntity target){
        return new DamageSourceRandomMessages(target.level().registryAccess().registry(Registries.DAMAGE_TYPE).get().getHolderOrThrow(INCINERATE), target);
    }

    private static class DamageSourceRandomMessages extends DamageSource {


        public DamageSourceRandomMessages(Holder<DamageType> damageTypeHolder, @Nullable Entity entity1, @Nullable Entity entity2, @Nullable Vec3 from) {
            super(damageTypeHolder, entity1, entity2, from);
        }

        public DamageSourceRandomMessages(Holder<DamageType> damageTypeHolder, @Nullable Entity entity1, @Nullable Entity entity2) {
            super(damageTypeHolder, entity1, entity2);
        }

        public DamageSourceRandomMessages(Holder<DamageType> damageTypeHolder, Vec3 from) {
            super(damageTypeHolder, from);
        }

        public DamageSourceRandomMessages(Holder<DamageType> damageTypeHolder, @Nullable Entity entity) {
            super(damageTypeHolder, entity);
        }

        public DamageSourceRandomMessages(Holder<DamageType> p_270475_) {
            super(p_270475_);
        }

        @Override
        public Component getLocalizedDeathMessage(LivingEntity attacked) {
            LivingEntity livingentity = attacked.getKillCredit();
            String s = "death.attack." + this.getMsgId();
            String s1 = s + ".player";
            return livingentity != null ? Component.translatable(s1, attacked.getDisplayName(), livingentity.getDisplayName()) : Component.translatable(s, attacked.getDisplayName());
        }
    }
}
