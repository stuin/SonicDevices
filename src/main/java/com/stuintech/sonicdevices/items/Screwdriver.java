package com.stuintech.sonicdevices.items;

import com.stuintech.sonicdevices.FakePoweredState;
import com.stuintech.sonicdevices.PropertyMap;
import com.sun.istack.internal.Nullable;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.Property;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/*
 * Created by Stuart Irwin on 4/4/2019.
 * Parts copied and adapted from minecraft debug stick and piston
 */

public class Screwdriver extends Device {
    public Screwdriver(boolean cane) { this(cane, 3); }

    //Actual constructor
    public Screwdriver(boolean cane, int maxLevel) {
        super(cane, cane ? maxLevel + 1 : maxLevel);
    }

    public boolean interact(int level, PlayerEntity player, World world) {
        return false;
    }

    public boolean interact(int level, PlayerEntity player, LivingEntity entity) {
        //Scan mob
        if(level == 3 && entity.getEntityWorld().isClient) {
            player.addChatMessage(new StringTextComponent("Scanning Entity:"), false);
            player.addChatMessage(new StringTextComponent("  Type: " + entity.getType().getTextComponent().getText()), false);
            player.addChatMessage(new StringTextComponent("  Health: " + entity.getHealth() + " / " + entity.getHealthMaximum()), false);
            player.addChatMessage(new StringTextComponent(""), false);
            return true;
        }
        return false;
    }

    public boolean interact(int level, World world, BlockPos pos, Direction dir) {
        int used = 1;

        //Activate and deactivate
        if((level == 1 || level == 2) && !world.isClient) {
            //Get block variables
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            StateFactory<Block, BlockState> stateFactory = block.getStateFactory();

            //Get relevant variable
            String code = PropertyMap.getCode(block.getTranslationKey());
            //System.out.println("Sonic used on: " + block.getTranslationKey() + " with variable " + code);

            //Check block tags
            BlockState blockState_2 = blockState;
            Property<?> property = stateFactory.getProperty(code);
            if(property != null) {
                blockState_2 = method_7758(blockState, property, level == 1);
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
                    break;
                case "block.minecraft.piston": case "block.minecraft.sticky_piston":
                    //Activate proper piston mechanics
                    if(blockState.get(PistonBlock.FACING) != dir)
                        method_11483(world, pos, blockState_2);
                    else
                        world.setBlockState(pos, blockState, 18);
                    break;
                case "block.minecraft.tnt":
                    TntBlock.primeTnt(world, pos);
                    world.breakBlock(pos, false);
                    break;
                case "block.minecraft.repeater": case "block.minecraft.comparator":
                    world.updateNeighbor(pos.offset(blockState.get(HorizontalFacingBlock.field_11177)), block, pos);
                    break;
                case "block.minecraft.redstone_torch": case "block.minecraft.daylight_detector":
                    world.updateNeighbors(pos, block);
                    break;
                case "block.minecraft.dispenser": case "block.minecraft.dropper":
                    if(level == 1) {
                        world.setBlockState(pos.down(), new FakePoweredState(world, pos.down(), Direction.UP));
                        world.updateNeighbor(pos, block, pos.down());
                    }
                    break;
                case "block.minecraft.observer":
                    if(level == 1) {
                        Direction direction_2 = blockState.get(FacingBlock.FACING);
                        blockPos_2 = pos.offset(direction_2);
                        world.setBlockState(blockPos_2, new FakePoweredState(world, blockPos_2, direction_2));
                    }
                    break;
                /*case "block.minecraft.redstone_wire": case "block.minecraft.powered_rail": case "block.minecraft.activator_rail":
                    if(level == 1) {
                        world.setBlockState(pos.down(), new FakePoweredState(world, pos.down(), Direction.UP));
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
    private static <T extends Comparable<T>> BlockState method_7758(BlockState blockState_1, Property<T> property_1, boolean value) {
        if(property_1.getValueClass() == Boolean.class) {
            if(!blockState_1.get(property_1).equals(value))
                return blockState_1.with(property_1, method_7760(property_1.getValues(), blockState_1.get(property_1), true));
            else
                return blockState_1;
        }

        return blockState_1.with(property_1, method_7760(property_1.getValues(), blockState_1.get(property_1), value));
    }
    private static <T> T method_7760(Iterable<T> iterable_1, @Nullable T object_1, boolean boolean_1) {
        return boolean_1 ? SystemUtil.previous(iterable_1, object_1) : SystemUtil.next(iterable_1, object_1);
    }

    //Based off PistonBlock
    private void method_11483(World world, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.get(PistonBlock.FACING);
        Block block = blockState.getBlock();

        if(blockState.get(PistonBlock.EXTENDED)) {
            //On extension
            PistonHandler pistonHandler = new PistonHandler(world, blockPos, direction, true);
            if(pistonHandler.calculatePush()) {
                world.setBlockState(blockPos.offset(direction, -1), new FakePoweredState(world, blockPos.offset(direction, -1), direction));
                world.addBlockAction(blockPos, block, 0, direction.getId());
            }
        }
    }

}
