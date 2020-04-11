package com.stuintech.sonicdevices.action;

import com.stuintech.sonicdevices.action.blaster.ResetAction;
import com.stuintech.sonicdevices.block.entity.ShiftedBlockEntity;
import com.stuintech.sonicdevices.util.PropertyMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/8/2020.
 */

public class ScanBlockAction extends IAction.IBlockAction {
    private static ResetAction resetAction = new ResetAction();

    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        boolean reset = resetAction.interact(player, world, pos, dir);
        if(PropertyMap.canShift(world.getBlockState(pos))) {
            ShiftedBlockEntity state = new ShiftedBlockEntity(world, pos, true);
            state.done();

            //Save position for future cancelling
            ItemStack itemStack = ResetAction.getDevice(player);
            if(itemStack != null)
                itemStack.getOrCreateTag().putInt("shifter", ResetAction.shiftedBlocks.addNext(new BlockPos[]{pos}));

            return true;
        }
        return reset;
    }
}
