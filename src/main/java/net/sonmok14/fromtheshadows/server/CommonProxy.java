package net.sonmok14.fromtheshadows.server;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Fromtheshadows.MODID, value = Dist.CLIENT)
public class CommonProxy {



        public void commonInit() {
        }

        public void clientInit() {
        }
        public Player getClientSidePlayer() {
                return null;
        }

        public void openBookGUI(ItemStack itemStackIn) {
        }

        public void openBookGUI(ItemStack itemStackIn, String page) {
        }

        public Object getArmorModel(int armorId, LivingEntity entity) {
                return null;
        }

        public void onEntityStatus(Entity entity, byte updateKind) {
        }

        public void updateBiomeVisuals(int x, int z) {
        }

        public void setRenderViewEntity(Entity entity) {

        }

        public void resetRenderViewEntity() {

        }

        public int getPreviousPOV(){
                return 0;
        }

        public boolean isFarFromCamera(double x, double y, double z) {
                return true;
        }

        public void resetVoidPortalCreation(Player player){}

        public Object getISTERProperties() {
                return null;
        }

        public Object getArmorRenderProperties() {
                return null;
        }

        public void spawnSpecialParticle(int i) {
        }

        public void processVisualFlag(Entity entity, int flag) {
        }

        public void setPupfishChunkForItem(int chunkX, int chunkZ) {
        }
}
