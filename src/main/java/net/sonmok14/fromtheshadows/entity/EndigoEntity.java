package net.sonmok14.fromtheshadows.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
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

import java.util.EnumSet;
import java.util.List;

public class EndigoEntity extends Monster implements Enemy, GeoEntity {

    public float squeakyProgress;
    public int blinkingCoolDown;
    public int attacktick;
    public int attackID;
    private static final EntityDataAccessor<Boolean> RIGHT = SynchedEntityData.defineId(EndigoEntity.class, EntityDataSerializers.BOOLEAN);
    public static final byte MELEE_ATTACK = 1;
    public static final byte DOUBLE_ATTACK = 2;
    public static final byte FLEE_BLINK = 3;
    public float layerBrightness, olayerBrightness;
    public int layerTicks;
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
                new AnimationController<>(this, "controller", 8, event -> {
                    event.getController().setAnimationSpeed(0.5D);
                    if (attackID == 1) {
                        if(isRight())
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.endigo.right_melee"));
                        else
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.endigo.left_melee"));
                    }
                    event.getController().setAnimationSpeed(0.5D);
                    if (attackID == 2) {
                        if(isRight())
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.endigo.right_double"));
                        else
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.endigo.left_double"));
                    }
                    if(event.isMoving() && isAggressive())
                    {
                        event.getController().setAnimationSpeed(0.8D);
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.endigo.run"));
                    }
                    if(event.isMoving())
                    {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.endigo.walk"));
                    }
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.endigo.idle"));
                }));
        controllerRegistrar.add(
                new AnimationController<>(this, "squeak", 5, event -> {
                    if(squeakyProgress < 50)
                    {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.endigo.squeaky"));
                    }
                    return PlayState.STOP;
                }));
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id <= 0) {
            this.attackID = Math.abs(id);
            this.attacktick = 0;
        } else {
            super.handleEntityEvent(id);
        }
    }
    private void meleeattack() {
        float range = 2f;
        float arc = 80;
        List<LivingEntity> entitiesHit = this.getEntityLivingBaseNearby(range, 3.5, range, range);
        for (LivingEntity entityHit : entitiesHit) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - this.getZ(), entityHit.getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = this.yHeadRot % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.getZ()) * (entityHit.getZ() - this.getZ()) + (entityHit.getX() - this.getX()) * (entityHit.getX() - this.getX()));
            if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) && (entityRelativeAngle >= 360 - arc / 2 == entityRelativeAngle <= -360 + arc / 2)) {
                if (!(entityHit instanceof EndigoEntity)) {
                    entityHit.hurt(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.3f);

                }

            }
        }
    }
    public  List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return level().getEntitiesOfClass(entityClass, getBoundingBox().inflate(dX, dY, dZ), e -> e != this && distanceTo(e) <= r + e.getBbWidth() / 2f && e.getY() <= getY() + dY);
    }
    @Override
    public boolean doHurtTarget(Entity p_85031_1_) {
        if (!this.level().isClientSide && this.attackID == 0) {
            if (this.random.nextInt(4) != 0) {
                this.attackID = MELEE_ATTACK;
            } else {
                this.attackID = DOUBLE_ATTACK;
            }
        }
        return true;
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RIGHT, false);
    }
    public boolean isRight() {
        return this.entityData.get(RIGHT);
    }
    public void setRight(boolean p_32759_) {
        this.entityData.set(RIGHT, p_32759_);
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new EndigoDoubleAttackGoal(this));
        this.goalSelector.addGoal(0, new EndigoMeleeAttackGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.6D, 25, true));
        super.registerGoals();
    }
    @Override
    public AnimatableInstanceCache animatableCacheOverride() {
        return GeoEntity.super.animatableCacheOverride();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.squeakyProgress == 0) {
            this.squeakyProgress = 250;
        }
        if (this.squeakyProgress > 0) {
            --this.squeakyProgress;
        }
        if(attackID == 0)
        {
            setRight(false);
        }
        if (this.squeakyProgress == 45 && isAlive())
        {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundRegistry.ENDIGO_IDLE.get(), SoundSource.HOSTILE, 1F, 0.9F + this.getRandom().nextFloat() * 0.1F);
        }
        if (this.blinkingCoolDown > 0) {
            --this.blinkingCoolDown;
        }
        if (this.blinkingCoolDown == 0 && this.attackID == FLEE_BLINK) {
            this.blinkingCoolDown = 300;
        }
        if (this.level().isClientSide){
            ++layerTicks;
            this.layerBrightness += (0.0F - this.layerBrightness) * 0.1F;
        }
        if(this.getTarget() != null)
        {
            yBodyRot = yHeadRot;
            setYRot(yBodyRot);
            getLookControl().setLookAt(getTarget(),15F, 90.0F);
        }
        ++this.tickCount;
        if (this.attackID != 0) {
            ++this.attacktick;
        }
    }

    public void setAttackID(int id) {
        this.attackID = id;
        this.attacktick = 0;
        this.level().broadcastEntityEvent(this, (byte) -id);
    }
    class EndigoMeleeAttackGoal extends Goal {
        private final EndigoEntity endigoEntity;
        private LivingEntity attackTarget;

        public EndigoMeleeAttackGoal(EndigoEntity p_i45837_1_) {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
            this.endigoEntity = p_i45837_1_;
        }

        public boolean canUse() {
            this.attackTarget = this.endigoEntity.getTarget();
            return attackTarget != null && this.endigoEntity.attackID == 1;
        }

        public void start() {
            setRight(random.nextInt(2) != 0);
            this.endigoEntity.setAttackID(1);
        }

        public void stop() {
            this.endigoEntity.setAttackID(0);
            this.attackTarget = null;
        }
        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return endigoEntity.attacktick < 15;
        }


        public void tick() {
            if(attacktick == 10)
            {
                meleeattack();
            }
            if (attacktick == 10 && distanceTo(attackTarget) <= 3.5F) {
                attackTarget.hurt(damageSources().mobAttack(endigoEntity), (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
            }
        }
    }

    class EndigoDoubleAttackGoal extends Goal {
        private final EndigoEntity endigoEntity;
        private LivingEntity attackTarget;

        public EndigoDoubleAttackGoal(EndigoEntity p_i45837_1_) {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
            this.endigoEntity = p_i45837_1_;
        }

        public boolean canUse() {
            this.attackTarget = this.endigoEntity.getTarget();
            return attackTarget != null && this.endigoEntity.attackID == 2;
        }

        public void start() {
            setRight(random.nextInt(2) != 0);
            this.endigoEntity.setAttackID(2);
        }

        public void stop() {
            this.endigoEntity.setAttackID(0);
            this.attackTarget = null;
        }
        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return endigoEntity.attacktick < 40;
        }


        public void tick() {

            if(attacktick == 10)
            {
                meleeattack();
            }
            if (attacktick == 10 && distanceTo(attackTarget) <= 3.5F) {
                attackTarget.hurt(damageSources().mobAttack(endigoEntity), (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
            }
            if(attacktick == 27)
            {
                meleeattack();
            }
            if (attacktick == 27 && distanceTo(attackTarget) <= 3.5F) {
                attackTarget.invulnerableTime = 0;
                attackTarget.hurt(damageSources().mobAttack(endigoEntity), (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
            }
        }
    }
}
