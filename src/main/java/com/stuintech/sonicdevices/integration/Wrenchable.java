package com.stuintech.sonicdevices.integration;

import com.stuintech.sonicdevices.action.IAction;
import com.zundrel.wrenchable.WrenchableRegistry;
import com.zundrel.wrenchable.block.BlockListener;
import com.zundrel.wrenchable.block.BlockWrenchable;
import com.zundrel.wrenchable.block.PropertyListener;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Iterator;

public class Wrenchable extends IAction.IBlockAction {
    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos pos, Direction dir) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        BlockHitResult blockHitResult = new BlockHitResult(player.getPos(), dir, pos, false);

        if(world.getBlockState(pos).getBlock() instanceof BlockWrenchable) {
            ((BlockWrenchable)world.getBlockState(pos).getBlock()).onWrenched(world, player, blockHitResult);
            if(blockEntity instanceof BlockWrenchable) {
                ((BlockWrenchable)blockEntity).onWrenched(world, player, blockHitResult);
            }
            return true;
        }

        if(blockEntity instanceof BlockWrenchable) {
            ((BlockWrenchable)blockEntity).onWrenched(world, player, blockHitResult);
            return true;
        }

        if(WrenchableRegistry.isBlockWrenchable(world.getBlockState(pos).getBlock())) {
            BlockListener wrenchablex = WrenchableRegistry.getBlockWrenchable(world.getBlockState(pos).getBlock());
            wrenchablex.onWrenched(world, player, blockHitResult);
            return true;
        }

        if(WrenchableRegistry.isBlockInstanceWrenchable(world.getBlockState(pos).getBlock())) {
            WrenchableRegistry.getBlockInstanceWrenchable(world.getBlockState(pos).getBlock()).onWrenched(world, player, blockHitResult);
            return true;
        }

        Iterator var8 = WrenchableRegistry.PROPERTY_LISTENERS.iterator();

        while(var8.hasNext()) {
            PropertyListener wrenchable = (PropertyListener)var8.next();
            if (world.getBlockState(pos).contains(wrenchable.getProperty())) {
                wrenchable.onWrenched(world, player, blockHitResult);
                return true;
            }
        }
        return false;
    }
}
