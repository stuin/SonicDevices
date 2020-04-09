package com.stuintech.sonicdevices.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/9/2020.
 */

public class ShiftedState extends FakeBlockState {
    private boolean setup = false;
    private boolean clear;

    public ShiftedState(World world, BlockPos pos, boolean clear) {
        super(world, pos);
        this.clear = clear;
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
        if(clear)
            return ModBlocks.clear;
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
            world.updateNeighbors(pos, getBlock());
        }
    }
}
