package com.stuintech.sonicdevices.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/4/2019.
 */

public abstract class FakeBlockState extends BlockState {
    private World world;
    private BlockState oldState;

    FakeBlockState(World world, BlockPos position) {
        super(world.getBlockState(position).getBlock(), world.getBlockState(position).getEntries());

        this.world = world;
        this.oldState = world.getBlockState(position);
    }

    @Override
    public <T extends Comparable<T>> T get(Property<T> property) {
        return oldState.get(property);
    }

    //Reset original blockstate
    public void restore(BlockPos blockPos) {
        world.setBlockState(blockPos, oldState);
    }

    @Override
    public BlockRenderType getRenderType() {
        return oldState.getRenderType();
    }

    @Override
    public void neighborUpdate(World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean bl) {
        oldState.neighborUpdate(world, pos, neighborBlock, neighborPos, bl);
    }
}