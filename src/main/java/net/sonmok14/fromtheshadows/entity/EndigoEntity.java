package net.sonmok14.fromtheshadows.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sonmok14.fromtheshadows.FTSConfig;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class EndigoEntity extends Monster implements Enemy, GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public EndigoEntity(EntityType<EndigoEntity> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Nullable
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_KNOCKBACK, 1D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.MAX_HEALTH, FTSConfig.SERVER.endigo_health.get())
                .add(Attributes.ATTACK_DAMAGE, FTSConfig.SERVER.endigo_melee_damage.get())
                .add(Attributes.ARMOR, 2.0D);
    }

    public boolean dampensVibrations() {
        return true;
    }

    public boolean canBeSeenAsEnemy() {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
                new AnimationController<>(this, "controller", 4, event -> {
                    if(event.isMoving())
                    {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.endigo.walk"));
                    }
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.endigo.idle"));
                }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.5D, 25, true));
        super.registerGoals();
    }
    @Override
    public AnimatableInstanceCache animatableCacheOverride() {
        return GeoEntity.super.animatableCacheOverride();
    }
}
