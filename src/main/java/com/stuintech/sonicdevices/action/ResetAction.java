package com.stuintech.sonicdevices.action;

import com.stuintech.sonicdevices.block.entity.ShiftedBlockEntity;
import com.stuintech.sonicdevices.item.Device;
import com.stuintech.sonicdevices.util.SyncedList;
import com.stuintech.sonicdevicesapi.IAction;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ResetAction extends IAction.IBlockAction {
    public static SyncedList<ArrayList<BlockPos>> shiftedBlocks = new SyncedList<>(null);

    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        return interact(player, world);
    }

    public static boolean interact(PlayerEntity player, World world) {
        ItemStack itemStack = getDevice(player);
        return resetItem(itemStack, world);
    }

    public static boolean resetItem(ItemStack itemStack, World world) {
        if(itemStack != null) {
            int shifter = itemStack.getOrCreateTag().getInt("shifter") - 1;
            if(shiftedBlocks.has(shifter)) {
                //Clear previous blocks
                ArrayList<BlockPos> positions = shiftedBlocks.get(shifter);
                for(BlockPos pos1 : positions) {
                    if(pos1 != null) {
                        BlockEntity blockEntity = world.getBlockEntity(pos1);
                        if(blockEntity instanceof ShiftedBlockEntity)
                            ((ShiftedBlockEntity) blockEntity).restore();
                    }
                }

                shiftedBlocks.clear(shifter);
                return true;
            }
        }
        return false;
    }

    public static ItemStack getDevice(PlayerEntity player) {
        ItemStack itemStack = player.getMainHandStack();
        if(itemStack != null && itemStack.getItem() instanceof Device)
            return itemStack;
        itemStack = player.getOffHandStack();
        if(itemStack != null && itemStack.getItem() instanceof Device)
            return itemStack;
        return null;
    }

    public static void add(BlockPos pos, int group) {
        ArrayList<BlockPos> list;
        if(!shiftedBlocks.has(group - 1)) {
            list = new ArrayList<>();
            for(int i = 0; i < group; i++)
                shiftedBlocks.add(new ArrayList<>());
        } else
            list = shiftedBlocks.get(group - 1);

        list.add(pos);
        shiftedBlocks.set(group - 1, list);

    }
}
