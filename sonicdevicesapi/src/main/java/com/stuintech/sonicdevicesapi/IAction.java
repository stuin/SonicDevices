package com.stuintech.sonicdevicesapi;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface IAction {
    boolean interact(PlayerEntity player, LivingEntity entity) throws CancelActionException;
    boolean interact(PlayerEntity player, World world, BlockPos pos,  Direction dir) throws CancelActionException;
    boolean interact(PlayerEntity player, World world, BlockPos pos, Vec3d hit, Direction dir) throws CancelActionException;

    abstract class IBlockAction implements IAction {
        public boolean interact(PlayerEntity player, LivingEntity entity) {
            return false;
        }
        public boolean interact(PlayerEntity player, World world, BlockPos pos, Vec3d hit, Direction dir) throws CancelActionException {
            return interact(player, world, pos, dir);
        }

    }

    abstract class IEntityAction implements IAction {
        public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
            return false;
        }
        public boolean interact(PlayerEntity player, World world, BlockPos pos, Vec3d hit, Direction dir) {
            return false;
        }
    }
}
