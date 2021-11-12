package com.stuintech.sonicdevices.socket;

import com.stuintech.sonicdevices.block.entity.ShiftedBlockEntity;
import com.stuintech.sonicdevices.util.PropertyMap;
import com.stuintech.socketwrench.socket.Socket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/9/2020.
 */

public class BlasterShiftSocket extends Socket.BlockActionSocket {

    @Override
    public boolean onFasten(PlayerEntity player, World world, BlockPos pos, Vec3d hit, Direction dir) {
        int i = 0;
        boolean reset = ResetAction.resetItem(player);
        if(player.canModifyBlocks()) {
            //Default direction variables
            Direction first =  Direction.NORTH;
            Direction second = Direction.DOWN;
            Direction.Axis axis = dir.getAxis();

            //Find proper axis
            if(axis == Direction.Axis.Z)
                first = Direction.EAST;
            else if(axis == Direction.Axis.Y)
                second = Direction.EAST;

            //Check each block in square
            ItemStack itemStack = ResetAction.getDevice(player);
            ShiftedBlockEntity[] state = new ShiftedBlockEntity[9];
            pos = pos.offset(first.getOpposite()).offset(second.getOpposite());
            for(int x = 0; x < 3; x++) {
                for(int y = 0; y < 3; y++) {
                    if(PropertyMap.canShift(world.getBlockState(pos))) {
                        //state[i] = new ShiftedBlockEntity(world, pos, false);
                        ResetAction.add(pos, itemStack);
                        i++;
                    }
                    pos = pos.offset(first);
                }
                first = first.getOpposite();
                pos = pos.offset(first).offset(second);
            }


            //Inform changed blocks that task is complete
            /*for(int j = 0; j < i; j++)
                state[j].done(group);*/
        }
        return reset | i > 0;
    }
}
