package net.sonmok14.fromtheshadows.server.entity.projectiles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.sonmok14.fromtheshadows.server.config.FTSConfig;
import net.sonmok14.fromtheshadows.server.utils.registry.EntityRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FrogVomit extends Projectile implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public FrogVomit(EntityType<? extends FrogVomit> p_37224_, Level p_37225_) {
        super(p_37224_, p_37225_);
    }

    public FrogVomit(Level p_37235_, LivingEntity p_37236_) {
        this(EntityRegistry.FROGLIN_VOMIT.get(), p_37235_);
        this.setOwner(p_37236_);
        this.setPos(p_37236_.getX() - (double)(p_37236_.getBbWidth() + 1.0F) * 0.5D * (double) Mth.sin(p_37236_.yBodyRot * ((float)Math.PI / 180F)), p_37236_.getEyeY() - (double)0.1F, p_37236_.getZ() + (double)(p_37236_.getBbWidth() + 1.0F) * 0.5D * (double)Mth.cos(p_37236_.yBodyRot * ((float)Math.PI / 180F)));
    }

    public void tick() {
        super.tick();
        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult))
            this.onHit(hitresult);
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();
        float f = 0.99F;
        float f1 = 0.06F;
        if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) {
            this.discard();
        } else if (this.isInWaterOrBubble()) {
            this.discard();
        } else {
            this.setDeltaMovement(vec3.scale((double)0.99F));
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, (double)-0.06F, 0.0D));
            }

            this.setPos(d0, d1, d2);
        }
    }

    private void spawnLingeringCloud() {
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
            areaeffectcloud.setRadius(2.5F);
            areaeffectcloud.setRadiusOnUse(-0.5F);
            areaeffectcloud.setWaitTime(10);
            areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());
                areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 90));
        areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.POISON, 90));
            this.level().addFreshEntity(areaeffectcloud);
        }

    protected void onHitEntity(EntityHitResult p_37241_) {
        super.onHitEntity(p_37241_);
        Entity entity = this.getOwner();

        if (entity instanceof LivingEntity livingentity) {
            boolean flag = p_37241_.getEntity().hurt(this.damageSources().mobProjectile(this, livingentity), FTSConfig.SERVER.froglin_vomit_damage.get().floatValue());
            if (flag) {
                livingentity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 90), this);
            }

        }
    }

    protected void onHitBlock(BlockHitResult p_37239_) {
        super.onHitBlock(p_37239_);
        if (!this.level().isClientSide) {
            spawnLingeringCloud();
            this.discard();
        }

    }

    protected void defineSynchedData() {
    }

    public void recreateFromPacket(ClientboundAddEntityPacket p_150162_) {
        super.recreateFromPacket(p_150162_);
        double d0 = p_150162_.getXa();
        double d1 = p_150162_.getYa();
        double d2 = p_150162_.getZa();

        for(int i = 0; i < 7; ++i) {
            double d3 = 0.4D + 0.1D * (double)i;
            this.level().addParticle(ParticleTypes.SPIT, this.getX(), this.getY(), this.getZ(), d0 * d3, d1, d2 * d3);
        }

        this.setDeltaMovement(d0, d1, d2);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}

