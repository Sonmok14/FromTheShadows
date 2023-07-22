package net.sonmok14.fromtheshadows.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sonmok14.fromtheshadows.FTSConfig;
import net.sonmok14.fromtheshadows.utils.registry.SoundRegistry;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ClericEntity extends AbstractIllager implements GeoEntity {

    //incomplete
    public float mumbleProgress;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ClericEntity(EntityType<ClericEntity> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
    }

    @Override
    public void applyRaidBuffs(int p_37844_, boolean p_37845_) {

    }

    @Nullable
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_KNOCKBACK, 1D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.MAX_HEALTH, FTSConfig.SERVER.cleric_health.get())
                .add(Attributes.ATTACK_DAMAGE, FTSConfig.SERVER.cleric_melee_damage.get())
                .add(Attributes.ARMOR, 2.0D);
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
                new AnimationController<>(this, "controller", 7, event -> {
                    event.getController().setAnimationSpeed(0.5D);
                    if (this.walkAnimation.speed() > 0.35F && isAggressive()) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.cultist.run"));
                    }
                    if (event.isMoving()) {
                        event.getController().setAnimationSpeed(1D);
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.cultist.walk"));

                    }
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.cultist.idle"));
                }));
        controllerRegistrar.add(
                new AnimationController<>(this, "mumble", 15, event -> {
                    event.getController().setAnimationSpeed(0.5D);
                    if (mumbleProgress <= 35) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.cultist.mumble"));
                    }
                    return PlayState.STOP;
                }).setSoundKeyframeHandler(event -> {
            if (event.getKeyframeData().getSound().matches("mumblekey"))
                if (this.level().isClientSide)
                    this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundRegistry.CULTIST_IDLE.get(), SoundSource.HOSTILE, 1F, 0.5F + this.getRandom().nextFloat() * 0.1F, true);
        }));


    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.mumbleProgress > 0) {
            --this.mumbleProgress;
        }
        if (this.mumbleProgress == 0) {
            this.mumbleProgress = 200;
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.7D, 25, true));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }


}
