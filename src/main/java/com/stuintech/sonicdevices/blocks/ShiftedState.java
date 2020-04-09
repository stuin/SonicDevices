package com.stuintech.sonicdevices.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class ShiftedState extends FakeBlockState {
    public ShiftedState(World world, BlockPos pos) {
        super(world, pos);
        world.getBlockTickScheduler().schedule(pos, ModBlocks.shifted, 10);
    }

    @Override
    public Block getBlock() {
        return ModBlocks.shifted;
    }

    @Override
    public BlockRenderType getRenderType() {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockView view, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public void neighborUpdate(World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean bl) {
        super.neighborUpdate(world, pos, neighborBlock, neighborPos, bl);
        //restore(pos);
    }

    @Override
    public void scheduledTick(ServerWorld world, BlockPos pos, Random random) {
        restore(pos);
    }
}
