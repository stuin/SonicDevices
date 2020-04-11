package com.stuintech.sonicdevices.block;

import com.stuintech.sonicdevices.block.entity.ShiftedBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class ShiftedBlock extends BlockWithEntity {
    public static BooleanProperty SETUP = BooleanProperty.of("setup");

    ShiftedBlock(Block.Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(SETUP, false));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new ShiftedBlockEntity();
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        for(int i = 0; i < 4; ++i) {
            double d = (double)pos.getX() + (double)random.nextFloat();
            double e = (double)pos.getY() + (double)random.nextFloat();
            double f = (double)pos.getZ() + (double)random.nextFloat();
            double g = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double h = ((double)random.nextFloat() - 0.5D) * 0.5D;
            double j = ((double)random.nextFloat() - 0.5D) * 0.5D;

            world.addParticle(ParticleTypes.PORTAL, d, e, f, g, h, j);
        }
    }

    public void restore(World world, BlockPos pos) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof ShiftedBlockEntity)
            ((ShiftedBlockEntity) entity).restore();
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved) {
        if(state.get(SETUP))
            restore(world, pos);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
        return VoxelShapes.fullCube();
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(state.get(SETUP))
            restore(world, pos);
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(state.get(SETUP))
            restore(world, pos);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext ctx) {
        return false;
    }

    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SETUP);
    }
}