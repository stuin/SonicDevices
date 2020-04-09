package com.stuintech.sonicdevices.actions.blaster;

import com.stuintech.sonicdevices.actions.IAction;
import com.stuintech.sonicdevices.blocks.ShiftedState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/9/2020.
 */

public class ShiftAction extends IAction.IBlockAction {

    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        if(player.canModifyWorld()) {
            Direction first =  Direction.NORTH;
            Direction second = Direction.DOWN;
            Direction.Axis axis = dir.getAxis();

            //Find axis
            if(axis == Direction.Axis.Z)
                first = Direction.EAST;
            else if(axis == Direction.Axis.Y)
                second = Direction.EAST;

            //Save for later
            ShiftedState[] state = new ShiftedState[9];
            int i = 0;

            //Set each block
            pos = pos.offset(first.getOpposite()).offset(second.getOpposite());
            for(int x = 0; x < 3; x++) {
                for(int y = 0; y < 3; y++) {
                    if(!world.getBlockState(pos).isAir()) {
                        state[i] = new ShiftedState(world, pos, false);
                        world.setBlockState(pos, state[i]);
                        i++;
                    }

                    pos = pos.offset(first);
                }
                first = first.getOpposite();
                pos = pos.offset(first).offset(second);
            }

            //Inform blocks that task is complete
            for(int j = 0; j < i; j++)
                state[j].done();
            return true;
        }
        return false;
    }
}
