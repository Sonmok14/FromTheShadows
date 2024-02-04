package net.sonmok14.fromtheshadows.server.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sonmok14.fromtheshadows.server.config.FTSConfig;
import net.sonmok14.fromtheshadows.server.entity.ai.*;
import net.sonmok14.fromtheshadows.server.entity.projectiles.CoralThornEntity;
import net.sonmok14.fromtheshadows.server.entity.projectiles.ScreenShakeEntity;
import net.sonmok14.fromtheshadows.server.utils.registry.SoundRegistry;
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

public class BulldrogiothEntity extends Monster implements Enemy, GeoEntity, ISemiAquatic {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(BulldrogiothEntity.class, EntityDataSerializers.INT);
    public float growlingProgress;
    public int attacktick;
    public int biteCooldown;
    public int comboCooldown;
    public int coralthornCooldown;
    public int coralthorncomboCooldown;
    public int attackID;

    private static final EntityDataAccessor<Boolean> IS_RIGHT = SynchedEntityData.defineId(BulldrogiothEntity.class, EntityDataSerializers.BOOLEAN);

    boolean searchingForLand;
    private boolean isLandNavigator;
    public float SwimProgress = 0;
    public float prevSwimProgress = 0;

    public static final byte MELEE_ATTACK = 1;
    public static final byte CLAW = 2;
    public static final byte COMBO = 3;
    public static final byte BITE = 4;
    public static final byte CORAL_THORN = 5;
    public static final byte CORAL_THORN_COMBO = 6;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public BulldrogiothEntity(EntityType<BulldrogiothEntity> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        switchNavigator(false);
        this.moveControl = new BulldrogiothMoveControl(this);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.LEAVES, 0.0F);
        this.xpReward = 30;
    }
    public void setAttackID(int id) {
        this.attackID = id;
        this.attacktick = 0;
        this.level().broadcastEntityEvent(this, (byte) -id);
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
        this.entityData.define(VARIANT, 0);
        this.entityData.define(IS_RIGHT, false);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT).intValue();
    }

    public boolean isRight() {
        return this.entityData.get(IS_RIGHT);
    }

    public void setRight(boolean p_32759_) {
        this.entityData.set(IS_RIGHT, p_32759_);
    }
    private static boolean isBiomeSwamp(LevelAccessor worldIn, BlockPos position) {
        return worldIn.getBiome(position).is(Biomes.SWAMP) || worldIn.getBiome(position).is(Biomes.MANGROVE_SWAMP);
    }


    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if(this.isBiomeSwamp(worldIn, this.blockPosition())){
            this.setVariant(2);
        }else{
            this.setVariant(this.getRandom().nextInt(2));
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }
    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Integer.valueOf(variant));
    }


    @Override
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
    public void handleEntityEvent(byte id) {
        if (id <= 0) {

            this.attackID = Math.abs(id);
            this.attacktick = 0;
        } else {
            super.handleEntityEvent(id);
        }
    }


    protected PathNavigation createNavigation(Level worldIn) {
        return new WaterBoundPathNavigation(this, worldIn);
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

    @Override
    public int getWaterSearchRange() {
        return 32;

    }


    @Override
    public boolean canRiderInteract() {
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

    boolean wantsToSwim() {
        if (this.searchingForLand) {
            return true;
        } else {
            LivingEntity livingentity = this.getTarget();
            return livingentity != null && livingentity.isInWater();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
                new AnimationController<>(this, "controller", 7, event -> {
                    if (dead || getHealth() < 0.01 || isDeadOrDying()) {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bulldrogioth.death"));
                    }
                        if (this.wasEyeInWater && attackID == 0) {
                            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bulldrogioth.swim"));
                        }
                        if (event.isMoving() && !isAggressive() && attackID == 0) {
                            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bulldrogioth.walk"));
                        }
                        if (attackID != 0) {
                            event.resetCurrentAnimation();
                        }
                        if (this.wasEyeInWater && this.walkAnimation.speed() > 0.35F && isAggressive() && attackID == 0) {
                            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bulldrogioth.swim"));
                        }

                        if (this.walkAnimation.speed() > 0.35F && isAggressive() && attackID == 0) {
                            event.getController().setAnimationSpeed(1.25D);
                            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bulldrogioth.walk"));
                        }
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bulldrogioth.idle"));
                }));

        controllerRegistrar.add(
                new AnimationController<>(this, "attack", 15, event -> {
                    if(isAlive()) {
                        if (attackID == 0) {
                            event.resetCurrentAnimation();
                        }
                        if (attackID == 1) {

                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.bulldrogioth.normal"));
                        }
                        if (attackID == 2 && !isRight()) {
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.bulldrogioth.claw_left"));
                        }
                        if (attackID == 2 && isRight()) {
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.bulldrogioth.claw_right"));
                        }
                        if (attackID == 5 && isRight()) {
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.bulldrogioth.thorn_right"));
                        }
                        if (attackID == 5 && !isRight()) {
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.bulldrogioth.thorn_left"));
                        }

                        if(this.attackID == 3) {
                            if(isRight()) {
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.bulldrogioth.combo_right"));
                        }
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.bulldrogioth.combo_left"));
                        }
                        if(this.attackID == 4) {
                            if(isRight()) {
                                return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.bulldrogioth.bite_right"));
                            }
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.bulldrogioth.bite_left"));
                        }
                        if(this.attackID == 6) {
                            if(isRight()) {
                                return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.bulldrogioth.thorn_combo_right"));
                            }
                            return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.bulldrogioth.thorn_combo_left"));
                        }
                    }
                    return PlayState.STOP;
                }).setSoundKeyframeHandler(event -> {
                    if (event.getKeyframeData().getSound().matches("attackkey"))
                        if (this.level().isClientSide)
                            this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundRegistry.BULLDROGIOTH_ATTACK.get(), SoundSource.HOSTILE, 0.5F, getVoicePitch() + this.getRandom().nextFloat() * 0.1F, false);
                            if (event.getKeyframeData().getSound().matches("combokey"))
                                if (this.level().isClientSide)
                                    this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundRegistry.BULLDROGIOTH_ATTACK.get(), SoundSource.HOSTILE, 0.5F, 0.3f + this.getRandom().nextFloat() * 0.1F, false);
                }
                        ));

        controllerRegistrar.add(
                new AnimationController<>(this, "growling", 20, event -> {
                    if (this.growlingProgress <= 30 && isAlive())
                    {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bulldrogioth.growl"));
                    }
                    return PlayState.STOP;
                }).setSoundKeyframeHandler(event -> {
                    if (event.getKeyframeData().getSound().matches("growlkey"))
                        if (this.level().isClientSide)
                            this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundRegistry.BULLDROGIOTH_IDLE.get(), SoundSource.HOSTILE, 0.5F, getVoicePitch() + this.getRandom().nextFloat() * 0.1F, false);
                }));
        controllerRegistrar.add(
                new AnimationController<>(this, "hurt", 20, event -> {

                    if (this.hurtTime > 0 && isAlive()) {
                        event.getController().setAnimationSpeed(0.5D);
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bulldrogioth.hurt"));
                    }
                    return PlayState.STOP;
                }).setSoundKeyframeHandler(event -> {
                    if (event.getKeyframeData().getSound().matches("hurtkey"))
                        if (this.level().isClientSide)
                            this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundRegistry.BULLDROGIOTH_HURT.get(), SoundSource.HOSTILE, 0.5F, getVoicePitch() + this.getRandom().nextFloat() * 0.1F, false);
                }));
    }



    @Nullable
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_KNOCKBACK, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.MAX_HEALTH, FTSConfig.SERVER.bulldrogioth_health.get())
                .add(Attributes.ATTACK_DAMAGE, FTSConfig.SERVER.bulldrogioth_melee_damage.get())
                .add(Attributes.ARMOR, 15.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 10.0D);
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.BULLDROGIOTH_HURT.get();
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (p_21016_.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(p_21016_, p_21017_);
        }
        if (p_21016_.is(DamageTypeTags.IS_PROJECTILE)) {
            return super.hurt(p_21016_, p_21017_ / 2);
        }
        if (p_21016_.is(DamageTypeTags.IS_FIRE)) {
            return super.hurt(p_21016_, p_21017_ * 2);
        }
        if (p_21016_.is(DamageTypeTags.IS_LIGHTNING)) {
            return super.hurt(p_21016_, p_21017_ * 4);
        }
        if (p_21016_.is(DamageTypeTags.IS_EXPLOSION)) {
            return super.hurt(p_21016_, p_21017_ / 4);
        }
        return super.hurt(p_21016_, p_21017_);
    }

    private void meleeattack() {
        float range = 3f;
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
                if (!(entityHit instanceof BulldrogiothEntity)) {
                    entityHit.hurt(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    strongKnockback(entityHit);
                }
                if (!(entityHit instanceof BulldrogiothEntity) && attackID == BITE) {
                    entityHit.hurt(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) / 3);

                }

            }
        }
    }


    private void rightClaw() {
        float range = 3.5f;
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
                if (!(entityHit instanceof BulldrogiothEntity)) {
                   entityHit.hurt(this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
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
    public boolean doHurtTarget(Entity target) {
        if (!this.level().isClientSide && this.attackID == 0) {
            if (this.random.nextInt(4) != 0) {
                this.attackID = CLAW;
            }
        else
                this.attackID = MELEE_ATTACK;
        }
        return true;
    }


    @Override
    public void tick() {

        super.tick();
        this.setMaxUpStep(1.0F);
        if (this.growlingProgress == 0) {
            this.growlingProgress = 150;
        }
        if(attackID == 0)
        {
            setRight(false);
        }

        if(attackID == 0 && this.getTarget() != null)
        {
            lookAt(getTarget(), 20.0F, 20.0F);
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

        if (this.horizontalCollision && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
            boolean flag = false;
            AABB aabb = this.getBoundingBox().inflate(0.2D);

            for(BlockPos blockpos : BlockPos.betweenClosed(Mth.floor(aabb.minX), Mth.floor(aabb.minY), Mth.floor(aabb.minZ), Mth.floor(aabb.maxX), Mth.floor(aabb.maxY), Mth.floor(aabb.maxZ))) {
                BlockState blockstate = this.level().getBlockState(blockpos);
                Block block = blockstate.getBlock();
                if (block instanceof LeavesBlock) {
                    flag = this.level().destroyBlock(blockpos, true, this) || flag;
                }
            }

            if (!flag && this.onGround()) {
                this.jumpFromGround();
            }
        }

        if (this.attackID != 0) {
            yBodyRot = yHeadRot;
            setYRot(yBodyRot);
            ++this.attacktick;
            if(getTarget() != null)
            {
                lookAt(getTarget(), 30.0F, 30.0F);
            }
        }

        if (this.comboCooldown > 0) {
            --this.comboCooldown;
        }

        if (this.biteCooldown > 0) {
            --this.biteCooldown;
        }

        if (this.coralthornCooldown > 0) {
            --this.coralthornCooldown;
        }

        if (this.coralthorncomboCooldown > 0) {
            --this.coralthorncomboCooldown;
        }



        if (this.biteCooldown == 0 && this.attackID == 4) {
            this.biteCooldown = 150;
        }


        if (this.comboCooldown == 0 && this.attackID == 3) {
            this.comboCooldown = 200;
        }

        if (this.coralthornCooldown == 0 && this.attackID == 5) {
            this.coralthornCooldown = 400;
        }

        if (this.coralthorncomboCooldown == 0 && this.attackID == 6) {
            this.coralthorncomboCooldown = 600;
        }

        if (this.growlingProgress > 0) {
            --this.growlingProgress;
        }

    }

    protected float nextStep() {
        if(this.isAggressive())
        {
            return this.moveDist + 2.5F;
        }
        else
            return this.moveDist + 3F;
    }

    protected void playStepSound(BlockPos p_33350_, BlockState p_33351_) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundRegistry.STOMP.get(),SoundSource.HOSTILE, 0.25F, 0.35F + this.getRandom().nextFloat() * 0.1F);
    }

    public static <T extends Mob> boolean canBulldrogiothSpawn(EntityType<BulldrogiothEntity> entityType, ServerLevelAccessor iServerWorld, MobSpawnType reason, BlockPos pos, RandomSource random) {

        return reason == MobSpawnType.SPAWNER || iServerWorld.canSeeSky(pos) && checkMonsterSpawnRules(entityType, iServerWorld, reason, pos, random);
    }

    @Override
    public boolean isAlliedTo(Entity p_32665_) {
        if (p_32665_ == null) {
            return false;
        } else if (p_32665_ == this) {
            return true;
        }
        else if (p_32665_ instanceof Warden) {
            return true;
        }
        else if (p_32665_ instanceof BulldrogiothEntity) {
            return true;
        }
        else if (p_32665_ instanceof NehemothEntity) {
            return true;
        }else if (super.isAlliedTo(p_32665_)) {
            return true;
        }else
            return false;
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    public void coralThorn()
    {
       if(this.getTarget() != null) {
           int count = 3;
           double offsetangle = Math.toRadians(20);

           double d1 = getTarget().getX() - this.getX();
           double d2 = getTarget().getY() - this.getY();
           double d3 = getTarget().getZ() - this.getZ();
           for (int i = 0; i <= (count - 1); ++i) {
               double angle = (i - ((count - 1) / 4)) * offsetangle;



               CoralThornEntity coralThornEntity = new CoralThornEntity(this.level(), this, null);

               double f0 = getTarget().getX() - this.getX();
               double f1 = getTarget().getY(0.3333333333333333D) - coralThornEntity.getY();
               double f2 = getTarget().getZ() - this.getZ();
               double f3 = Math.sqrt(f0 * f0 + f2 * f2);
               double x = d1 * Math.cos(angle) + d3 * Math.sin(angle);
               double z = -d1 * Math.sin(angle) + d3 * Math.cos(angle);

               coralThornEntity.shoot(x, f1 + f3 * (double) 0.2F, z, 1.5F, (float)(16 - this.level().getDifficulty().getId() * 4));
               this.level().addFreshEntity(coralThornEntity);
           }
       }
    }

    public void coralThornFive()
    {
        if(this.getTarget() != null) {
            int count = 6;
            double offsetangle = Math.toRadians(15);

            double d1 = getTarget().getX() - this.getX();
            double d2 = getTarget().getY() - this.getY();
            double d3 = getTarget().getZ() - this.getZ();
            for (int i = 0; i <= (count - 1); ++i) {
                double angle = (i - ((count - 1) / 4)) * offsetangle;



                CoralThornEntity coralThornEntity = new CoralThornEntity(this.level(), this, null);

                double f0 = getTarget().getX() - this.getX();
                double f1 = getTarget().getY(0.3333333333333333D) - coralThornEntity.getY();
                double f2 = getTarget().getZ() - this.getZ();
                double f3 = Math.sqrt(f0 * f0 + f2 * f2);
                double x = d1 * Math.cos(angle) + d3 * Math.sin(angle);
                double z = -d1 * Math.sin(angle) + d3 * Math.cos(angle);

                coralThornEntity.shoot(x, f1 + f3 * (double) 0.2F, z, 1.5F, (float)(16 - this.level().getDifficulty().getId() * 4));
                this.level().addFreshEntity(coralThornEntity);
            }
        }
    }

    @Deprecated
    public static boolean canDestroy(BlockState p_31492_) {
        return !p_31492_.isAir() && !p_31492_.is(BlockTags.WITHER_IMMUNE);
    }
    public void breakBlock()
    {
        if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
            int j1 = Mth.floor(this.getY());
            int i2 = Mth.floor(this.getX());
            int j2 = Mth.floor(this.getZ());
            boolean flag = false;

            for(int j = -1; j <= 1; ++j) {
                for(int k2 = -1; k2 <= 1; ++k2) {
                    for(int k = 0; k <= 4; ++k) {
                        int l2 = i2 + j;
                        int l = j1 + k;
                        int i1 = j2 + k2;
                        BlockPos blockpos = new BlockPos(l2, l, i1);
                        BlockState blockstate = this.level().getBlockState(blockpos);
                        if (blockstate.canEntityDestroy(this.level(), blockpos, this) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                            flag = this.level().destroyBlock(blockpos, true, this) || flag;
                        }
                    }
                }
            }
            if (flag) {
                this.level().levelEvent((Player)null, 1022, this.blockPosition(), 0);
            }
        }
    }


    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return null;
    }

    @Override
    protected void registerGoals() {
        this.targetSelector.addGoal(0, (new CoralThornComboGoal(this)));
        this.targetSelector.addGoal(0, (new CoralThornGoal(this)));
        this.targetSelector.addGoal(0, (new BiteAttackGoal()));
        this.targetSelector.addGoal(0, (new ComboAttackGoal()));
        this.targetSelector.addGoal(0, (new RightClawAttackGoal()));
        this.targetSelector.addGoal(0, (new BulldrogiothMeleeAttackGoal()));
        this.goalSelector.addGoal(5, new BulldrogiothGoToBeachGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new BulldrogiothSwimUpGoal(this, 1.3D, this.level().getSeaLevel()));
        this.goalSelector.addGoal(8, new MobAIFindWater(this,1.0D));
        this.goalSelector.addGoal(8, new MobAILeaveWater(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Axolotl.class, true));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.7D, 25, true));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
            this.moveRelative(0.4F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.1D));
        }
        if (this.attackID != 0) {
            if (this.getNavigation().getPath() != null) {
                this.getNavigation().stop();
            }
            travelVector = Vec3.ZERO;
            super.travel(travelVector);
            return;
        }
        super.travel(travelVector);

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

    public boolean checkSpawnObstruction(LevelReader p_32829_) {
        return p_32829_.isUnobstructed(this);
    }

    class BulldrogiothMeleeAttackGoal extends Goal {

        private LivingEntity attackTarget;

        public BulldrogiothMeleeAttackGoal() {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));

        }
        @Override
        public boolean canUse() {
            this.attackTarget = getTarget();
            return attackTarget != null && attackID == 1;
        }

        @Override
        public void start() {
            setAttackID(1);
        }

        @Override
        public void stop() {
            setAttackID(0);
            this.attackTarget = null;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }
        
        @Override
        public boolean canContinueToUse() {
            return attacktick < 45;
        }

        @Override
        public void tick() {
            if (attacktick < 45 && attackTarget.isAlive()) {
                lookAt(attackTarget, 30.0F, 30.0F);
            }

                if (attacktick == 29) {
                    float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                    float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));
                    push(f1 * 1, 0, f2 * 1);
                }
                if (attacktick == 32) {
                    breakBlock();
                    meleeattack();
                    ScreenShakeEntity.ScreenShake(level(), position(), 5, 0.4f, 5, 3);
                    playSound(SoundEvents.WITHER_BREAK_BLOCK, 1.0F, 1.0F);
                }


            double dist = (double)distanceTo(attackTarget);
            if (attacktick == 32 && dist <= 4 && attackTarget != null) {
                strongKnockback(attackTarget);
                attackTarget.hurt(damageSources().mobAttack(BulldrogiothEntity.this), (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
                if (!attackTarget.hurt(damageSources().mobAttack(BulldrogiothEntity.this), (float) getAttributeValue(Attributes.ATTACK_DAMAGE))) {
                    if (getTarget() instanceof Player) {
                        Player player = (Player) getTarget();
                        if (player.isBlocking())
                            player.disableShield(true);
                    }
                }
            }

        }
    }


    private void strongKnockback(Entity p_33340_) {
        double d0 = p_33340_.getX() - this.getX();
        double d1 = p_33340_.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_33340_.push(d0 / d2 * 2.0D, 0.2D, d1 / d2 * 2.0D);
    }

    class RightClawAttackGoal extends Goal {
        private LivingEntity attackTarget;
        public RightClawAttackGoal() {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }
        @Override
        public boolean canUse() {
            this.attackTarget = getTarget();
            return attackTarget != null && attackID == 2;
        }
        @Override
        public void start() {
            setRight(random.nextInt(2) != 0);
            setAttackID(2);
        }
        @Override
        public void stop() {
            setAttackID(0);
            this.attackTarget = null;
        }
        public boolean requiresUpdateEveryTick() {
            return true;
        }
        @Override
        public boolean canContinueToUse() {
            return attacktick < 36;
        }
        @Override
        public void tick() {
            double dist = distanceTo(attackTarget);
            if (attackID == 2) {
                if (attacktick == 17) {
                    float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                    float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));
                    push(f1 * -0.2, 0.0, f2 * 0.2);
                }
                if (attacktick == 20) {
                    float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                    float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));
                    push(f1 * 0.4, 0.0, f2 * 0.4);
                }
                if (attacktick == 23) {
                    rightClaw();
                    breakBlock();
                }
            }
            if (attacktick == 23 && dist <= 4 && attackTarget != null) {
                attackTarget.hurt(damageSources().mobAttack(BulldrogiothEntity.this), (float) getAttributeValue(Attributes.ATTACK_DAMAGE) / 2);
            }
        }
    }

    class CoralThornComboGoal extends Goal {
        private final BulldrogiothEntity mob;
        private LivingEntity attackTarget;
        public CoralThornComboGoal(BulldrogiothEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }
        @Override
        public boolean canUse() {
            this.attackTarget = getTarget();
            return  attackTarget != null && attackID == 0 && distanceTo(attackTarget) <= 1024 && random.nextInt(16) == 0 && coralthorncomboCooldown == 0 && mob.getHealth() <= mob.getMaxHealth() / 2;
        }
        @Override
        public void start() {
            setRight(random.nextInt(2) != 0);
            setAttackID(6);
        }
        @Override
        public void stop() {
            setAttackID(0);
            attackTarget = null;
        }
        public boolean requiresUpdateEveryTick() {
            return true;
        }
        @Override
        public boolean canContinueToUse() {
            return attacktick < 65;
        }
        @Override
        public void tick() {
            if (attacktick == 20) {
                float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));
                push(f1 * 1, 0.0, f2 * 1);
            }
            if (attacktick == 23) {
                coralThornFive();
            }
            if (attacktick == 39) {
                float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));
                push(f1 * 1, 0.0, f2 * 1);
            }
            if (attacktick == 42) {
                coralThornFive();
            }
        }
    }

    class CoralThornGoal extends Goal {
        private final BulldrogiothEntity mob;
        private LivingEntity attackTarget;
        public CoralThornGoal(BulldrogiothEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }
        @Override
        public boolean canUse() {
            this.attackTarget = getTarget();
            return  attackTarget != null && attackID == 0 && distanceTo(attackTarget) <= 1024 && random.nextInt(8) == 0 && coralthornCooldown == 0;
        }
        @Override
        public void start() {
            setRight(random.nextInt(2) != 0);
            setAttackID(5);
        }
        @Override
        public void stop() {
            setAttackID(0);
            attackTarget = null;
        }
        public boolean requiresUpdateEveryTick() {
            return true;
        }
        @Override
        public boolean canContinueToUse() {
            return attacktick < 45;
        }
        @Override
        public void tick() {
            if (attacktick == 20) {
                float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));
                push(f1 * 1, 0.0, f2 * 1);
            }
            if (attacktick == 23) {
                coralThorn();
                }
        }
    }

    class ComboAttackGoal extends Goal {
        private LivingEntity attackTarget;
        public ComboAttackGoal() {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }
        @Override
        public boolean canUse() {
            this.attackTarget = getTarget();
            return  attackTarget != null && attackID == 0 && distanceTo(attackTarget) <= 4.5 && random.nextInt(4) == 0 && comboCooldown == 0;
        }
        @Override
        public void start() {
            setRight(random.nextInt(2) != 0);
            setAttackID(3);
        }
        @Override
        public void stop() {
            setAttackID(0);
           attackTarget = null;
        }
        public boolean requiresUpdateEveryTick() {
            return true;
        }
        @Override
        public boolean canContinueToUse() {
            return attacktick < 60;
        }
        @Override
        public void tick() {
            double dist = distanceTo(attackTarget);
            if (attacktick == 25) {
                float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));
                push(f1 * 0.4, 0.0, f2 * 0.4);
            }
            if (attacktick == 28) {
                rightClaw();
                breakBlock();
            }
            if (attacktick == 40) {
                float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));
                push(f1 * 0.4, 0.0, f2 * 0.4);
            }
            if (attacktick == 43) {
                rightClaw();
                breakBlock();
            }
            if (attacktick == 28 && dist <= 4 && attackTarget != null) {
                attackTarget.hurt(damageSources().mobAttack(BulldrogiothEntity.this), (float) getAttributeValue(Attributes.ATTACK_DAMAGE) / 2);
            }
            if (attacktick == 43 && dist <= 4 && attackTarget != null) {
                attackTarget.hurt(damageSources().mobAttack(BulldrogiothEntity.this), (float) getAttributeValue(Attributes.ATTACK_DAMAGE) / 2);
            }
        }
        }

    class BiteAttackGoal extends Goal {
        private LivingEntity attackTarget;
        public BiteAttackGoal() {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }
        @Override
        public boolean canUse() {
            this.attackTarget = getTarget();
            return  attackTarget != null && attackID == 0 && distanceTo(attackTarget) <= 6 && random.nextInt(2) == 0 && biteCooldown == 0;
        }
        @Override
        public void start() {
            setRight(random.nextInt(2) != 0);
            setAttackID(4);
        }
        @Override
        public void stop() {
            setAttackID(0);
            attackTarget = null;
        }
        public boolean requiresUpdateEveryTick() {
            return true;
        }
        @Override
        public boolean canContinueToUse() {
            return attacktick < 36;
        }
        @Override
        public void tick() {
                if (attacktick == 20) {
                    float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                    float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));
                    push(f1 * 1.5, 0.0, f2 * 1.5);
                }
                if (attacktick == 23) {
                    meleeattack();
                    breakBlock();
                }
            double dist = distanceTo(attackTarget);
            if (attacktick == 23 && dist <= 4 && attackTarget != null) {
                attackTarget.hurt(damageSources().mobAttack(BulldrogiothEntity.this), (float) getAttributeValue(Attributes.ATTACK_DAMAGE) / 3);
            }
        }
    }

    static class BulldrogiothGoToBeachGoal extends MoveToBlockGoal {
        private final BulldrogiothEntity bulldrogiothEntity;
        public BulldrogiothGoToBeachGoal(BulldrogiothEntity p_32409_, double p_32410_) {
            super(p_32409_, p_32410_, 8, 2);
            this.bulldrogiothEntity = p_32409_;
        }
        public boolean canUse() {
            return super.canUse() && this.bulldrogiothEntity.level().isRaining() && this.bulldrogiothEntity.isInWater() && this.bulldrogiothEntity.getY() >= (double)(this.bulldrogiothEntity.level().getSeaLevel() - 3);
        }
        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }
        protected boolean isValidTarget(LevelReader p_32413_, BlockPos p_32414_) {
            BlockPos blockpos = p_32414_.above();
            return p_32413_.isEmptyBlock(blockpos) && p_32413_.isEmptyBlock(blockpos.above()) ? p_32413_.getBlockState(p_32414_).entityCanStandOn(p_32413_, p_32414_, this.bulldrogiothEntity) : false;
        }
        public void start() {
            this.bulldrogiothEntity.setSearchingForLand(false);
            super.start();
        }
        public void stop() {
            super.stop();
        }
    }

    static class BulldrogiothSwimUpGoal extends Goal {
        private final BulldrogiothEntity bulldrogiothEntity;
        private final double speedModifier;
        private final int seaLevel;
        private boolean stuck;
        public BulldrogiothSwimUpGoal(BulldrogiothEntity p_32440_, double p_32441_, int p_32442_) {
            this.bulldrogiothEntity = p_32440_;
            this.speedModifier = p_32441_;
            this.seaLevel = p_32442_;
        }
        public boolean canUse() {
            return (this.bulldrogiothEntity.level().isRaining() || this.bulldrogiothEntity.isInWater())&& this.bulldrogiothEntity.getY() < (double)(this.seaLevel - 2);
        }
        public boolean canContinueToUse() {
            return this.canUse() && !this.stuck;
        }
        public void tick() {
            if (this.bulldrogiothEntity.getY() < (double)(this.seaLevel - 1) && (this.bulldrogiothEntity.getNavigation().isDone() || this.bulldrogiothEntity.closeToNextPos())) {
                Vec3 vec3 = DefaultRandomPos.getPosTowards(this.bulldrogiothEntity, 4, 8, new Vec3(this.bulldrogiothEntity.getX(), (double)(this.seaLevel - 1), this.bulldrogiothEntity.getZ()), (double)((float)Math.PI / 2F));
                if (vec3 == null) {
                    this.stuck = true;
                    return;
                }
                this.bulldrogiothEntity.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.speedModifier);
            }
        }
        public void start() {
            this.bulldrogiothEntity.setSearchingForLand(true);
            this.stuck = false;
        }
        public void stop() {
            this.bulldrogiothEntity.setSearchingForLand(false);
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

    static class BulldrogiothMoveControl extends MoveControl {
        private final BulldrogiothEntity bulldrogiothEntity;
        public BulldrogiothMoveControl(BulldrogiothEntity p_32433_) {
            super(p_32433_);
            this.bulldrogiothEntity = p_32433_;
        }
        public void tick() {
            LivingEntity livingentity = this.bulldrogiothEntity.getTarget();
            if (this.bulldrogiothEntity.wantsToSwim() && this.bulldrogiothEntity.isInWater()) {
                if (livingentity != null && livingentity.getY() > this.bulldrogiothEntity.getY() || this.bulldrogiothEntity.searchingForLand) {
                    this.bulldrogiothEntity.setDeltaMovement(this.bulldrogiothEntity.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
                }
                if (this.operation != MoveControl.Operation.MOVE_TO || this.bulldrogiothEntity.getNavigation().isDone()) {
                    this.bulldrogiothEntity.setSpeed(0.0F);
                    return;
                }
                double d0 = this.wantedX - this.bulldrogiothEntity.getX();
                double d1 = this.wantedY - this.bulldrogiothEntity.getY();
                double d2 = this.wantedZ - this.bulldrogiothEntity.getZ();
                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                d1 /= d3;
                float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.bulldrogiothEntity.setYRot(this.rotlerp(this.bulldrogiothEntity.getYRot(), f, 90.0F));
                this.bulldrogiothEntity.yBodyRot = this.bulldrogiothEntity.getYRot();
                float f1 = (float)(this.speedModifier * this.bulldrogiothEntity.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float f2 = Mth.lerp(0.125F, this.bulldrogiothEntity.getSpeed(), f1);
                this.bulldrogiothEntity.setSpeed(f2);
                this.bulldrogiothEntity.setDeltaMovement(this.bulldrogiothEntity.getDeltaMovement().add((double)f2 * d0 * 0.005D, (double)f2 * d1 * 0.1D, (double)f2 * d2 * 0.005D));
            } else {
                if (!this.bulldrogiothEntity.onGround()) {
                    this.bulldrogiothEntity.setDeltaMovement(this.bulldrogiothEntity.getDeltaMovement().add(0.0D, -0.008D, 0.0D));
                }
                super.tick();
            }
        }
    }

    }
