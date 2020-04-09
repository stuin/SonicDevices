package com.stuintech.sonicdevices.actions;

import com.stuintech.sonicdevices.blocks.ShiftedState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/8/2020.
 */

public class ScanBlockAction extends IAction.IBlockAction {
    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        if(!world.getBlockState(pos).isAir()) {
            ShiftedState state = new ShiftedState(world, pos, true);
            world.setBlockState(pos, state);
            state.done();
            return true;
        }
        return false;
    }
}
