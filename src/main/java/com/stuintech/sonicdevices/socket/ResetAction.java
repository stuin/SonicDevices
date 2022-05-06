package com.stuintech.sonicdevices.socket;

import com.redgrapefruit.itemnbt3.specification.Specification;
import com.stuintech.socketwrench.socket.CancelFasteningException;
import com.stuintech.sonicdevices.block.entity.ShiftedBlockEntity;
import com.stuintech.sonicdevices.item.Device;
import com.stuintech.socketwrench.socket.Socket;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ResetAction implements Socket {

    @Override
    public boolean onFasten(PlayerEntity player, LivingEntity entity) {
        if(!player.isSneaking())
            return resetItem(getDevice(player), player.world);
        return false;
    }

    @Override
    public boolean onFasten(PlayerEntity player, World world, BlockPos pos, Vec3d hit, Direction dir) {
        if(!player.isSneaking())
            return resetItem(getDevice(player), world);
        return false;
    }

    public static boolean resetItem(PlayerEntity player) {
        return resetItem(getDevice(player), player.world);
    }

    public static boolean resetItem(ItemStack itemStack, World world) {
        if(itemStack != null) {
            /*ArrayList<BlockPos> positions = shiftedBlocks.getData(itemStack);
            if(positions != null) {
                //Clear previous blocks
                for(BlockPos pos1 : positions) {
                    if(pos1 != null) {
                        BlockEntity blockEntity = world.getBlockEntity(pos1);
                        if(blockEntity instanceof ShiftedBlockEntity)
                            ((ShiftedBlockEntity) blockEntity).restore();
                    }
                }

                shiftedBlocks.clear(itemStack);
                return true;
            }*/
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

    public static void add(BlockPos pos, ItemStack stack) {
        /*ArrayList<BlockPos> list = shiftedBlocks.getData(stack);
        if(list == null) {
            list = new ArrayList<>();
            shiftedBlocks.setData(stack, list);
        }

        list.add(pos);*/
    }
}
