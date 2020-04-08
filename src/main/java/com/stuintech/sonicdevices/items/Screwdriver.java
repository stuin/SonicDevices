package com.stuintech.sonicdevices.items;

import com.stuintech.sonicdevices.blocks.WeakPoweredState;
import com.stuintech.sonicdevices.PropertyMap;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import static net.minecraft.util.math.Direction.Axis.*;

/*
 * Created by Stuart Irwin on 4/4/2019.
 * Parts copied and adapted from minecraft debug stick and piston
 */

public class Screwdriver extends Device {
    public Screwdriver(boolean cane) { this(cane, 3); }

    //Actual constructor
    public Screwdriver(boolean cane, int maxLevel) {
        super(cane, maxLevel);
    }

    public boolean interact(int level, PlayerEntity player, World world) {
        return false;
    }

    public boolean interact(int level, PlayerEntity player, LivingEntity entity) {
        return false;
    }

    public boolean interact(int level, PlayerEntity player, World world, BlockPos pos, Direction dir) {
        int used = 0;

        //Get block variables
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        BlockState blockState_2 = blockState;
        StateManager<Block, BlockState> stateFactory = block.getStateManager();

        //Activate and deactivate
        if((level == 1 || level == 2) && !world.isClient) {
            //Get relevant variable
            String code = PropertyMap.getCode(block.getTranslationKey());
            //System.out.println("Sonic used on: " + block.getTranslationKey() + " with variable " + code);

            //Check block tags
            Property<?> property = stateFactory.getProperty(code);
            if(property != null) {
                blockState_2 = cycle(blockState, property, level == 1);
                if(blockState_2 != blockState) {
                    world.setBlockState(pos, blockState_2, 18);
                    used++;
                }
            }


            //Special cases
            BlockPos blockPos_2;
            switch(block.getTranslationKey()) {
                case "block.minecraft.iron_door":
                    if(used > 0) {
                        //Locate rest of door
                        if (blockState.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER)
                            blockPos_2 = pos.up();
                        else
                            blockPos_2 = pos.down();


                        //Apply update to rest of door
                        blockState_2 = world.getBlockState(blockPos_2);
                        blockState_2 = blockState_2.with(DoorBlock.OPEN, level == 1);
                        world.setBlockState(blockPos_2, blockState_2, 18);
                        world.playLevelEvent(null, level == 1 ? 1011 : 1005, pos, 0);
                    }
                    break;
                case "block.minecraft.iron_trapdoor":
                    if(used > 0)
                        world.playLevelEvent(null, level == 1 ? 1037 : 1036, pos, 0);
                    break;
                case "block.minecraft.obsidian":
                    //Create nether portal
                    if(level == 1)
                        if(((NetherPortalBlock) Blocks.NETHER_PORTAL).createPortalAt(world, pos.offset(dir))) {
                            used++;
                            world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, RANDOM.nextFloat() * 0.4F + 0.8F);
                        }
                    break;
                case "block.minecraft.nether_portal":
                    //Break nether portal
                    if(level == 2) {
                        used++;
                        world.breakBlock(pos, false);
                    }
                    break;
                case "block.minecraft.piston": case "block.minecraft.sticky_piston":
                    if(method_11483(world, pos, blockState_2, level == 2))
                        used++;
                    break;
                case "block.minecraft.piston_head":
                    //Activate connected piston
                    interact(level, player, world, pos.offset(blockState.get(PistonHeadBlock.FACING).getOpposite()), dir);
                    break;
                case "block.minecraft.tnt":
                    if(level == 1) {
                        //Run tnt activation
                        TntBlock.primeTnt(world, pos);
                        world.breakBlock(pos, false);
                    }
                    break;
                case "block.minecraft.repeater":
                    //Update output block
                    world.updateNeighbor(pos.offset(blockState.get(HorizontalFacingBlock.FACING)), block, pos);
                    break;
                case "block.minecraft.daylight_detector": case "block.minecraft.redstone_torch":
                    //Update output blocks
                    world.updateNeighbors(pos, block);
                    break;
                case "block.minecraft.dispenser": case "block.minecraft.dropper":
                    //Activate with fake redstone source
                    if(level == 1) {
                        world.setBlockState(pos.down(), new WeakPoweredState(world, pos.down(), Direction.UP));
                        world.updateNeighbor(pos, block, pos.down());
                        used++;
                    }
                    break;
                case "block.minecraft.observer":
                    //Run tick update
                    if(level == 1) {
                        world.getBlockTickScheduler().schedule(pos, block, 1);
                        used++;
                    }
                    break;
                /*case "block.minecraft.redstone_wire": case "block.minecraft.powered_rail": case "block.minecraft.activator_rail":
                    //Activate with fake redstone source
                    if(level == 1) {
                        world.setBlockState(pos.down(), new StrongPoweredState(world, pos.down(), Direction.UP));
                        world.updateNeighbor(pos, block, pos.down());
                    }
                    break;*/
            }
        }

