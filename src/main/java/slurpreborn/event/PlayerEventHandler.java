package slurpreborn.event;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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
        final Player player = event.getPlayer() instanceof Player ? (Player) event.getPlayer() : null;
        final LevelAccessor level = event.getWorld();

        final HitResult hitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (player.getPose() == Pose.CROUCHING && hitresult.getType() == HitResult.Type.BLOCK &&
                level.getFluidState(new BlockPos(hitresult.getLocation())).getType() == Fluids.WATER) {

            if (ThirstHelper.isThirstEnabled() && ThirstHelper.canDrink(player, false)) {
                level.playSound(player, new BlockPos(player.getPosition(0f)), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 0.4f, 1.0f);

                NetworkManager.INSTANCE.sendToServer(new DrinkWater());
            }
        }
    }

    private static BlockHitResult getPlayerPOVHitResult(LevelAccessor pLevel, Player pPlayer, ClipContext.Fluid pFluidMode) {
        final float xRot = pPlayer.getXRot();
        final float yRot = pPlayer.getYRot();
        final Vec3 eyePos = pPlayer.getEyePosition();
        final float cosRotY = Mth.cos(-yRot * ((float)Math.PI / 180F) - (float)Math.PI);
        final float sinRotY = Mth.sin(-yRot * ((float)Math.PI / 180F) - (float)Math.PI);
        final float cosRotX = -Mth.cos(-xRot * ((float)Math.PI / 180F));
        final float sinRotX = Mth.sin(-xRot * ((float)Math.PI / 180F));
        final float pX = sinRotY * cosRotX;
        final float pZ = cosRotY * cosRotX;
        final double reach = pPlayer.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();
        final Vec3 reachPos = eyePos.add((double)pX * reach, (double)sinRotX * reach, (double)pZ * reach);

        return pLevel.clip(new ClipContext(eyePos, reachPos, ClipContext.Block.OUTLINE, pFluidMode, pPlayer));
    }

}
