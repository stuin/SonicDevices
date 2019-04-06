package com.stuintech.sonicdevices;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/5/2019.
 */

public class FakePoweredState extends BlockState {
    private World world;
    private BlockState oldState;
    private Direction direction;
    private boolean checked;

    public FakePoweredState(World world, BlockPos position, Direction direction) {
        super(world.getBlockState(position).getBlock(), ImmutableMap.of());

        this.world = world;
        this.oldState = world.getBlockState(position);
        this.direction = direction.getOpposite();
    }

    @Override
    public boolean isSimpleFullBlock(BlockView blockView_1, BlockPos blockPos_1) {
        return false;
    }

    @Override
    public int getWeakRedstonePower(BlockView blockView_1, BlockPos blockPos_1, Direction direction_1) {
        if(direction == direction_1) {
            if(checked)
                world.setBlockState(blockPos_1, oldState);
            checked = true;
            return 15;
        }
        return oldState.getWeakRedstonePower(blockView_1, blockPos_1, direction_1);
    }

    @Override
    public int getStrongRedstonePower(BlockView blockView_1, BlockPos blockPos_1, Direction direction_1) {
        if(direction == direction_1) {
            if(checked)
                world.setBlockState(blockPos_1, oldState);
            checked = true;
            return 15;
        }
        return oldState.getWeakRedstonePower(blockView_1, blockPos_1, direction_1);
    }
}
