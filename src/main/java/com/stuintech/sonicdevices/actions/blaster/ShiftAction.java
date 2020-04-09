package com.stuintech.sonicdevices.actions.blaster;

import com.stuintech.sonicdevices.actions.IAction;
import com.stuintech.sonicdevices.blocks.ShiftedState;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ShiftAction extends IAction.IBlockAction {
    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        if(player.canModifyWorld()) {
            Direction first =  Direction.NORTH;
            Direction second = Direction.DOWN;
            Direction.Axis axis = dir.getAxis();

            //Find axis
            if(axis == Direction.Axis.Y)
                first = Direction.EAST;
            else if(axis == Direction.Axis.Z)
                second = Direction.EAST;

            pos = pos.offset(first.getOpposite()).offset(second.getOpposite());
            for(int x = 0; x < 3; x++) {
                for(int y = 0; y < 3; y++) {
                    world.setBlockState(pos, new ShiftedState(world, pos));
                    pos = pos.offset(first);
                }
                first = first.getOpposite();
                pos = pos.offset(first).offset(second);
            }
            return true;
        }
        return false;
    }
}