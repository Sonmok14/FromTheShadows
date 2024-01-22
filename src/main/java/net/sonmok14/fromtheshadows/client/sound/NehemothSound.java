package net.sonmok14.fromtheshadows.client.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.sonmok14.fromtheshadows.server.entity.NehemothEntity;
import net.sonmok14.fromtheshadows.server.utils.registry.SoundRegistry;

public class NehemothSound extends AbstractTickableSoundInstance implements UnlimitedPitch {
    private final NehemothEntity nehemoth;

    public NehemothSound(NehemothEntity nehemoth) {
        super(SoundRegistry.NEHEMOTH_IDLE.get(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.nehemoth = nehemoth;
        this.attenuation = SoundInstance.Attenuation.LINEAR;
        this.looping = true;
        this.x = (double)((float)this.nehemoth.getX());
        this.y = (double)((float)this.nehemoth.getY());
        this.z = (double)((float)this.nehemoth.getZ());
        this.delay = 0;
    }

    public boolean canPlaySound() {
        return !this.nehemoth.isSilent();
    }

    public void tick() {
        if (this.nehemoth.isAlive()) {
            this.x = (double)((float)this.nehemoth.getX());
            this.y = (double)((float)this.nehemoth.getY());
            this.z = (double)((float)this.nehemoth.getZ());
            float f1 = (float) Math.pow(1, 0.5F);
            this.volume = 1.0F + 1 * 2.0F;
            this.pitch = 1F + 6F * f1;
        } else {
            this.stop();
        }
    }

    public boolean canStartSilent() {
        return true;
    }

    public boolean isSameEntity(NehemothEntity nehemoth) {
        return this.nehemoth.isAlive() && this.nehemoth.getId() == nehemoth.getId();
    }
}
