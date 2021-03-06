package com.stuintech.sonicdevices.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/5/2019.
 */

public class WeakPoweredState extends FakeBlockState {
    private final Direction dir;
    private boolean checked;

    public WeakPoweredState(World world, BlockPos position, Direction direction) {
        super(world, world.getBlockState(position));

        this.dir = direction.getOpposite();
    }

    @Override
    public boolean isSolidBlock(BlockView view, BlockPos pos) {
        return false;
    }

    @Override
    public int getWeakRedstonePower(BlockView blockView, BlockPos blockPos, Direction direction) {
        if(dir == direction) {
            if(checked)
                restore(blockPos);
            checked = true;
            return 15;
        }
        return super.getWeakRedstonePower(blockView, blockPos, direction);
    }
}
