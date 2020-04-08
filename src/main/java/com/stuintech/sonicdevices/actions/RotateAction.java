package com.stuintech.sonicdevices.actions;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static net.minecraft.util.math.Direction.Axis.*;

public class RotateAction extends IAction.IBlockAction {
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        //Rotate block
        if(player.canModifyWorld()) {
            //Get block variables
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            BlockState blockState_2 = blockState;
            StateManager<Block, BlockState> stateFactory = block.getStateManager();

            Property<?> property = stateFactory.getProperty("facing");
            if (property != null) {
                if (block instanceof PistonHeadBlock ||
                        block instanceof WallMountedBlock ||
                        block instanceof AbstractSignBlock ||
                        block instanceof CommandBlock)
                    return false;
                else if (block instanceof PistonBlock) {
                    if (!blockState.get(PistonBlock.EXTENDED))
                        blockState_2 = rotate(blockState, property, dir.getAxis());
                } else if (block instanceof HorizontalFacingBlock ||
                        block instanceof AnvilBlock ||
                        block instanceof StonecutterBlock ||
                        block instanceof StairsBlock)
                    blockState_2 = rotate(blockState, property, Y);
                else if (block instanceof DispenserBlock ||
                        block instanceof BarrelBlock ||
                        block instanceof ShulkerBoxBlock)
                    blockState_2 = rotate(blockState, property, dir.getAxis());
                else if (block instanceof BlockEntityProvider)
                    blockState_2 = rotate(blockState, property, Y);
                else if (block instanceof FacingBlock)
                    blockState_2 = rotate(blockState, property, dir.getAxis());


                //Apply rotation
                if (blockState != blockState_2) {
                    world.setBlockState(pos, blockState_2);
                    return true;
                }
            } else if ((property = stateFactory.getProperty("axis")) != null) {
                if (block instanceof NetherPortalBlock)
                    return false;
                else
                    blockState_2 = rotate(blockState, property, Y);

                //Apply rotation
                if (blockState != blockState_2) {
                    world.setBlockState(pos, blockState_2);
                    return true;
                }
            }
        }
        return false;
    }

    //Based off of Botania Wand
    private static <T extends Comparable<T>> BlockState rotate(BlockState old, Property<T> property, Direction.Axis axis) {
        if(property.getType() == Direction.class)
            return old.with(property, (T)rotateAround((Direction)old.get(property), axis));
        if(property.getType() == Direction.Axis.class) {
            switch((Direction.Axis)old.get(property)) {
                case X:
                    axis = Y;
                    break;
                case Y:
                    axis = Z;
                    break;
                case Z:
                    axis = X;
                    break;
            }
            return old.with(property, (T)axis);
        }

        return old;
    }

    private static Direction rotateAround(Direction old, Direction.Axis axis) {
        switch(axis) {
            case X: {
                switch (old) {
                    case DOWN:
                        return Direction.SOUTH;
                    case SOUTH:
                        return Direction.UP;
                    case UP:
                        return Direction.NORTH;
                    case NORTH:
                        return Direction.DOWN;
                }
                break;
            }
            case Y: {
                switch (old) {
                    case NORTH:
                        return Direction.EAST;
                    case EAST:
                        return Direction.SOUTH;
                    case SOUTH:
                        return Direction.WEST;
                    case WEST:
                        return Direction.NORTH;
                }
                break;
            }
            case Z: {
                switch (old) {
                    case DOWN:
                        return Direction.WEST;
                    case WEST:
                        return Direction.UP;
                    case UP:
                        return Direction.EAST;
                    case EAST:
                        return Direction.DOWN;
                }
                break;
            }
        }

        return old;
    }
}
