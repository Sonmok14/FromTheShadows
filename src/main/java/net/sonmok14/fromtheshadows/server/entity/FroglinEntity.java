package net.sonmok14.fromtheshadows.server.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.sonmok14.fromtheshadows.server.FTSConfig;
import net.sonmok14.fromtheshadows.server.entity.ai.*;
import net.sonmok14.fromtheshadows.server.entity.projectiles.FrogVomit;
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

public class FroglinEntity extends Monster implements Enemy, GeoEntity, ISemiAquatic {
    boolean searchingForLand;
    private boolean isLandNavigator;
    public float SwimProgress = 0;
    public float prevSwimProgress = 0;
    private static final EntityDataAccessor<Boolean> RIGHT = SynchedEntityData.defineId(FroglinEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FULL = SynchedEntityData.defineId(FroglinEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(FroglinEntity.class, EntityDataSerializers.INT);
    public float blinkProgress;
    public float croakingProgress;
    public int attacktick;
    public int attackID;
    public static final byte MELEE_ATTACK = 1;
    public static final byte VOMIT_ATTACK = 2;
    public static final byte JUMP_ATTACK = 3;
    public static final byte STRIKE_ATTACK = 4;
    public static final byte SWALLOW_ATTACK = 5;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FroglinEntity(EntityType<FroglinEntity> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        switchNavigator(false);
        this.moveControl = new FroglinMoveControl(this);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }
    @Override
    protected int calculateFallDamage(float p_21237_, float p_21238_) {
        return 0;
    }

    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
    }
    @Nullable
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.MAX_HEALTH, FTSConfig.SERVER.froglin_health.get())
                .add(Attributes.ATTACK_DAMAGE, FTSConfig.SERVER.froglin_melee_damage.get())
                .add(Attributes.ARMOR, 2.0D);
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
                new AnimationController<>(this, "controller", 4, event -> {

                    if (event.isMoving() && this.walkAnimation.speed() > 0.35F && attackID == 0) {
                        event.getController().setAnimationSpeed(1.5D);
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.frog.walk"));
                    }
                    if (event.isMoving() && attackID == 0) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.frog.walkslow"));
                    }
                    if (!event.isMoving() && attackID == 0) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.frog.idle"));
                    }
                    if (attackID == 3 && attacktick > 9) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.frog.dash"));
                    }

                    if (attackID == 5 && attacktick > 9) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.frog.swallow"));
                    }

                    if (attackID == 5 && attacktick < 9) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.frog.crouched"));
                    }
                    if (attackID == 3 && attacktick < 9) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.frog.crouched"));
                    }
                    if (attackID == 2 && attacktick > 10) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.frog.vomitattack"));
                    }
                    if (attackID == 2 && attacktick < 10) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.frog.vomitready"));
                    }
                    if (attackID == 4) {
                        return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.frog.strike"));
                    }
                    if (attackID == 1) {
                        if(isRight())
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.frog.mtright"));
                        else
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.frog.mtleft"));
                        }
                    return PlayState.CONTINUE;
                    }));

        controllerRegistrar.add(
                new AnimationController<>(this, "eye", 20, event -> {
                    if (this.blinkProgress <= 10)
                     {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.frog.eye"));
                    }
                    return PlayState.STOP;
                }).setSoundKeyframeHandler(event -> {
                    if (event.getKeyframeData().getSound().matches("blinkSoundkey"))
                        if (this.level().isClientSide)
                            this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.SALMON_FLOP, SoundSource.HOSTILE, 1F, 0.5F, true);
                }));

        controllerRegistrar.add(
                new AnimationController<>(this, "full", 20, event -> {
                    if (this.isFull())
                    {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.frog.full"));
                    }
                    return PlayState.STOP;
                }));

        controllerRegistrar.add(
                new AnimationController<>(this, "croaking", 20, event -> {
                    if (this.croakingProgress <= 10)
                    {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.frog.croaking"));
                    }
                    return PlayState.STOP;
                }).setSoundKeyframeHandler(event -> {
                    if (event.getKeyframeData().getSound().matches("croakingSoundkey"))
                        if (this.level().isClientSide)
                            this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.FROG_AMBIENT, SoundSource.HOSTILE, 1F, 0.5F, true);
                }));
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void setAttackID(int id) {
        this.attackID = id;
        this.attacktick = 0;
        this.level().broadcastEntityEvent(this, (byte) -id);
    }
    private static boolean isBiomeSwamp(LevelAccessor worldIn, BlockPos position) {
        return worldIn.getBiome(position).is(Tags.Biomes.IS_SWAMP);
    }

    public static boolean checkMonsterSpawnRules(EntityType<? extends Monster> p_33018_, ServerLevelAccessor p_33019_, MobSpawnType p_33020_, BlockPos p_33021_, RandomSource p_33022_) {
        return p_33019_.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(p_33018_, p_33019_, p_33020_, p_33021_, p_33022_);
    }
    public static <T extends Mob> boolean canFroglinSpawn(EntityType<FroglinEntity> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if(isBiomeSwamp(iServerWorld, pos))
        {
            return reason == MobSpawnType.SPAWNER || checkMonsterSpawnRules(entityType, iServerWorld, reason, pos, random) && random.nextInt(2) == 0;
        }
            return reason == MobSpawnType.SPAWNER || !iServerWorld.canSeeSky(pos) && pos.getY() <= 0 && checkMonsterSpawnRules(entityType, iServerWorld, reason, pos, random);
    }
    @Override
    public boolean canRiderInteract() {
        return false;
    }

    @Override
    protected boolean canRide(Entity p_20339_) {
        return false;
    }

    @Override
    public boolean shouldEnterWater() {
        return false;
    }

    @Override
    public boolean shouldLeaveWater() {
        return this.getTarget() != null && !this.getTarget().isInWater();
    }

    @Override
    public boolean shouldStopMoving() {
        return false;
    }

    @Override
    public int getWaterSearchRange() {
        return 32;

    }
    boolean wantsToSwim() {
        if (this.searchingForLand) {
            return true;
        } else {
            LivingEntity livingentity = this.getTarget();
            return livingentity != null && livingentity.isInWater();
        }
    }
    public void travel(Vec3 p_32394_) {
        if (this.attackID != 0) {
            if (this.getNavigation().getPath() != null) {
                this.getNavigation().stop();
            }
            p_32394_ = Vec3.ZERO;
            super.travel(p_32394_);
            return;
        }
        if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
            this.moveRelative(0.03F, p_32394_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
        } else {
            super.travel(p_32394_);
        }

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

    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public MobType getMobType() {
        return MobType.WATER;
    }

    public void switchNavigator(boolean onLand) {
        if (onLand) {
            this.navigation = new GroundPathNavigatorWide(this, level());
            this.isLandNavigator = true;
        } else {
            this.navigation = new SemiAquaticPathNavigator(this, level());
            this.isLandNavigator = false;
        }
    }

    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        } else {
            this.noActionTime = 0;
        }
    }

    @Override
    public boolean doHurtTarget(Entity p_85031_1_) {
        if (!this.level().isClientSide && this.attackID == 0) {
            if (this.random.nextInt(4) != 0) {
                this.attackID = MELEE_ATTACK;
            } else {
                this.attackID = STRIKE_ATTACK;
            }
        }
        return true;
    }


    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    public CompoundTag serializeNBT() {
        return super.serializeNBT();
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RIGHT, false);
        this.entityData.define(FULL, false);
        this.entityData.define(VARIANT, 0);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT).intValue();
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setVariant(this.getRandom().nextInt(3));
        if(random.nextInt(2) == 0)
        {
            this.setFull(true);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }
    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Integer.valueOf(variant));
    }
    public boolean isRight() {
        return this.entityData.get(RIGHT);
    }
    public void setRight(boolean p_32759_) {
        this.entityData.set(RIGHT, p_32759_);
    }
    public boolean isFull() {
        return this.entityData.get(FULL);
    }
    public void setFull(boolean p_32759_) {
        this.entityData.set(FULL, p_32759_);
    }

    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putInt("Variant", this.getVariant());


    }


    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        this.setVariant(p_21450_.getInt("Variant"));
    }



    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundEvents.FROG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FROG_DEATH;
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
                if (!(entityHit instanceof FroglinEntity)) {
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
    public void tick() {
        super.tick();

        if (!level().isClientSide) {
            this.removeEffect(MobEffects.POISON);
        }

        this.setMaxUpStep(1.0F);
        if(attackID == 0)
        {
            setRight(false);
        }
        if (this.blinkProgress == 0) {
           this.blinkProgress = 100;
        }
        if (this.blinkProgress > 0) {
            --this.blinkProgress;
        }
        if (this.croakingProgress == 0) {
            this.croakingProgress = 50;
        }
        if (this.croakingProgress > 0) {
            --this.croakingProgress;
        }
        if (isInWater() && this.isLandNavigator) {
            switchNavigator(false);
        }
        if (!isInWater() && !this.isLandNavigator) {
            switchNavigator(true);
        }
        this.prevSwimProgress = SwimProgress;
        if (this.isInWater()) {
            if (this.SwimProgress < 10F)
                this.SwimProgress++;
        } else {
            if (this.SwimProgress > 0F)
                this.SwimProgress--;
        }
        if (this.attackID != 0) {
            yBodyRot = yHeadRot;
            setYRot(yBodyRot);
            ++this.attacktick;
            if(getTarget() != null) {
                getLookControl().setLookAt(getTarget(), 30F, 90.0F);
            }
        }
    }

    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn == this) {
            return true;
        } else if (super.isAlliedTo(entityIn)) {
            return true;
        } else if (entityIn instanceof FroglinEntity){
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new FroglinGoToBeachGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new FroglinSwimUpGoal(this, 1.0D, this.level().getSeaLevel()));
        this.goalSelector.addGoal(4, new MobAIFindWater(this,1.0D));
        this.goalSelector.addGoal(4, new MobAILeaveWater(this));
        this.goalSelector.addGoal(0, new FroglinSwallowAttackGoal(this));
        this.goalSelector.addGoal(0, new FroglinVomitGoal(this));
        this.goalSelector.addGoal(0, new FroglinMeleeAttackGoal(this));
        this.goalSelector.addGoal(0, new FroglinStrikeAttackGoal(this));
        this.goalSelector.addGoal(0, new FroglinJumpAttackGoal(this));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Axolotl.class, true));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, NehemothEntity.class, 6.0F, 1.0D, 1.2D));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Chicken.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractFish.class, true));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.5D, 25, true));
        super.registerGoals();
    }
    @Override
    public boolean killedEntity(ServerLevel p_216988_, LivingEntity p_216989_) {
        if(!isFull() && p_216989_.getBbHeight() < 1F && random.nextInt(3) == 0)
        {
            this.playSound(SoundEvents.GENERIC_EAT, this.getSoundVolume(), this.getVoicePitch());
            setFull(true);
        }
        return super.killedEntity(p_216988_, p_216989_);
    }
    class FroglinMeleeAttackGoal extends Goal {
        private final FroglinEntity froglinEntity;
        private LivingEntity attackTarget;

        public FroglinMeleeAttackGoal(FroglinEntity p_i45837_1_) {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
            this.froglinEntity = p_i45837_1_;
        }

        public boolean canUse() {
            this.attackTarget = this.froglinEntity.getTarget();
            return attackTarget != null && this.froglinEntity.attackID == 1;
        }

        public void start() {
            setRight(random.nextInt(2) != 0);
            this.froglinEntity.setAttackID(1);
        }

        public void stop() {
            this.froglinEntity.setAttackID(0);
            this.attackTarget = null;
        }
        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return froglinEntity.attacktick < 20;
        }


        public void tick() {
                if (attacktick == 4) {
                    float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                    float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));

                    push(f1 * 0.3, 0, f2 * 0.3);
                }
                if (attacktick == 9 && distanceTo(attackTarget) <= 3.5F) {
                    attackTarget.hurt(damageSources().mobAttack(froglinEntity), (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
                }
            getNavigation().recomputePath();
        }
    }
    class FroglinStrikeAttackGoal extends Goal {
        private final FroglinEntity froglinEntity;
        private LivingEntity attackTarget;

        public FroglinStrikeAttackGoal(FroglinEntity p_i45837_1_) {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
            this.froglinEntity = p_i45837_1_;
        }

        public boolean canUse() {
            this.attackTarget = this.froglinEntity.getTarget();
            return attackTarget != null && this.froglinEntity.attackID == 4;
        }

        public void start() {
            this.froglinEntity.setAttackID(4);
        }

        public void stop() {
            this.froglinEntity.setAttackID(0);
            this.attackTarget = null;
        }
        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
        @Override
        public boolean canContinueToUse() {
            return froglinEntity.attacktick < 22;
        }

        public void tick() {
            if (attacktick == 5) {
                yBodyRot = yHeadRot;
                float f1 = (float) Math.cos(Math.toRadians(froglinEntity.getYRot() + 90));
                float f2 = (float) Math.sin(Math.toRadians(froglinEntity.getYRot() + 90));

                froglinEntity.push(f1 * 0.3, 0, f2 * 0.3);
            }
            if (attacktick == 12) {
              meleeattack();
            }
            getNavigation().recomputePath();
        }
    }
    class FroglinVomitGoal extends Goal {
        private final FroglinEntity froglinEntity;
        private LivingEntity attackTarget;

        public FroglinVomitGoal(FroglinEntity p_i45837_1_) {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
            this.froglinEntity = p_i45837_1_;
        }

        public boolean canUse() {
            this.attackTarget = this.froglinEntity.getTarget();
            return attackTarget != null && this.froglinEntity.attackID == 0 && (distanceTo(attackTarget) > 5.0D  && onGround() && random.nextInt(5) == 0 && isFull());
        }

        public void start() {
            this.froglinEntity.setAttackID(2);
        }

        public void stop() {
            this.froglinEntity.setAttackID(0);
            this.attackTarget = null;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return froglinEntity.attacktick < 30;
        }


        public void tick() {
            if(attacktick == 10)
            {
                froglinEntity.playSound(SoundEvents.PLAYER_BURP, 2f, 0.2F + froglinEntity.getRandom().nextFloat() * 0.1F);
                FrogVomit frogVomit = new FrogVomit(froglinEntity.level(), froglinEntity);
                double d0 = attackTarget.getX() - froglinEntity.getX();
                double d1 = attackTarget.getY(0.3333333333333333D) - frogVomit.getY();
                double d2 = attackTarget.getZ() - froglinEntity.getZ();
                double d3 = Math.sqrt(d0 * d0 + d2 * d2) * (double)0.4F;
                frogVomit.shoot(d0, d1 + d3, d2, 1F, 5.0F);
                froglinEntity.level().addFreshEntity(frogVomit);
            }
            if(attacktick == 20)
            {
                froglinEntity.playSound(SoundEvents.PLAYER_BURP, 1f, 0.8F + froglinEntity.getRandom().nextFloat() * 0.1F);
                FrogVomit llamaspit = new FrogVomit(froglinEntity.level(), froglinEntity);
                double d0 = attackTarget.getX() - froglinEntity.getX();
                double d1 = attackTarget.getY(0.3333333333333333D) - llamaspit.getY();
                double d2 = attackTarget.getZ() - froglinEntity.getZ();
                double d3 = Math.sqrt(d0 * d0 + d2 * d2) * (double)0.4F;
                llamaspit.shoot(d0, d1 + d3, d2, 1F, 5.0F);
                setFull(false);
                froglinEntity.level().addFreshEntity(llamaspit);
            }


            getNavigation().recomputePath();
        }
    }
     class FroglinSwallowAttackGoal extends Goal {
        private final FroglinEntity froglinEntity;
        private LivingEntity attackTarget;

        public FroglinSwallowAttackGoal(FroglinEntity p_i45837_1_) {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
            this.froglinEntity = p_i45837_1_;
        }

        public boolean canUse() {
            this.attackTarget = this.froglinEntity.getTarget();
            return attackTarget != null && this.froglinEntity.attackID == 0 && (distanceTo(attackTarget) <= 4.0D && random.nextInt(10) == 0 && attackTarget.getBbHeight() < 1F && !isFull());
        }

        public void start() {
            this.froglinEntity.setAttackID(5);
        }

        public void stop() {
            this.froglinEntity.setAttackID(0);
            this.attackTarget = null;
        }


        @Override
        public boolean canContinueToUse() {
            return froglinEntity.attacktick < 20;
        }

         @Override
         public boolean requiresUpdateEveryTick() {
             return true;
         }

         public void tick() {
                if (froglinEntity.attacktick == 2) {

                    float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                    float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));

                    push(f1 * 0.4, 0, f2 * 0.4);
                }
                 if (attacktick == 9) {
                     setDeltaMovement((attackTarget.getX() - getX()) * 0.21D, 0.5D, (attackTarget.getZ() - getZ()) * 0.21D);
                 }
                if (froglinEntity.attacktick >= 14 && froglinEntity.distanceTo(attackTarget) <= 4F && attackTarget != null) {
                    setFull(true);
                    froglinEntity.gameEvent(GameEvent.EAT);
                    froglinEntity.playSound(SoundEvents.GENERIC_EAT, froglinEntity.getSoundVolume(), froglinEntity.getVoicePitch());
                    attackTarget.discard();
                }
            getNavigation().recomputePath();
        }
    }
    class FroglinJumpAttackGoal extends Goal {
        private final FroglinEntity froglinEntity;
        private LivingEntity attackTarget;

        public FroglinJumpAttackGoal(FroglinEntity p_i45837_1_) {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
            this.froglinEntity = p_i45837_1_;
        }

        public boolean canUse() {
            this.attackTarget = this.froglinEntity.getTarget();
            return attackTarget != null && this.froglinEntity.attackID == 0 && (distanceTo(attackTarget) > 4.0D && onGround() && random.nextInt(15) == 0);
        }

        public void start() {
            this.froglinEntity.setAttackID(3);
        }

        public void stop() {
            this.froglinEntity.setAttackID(0);
            this.attackTarget = null;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return froglinEntity.attacktick < 20;
        }


        public void tick() {
            if (froglinEntity.attacktick == 2) {
                float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));
                push(f1 * 0.4, 0, f2 * 0.4);
            }
            if (attacktick == 9) {
             setDeltaMovement((attackTarget.getX() - getX()) * 0.21D, 0.5D, (attackTarget.getZ() - getZ()) * 0.21D);
            }
            if (froglinEntity.attacktick >= 14) {
                meleeattack();
            }
            getNavigation().recomputePath();
        }
    }
    static class FroglinGoToBeachGoal extends MoveToBlockGoal {
        private final FroglinEntity froglinEntity;

        public FroglinGoToBeachGoal(FroglinEntity p_32409_, double p_32410_) {
            super(p_32409_, p_32410_, 8, 2);
            this.froglinEntity = p_32409_;
        }

        public boolean canUse() {
            return super.canUse() && this.froglinEntity.level().isRaining() && this.froglinEntity.isInWater() && this.froglinEntity.getY() >= (double)(this.froglinEntity.level().getSeaLevel() - 3);
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        protected boolean isValidTarget(LevelReader p_32413_, BlockPos p_32414_) {
            BlockPos blockpos = p_32414_.above();
            return p_32413_.isEmptyBlock(blockpos) && p_32413_.isEmptyBlock(blockpos.above()) ? p_32413_.getBlockState(p_32414_).entityCanStandOn(p_32413_, p_32414_, this.froglinEntity) : false;
        }

        public void start() {
            this.froglinEntity.setSearchingForLand(false);
            super.start();
        }

        public void stop() {
            super.stop();
        }
    }
    static class FroglinSwimUpGoal extends Goal {
        private final FroglinEntity froglinEntity;
        private final double speedModifier;
        private final int seaLevel;
        private boolean stuck;

        public FroglinSwimUpGoal(FroglinEntity p_32440_, double p_32441_, int p_32442_) {
            this.froglinEntity = p_32440_;
            this.speedModifier = p_32441_;
            this.seaLevel = p_32442_;
        }

        public boolean canUse() {
            return (this.froglinEntity.level().isRaining() || this.froglinEntity.isInWater())&& this.froglinEntity.getY() < (double)(this.seaLevel - 2);
        }

        public boolean canContinueToUse() {
            return this.canUse() && !this.stuck;
        }

        public void tick() {
            if (this.froglinEntity.getY() < (double)(this.seaLevel - 1) && (this.froglinEntity.getNavigation().isDone() || this.froglinEntity.closeToNextPos())) {
                Vec3 vec3 = DefaultRandomPos.getPosTowards(this.froglinEntity, 4, 8, new Vec3(this.froglinEntity.getX(), (double)(this.seaLevel - 1), this.froglinEntity.getZ()), (double)((float)Math.PI / 2F));
                if (vec3 == null) {
                    this.stuck = true;
                    return;
                }

                this.froglinEntity.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.speedModifier);
            }

        }


        public void start() {
            this.froglinEntity.setSearchingForLand(true);
            this.stuck = false;
        }

        public void stop() {
            this.froglinEntity.setSearchingForLand(false);
        }
    }
    public void setSearchingForLand(boolean p_32399_) {
        this.searchingForLand = p_32399_;
    }
    protected boolean closeToNextPos() {
        Path path = this.getNavigation().getPath();
        if (path != null) {
            BlockPos blockpos = path.getTarget();
            if (blockpos != null) {
                double d0 = this.distanceToSqr((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
                if (d0 < 4.0D) {
                    return true;
                }
            }
        }
        return false;
    }
    static class FroglinMoveControl extends MoveControl {
        private final FroglinEntity froglinEntity;

        public FroglinMoveControl(FroglinEntity p_32433_) {
            super(p_32433_);
            this.froglinEntity = p_32433_;
        }

        public void tick() {
            LivingEntity livingentity = this.froglinEntity.getTarget();
            if (this.froglinEntity.wantsToSwim() && this.froglinEntity.isInWater()) {
                if (livingentity != null && livingentity.getY() > this.froglinEntity.getY() || this.froglinEntity.searchingForLand) {
                    this.froglinEntity.setDeltaMovement(this.froglinEntity.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
                }

                if (this.operation != MoveControl.Operation.MOVE_TO || this.froglinEntity.getNavigation().isDone()) {
                    this.froglinEntity.setSpeed(0.0F);
                    return;
                }

                double d0 = this.wantedX - this.froglinEntity.getX();
                double d1 = this.wantedY - this.froglinEntity.getY();
                double d2 = this.wantedZ - this.froglinEntity.getZ();
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 /= d3;
                float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.froglinEntity.setYRot(this.rotlerp(this.froglinEntity.getYRot(), f, 90.0F));
                this.froglinEntity.yBodyRot = this.froglinEntity.getYRot();
                float f1 = (float)(this.speedModifier * this.froglinEntity.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float f2 = Mth.lerp(0.125F, this.froglinEntity.getSpeed(), f1);
                this.froglinEntity.setSpeed(f2);
                this.froglinEntity.setDeltaMovement(this.froglinEntity.getDeltaMovement().add((double)f2 * d0 * 0.005D, (double)f2 * d1 * 0.1D, (double)f2 * d2 * 0.005D));
            } else {
                if (!this.froglinEntity.onGround()) {
                    this.froglinEntity.setDeltaMovement(this.froglinEntity.getDeltaMovement().add(0.0D, -0.008D, 0.0D));
                }

                super.tick();
            }

        }
    }


}
