package slurpreborn.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import slurpreborn.config.ConfigHandler;
import slurpreborn.SlurpReborn;

import toughasnails.api.potion.TANEffects;
import toughasnails.api.thirst.ThirstHelper;
import toughasnails.api.thirst.IThirst;

public class DrinkWater {

    public DrinkWater() {}

    public DrinkWater(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public void process(Supplier<NetworkEvent.Context> ctx) {
        Player player = ctx.get().getSender();
        IThirst thirst = ThirstHelper.getThirst(player);

        thirst.addThirst(ConfigHandler.Common.drinkAmount());
        thirst.addHydration(ConfigHandler.Common.drinkHydration());

        if (SlurpReborn.RANDOM.nextFloat() < ConfigHandler.Common.effectChance()) {
            player.addEffect(new MobEffectInstance(
                    TANEffects.THIRST,
                    ConfigHandler.Common.effectDuration(),
                    ConfigHandler.Common.effectPotency(),
                    false, false, false));
        }
    }

}
