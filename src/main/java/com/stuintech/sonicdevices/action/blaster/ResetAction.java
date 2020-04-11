package com.stuintech.sonicdevices.action.blaster;

import com.stuintech.sonicdevices.action.IAction;
import com.stuintech.sonicdevices.block.ShiftedBlock;
import com.stuintech.sonicdevices.block.entity.ShiftedBlockEntity;
import com.stuintech.sonicdevices.item.Device;
import com.stuintech.sonicdevices.util.SyncedList;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ResetAction extends IAction.IBlockAction {
    public static SyncedList<BlockPos[]> shiftedBlocks = new SyncedList<>(null);

    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        ItemStack itemStack = getDevice(player);
        return resetItem(itemStack, player, world);
    }

    public boolean resetItem(ItemStack itemStack, PlayerEntity player, World world) {
        if(itemStack != null) {
            int shifter = itemStack.getOrCreateTag().getInt("shifter") - 1;
            if(shiftedBlocks.has(shifter)) {
                //Clear previous blocks
                BlockPos[] positions = shiftedBlocks.get(shifter);
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
}
