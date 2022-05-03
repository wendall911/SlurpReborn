package slurpreborn;

import java.util.Random;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import slurpreborn.network.NetworkManager;
import slurpreborn.proxy.CommonProxy;
import slurpreborn.proxy.ServerProxy;

@Mod(SlurpReborn.MODID)
public class SlurpReborn {

    public static final String MODID = "slurpreborn";
    public static final Logger LOGGER = LogManager.getFormatterLogger(SlurpReborn.MODID);
    public static final Random RANDOM = new Random();

    public static SlurpReborn INSTANCE;
    public static IEventBus BUS;

    public SlurpReborn() {
        INSTANCE = this;
        BUS = FMLJavaModLoadingContext.get().getModEventBus();

        BUS.addListener(INSTANCE::setup);

        DistExecutor.safeRunForDist(() -> CommonProxy::new, () -> ServerProxy::new);
    }

    public void setup(final FMLCommonSetupEvent event) {
        NetworkManager.setup();
    }

}
