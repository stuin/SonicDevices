package com.stuintech.sonicdevices.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class ShiftedState extends FakeBlockState {
    private boolean setup = false;

    public ShiftedState(World world, BlockPos pos) {
        super(world, pos);
    }

    public void done() {
        setup = true;
    }

    @Override
    public boolean isOpaque() {
        return false;
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
        return VoxelShapes.fullCube();
    }

    @Override
    public ActionResult onUse(World world, PlayerEntity player, Hand hand, BlockHitResult hit) {
        restore(hit.getBlockPos());
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBlockRemoved(World world, BlockPos pos, BlockState newState, boolean moved) {
        restore(pos);
    }

    @Override
    public boolean canReplace(ItemPlacementContext ctx) {
        return false;
    }

    @Override
    public void neighborUpdate(World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean bl) {
        super.neighborUpdate(world, pos, neighborBlock, neighborPos, bl);
        if(setup) {
            restore(pos);
            world.updateNeighbors(pos, ModBlocks.shifted);
        }
    }
}
