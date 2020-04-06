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
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.naming.spi.StateFactory;
import javax.sound.midi.Soundbank;

/*
 * Created by Stuart Irwin on 4/4/2019.
 * Parts copied and adapted from minecraft debug stick and piston
 */

public class Screwdriver extends Device {
    public Screwdriver(boolean cane) { this(cane, 2); }

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
        int used = 1;

        //Activate and deactivate
        if((level == 1 || level == 2) && !world.isClient) {
            //Get block variables
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            StateManager<Block, BlockState> stateFactory = block.getStateManager();

            //Get relevant variable
            String code = PropertyMap.getCode(block.getTranslationKey());
            //System.out.println("Sonic used on: " + block.getTranslationKey() + " with variable " + code);

            //Check block tags
            BlockState blockState_2 = blockState;
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
                    //Locate rest of door
                    if(blockState.get(DoorBlock.HALF) == DoubleBlockHalf.LOWER)
                        blockPos_2 = pos.up();
                    else
                        blockPos_2 = pos.down();

                    //Apply update to rest of door
                    blockState_2 = world.getBlockState(blockPos_2);
                    blockState_2 = blockState_2.with(DoorBlock.OPEN, level == 1);
                    world.setBlockState(blockPos_2, blockState_2, 18);
                    if(used > 1)
                        world.playLevelEvent(null, level == 1 ? 1011 : 1005, pos, 0);
                    break;
                case "block.minecraft.iron_trapdoor":
                    if(used > 1)
                        world.playLevelEvent(null, level == 1 ? 1037 : 1036, pos, 0);
                    break;
                case "block.minecraft.obsidian":
                    if(level == 1)
                        if(!((NetherPortalBlock)Blocks.NETHER_PORTAL).createPortalAt(world, pos.offset(dir)))
                            used--;
                        else
                            world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, RANDOM.nextFloat() * 0.4F + 0.8F);
                    break;
                case "block.minecraft.nether_portal":
                    if(level == 2)
                        world.breakBlock(pos, false);
                    break;
                case "block.minecraft.piston": case "block.minecraft.sticky_piston":
                    method_11483(world, pos, blockState_2, level == 2);
                    break;
                case "block.minecraft.piston_head":
                    //Activate connected piston
                    interact(level, player, world, pos.offset(blockState.get(PistonHeadBlock.FACING).getOpposite()), dir);
                    break;
                case "block.minecraft.tnt":
                    //Run tnt activation
                    TntBlock.primeTnt(world, pos);
                    world.breakBlock(pos, false);
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
                    }
                    break;
                case "block.minecraft.observer":
                    //Run tick update
                    if(level == 1)
                        world.getBlockTickScheduler().schedule(pos, block, 1);
                    break;
                /*case "block.minecraft.redstone_wire": case "block.minecraft.powered_rail": case "block.minecraft.activator_rail":
                    //Activate with fake redstone source
                    if(level == 1) {
                        world.setBlockState(pos.down(), new StrongPoweredState(world, pos.down(), Direction.UP));
                        world.updateNeighbor(pos, block, pos.down());
                    }
                    break;*/
                default:
                    used--;
                    break;
            }
        }
        return used > 0;
    }

    //Based off of DebugStickItem
    private static <T extends Comparable<T>> BlockState cycle(BlockState blockState_1, Property<T> property_1, boolean value) {
        if(property_1.getType() == Boolean.class) {
            if(!blockState_1.get(property_1).equals(value))
                return blockState_1.with(property_1, cycle(property_1.getValues(), blockState_1.get(property_1), true));
            else
                return blockState_1;
        }

        return blockState_1.with(property_1, cycle(property_1.getValues(), blockState_1.get(property_1), value));
    }
    private static <T> T cycle(Iterable<T> iterable_1, T object_1, boolean boolean_1) {
        return boolean_1 ? Util.previous(iterable_1, object_1) : Util.next(iterable_1, object_1);
    }

    //Based off PistonBlock
    private void method_11483(World world, BlockPos blockPos, BlockState blockState, boolean changed) {
        Direction direction = blockState.get(PistonBlock.FACING);
        Block block = blockState.getBlock();

        //On extension
        PistonHandler pistonHandler = new PistonHandler(world, blockPos, direction, true);
        if(!blockState.get(PistonBlock.EXTENDED) && pistonHandler.calculatePush()) {

            //One tick pulse
            if(changed) {
                String s = world.getBlockState(blockPos.offset(direction, -1)).getBlock().getTranslationKey();
                if(!s.contains("piston") && !s.contains("slab")) {
                    world.setBlockState(blockPos, cycle(blockState, block.getStateManager().getProperty("extended"), true), 18);
                }
            }

            //Actually activate piston
            world.setBlockState(blockPos.offset(direction, -1), new WeakPoweredState(world, blockPos.offset(direction, -1), direction));
            world.addBlockAction(blockPos, block, 0, direction.getId());
        }

    }
}
