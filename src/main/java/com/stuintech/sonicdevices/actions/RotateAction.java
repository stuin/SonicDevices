package com.stuintech.sonicdevices.actions;

import grondag.fermion.modkeys.api.ModKeys;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static net.minecraft.util.math.Direction.Axis.*;

/*
 * Created by Stuart Irwin on 4/8/2020.
 */

public class RotateAction extends IAction.IBlockAction {
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) throws CancelActionException {
        //Rotate block
        if(player.canModifyWorld()) {
            //Get block variables
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            BlockState blockState_2 = blockState;
            StateManager<Block, BlockState> stateFactory = block.getStateManager();
            boolean reverse = ModKeys.isSecondaryPressed(player);

            //Proper blacklist
            if (block instanceof NetherPortalBlock ||
                    block instanceof PistonHeadBlock)
                throw new CancelActionException();

            //Rotate block
            Property<?> property = stateFactory.getProperty("facing");
            if(property != null) {
                //Blacklist
                if(block instanceof WallMountedBlock ||
                        block instanceof AbstractSignBlock ||
                        block instanceof BellBlock)
                    return false;
                //Piston
                else if(block instanceof PistonBlock) {
                    if(!blockState.get(PistonBlock.EXTENDED))
                        blockState_2 = rotate(blockState, property, dir.getAxis(), reverse);
                    else
                        throw new CancelActionException();
                }
                //Chest
                else if(block instanceof ChestBlock) {
                    if(ChestBlock.getDoubleBlockType(blockState) == DoubleBlockProperties.Type.SINGLE)
                        blockState_2 = rotate(blockState, property, Y, reverse);
                    else
                        throw new CancelActionException();
                }
                //Horizontal Blocks
                else if(block instanceof HorizontalFacingBlock ||
                        block instanceof AnvilBlock ||
                        block instanceof StonecutterBlock ||
                        block instanceof StairsBlock)
                    blockState_2 = rotate(blockState, property, Y, reverse);
                //Facing block entities
                else if(block instanceof DispenserBlock ||
                        block instanceof BarrelBlock ||
                        block instanceof ShulkerBoxBlock)
                    blockState_2 = rotate(blockState, property, dir.getAxis(), reverse);
                //Horizontal block entities
                else if(block instanceof BlockEntityProvider)
                    blockState_2 = rotate(blockState, property, Y, reverse);
                //Facing blocks
                else if(block instanceof FacingBlock)
                    blockState_2 = rotate(blockState, property, dir.getAxis(), reverse);

                //Apply rotation
                if(blockState != blockState_2) {
                    world.setBlockState(pos, blockState_2);
                    return true;
                }
            }
        }
        return false;
    }

    //Based off of Botania Wand
    private static <T extends Comparable<T>> BlockState rotate(BlockState old, Property<T> property, Direction.Axis axis, boolean reverse) {
        if(property.getType() == Direction.class)
            return old.with(property, (T)rotateAround((Direction)old.get(property), axis, reverse));
        return old;
    }

    private static Direction rotateAround(Direction old, Direction.Axis axis, boolean reverse) {
        if(old.getAxis() == axis)
            return old.getOpposite();
        if(reverse) {
            old = rotateAround(old, axis);
            old = rotateAround(old, axis);
        }
        return rotateAround(old, axis);
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
