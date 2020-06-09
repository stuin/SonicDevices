package com.stuintech.sonicdevices.action;

import com.stuintech.sonicdevices.block.entity.ShiftedBlockEntity;
import com.stuintech.sonicdevices.util.PropertyMap;
import com.stuintech.sonicdevicesapi.IAction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;

/*
 * Created by Stuart Irwin on 4/8/2020.
 */

public class ScanBlockAction extends IAction.IBlockAction {

    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        boolean reset = ResetAction.interact(player, world);
        if(PropertyMap.canShift(world.getBlockState(pos))) {
            ShiftedBlockEntity state = new ShiftedBlockEntity(world, pos, true);

            //Save position for future cancelling
            int group = ResetAction.shiftedBlocks.addNext(new ArrayList<>());
            ResetAction.add(pos, group);
            ItemStack itemStack = ResetAction.getDevice(player);
            if(itemStack != null)
                itemStack.getOrCreateTag().putInt("shifter", group);
            state.done(group);

            return true;
        }
        return reset;
    }
}
