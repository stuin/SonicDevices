package com.stuintech.sonicdevices.action.blaster;

import com.stuintech.sonicdevices.action.IAction;
import com.stuintech.sonicdevices.block.entity.ShiftedBlockEntity;
import com.stuintech.sonicdevices.util.PropertyMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
            BlockPos[] positions = new BlockPos[9];
            ShiftedBlockEntity[] state = new ShiftedBlockEntity[9];
            pos = pos.offset(first.getOpposite()).offset(second.getOpposite());
            int i = 0;
            for(int x = 0; x < 3; x++) {
                for(int y = 0; y < 3; y++) {
                    if(PropertyMap.canShift(world.getBlockState(pos))) {
                        state[i] = new ShiftedBlockEntity(world, pos, false);
                        positions[i] = pos;
                        i++;
                    }
                    pos = pos.offset(first);
                }
                first = first.getOpposite();
                pos = pos.offset(first).offset(second);
            }

            //Inform changed blocks that task is complete
            for(int j = 0; j < i; j++)
                state[j].done();

            //Save positions for future cancelling
            ItemStack itemStack = ResetAction.getDevice(player);
            if(itemStack != null && i > 0)
                itemStack.getOrCreateTag().putInt("shifter", ResetAction.shiftedBlocks.addNext(positions));
        }
        return true;
    }
}
