package slurpreborn.event;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import slurpreborn.network.DrinkWater;
import slurpreborn.network.NetworkManager;
import slurpreborn.SlurpReborn;

import toughasnails.api.thirst.ThirstHelper;

@Mod.EventBusSubscriber(modid = SlurpReborn.MODID)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        final Player player = event.getEntity();

        if (player != null && player.level.isClientSide) {
            drinkWater(player, event);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        final Player player = event.getEntity();

        if (player != null && player.level.isClientSide) {
            drinkWater(player, event);
        }
    }

    private static void drinkWater(Player player, PlayerInteractEvent event) {
        final InteractionHand hand = event.getHand();

        if (hand != InteractionHand.OFF_HAND
                || player.getPose() != Pose.CROUCHING
                || !player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) return;

        final LevelAccessor level = event.getLevel();
        final HitResult hitresult = player.pick(2.0D, 0.0F, true);
        BlockPos pos = ((BlockHitResult)hitresult).getBlockPos();

        if (hitresult.getType() == HitResult.Type.BLOCK
                && level.getFluidState(pos).getType() == Fluids.WATER) {
            if (ThirstHelper.isThirstEnabled() && ThirstHelper.canDrink(player, false)) {
                level.playSound(player, new BlockPos(player.getPosition(0f)), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.4f, 1.0f);

                NetworkManager.INSTANCE.sendToServer(new DrinkWater());
            }
        }
    }

}
