package com.stuintech.sonicdevices.socket;

import com.stuintech.sonicdevices.util.PropertyMap;
import com.stuintech.socketwrench.socket.Socket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/8/2020.
 */

public class ScanBlockAction extends Socket.BlockActionSocket {

    @Override
    public boolean onFasten(PlayerEntity player, World world, BlockPos pos, Vec3d hit, Direction dir) {
        boolean reset = ResetAction.resetItem(player);
        if(PropertyMap.canShift(world.getBlockState(pos))) {
            //ShiftedBlockEntity state = new ShiftedBlockEntity(world, pos, true);

            //Save position for future cancelling
            ItemStack itemStack = ResetAction.getDevice(player);
            ResetAction.add(pos, itemStack);

            return true;
        }
        return reset;
    }
}
