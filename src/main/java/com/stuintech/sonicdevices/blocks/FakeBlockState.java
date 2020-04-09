package com.stuintech.sonicdevices.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class FakeBlockState extends BlockState {
    private World world;
    private BlockState oldState;

    FakeBlockState(World world, BlockPos position) {
        super(world.getBlockState(position).getBlock(), ImmutableMap.of());

        this.world = world;
        this.oldState = world.getBlockState(position);
    }

    //Reset original blockstate
    public void restore(BlockPos blockPos) {
        world.setBlockState(blockPos, oldState);
    }

    @Override
    public <T extends Comparable<T>> T get(Property<T> property_1) {
        return oldState.get(property_1);
    }

    @Override
    public <T extends Comparable<T>, V extends T> BlockState with(Property<T> property_1, V comparable_1) {
        return oldState.with(property_1, comparable_1);
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
