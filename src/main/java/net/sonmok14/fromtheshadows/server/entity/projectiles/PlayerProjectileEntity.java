package net.sonmok14.fromtheshadows.server.entity.projectiles;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.sonmok14.fromtheshadows.client.models.ControlledAnimation;
import net.sonmok14.fromtheshadows.server.config.FTSConfig;
import net.sonmok14.fromtheshadows.server.utils.registry.DamageRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerProjectileEntity extends Entity {
    public static final double RADIUS = 30;
    public LivingEntity caster;
    public double endPosX, endPosY, endPosZ;
    public double collidePosX, collidePosY, collidePosZ;
    public double prevCollidePosX, prevCollidePosY, prevCollidePosZ;
    public float renderYaw, renderPitch;
    public ControlledAnimation appear = new ControlledAnimation(3);

    public boolean on = true;

    public Direction blockSide = null;

    private static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(PlayerProjectileEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(PlayerProjectileEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(PlayerProjectileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CASTER = SynchedEntityData.defineId(PlayerProjectileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HEAD = SynchedEntityData.defineId(PlayerProjectileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FIRE = SynchedEntityData.defineId(PlayerProjectileEntity.class, EntityDataSerializers.BOOLEAN);
    public float prevYaw;
    public float prevPitch;

    @OnlyIn(Dist.CLIENT)
    private Vec3[] attractorPos;

    public PlayerProjectileEntity(EntityType<? extends PlayerProjectileEntity> type, Level world) {
        super(type, world);
        noCulling = true;
        if (world.isClientSide) {
            attractorPos = new Vec3[] {new Vec3(0, 0, 0)};
        }
    }

    public PlayerProjectileEntity(EntityType<? extends PlayerProjectileEntity> type, Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        this(type, world);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.calculateEndPos();
        if (!world.isClientSide) {
            this.setCasterID(caster.getId());
        }
    }


    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    @Override
    public void tick() {
        super.tick();
        prevCollidePosX = collidePosX;
        prevCollidePosY = collidePosY;
        prevCollidePosZ = collidePosZ;
        prevYaw = renderYaw;
        prevPitch = renderPitch;
        xo = getX();
        yo = getY();
        zo = getZ();
        if (tickCount == 1 && level().isClientSide) {
            caster = (LivingEntity) level().getEntity(getCasterID());
        }

        if (!level().isClientSide) {
            if (caster instanceof ServerPlayer) {
                this.updateWithPlayer();
            }
        }

        if (caster != null) {
            renderYaw = (float) ((caster.yHeadRot + 90.0d) * Math.PI / 180.0d);
            renderPitch = (float) (-caster.getXRot() * Math.PI / 180.0d);
        }

        if (!on && appear.getTimer() == 0) {
            this.discard();
        }
        if (on && tickCount > 2) {
            appear.increaseTimer();
        } else {
            appear.decreaseTimer();
        }

        if (caster != null && !caster.isAlive()) discard();

        if (tickCount > 2) {
            this.calculateEndPos();
            List<LivingEntity> hit = raytraceEntities(level(), new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ), false, true, true).entities;
            if (blockSide != null) {
                if (!this.level().isClientSide) {
                    for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(collidePosX - 0.5F), Mth.floor(collidePosY - 0.5F), Mth.floor(collidePosZ - 0.5F), Mth.floor(collidePosX + 0.5F), Mth.floor(collidePosY + 0.5F), Mth.floor(collidePosZ + 0.5F))) {
                        BlockState block = level().getBlockState(pos);
                    }
                    for (BlockPos pos : BlockPos.betweenClosed(Mth.floor(collidePosX - 2.5F), Mth.floor(collidePosY - 2.5F), Mth.floor(collidePosZ - 2.5F), Mth.floor(collidePosX + 2.5F), Mth.floor(collidePosY + 2.5F), Mth.floor(collidePosZ + 2.5F))) {
                        BlockState block = level().getBlockState(pos);
                    }
                    if(this.getFire()) {
                        BlockPos blockpos1 = new BlockPos.MutableBlockPos(collidePosX, collidePosY, collidePosZ);
                            if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
                                if (this.level().isEmptyBlock(blockpos1)) {
                                    this.level().setBlockAndUpdate(blockpos1, BaseFireBlock.getState(this.level(), blockpos1));
                                }
                            }


                    }
                }
            }
            if (!level().isClientSide) {
                for (LivingEntity target : hit) {
                    if (target != this.caster) {

                        boolean flag = target.hurt(DamageRegistry.causeIncinerateDamage(caster), FTSConfig.SERVER.thirst_for_blood_laser_damage.get().floatValue());
                            if (flag) {
                                target.setSecondsOnFire(5);
                            }
                        if(!target.isAlive())
                        {
                            for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(4D))) {
                                if(livingentity != caster) {
                                    livingentity.hurt(DamageRegistry.causeIncinerateDamage(caster), 4);
                                    livingentity.setSecondsOnFire(40);
                                }
                            }
                        }
                    }


                }
            }
        }
        if (tickCount - 2 > getDuration()) {
            on = false;
        }
    }





    @Override
    protected void defineSynchedData() {
        this.entityData.define(YAW, 0F);
        this.entityData.define(PITCH, 0F);
        this.entityData.define(DURATION, 0);
        this.entityData.define(CASTER, -1);
        this.entityData.define(HEAD, 0);
        this.entityData.define(FIRE, false);
    }

    public float getYaw() {
        return entityData.get(YAW);
    }

    public void setYaw(float yaw) {
        entityData.set(YAW, yaw);
    }

    public float getPitch() {
        return entityData.get(PITCH);
    }

    public void setPitch(float pitch) {
        entityData.set(PITCH, pitch);
    }

    public int getDuration() {
        return entityData.get(DURATION);
    }

    public void setDuration(int duration) {
        entityData.set(DURATION, duration);
    }

    public int getHead() {
        return entityData.get(HEAD);
    }

    public void setHead(int head) {
        entityData.set(HEAD, head);
    }


    public int getCasterID() {
        return entityData.get(CASTER);
    }

    public void setCasterID(int id) {
        entityData.set(CASTER, id);
    }

    public boolean getFire() {
        return this.entityData.get(FIRE);
    }

    public void setFire(boolean fire) {
        this.entityData.set(FIRE, fire);
    }


    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {}

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    private void calculateEndPos() {
        if (level().isClientSide()) {
            endPosX = getX() + RADIUS * Math.cos(renderYaw) * Math.cos(renderPitch);
            endPosZ = getZ() + RADIUS * Math.sin(renderYaw) * Math.cos(renderPitch);
            endPosY = getY() + RADIUS * Math.sin(renderPitch);
        } else {
            endPosX = getX() + RADIUS * Math.cos(getYaw()) * Math.cos(getPitch());
            endPosZ = getZ() + RADIUS * Math.sin(getYaw()) * Math.cos(getPitch());
            endPosY = getY() + RADIUS * Math.sin(getPitch());
        }
    }

    public LaserbeamHitResult raytraceEntities(Level world, Vec3 from, Vec3 to, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        LaserbeamHitResult result = new LaserbeamHitResult();
        result.setBlockHit(world.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));
        if (result.blockHit != null) {
            Vec3 hitVec = result.blockHit.getLocation();
            collidePosX = hitVec.x;
            collidePosY = hitVec.y;
            collidePosZ = hitVec.z;
            blockSide = result.blockHit.getDirection();
        } else {
            collidePosX = endPosX;
            collidePosY = endPosY;
            collidePosZ = endPosZ;
            blockSide = null;
        }
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(Math.min(getX(), collidePosX), Math.min(getY(), collidePosY), Math.min(getZ(), collidePosZ), Math.max(getX(), collidePosX), Math.max(getY(), collidePosY), Math.max(getZ(), collidePosZ)).inflate(1, 1, 1));
        for (LivingEntity entity : entities) {
            if (entity == caster) {
                continue;
            }
            float pad = entity.getPickRadius() + 0.5f;
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
            Optional<Vec3> hit = aabb.clip(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
            } else if (hit.isPresent()) {
                result.addEntityHit(entity);
            }
        }
        return result;
    }

    @Override
    public void push(Entity entityIn) {
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 1024;
    }

    private void updateWithPlayer() {
        this.setYaw((float) ((caster.yHeadRot + 90) * Math.PI / 180.0d));
        this.setPitch((float) (-caster.getXRot() * Math.PI / 180.0d));
        Vec3 vecOffset = caster.getLookAngle().normalize().scale(1);
        this.setPos(caster.getX() + vecOffset.x(), caster.getY() + 1.2f + vecOffset.y(), caster.getZ() + vecOffset.z());
    }

    public static class LaserbeamHitResult {
        private BlockHitResult blockHit;

        private final List<LivingEntity> entities = new ArrayList<>();

        public BlockHitResult getBlockHit() {
            return blockHit;
        }

        public void setBlockHit(HitResult rayTraceResult) {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK)
                this.blockHit = (BlockHitResult) rayTraceResult;
        }

        public void addEntityHit(LivingEntity entity) {
            entities.add(entity);
        }
    }
}
