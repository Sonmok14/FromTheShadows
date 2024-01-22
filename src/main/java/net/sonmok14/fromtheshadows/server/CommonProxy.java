package net.sonmok14.fromtheshadows.server;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;


@Mod.EventBusSubscriber(modid = Fromtheshadows.MODID, value = Dist.CLIENT)
public class CommonProxy {
        public void playWorldSound(@Nullable Object soundEmitter, byte type) {

        }
        public void commonInit() {
        }

        public void clientInit() {
        }
        public void clearSoundCacheFor(Entity entity) {

        }
}
