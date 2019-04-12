package com.stuintech.sonicdevices.extensions;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ComputerCraft implements IAction {
    public boolean interact(int level, World world, BlockPos pos, Direction dir) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return false;
    }
}