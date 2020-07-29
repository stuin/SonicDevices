package com.stuintech.sonicdevices.integration;

import com.stuintech.sonicdevicesapi.DeviceList;
import com.stuintech.sonicdevicesapi.IAction;
import com.stuintech.sonicdevicesapi.ILoader;
import net.kqp.ezpas.block.entity.PullerPipeBlockEntity;
import net.kqp.ezpas.init.Ezpas;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EZPAS extends IAction.IBlockAction implements ILoader {
    @Override
    public boolean interact(PlayerEntity playerEntity, World world, BlockPos blockPos, Direction direction) {
        return false;
    }

    @Override
    public boolean interact(PlayerEntity player, World world, BlockPos blockPos, Vec3d hit, Direction direction) {
        BlockEntity entity = world.getBlockEntity(blockPos);
        if(!world.isClient && entity instanceof PullerPipeBlockEntity) {
            ItemUsageContext context = new ItemUsageContext(player, Hand.MAIN_HAND,
                    new BlockHitResult(hit, direction, blockPos, false));
            Ezpas.PIPE_PROBE.useOnBlock(context);
            return true;
        }
        return false;
    }

    @Override
    public void onInitialize() {
        DeviceList.allActions[DeviceList.SCREWDRIVER][1].add(this);
        DeviceList.allActions[DeviceList.SCREWDRIVER][2].add(this);
    }
}
