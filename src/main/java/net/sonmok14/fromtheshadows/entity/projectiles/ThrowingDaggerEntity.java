package net.sonmok14.fromtheshadows.entity.projectiles;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.sonmok14.fromtheshadows.FTSConfig;
import net.sonmok14.fromtheshadows.utils.registry.EffectRegistry;
import net.sonmok14.fromtheshadows.utils.registry.EntityRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class ThrowingDaggerEntity extends AbstractArrow implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public ThrowingDaggerEntity(EntityType<ThrowingDaggerEntity> p_36721_, Level p_36722_) {
        super(p_36721_, p_36722_);
    }

    public ThrowingDaggerEntity(Level p_37569_, LivingEntity p_37570_, ItemStack p_37571_) {
        super(EntityRegistry.THROWING_DAGGER.get(), p_37570_, p_37569_);

    }


    @Override
    protected ItemStack getPickupItem() {
        return null;
    }
    protected void defineSynchedData() {
        super.defineSynchedData();

    }

    public void tick() {

        Entity entity = this.getOwner();

        if (this.isNoPhysics() && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 p_37575_, Vec3 p_37576_) {
        return super.findHitEntity(p_37575_, p_37576_);
    }

    protected void onHitEntity(EntityHitResult p_37573_) {
        boolean flag;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        Entity entity = this.getOwner();
        Entity entity2 = p_37573_.getEntity();
        if (entity instanceof LivingEntity livingentity) {
            flag = entity2.hurt(this.damageSources().mobProjectile(this, livingentity), FTSConfig.SERVER.cleric_projectile_damage.get().floatValue() / 2);
            if (flag && entity2 instanceof LivingEntity livingEntity2) {
                if (entity.isAlive() && entity2 != null) {
                    this.playSound(soundevent, 0.5f, 1.0F);
                    livingEntity2.invulnerableTime = 0;
                    livingEntity2.addEffect(new MobEffectInstance(EffectRegistry.PLAGUE.get(), 100), this);
                    discard();
                }
            }

        }
    }




    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }


    public void readAdditionalSaveData(CompoundTag p_37578_) {
        super.readAdditionalSaveData(p_37578_);
    }

    public void addAdditionalSaveData(CompoundTag p_37582_) {
        super.addAdditionalSaveData(p_37582_);

    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean shouldRender(double p_37588_, double p_37589_, double p_37590_) {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
