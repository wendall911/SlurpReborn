package slurpreborn.network;

import java.util.function.Function;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import slurpreborn.network.DrinkWater;
import slurpreborn.SlurpReborn;

public class NetworkManager {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(SlurpReborn.MODID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void setup() {
        int idx = 0;

        register(idx++, DrinkWater.class, DrinkWater::new);
    }

    private static void register(int idx, Class<DrinkWater> type, Function<FriendlyByteBuf, DrinkWater> decoder) {
        INSTANCE.registerMessage(idx, type, DrinkWater::toBytes, decoder, (msg, ctx) -> {
            msg.process(ctx);
            ctx.get().setPacketHandled(true);
        });
    }

}
