package net.sonmok14.fromtheshadows.server.entity;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.sonmok14.fromtheshadows.server.config.FTSConfig;
import net.sonmok14.fromtheshadows.server.entity.projectiles.ThrowingDaggerEntity;
import net.sonmok14.fromtheshadows.server.utils.registry.EffectRegistry;
import net.sonmok14.fromtheshadows.server.utils.registry.EnchantmentRegistry;
import net.sonmok14.fromtheshadows.server.utils.registry.ItemRegistry;
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
import java.util.Map;
import java.util.function.Predicate;

public class ClericEntity extends AbstractIllager implements GeoEntity {
    static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = (p_34082_) -> {
        return p_34082_ == Difficulty.NORMAL || p_34082_ == Difficulty.HARD;
    };
    public int throwingdaggercooldown;
    public int attackID;
    public int attacktick;
    public float mumbleProgress;
    public static final byte MELEE_ATTACK = 1;
    public static final byte THROWING_ATTACK = 2;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ClericEntity(EntityType<ClericEntity> p_32105_, Level p_32106_) {
        super(p_32105_, p_32106_);
    }


    @Nullable
    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.ATTACK_KNOCKBACK, 1D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.MAX_HEALTH, FTSConfig.SERVER.cleric_health.get())
                .add(Attributes.ATTACK_DAMAGE, FTSConfig.SERVER.cleric_melee_damage.get())
                .add(Attributes.ARMOR, 2.0D);
    }


    protected void customServerAiStep() {
        if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
            boolean flag = ((ServerLevel)this.level()).isRaided(this.blockPosition());
            ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(flag);
        }

        super.customServerAiStep();
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
                new AnimationController<>(this, "controller", 7, event -> {
                    event.getController().setAnimationSpeed(0.5D);
                    if (attackID == 2 && attacktick <= 13) {
                        event.getController().setAnimationSpeed(1D);
                        return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.cultist.throw_dagger_ready"));
                    }
                    if (attackID == 2 && attacktick > 13) {
                        event.getController().setAnimationSpeed(1D);
                        return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.cultist.throw_dagger"));
                    }
                    if (attackID == 1) {
                        event.getController().setAnimationSpeed(1D);
                        return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation.cultist.melee_attack"));
                    }
                    if(this.attackID == 0)
                    if (this.walkAnimation.speed() > 0.35F && isAggressive()) {
                        event.getController().setAnimationSpeed(1D);
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

    public boolean canBeAffected(MobEffectInstance p_31495_) {
        return p_31495_.getEffect() == EffectRegistry.PLAGUE.get() ? false : super.canBeAffected(p_31495_);
    }
    public MobType getMobType() {
        return MobType.ILLAGER;
    }
    @Override
    public void tick() {
        super.tick();

        this.xpReward = 10;
        if (!level().isClientSide) {
            this.removeEffect(EffectRegistry.PLAGUE.get());
        }

        if (this.throwingdaggercooldown > 0) {
            --this.throwingdaggercooldown;
        }
        if (this.throwingdaggercooldown == 0 && this.attackID == 2) {
            this.throwingdaggercooldown = 200;
        }
        if (this.attackID != 0) {
            yBodyRot = yHeadRot;
            setYRot(yBodyRot);
            ++this.attacktick;
            if(getTarget() != null) {
                getLookControl().setLookAt(getTarget(), 30F, 90.0F);
            }
        }
        if (this.mumbleProgress > 0) {
            --this.mumbleProgress;
        }
        if (this.mumbleProgress == 0) {
            this.mumbleProgress = 200;
        }
    }

    public void setAttackID(int id) {
        this.attackID = id;
        this.attacktick = 0;
        this.level().broadcastEntityEvent(this, (byte) -id);
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
        else {
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
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34088_, DifficultyInstance p_34089_, MobSpawnType p_34090_, @javax.annotation.Nullable SpawnGroupData p_34091_, @javax.annotation.Nullable CompoundTag p_34092_) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(p_34088_, p_34089_, p_34090_, p_34091_, p_34092_);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        RandomSource randomsource = p_34088_.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, p_34089_);
        this.populateDefaultEquipmentEnchantments(randomsource, p_34089_);
        return spawngroupdata;
    }
    protected void populateDefaultEquipmentSlots(RandomSource p_219149_, DifficultyInstance p_219150_) {
        if (this.getCurrentRaid() == null) {
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.PLAGUE_DOCTOR_MASK.get()));
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        }

    }
    @Override
    public boolean doHurtTarget(Entity p_85031_1_) {
        if (!this.level().isClientSide && this.attackID == 0) {
                this.attackID = MELEE_ATTACK;
        }
        return true;
    }
    @Override
    public boolean canBeLeader() {
        return false;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundRegistry.CULTIST_HURT.get();
    }
    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.CULTIST_DEATH.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return SoundEvents.EVOKER_CELEBRATE;
    }


    public boolean isAlliedTo(Entity p_34110_) {
        if (super.isAlliedTo(p_34110_)) {
            return true;
        } else if (p_34110_ instanceof LivingEntity && ((LivingEntity)p_34110_).getMobType() == MobType.ILLAGER) {
            return this.getTeam() == null && p_34110_.getTeam() == null;
        } else {
            return false;
        }
    }

    public void applyRaidBuffs(int p_34079_, boolean p_34080_) {
        ItemStack itemstack = new ItemStack(Items.IRON_AXE);
        Raid raid = this.getCurrentRaid();
        int i = 1;
        if (p_34079_ > raid.getNumGroups(Difficulty.NORMAL)) {
            i = 2;
        }
        boolean flag = this.random.nextFloat() <= raid.getEnchantOdds();
        if (flag) {
            Map<Enchantment, Integer> map = Maps.newHashMap();
            map.put(EnchantmentRegistry.FREEZING.get(), i);
            EnchantmentHelper.setEnchantments(map, itemstack);
        }

        this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
    }



    public void throwDagger()
    {
        if(this.getTarget() != null) {
            int count = 3;
            double offsetangle = Math.toRadians(5);

            double d1 = getTarget().getX() - this.getX();
            double d2 = getTarget().getY() - this.getY();
            double d3 = getTarget().getZ() - this.getZ();
            for (int i = 0; i <= (count - 1); ++i) {
                double angle = (i - ((count - 1) / 4)) * offsetangle;



                ThrowingDaggerEntity throwingDaggerEntity = new ThrowingDaggerEntity(this.level(), this, null);

                double f0 = getTarget().getX() - this.getX();
                double f1 = getTarget().getY(0.3333333333333333D) - throwingDaggerEntity.getY();
                double f2 = getTarget().getZ() - this.getZ();
                double f3 = Math.sqrt(f0 * f0 + f2 * f2);
                double x = d1 * Math.cos(angle) + d3 * Math.sin(angle);
                double z = -d1 * Math.sin(angle) + d3 * Math.cos(angle);

                throwingDaggerEntity.shoot(x, f1 + f3 * (double) 0.1F, z, 2, (float)(16 - this.level().getDifficulty().getId() * 4));
                this.level().addFreshEntity(throwingDaggerEntity);
            }
        }
    }

    @Override
    protected int calculateFallDamage(float p_21237_, float p_21238_) {
        if(attackID == THROWING_ATTACK)
        {
            return 0;
        }
        return super.calculateFallDamage(p_21237_, p_21238_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClericBreakDoorGoal(this));
        this.goalSelector.addGoal(2, new AbstractIllager.RaiderOpenDoorGoal(this));
        this.goalSelector.addGoal(3, new Raider.HoldGroundAttackGoal(this, 10.0F));
        this.goalSelector.addGoal(0, new ThrowingAttackGoal(this));
        this.goalSelector.addGoal(0, new AxeMeleeAttackGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.7D, 25, true));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    class AxeMeleeAttackGoal extends Goal {
        private final ClericEntity clericEntity;
        private LivingEntity attackTarget;

        public AxeMeleeAttackGoal(ClericEntity p_i45837_1_) {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
            this.clericEntity = p_i45837_1_;
        }

        public boolean canUse() {
            this.attackTarget = this.clericEntity.getTarget();
            return attackTarget != null && this.clericEntity.attackID == 1;
        }

        public void start() {
            this.clericEntity.setAttackID(1);
        }

        public void stop() {
            this.clericEntity.setAttackID(0);
            this.attackTarget = null;
        }


        @Override
        public boolean canContinueToUse() {
            return clericEntity.attacktick < 28;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if(attacktick == 1)
            {
                clericEntity.playSound(SoundRegistry.CULTIST_PREATTACK.get(), 2f, 0.3F + clericEntity.getRandom().nextFloat() * 0.1F);
            }
            if (attacktick == 16) {
                float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));
                clericEntity.playSound(SoundRegistry.CULTIST_ATTACK.get(), 2f, 0.3F + clericEntity.getRandom().nextFloat() * 0.1F);
                push(f1 * 1, 0, f2 * 1);
            }
            if (attacktick == 20 && distanceTo(attackTarget) <= 3.5F) {
                attackTarget.hurt(damageSources().mobAttack(clericEntity), (float) getAttributeValue(Attributes.ATTACK_DAMAGE));
            }
            getNavigation().recomputePath();
        }
    }

    class ThrowingAttackGoal extends Goal {
        private final ClericEntity clericEntity;
        private LivingEntity attackTarget;

        public ThrowingAttackGoal(ClericEntity p_i45837_1_) {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
            this.clericEntity = p_i45837_1_;
        }

        public boolean canUse() {
            this.attackTarget = this.clericEntity.getTarget();
            return  attackTarget != null && attackID == 0 && distanceTo(attackTarget) <= 1024 && random.nextInt(8) == 0 && throwingdaggercooldown == 0;
        }

        public void start() {
            this.clericEntity.setAttackID(2);
        }

        public void stop() {
            this.clericEntity.setAttackID(0);
            this.attackTarget = null;
        }


        @Override
        public boolean canContinueToUse() {
            return clericEntity.attacktick < 30;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if(attacktick == 1)
            {
                clericEntity.playSound(SoundRegistry.CULTIST_PREATTACK.get(), 2f, 0.3F + clericEntity.getRandom().nextFloat() * 0.1F);
            }
            if (attacktick == 16) {
                clericEntity.playSound(SoundRegistry.CULTIST_ATTACK.get(), 2f, 0.3F + clericEntity.getRandom().nextFloat() * 0.1F);
                float f1 = (float) Math.cos(Math.toRadians(getYRot() + 90));
                float f2 = (float) Math.sin(Math.toRadians(getYRot() + 90));

                push(f1 * -1, 0.5, f2 * -1);
            }
            if (attacktick == 18) {
                throwDagger();
            }
            getNavigation().recomputePath();
        }
    }

    static class ClericBreakDoorGoal extends BreakDoorGoal {
        public ClericBreakDoorGoal(Mob p_34112_) {
            super(p_34112_, 6, ClericEntity.DOOR_BREAKING_PREDICATE);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canContinueToUse() {
            ClericEntity clericEntity = (ClericEntity)this.mob;
            return clericEntity.hasActiveRaid() && super.canContinueToUse();
        }

        public boolean canUse() {
            ClericEntity clericEntity = (ClericEntity)this.mob;
            return clericEntity.hasActiveRaid() && clericEntity.random.nextInt(reducedTickDelay(10)) == 0 && super.canUse();
        }

        public void start() {
            super.start();
            this.mob.setNoActionTime(0);
        }
    }
}
