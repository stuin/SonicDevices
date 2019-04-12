package com.stuintech.sonicdevices.extensions;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface IAction {
    public boolean interact(int level, World world, BlockPos pos, Direction dir);

}
