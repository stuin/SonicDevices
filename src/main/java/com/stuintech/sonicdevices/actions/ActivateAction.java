package com.stuintech.sonicdevices.actions;

import com.stuintech.sonicdevices.PropertyMap;
import com.stuintech.sonicdevices.blocks.WeakPoweredState;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/8/2020.
 * Parts copied and adapted from minecraft debug stick and piston
 */

public class ActivateAction extends IAction.IBlockAction {
    private final boolean deactivate;

    public ActivateAction(boolean deactivate) {
        this.deactivate = deactivate;
    }

    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        boolean used = false;
        if(!world.isClient) {
            //Get block variables
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            BlockState blockState_2 = blockState;
            StateManager<Block, BlockState> stateFactory = block.getStateManager();

            //Get relevant variable
            String code = PropertyMap.getCode(block.getTranslationKey());

            //Check block tags
            Property<?> property = stateFactory.getProperty(code);
            if(property != null) {
                blockState_2 = cycle(blockState, property, !deactivate);
                if(blockState_2 != blockState) {
                    world.setBlockState(pos, blockState_2, 18);
                    used = true;
                }
            }

            //Break leaves
            if(deactivate && block instanceof LeavesBlock) {
                world.breakBlock(pos, true);
            }

            //Special cases
            BlockPos blockPos_2;
            switch(block.getTranslationKey()) {
                case "block.minecraft.iron_door":
                    if(used) {
                        //Locate rest of door
                        if (blockState.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER)
                            blockPos_2 = pos.up();
                        else
                            blockPos_2 = pos.down();


                        //Apply update to rest of door
                        blockState_2 = world.getBlockState(blockPos_2);
                        blockState_2 = blockState_2.with(DoorBlock.OPEN, !deactivate);
                        world.setBlockState(blockPos_2, blockState_2, 18);
                        world.playLevelEvent(null, deactivate ? 1005 : 1011, pos, 0);
                    }
                    break;
                case "block.minecraft.iron_trapdoor":
                    if(used)
                        world.playLevelEvent(null, deactivate ? 1036 : 1037, pos, 0);
                    break;
                case "block.minecraft.obsidian":
                    //Create nether portal
                    if(!deactivate)
                        if(((NetherPortalBlock) Blocks.NETHER_PORTAL).createPortalAt(world, pos.offset(dir)))
                            used = true;
                    break;
                case "block.minecraft.nether_portal":
                    //Break nether portal
                    if(deactivate) {
                        used = true;
                        world.breakBlock(pos, false);
                    }
                    break;
                case "block.minecraft.piston": case "block.minecraft.sticky_piston":
                    if(method_11483(world, pos, blockState_2, deactivate))
                        used = true;
                    break;
                case "block.minecraft.piston_head":
                    //Activate connected piston
                    interact(player, world, pos.offset(blockState.get(PistonHeadBlock.FACING).getOpposite()), dir);
                    break;
                case "block.minecraft.tnt":
                    if(!deactivate) {
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
                    if(!deactivate) {
                        world.setBlockState(pos.down(), new WeakPoweredState(world, pos.down(), Direction.UP));
                        world.updateNeighbor(pos, block, pos.down());
                        used = true;
                    }
                    break;
                case "block.minecraft.observer":
                    //Run tick update
                    if(!deactivate) {
                        world.getBlockTickScheduler().schedule(pos, block, 1);
                        used = true;
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
        return used;
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
}
