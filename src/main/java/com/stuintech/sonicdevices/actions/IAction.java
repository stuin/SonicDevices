package com.stuintech.sonicdevices.actions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface IAction {
    boolean interact(PlayerEntity player, LivingEntity entity) throws CancelActionException;
    boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) throws CancelActionException;

    abstract class IBlockAction implements IAction {
        public boolean interact(PlayerEntity player, LivingEntity entity) {
            return false;
        }
    }

    abstract class IEntityAction implements IAction {
        public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
            return false;
        }
    }
}
