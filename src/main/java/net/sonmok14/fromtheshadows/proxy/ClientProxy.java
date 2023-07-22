package net.sonmok14.fromtheshadows.proxy;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.sonmok14.fromtheshadows.Fromtheshadows;
import net.sonmok14.fromtheshadows.client.ClientEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Fromtheshadows.MODID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void init(final IEventBus modbus) {
        super.init(modbus);


        MinecraftForge.EVENT_BUS.register(ClientEvent.INSTANCE);


    }

}