        //Rotate block
        if(level == 3 && player.canModifyWorld()) {
            Property<?> property = stateFactory.getProperty("facing");
            if(property != null) {
                if(block instanceof PistonHeadBlock ||
                        block instanceof WallMountedBlock ||
                        block instanceof AbstractSignBlock ||
                        block instanceof CommandBlock)
                    return false;
                else if(block instanceof PistonBlock) {
                    if(!blockState.get(PistonBlock.EXTENDED))
                        blockState_2 = rotate(blockState, property, dir.getAxis());
                }
                else if(block instanceof HorizontalFacingBlock ||
                        block instanceof AnvilBlock ||
                        block instanceof StonecutterBlock ||
                        block instanceof StairsBlock)
                    blockState_2 = rotate(blockState, property, Y);
                else if(block instanceof DispenserBlock ||
                        block instanceof BarrelBlock ||
                        block instanceof ShulkerBoxBlock)
                    blockState_2 = rotate(blockState, property, dir.getAxis());
                else if(block instanceof BlockWithEntity)
                    blockState_2 = rotate(blockState, property, Y);
                else if(block instanceof FacingBlock)
                    blockState_2 = rotate(blockState, property, dir.getAxis());


                //Apply rotation
                if (blockState != blockState_2) {
                    world.setBlockState(pos, blockState_2);
                    return true;
                }
            } else if((property = stateFactory.getProperty("axis")) != null) {
                if(block instanceof NetherPortalBlock)
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
        return used > 0;
    }

    //Based off of DebugStickItem
    private static <T extends Comparable<T>> BlockState cycle(BlockState old, Property<T> property, boolean value) {
        if(property.getType() == Boolean.class) {
            if(!old.get(property).equals(value))
                return old.with(property, cycle(property.getValues(), old.get(property), true));
            else
                return old;
        }

        return old.with(property, cycle(property.getValues(), old.get(property), value));
    }
    private static <T> T cycle(Iterable<T> iterable_1, T object_1, boolean boolean_1) {
        return boolean_1 ? Util.previous(iterable_1, object_1) : Util.next(iterable_1, object_1);
    }

    //Based off PistonBlock
    private boolean method_11483(World world, BlockPos blockPos, BlockState blockState, boolean tick) {
        Direction direction = blockState.get(PistonBlock.FACING);
        Block block = blockState.getBlock();

        //On extension
        PistonHandler pistonHandler = new PistonHandler(world, blockPos, direction, true);
        if(!blockState.get(PistonBlock.EXTENDED) && pistonHandler.calculatePush()) {

            //One tick pulse
            if(tick) {
                String s = world.getBlockState(blockPos.offset(direction, -1)).getBlock().getTranslationKey();
                if(!s.contains("piston") && !s.contains("slab")) {
                    world.setBlockState(blockPos, cycle(blockState, block.getStateManager().getProperty("extended"), true), 18);
                }
            }

            //Actually activate piston
            world.setBlockState(blockPos.offset(direction, -1), new WeakPoweredState(world, blockPos.offset(direction, -1), direction));
            world.addBlockAction(blockPos, block, 0, direction.getId());
            return true;
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
