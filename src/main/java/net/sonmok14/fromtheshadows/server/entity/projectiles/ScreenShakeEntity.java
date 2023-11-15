package net.sonmok14.fromtheshadows.server.entity.projectiles;



import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.sonmok14.fromtheshadows.server.utils.registry.EntityRegistry;

public class ScreenShakeEntity extends Entity {
    private static final EntityDataAccessor<Float> RADIUS = SynchedEntityData.defineId(ScreenShakeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> MAGNITUDE = SynchedEntityData.defineId(ScreenShakeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(ScreenShakeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FADE_DURATION = SynchedEntityData.defineId(ScreenShakeEntity.class, EntityDataSerializers.INT);

    public ScreenShakeEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    public ScreenShakeEntity(Level world, Vec3 position, float radius, float magnitude, int duration, int fadeDuration) {
        super(EntityRegistry.SCREEN_SHAKE.get(), world);
        setRadius(radius);
        setMagnitude(magnitude);
        setDuration(duration);
        setFadeDuration(fadeDuration);
        setPos(position.x, position.y, position.z);
    }

    @OnlyIn(Dist.CLIENT)
    public float getShakeAmount(Player player, float delta) {
        float ticksDelta = tickCount + delta;
        float timeFrac = 1.0f - (ticksDelta - getDuration()) / (getFadeDuration() + 1.0f);
        float baseAmount = ticksDelta < getDuration() ? getMagnitude() : timeFrac * timeFrac * getMagnitude();
        Vec3 playerPos = player.getEyePosition(delta);
        float distFrac = (float) (1.0f - Mth.clamp(position().distanceTo(playerPos) / getRadius(), 0, 1));
        return baseAmount * distFrac * distFrac;
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > getDuration() + getFadeDuration()) discard();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(RADIUS, 10.0f);
        this.entityData.define(MAGNITUDE, 1.0f);
        this.entityData.define(DURATION, 0);
        this.entityData.define(FADE_DURATION, 5);
    }

    public float getRadius() {
        return this.entityData.get(RADIUS);
    }

    public void setRadius(float radius) {
        this.entityData.set(RADIUS, radius);
    }

    public float getMagnitude() {
        return this.entityData.get(MAGNITUDE);
    }

    public void setMagnitude(float magnitude) {
        this.entityData.set(MAGNITUDE, magnitude);
    }

    public int getDuration() {
        return this.entityData.get(DURATION);
    }

    public void setDuration(int duration) {
        this.entityData.set(DURATION, duration);
    }

    public int getFadeDuration() {
        return this.entityData.get(FADE_DURATION);
    }

    public void setFadeDuration(int fadeDuration) {
        this.entityData.set(FADE_DURATION, fadeDuration);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        setRadius(compound.getFloat("radius"));
        setMagnitude(compound.getFloat("magnitude"));
        setDuration(compound.getInt("duration"));
        setFadeDuration(compound.getInt("fade_duration"));
        tickCount = compound.getInt("ticks_existed");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("radius", getRadius());
        compound.putFloat("magnitude", getMagnitude());
        compound.putInt("duration", getDuration());
        compound.putInt("fade_duration", getFadeDuration());
        compound.putInt("ticks_existed", tickCount);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public static void ScreenShake(Level world, Vec3 position, float radius, float magnitude, int duration, int fadeDuration) {
        if (!world.isClientSide) {
            ScreenShakeEntity ScreenShake = new ScreenShakeEntity(world, position, radius, magnitude, duration, fadeDuration);
            world.addFreshEntity(ScreenShake);
        }
    }
}
