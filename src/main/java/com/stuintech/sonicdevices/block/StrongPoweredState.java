package com.stuintech.sonicdevices.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/5/2019.
 */

public class StrongPoweredState extends FakeBlockState {
    private Direction dir;
    private boolean checked;

    public StrongPoweredState(World world, BlockPos position, Direction direction) {
        super(world, world.getBlockState(position));

        this.dir = direction.getOpposite();
    }

    @Override
    public boolean isSolidBlock(BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public int getStrongRedstonePower(BlockView blockView, BlockPos blockPos, Direction direction) {
        if(dir == direction) {
            if(checked)
                restore(blockPos);
            checked = true;
            return 15;
        }
        return super.getStrongRedstonePower(blockView, blockPos, direction);
    }
}
