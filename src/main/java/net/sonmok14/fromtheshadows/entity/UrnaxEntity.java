package net.sonmok14.fromtheshadows.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.sonmok14.fromtheshadows.FTSConfig;
import net.sonmok14.fromtheshadows.entity.projectiles.FallingBlockEntity;
import net.sonmok14.fromtheshadows.entity.projectiles.ScreenShakeEntity;
import net.sonmok14.fromtheshadows.utils.registry.ParticleRegistry;
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

public class UrnaxEntity extends Monster implements Enemy, GeoEntity {
    public int thunderslamCooldown;
    public int attacktick;
    public int attackID;
    public float clenchProgress;
    public float growlProgress;
    public static final byte PUNCH = 1;
    public static final byte THUNDER_SLAM = 2;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public UrnaxEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
                new AnimationController<>(this, "controller", 7, event -> {
                    if(attackID == THUNDER_SLAM)
                    {
                        if(!onGround()) {
                            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.urnax.slam_flight"));
                        }
                        if(attacktick < 15)
                        {
                            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.urnax.slam_ready"));
                        }
                    }
                    if(event.isMoving() && isAggressive())
                    {
                        event.getController().setAnimationSpeed(1.2D);
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.urnax.run"));
                    }
                    if(event.isMoving())
                    {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.urnax.walk"));
                    }
                    return event.setAndContinue(RawAnimation.begin().thenLoop("animation.urnax.idle"));
                }).setSoundKeyframeHandler(event -> {
                    if (event.getKeyframeData().getSound().matches("stompkey"))
                        if (this.level().isClientSide && !isInWater())
                            this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundRegistry.STOMP.get(), SoundSource.HOSTILE, 0.1F, 0.5F + this.getRandom().nextFloat() * 0.1F, true);
                }));
        controllerRegistrar.add(
                new AnimationController<>(this, "clench", 10, event -> {
                    if(clenchProgress < 20)
                    {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.urnax.clench"));
                    }
                    return PlayState.STOP;
                }));
        controllerRegistrar.add(
                new AnimationController<>(this, "growl", 15, event -> {
                    if(this.growlProgress <= 30)
                    {
                        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.urnax.growl"));
                    }
                    return PlayState.STOP;
                })
                        .setSoundKeyframeHandler(event -> {
                            if (event.getKeyframeData().getSound().matches("growlkey"))
                                if (this.level().isClientSide)
                                    this.getCommandSenderWorld().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundRegistry.BULLDROGIOTH_IDLE.get(), SoundSource.HOSTILE, 0.5F, 2F + this.getRandom().nextFloat() * 0.1F, true);
                        }));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PunchAttackGoal(this));
        this.goalSelector.addGoal(0, new ThunderSlamGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.05, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.6D, 25, true));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        super.registerGoals();
    }
    @Override
    public AnimatableInstanceCache animatableCacheOverride() {
        return GeoEntity.super.animatableCacheOverride();
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
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
    @Override
    public void handleEntityEvent(byte id) {
        if (id <= 0) {
            this.attackID = Math.abs(id);
            this.attacktick = 0;
        } else {
            super.handleEntityEvent(id);
        }
    }
    @Override
    public boolean doHurtTarget(Entity p_85031_1_) {
        if (!this.level().isClientSide && this.attackID == 0) {
            this.attackID = PUNCH;
        }
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getTarget() != null) {
            yBodyRot = yHeadRot;
            setYRot(yBodyRot);
            getLookControl().setLookAt(getTarget(), 15F, 90.0F);
        }
        if (this.attackID != 0) {
            ++this.attacktick;
        }
        if (this.thunderslamCooldown > 0) {
            --this.thunderslamCooldown;
        }
        if (this.thunderslamCooldown == 0 && this.attackID == THUNDER_SLAM) {
            this.thunderslamCooldown = 100;
        }
        if (this.clenchProgress == 0) {
            this.clenchProgress = 250;
        }
        if (this.clenchProgress > 0) {
            --this.clenchProgress;
        }
        if (this.clenchProgress == 5) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundRegistry.SHOCK.get(), SoundSource.HOSTILE, 1F, 0.2F + this.getRandom().nextFloat() * 0.2F);
        }
        if (this.growlProgress == 0) {
            this.growlProgress = 250;
        }
        if (this.growlProgress > 0) {
            --this.growlProgress;
        }
        if (this.growlProgress == 29) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundRegistry.SHOCK.get(), SoundSource.HOSTILE, 1F, 0.2F + this.getRandom().nextFloat() * 0.2F);
        }
    }
    private void thunderEffect() {
            for (int i = 0; i < 4; ++i) {
                this.level().addParticle(ParticleRegistry.LIGHTNING.get(), this.getX(), this.getY(), this.getZ(), getRandom().nextGaussian() * 4D, getRandom().nextGaussian() * 4D, getRandom().nextGaussian() * 4D);
            }
    }
    public void setAttackID(int id) {
        this.attackID = id;
        this.attacktick = 0;
        this.level().broadcastEntityEvent(this, (byte) -id);
    }

    @Override
    protected int calculateFallDamage(float p_21237_, float p_21238_) {
        return 0;
    }

    public void travel(Vec3 travelVector) {
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

    class PunchAttackGoal extends Goal {
        private final UrnaxEntity urnax;
        private LivingEntity attackTarget;

        public PunchAttackGoal(UrnaxEntity p_i45837_1_) {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
            this.urnax = p_i45837_1_;
        }

        public boolean canUse() {
            this.attackTarget = this.urnax.getTarget();
            return attackTarget != null && this.urnax.attackID == PUNCH;
        }

        public void start() {
            this.urnax.setAttackID(PUNCH);
        }

        public void stop() {
            this.urnax.setAttackID(0);
            this.attackTarget = null;
        }
        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return urnax.attacktick < 15;
        }


        public void tick() {
            if (attacktick == 10 && distanceTo(attackTarget) <= 3.5F) {
                attackTarget.hurt(damageSources().mobAttack(urnax), (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
            }
        }
    }

    private void slam(int distance) {
        double perpFacing = this.yBodyRot * (Math.PI / 180);
        double facingAngle = perpFacing + Math.PI / 2;
        int hitY = Mth.floor(this.getBoundingBox().minY - 0.5);
        double spread = Math.PI * 2;
        int arcLen = Mth.ceil(distance * spread);
        for (int i = 0; i < arcLen; i++) {
            double theta = (i / (arcLen - 1.0) - 0.5) * spread + facingAngle;
            double vx = Math.cos(theta);
            double vz = Math.sin(theta);
            double px = this.getX() + vx * distance;
            double pz = this.getZ() + vz * distance;
            if (ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                int hitX = Mth.floor(px);
                int hitZ = Mth.floor(pz);
                BlockPos pos = new BlockPos(hitX, hitY, hitZ);
                BlockPos abovePos = new BlockPos(pos).above();
                BlockState block = level().getBlockState(pos);
                BlockState blockAbove = level().getBlockState(abovePos);

                if (level().getBlockState(pos) != Blocks.AIR.defaultBlockState()) {
                    FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(level(), hitX + 0.5D, hitY + 0.5D, hitZ + 0.5D, block);
                    level().setBlock(pos, block.getFluidState().createLegacyBlock(), 3);
                    fallingBlockEntity.push(getRandom().nextGaussian() * 4, 0.2D + getRandom().nextGaussian() * 0.5D, getRandom().nextGaussian() * 4);
                    level().addFreshEntity(fallingBlockEntity);
                }

            }
        }

    }
    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {

        if (p_21016_.is(DamageTypeTags.IS_LIGHTNING)) {
            return false;
        }
        if (p_21016_.is(DamageTypeTags.IS_EXPLOSION)) {
            return false;
        }
        return super.hurt(p_21016_, p_21017_);
    }

    class ThunderSlamGoal extends Goal {
        private final UrnaxEntity urnax;
        private LivingEntity attackTarget;

        public ThunderSlamGoal(UrnaxEntity p_i45837_1_) {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
            this.urnax = p_i45837_1_;
        }

        public boolean canUse() {
            this.attackTarget = this.urnax.getTarget();
            return attackTarget != null && this.urnax.thunderslamCooldown == 0 && this.urnax.attackID == 0 && distanceTo(attackTarget) > 8.0D && onGround() && random.nextInt(2) == 0 && !horizontalCollision ;
        }

        public void start() {
            this.urnax.setAttackID(THUNDER_SLAM);
        }

        public void stop() {
            ScreenShakeEntity.ScreenShake(level(), position(), 15, 0.2f, 0, 10);
                BlockPos blockpos = urnax.blockPosition();
                slam(4);
                if (urnax.level().canSeeSky(blockpos)) {
                    LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(urnax.level());
                    if (lightningbolt != null) {
                        lightningbolt.moveTo(Vec3.atBottomCenterOf(blockpos));
                        urnax.level().addFreshEntity(lightningbolt);
                    }
                }
            playSound(SoundEvents.GENERIC_EXPLODE, 2f, 0.2F + getRandom().nextFloat() * 0.1F);
            this.urnax.setAttackID(0);
            this.attackTarget = null;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return urnax.attacktick < 48 && attackID == THUNDER_SLAM;
        }

        public void tick() {
            if(attackTarget != null)
                if (attacktick < 48) {
                    if (attackTarget != null) {
                        getLookControl().setLookAt(attackTarget,30F, 90.0F);
                    }
                }
            if (attacktick == 2) {
                float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));

                push(f1 * 0.4, 0, f2 * 0.4);
            }

            if (attacktick == 14) {
                setDeltaMovement((attackTarget.getX() - getX()) * 0.21D, 0.8D, (attackTarget.getZ() - getZ()) * 0.21D);
            }
       if(attacktick > 15 && urnax.onGround()){
          stop();
       }
            getNavigation().recomputePath();
        }
    }
}
