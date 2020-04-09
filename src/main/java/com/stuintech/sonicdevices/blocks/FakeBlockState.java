package com.stuintech.sonicdevices.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class FakeBlockState extends BlockState {
    private World world;
    private BlockState oldState;

    FakeBlockState(World world, BlockPos position) {
        super(world.getBlockState(position).getBlock(), world.getBlockState(position).getEntries());

        this.world = world;
        this.oldState = world.getBlockState(position);
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
